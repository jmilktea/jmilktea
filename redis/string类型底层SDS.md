## 简介  
string作为redis基本数据类型的一种，在redis中使用非常广泛。redis是基于c开发的，但是它并没有使用c里面的字符串类型，c字符串是以空字符结尾的数组，如字符串“redis”，则存储格式如下：  
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-1.png)  
而redis自己定义了一套SDS的数据结构（simple dynamic string 简单动态字符串）来表示字符串。  
redis中SDS定义如下：
```
struct sdshdr {    
    // 记录buf数组中已使用字节的数量，等于字符串的长度
    int len;

    // 记录buf数组中未使用字节的数量，数组是有空余的
    int free;
    
    // 字节数组，用于保存实际字符串
    char buf[];
};
```
可以看到SDS保存了一些额外信息，len字符串长度，free剩余未使用数量，以及存储实际字符串的字节数组。  
redis3.2之后的版本对SDS进行优化，根据存储空间大小拆成sdshdr5，sdshdr8，sdshdr16，sdshdr32，sdshdr64几个数据结构，分别用来存储大小为：32 字节（2 的 5 次方）,256 字节（2 的 8 次方），64KB（2 的 16 次方），4GB 大小（2 的 32 次方）以及 2 的 64 次方大小的字符串。[源码](https://github.com/redis/redis/blob/unstable/src/sds.h)   
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-6.png)   

```
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len; /* used */
    uint8_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
```
以sdshdr8为例，len记录已经使用的字节数，alloc记录总字节数(包括未使用的)，flags用来表示当前类型是sdshdr8，buf是存储内容的字节数组。  
那么redis为什么不直接使用c的字符串，而要重新定义一个数据结构呢？

## 为什么使用SDS  
这是由于c字符串的局限性不满足redis高性能，安全的要求，我们从以下几个方面进行对比。

- 计算字符串长度  
由于c字符串只是简单通过数组保存字符串，所以每次要计算字符串的长度都需要遍历数组进行计数，直到遇到\0字符才能获取字符串长度，时间复杂度为O(n)。  
而SDS保存了字符串的长度，可以直接获得，时间复杂度为O(1)。redis中可以通过strlen key命令获取对应value的长度，该命令不会有性能问题。

- 缓冲区溢出  
假设存在如下字符串  
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-2.png)  
假设需要给s1拼接一个字符串，使用：strcat(s1, " Cluster"); 从图可以看到已经没有足够连续的空间，如果此时编码人员忘记重新分配空间，执行后就会把s2的内容给改了。这种依靠人为控制的容易出错，而且非常隐蔽。  
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-3.png)  
SDS则每次修改都会去判断是否有足够的free，如果不足就会重新分配空间。这样在使用SDS时就不再需要关心溢出的问题了  
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-4.png)  
![image](https://github.com/jmilktea/jmilktea/blob/master/redis/images/sds-5.png)  

- 内存分配  
由于c字符串是用一个数组来存储字符串，那么每次对字符串进行拼接时，就需要重新申请空间，否则就会出现上面的溢出情况；同理，如果缩短字符串，也需要重新申请内存，并把老的内存释放，否则就会出现内存泄漏。
如果对一个字符串经常修改，频繁的申请和释放内存是低效的，SDS通过空间预分配和惰性释放空间对此进行优化。  
**空间预分配**    
空间预分配的目的是通过预先分配一些空闲空间，避免在使用时频繁的申请。SDS的预分配有两种情况：   
1.如果分配的空间小于1M，就会分配和len一样大小的未使用空间。比如字符串需要10字节的空间，则也会分配10字节的未使用空间，另外还会分配1字节空间存储空字符串\0。总分配字节数为：10 + 10 + 1 = 21   
2.如果分配的空间大于等于1M，就会多分配1M大小的未使用空间。比如字符串需要10M的空间，则会分配1M的未使用空间，另外还会分配1字节空间存储空字符串\0。总分配数为：10M + 1M + 1Byte  
**惰性释放**  
当存储的内容变小时，SDS不会立即释放空间，而是把它加到free上面去，这样可以避免频繁释放空间带来的开销，后续如果需要增加就不需要再申请了。  
当然SDS也提供了api，允许手动对这部分空间进行释放。

- 二进制安全  
由于c字符串认为\0就是字符串结尾，所以它只能存储普通字符串，不能存储图片，视频等二进制数据，否则可能出现数据不完整的情况，所以认为它是二进制不安全的。  
SDS的buf是字节数组，而不是普通字符串数组，它存储的就是二进制数据，写进去的是什么内容，读取出来就是什么内容，所以认为它是二进制安全的。

## 对比    
c字符串 | SDS
---|---
获取字符串长度复杂度为O(n) | 获取字符串长度复杂度为O(1)
不安全，容易出现缓冲区溢出 | 不会出现溢出
修改字符串长度N次，需要N次内存重分配 | 修改字符串长度N次，最多需要N次内存重分配
只能保存文本内容 | 可以保存文本或二进制数据
可以使用c字符串函数库所有api | 可以使用部分c字符串函数库api

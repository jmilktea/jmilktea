开发过程中经常需要调试改代码，每次都重启服务效率很低。尽管spring boot的启动速度怎么优化，当项目稍微复杂时，启动还是需要一定的时间，例如需要重新加载资源，重新初始化数据库链接池等，而这些通常我们是不会修改的。我们经常会简单改一个配置或者简单改一行代码而重启等了2,3分钟，效率很低。  

jrebel插件可以实现热加载，也就是修改了哪些文件，就加载哪些文件。这比修改一个类而重新加载所有类的效率显然要快许多。  
使用idea-setting-plugin直接搜索安装jrebel。

### 激活
选择TeamURL方式  
TeamURL填如下值：http://jrebel.whrj999.com/3d64b43e-0da7-40a3-925a-844e5a57aea8   
邮箱填自己的有效邮箱，点击激活后，会收到邮件，点击邮件链接激活即可。  
![iamge](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/jrebel-1.png)  

### 配置  
配置我们的springboot项目，当修改class类型和resource文件时，重新加载   
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/jrebel-2.png)  

### 启动调试
如图启动，如果有修改，点击左下来update application按钮即可。
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/jrebel-4.png)  
![iamge](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/jrebel-3.png)  

每次修改点击一下，可以看到可以快速生效，不需要重启整个服务了，终于不用996了~~~

## 前言  
为了确保代码质量，保证项目的可持续发展，每个开发团队都会按照一定的规范和风格进行编码，这样可以减少出错的概率，提升代码的可维护性。阿里出品的java编程规约在业界非常受欢迎，我们推荐使用该规约作为团队的开发规范。详细地址为：https://github.com/alibaba/p3c/blob/master/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C%EF%BC%88%E5%8D%8E%E5%B1%B1%E7%89%88%EF%BC%89.pdf  

## p3c插件  
我们可以结合p3c插件来约束队员遵守规范，插件地址为：https://github.com/alibaba/p3c。
安装完成后插件会出现在Tools菜单下，我们可以把它添加到菜单栏，方便操作。当然也可以右键对某个文件或者目录进行扫描。
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-1.png)
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-2.png)
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-3.png)

我们使用idea git提交代码时，可以选择该插件，这样会在提交前再做一套把关
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-4.png)

如果有需要优化的，会出现在下方。如下出现三个等级的建议：Blocker和Critical通常是需要处理的
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-5.png)

所有的优化建议都在这里，对于一些不需要的可以选择关掉
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/idea/images/p3c-6.png)

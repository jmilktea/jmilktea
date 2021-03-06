## 简介  
git stash用于保存和恢复工作进度。当你切换到某个开发分支开发到一半时，突然有个事需要切换到另一个分支，这个时候代码还是个半成品，可以使用git stash暂存当前的修改，过后可以回到当前的stash继续修改。
与commit的区别？git stash是一种可以切换分支的not commit状态。commit应该表示工作已经基本完成，可以work，是一次比较完整的提交。而stash还处于工作的过程中，属于半成品，需要继续开发。两者的目的不同。

## 命令
- git stash [save message]，save message是备注，可选
- git stash list，显示所有stash
- git stash pop，恢复最新的进度到工作区
- git stash apply stash@{0}，恢复到某个stash到工作区

## 操作  
在idea里可以直接使用上面的命令在Terminal命令行操作。也可以使用图形化操作。  
选择git stash后可以写message，然后发现本地修改已经被暂存，恢复到原来的状态，可以切换分支  
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/git/images/git-stash.png)  

选择git unstash后可以选择恢复到原来的修改，也可以通过view查看本次暂存了哪些文件  
![image](https://github.com/jmilktea/jmilktea/blob/master/%E5%B7%A5%E5%85%B7%E7%B1%BB/git/images/git-unstash.png)  
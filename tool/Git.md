# Git

## 工作流程

![图片描述](img_Git/59c31e4400013bc911720340.png)

Workspace：工作区
Index / Stage：暂存区
Repository：仓库区（或本地仓库）
Remote：远程仓库



## 应用场景

### 使用Git和Github同步文件

1. 在Github上创建仓库Note

2. 安装Git(下载网址http://git-scm.com/download/，选择windows-->Git for Windows Setup)

3. 安装完成打开

4. 输入`ssh-keygen –t rsa –C "邮箱地址"`，之后直接回车，不要修改密钥存放路径，打开密钥id_rsa.pub复制内容

5. Github上选择settings-->SSH Keys-->Add SSH Key,在Title这一栏随便填一个名字，之后将id_rsa.pub里面的内容复制到到Key这一栏中确定

6. Git上输入`ssh –T git@github.com` 验证是否设置成功，如果出现问题`ssh:connect to host github.com port 22: Connection timed out`，可以更换端口22，在密钥id_rsa.pub同步文件夹下新建config文件，内容：

   ```java
   Host github.com
   User git
   Hostname ssh.github.com
   PreferredAuthentications publickey
   IdentityFile ~/.ssh/id_rsa
   Port 443
   
   Host gitlab.com
   Hostname altssh.gitlab.com
   User git
   Port 443
   PreferredAuthentications publickey
   IdentityFile ~/.ssh/id_rsa
   ```

   ​	之后重试，有个地方要输入yes

7. 配置一下用户名和邮箱(`git config --global`表示本机器上所有的Git仓库都会使用这个配置)       

   ```java
   $git config –global user.name "wj456789"
   $git config –global user.email "1256116295@qq.com"	
   ```

8. 本地新建目录作为本地仓库，在新建目录右击选择打开Git Bash Here

9. 输入`git init`初始化仓库

10. 输入`git remote add origin https://github.com/wj456789/Note.git`关联远程仓库

11. 输入`git pull git@github.com:wj456789/Note.git`同步远程仓库

12. 输入`git add .`添加所有文件到暂存区
    		`git commit –m .`提交所有文件到本地仓库
    		`git push -u origin master  `推送本地仓库分支master所有文件到远程仓库origin 	
    
13. 之后就可以输入`git push origin master`和`git pull origin master`来推送和拉取文件

参考：[使用git和github进行文件同步](https://blog.csdn.net/u011622208/article/details/80637661)

### 代码出现冲突

**git提交时报错:Updates were rejected because the tip of your current branch is behind**

**git合并时报错**

```java
//如：冲突文件为test.txt
//当前分支内容为
1111111111111
2222222222222
    
//冲突分支内容为
1111111111111
3333333333333
    
$git status查看冲突状态，
$cat test.txt查看冲突代码
1111111111111
<<<<<<<HEAD
2222222222222
=======
3333333333333
>>>>>>>REMOTE    
修改冲突之后重新操作    
```

```java
先将远程repository修改pull下来再使用强制push
git pull origin master
git push origin master -f
```

参考：[Git使用教程,最详细，最傻瓜，最浅显，真正手把手教](https://blog.csdn.net/qq_36150631/article/details/81038485)

## 常用git命令

### 新建代码库

在当前目录新建一个Git代码库

$ git init

新建一个目录，将其初始化为Git代码库

$ git init [project-name]

下载一个项目和它的整个代码历史

$ git clone [url]



### 配置

显示当前的Git配置

$ git config --list

编辑Git配置文件

$ git config -e [--global]

设置提交代码时的用户信息

$ git config [--global] user.name "[name]"
$ git config [--global] user.email "[email address]"





### 增加/删除文件

添加指定文件到暂存区

$ git add [file1] [file2] ...

添加指定目录到暂存区，包括子目录

$ git add [dir]

添加当前目录的所有文件到暂存区

**$ git add .**

添加每个变化前，都会要求确认

对于同一个文件的多处变化，可以实现分次提交

$ git add -p

删除工作区文件，并且将这次删除放入暂存区

$ git rm [file1] [file2] ...

停止追踪指定文件，但该文件会保留在工作区

$ git rm --cached [file]

改名文件，并且将这个改名放入暂存区

$ git mv [file-original] [file-renamed]





### 代码提交

提交暂存区到仓库区，message是注释

**$ git commit -m [message]**

提交暂存区的指定文件到仓库区

**$ git commit [file1] [file2] ... -m [message]**

提交工作区自上次commit之后的变化，直接到仓库区

$ git commit -a

提交时显示所有diff信息

$ git commit -v

使用一次新的commit，替代上一次提交

如果代码没有任何新变化，则用来改写上一次commit的提交信息

$ git commit --amend -m [message]

重做上一次commit，并包括指定文件的新变化

$ git commit --amend [file1] [file2] ...







### 远程同步

下载远程仓库的所有变动

$ git fetch [remote]

显示所有远程仓库

**$ git remote -v**

显示某个远程仓库的信息

$ git remote show [remote]

增加一个新的远程仓库，并命名

$ git remote add [shortname] [url]

取回远程仓库的变化，并与本地分支合并

**$ git pull [remote] [branch]**

上传本地指定分支到远程仓库

**$ git push [remote] [branch]**

强行推送当前分支到远程仓库，即使有冲突

$ git push [remote] --force

推送所有分支到远程仓库

$ git push [remote] --all

将本地分支推送到远程分支上，若无远程分支则自动创建

$ git push origin 远程分支名:远程分支名



### 查看信息

显示工作区和暂存区是否有未提交的文件

**$ git status**

显示当前分支的版本历史

$ git log

显示commit历史，以及每次commit发生变更的文件

$ git log --stat

搜索提交历史，根据关键词

$ git log -S [keyword]

显示某个commit之后的所有变动，每个commit占据一行

$ git log [tag] HEAD --pretty=format:%s

显示某个commit之后的所有变动，其"提交说明"必须符合搜索条件

$ git log [tag] HEAD --grep feature

显示某个文件的版本历史，包括文件改名

$ git log --follow [file]
$ git whatchanged [file]

显示指定文件相关的每一次diff

$ git log -p [file]

显示过去5次提交

$ git log -5 --pretty --oneline

显示所有提交过的用户，按提交次数排序

$ git shortlog -sn

显示指定文件是什么人在什么时间修改过

$ git blame [file]

显示暂存区和工作区的差异

**$ git diff**

显示暂存区和上一个commit的差异

$ git diff --cached [file]

显示工作区与当前分支最新commit之间的差异

$ git diff HEAD

显示两次提交之间的差异

$ git diff [first-branch]...[second-branch]

显示今天你写了多少行代码

$ git diff --shortstat "@{0 day ago}"

显示某次提交的元数据和内容变化

$ git show [commit]

显示某次提交发生变化的文件

$ git show --name-only [commit]

显示某次提交时，某个文件的内容

$ git show [commit]:[filename]

显示当前分支的最近几次提交

$ git reflog

```java
$ git reflog
版本号	  版本记录											
43156bc (HEAD -> master) HEAD@{0}: commit: 注释
a2123c2 HEAD@{1}: commit: 注释
a65616d HEAD@{2}: initial pull

```



### 撤销

恢复暂存区的指定文件到工作区，撤销工作区修改

**$ git checkout [file]**

恢复暂存区的所有文件到工作区，撤销工作区修改

**$ git checkout .**

恢复暂存区的所有文件到工作区，撤销工作区修改

**$ git restore .**

恢复某个commit的指定文件到暂存区和工作区，撤销暂存区和工作区修改

$ git checkout [commit] [file]

重置暂存区的指定文件，与上一次commit保持一致，但工作区不变

$ git reset [file]

重置暂存区与工作区，与上一次commit保持一致

$ git reset --hard

重置当前分支的指针为指定commit，同时重置暂存区，但工作区不变

$ git reset [commit]

重置当前分支的HEAD为指定commit，同时重置暂存区和工作区，与指定commit一致，[commit]即是版本号

**$ git reset --hard [commit]**

如：回退到a2123c2版本

**$git reset --hard a2123c2**

如：回退到上一个版本

**$git reset --hard HEAD^**

如：回退到上一个版本

**$git reset --hard HEAD@{1}**

重置当前HEAD为指定commit，但保持暂存区和工作区不变

$ git reset --keep [commit]

新建一个commit，用来撤销指定commit

后者的所有变化都将被前者抵消，并且应用到当前分支

$ git revert [commit]

暂时将未提交的变化移除，稍后再移入

$ git stash
$ git stash pop



### 分支

列出所有本地分支，当前分支前面会添加一个星号

**$ git branch**

列出所有远程分支

**$ git branch -r**

列出所有本地分支和远程分支

$ git branch -a

新建一个分支，但依然停留在当前分支

**$ git branch [branch-name]**

新建一个分支，并切换到该分支

**$ git checkout -b [branch]**

将远程git仓库里的指定分支拉取到本地，同时在本地新建了一个分支，并和指定的远程分支关联了起来。

**$ git checkout -b 本地分支名 origin/远程分支名**

新建一个分支，指向指定commit

$ git branch [branch] [commit]

新建一个分支，与指定的远程分支建立追踪关系

$ git branch --track [branch] [remote-branch]

切换到指定分支，并更新工作区

**$ git checkout [branch-name]**

切换到上一个分支

$ git checkout -

建立追踪关系，在现有分支与指定的远程分支之间

**$ git branch --set-upstream [branch] [remote-branch]**

合并指定分支到当前分支

**$ git merge [branch]**

选择一个commit，合并进当前分支

$ git cherry-pick [commit]

删除分支

**$ git branch -d [branch-name]**

删除远程分支

$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]

查看本地分支及追踪的分支，可以显示本地所有分支

$ git branch -vv







### 标签

列出所有tag

$ git tag

新建一个tag在当前commit

$ git tag [tag]

新建一个tag在指定commit

$ git tag [tag] [commit]

删除本地tag

$ git tag -d [tag]

删除远程tag

$ git push origin :refs/tags/[tagName]

查看tag信息

$ git show [tag]

提交指定tag

$ git push [remote] [tag]

提交所有tag

$ git push [remote] --tags

新建一个分支，指向某个tag

$ git checkout -b [branch] [tag]





### 其他

查看远程origin/master分支日志信息，可以按Q退出

$ git log remotes/origin/master
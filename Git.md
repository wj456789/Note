# Git

## 工作流程

![图片描述](img_Git/59c31e4400013bc911720340.png)

Workspace：工作区
Index / Stage：暂存区
Repository：仓库区（或本地仓库）
Remote：远程仓库



## 应用场景

### Github配置

1. 在Github上创建仓库Note

2. 安装Git(下载网址http://git-scm.com/download/，选择windows-->Git for Windows Setup)

3. 安装完成打开

4. 手动输入`ssh-keygen –t rsa –C "邮箱地址"`，之后直接回车，不要修改密钥存放路径，打开密钥id_rsa.pub复制内容

5. Github上选择settings-->SSH Keys-->Add SSH Key，在Title这一栏随便填一个名字，之后将id_rsa.pub里面的内容复制到到Key这一栏中确定

6. Git上输入`ssh –T git@github.com` 验证是否设置成功，如果出现问题`ssh:connect to host github.com port 22: Connection timed out`，可以更换端口22，在密钥id_rsa.pub同步文件夹下新建config文件，内容：

   ```config
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

   ```sh
   $ git config –global user.name "wj456789"
   $ git config –global user.email "1256116295@qq.com"	
   ```


### 初始化

1. 首先，登录GitLab，创建一个新项目的私人仓库； 

2. 本地新建目录作为本地仓库，右键，Git Bash Here，打开Git命令窗口； 

3. 按照如下步骤，添加远程仓库地址，并提交代码；  

   ```sh
   git init                           #初始化仓库
   git remote add origin 仓库地址      #设置远程仓库地址，创建远程主分支；
   git pull origin master 		       #同步远程仓库
   git add .						 #添加所有文件到暂存区
   git commit -m "first commit"       #把代码提交到本地仓库，并备注信息；
   git push -u origin master          #提交代码到远程仓库origin的master分支，-u指定默认分支，之后就可以输入git push和git pull来推送和拉取文件
   ```

   **问题：**

   1) 添加远程仓库可能报错：`fatal: remote origin already exists.` 

   ```sh
   git remote -v：             #查看远程仓库详细信息，可以看到仓库名称
   git remote rm origin        #先删除远程 Git 仓库
   git remote add origin 仓库地址      #再添加远程 Git 仓库
   
   #也可以手动修改gitconfig文件的内容，把 [remote “origin”] 那一行删掉就好了
   vi .git/config              
   ```

   2) 不使用 pull 直接 push 可能报如下错误：`Updates were rejected because the remote contains work that you do`

   3) 直接使用 pull 可能报如下错误：`fatal: refusing to merge unrelated histories` 

   ```sh
   git pull origin master --allow-unrelated-histories
   ```

参考：[使用git和github进行文件同步](https://blog.csdn.net/u011622208/article/details/80637661)



### 冲突

**git提交时报错:Updates were rejected because the tip of your current branch is behind**

**git合并时报错**

```sh
# 如：冲突文件为test.txt
# 当前分支内容为
1111111111111
2222222222222
    
# 冲突分支内容为
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

```sh
# 先将远程repository修改pull下来再使用强制push，不推荐使用
$ git pull origin master
$ git push origin master -f
```

参考：[Git使用教程,最详细，最傻瓜，最浅显，真正手把手教](https://blog.csdn.net/qq_36150631/article/details/81038485)



### 推送分支

```sh
# 其中local-branch本地分支	remote-branch远程分支	origin远程仓库别名
$ git checkout local-branch    		# 切换到本地分支
$ git push -u origin local-branch	# 推送本地分支到远程同名分支，并且相互关联

# 关联之后可以直接使用如下推送拉取    
$ git push
$ git pull    
```

```sh
# 其中$ git push -u origin local-branch相当于
$ git push origin local-branch		# 远程库无local-branch同名分支则自动创建
$ git branch --set-upstream-to=remote-branch local-branch	# 关联本地分支和远程分支 
```

### 将文件取消版本控制

以 idea 为例：

如果在 .gitignore 文件中没有指定忽略 .idea 文件夹（这个文件夹包含的是本地对项目的配置，如 maven，字符编码等，不需要共享，所以无需提交），那么提交时就会将其提交到远程代码库中。如果已经 .idea 已经在远程仓库，那么再修改 .gitignore 文件就已经没用了，所以需要通过命令将远程的 .idea 文件夹取消版本控制

```sh
git rm -r --cached "要取消版本控制的文件或文件夹"		#将文件或文件夹取消版本控制
git commit -m "提交的信息"						  #提交到本地仓库 
git push origin master				#提交到远程分支，远程仓库的文件或文件夹将会被删除
```

最后在 .gitignore 文件中添加忽略 .idea 文件夹，并提交到远程仓库，如果需要将 .idea 文件夹添加版本控制，只需要将 .gitignore 文件中的 .idea/ 删除即可

### 代码回退

**情况1：** 已提交到暂存区，未提交到本地版本库（即已进行git add 的操作，未进行 git commit 操作）

- git reset ：撤回暂存区的所有提交

- git reset HEAD 文件名 ：撤回暂存区的某个文件

**情况2：** 已提交到暂存区，已提交到本地版本库 （即已进行git add 和 git commit 操作）

**若需要回退到上次版本的代码：**

1. git reset --soft HEAD~1 ：回退到git add后的状态

2. git reset ： 撤回所有暂存区的文件

**若需要回退到某个版本的提交：**

1. git log ： 查看之前提交的版本号

2. git reset --soft 版本号：回退到某个版本的git add的状态

3. git reset ：撤回所有暂存区的提交

**情况3：** 已提交到暂存区，已提交到本地版本库，已推送远程分支 （即已进行git add ，git commit 和git push操作）

首先关闭远程分支的MR

**若需要回退到上次版本的代码：**

1. git reset --soft HEAD~1 ：回退到git add后的状态

2. git reset ： 撤回所有暂存区的文件

3. 进行再次提交时，会产生版本错误，即提交失败，因为当前分支的版本低于远程分支的版本，所以要覆盖掉它

   git push origin 分支 --force ：覆盖远程分支的版本

4. git push ：提交修改即可

**若需要回退到某个版本的提交：**

1. git log ： 查看之前提交的版本号

2. git reset --soft 版本号：回退到某个版本的git add的状态

3. git reset ：撤回所有暂存区的提交

4. 进行再次提交时，会产生版本错误，即提交失败，因为当前分支的版本低于远程分支的版本，所以要覆盖掉它

   git push origin 分支 --force ：覆盖远程分支的版本

5. git push ：提交修改即可

**情况4：** 如需要将本地工作区代码也进行同步回退

将上述几种情况的git reset --soft 的命令改为git reset --hard 即可实现本地工作区代码的同步回退

```sh
# push回退举例
# 1.查看提交的记录，找到想回到XXX版本的commitId
$ git log 
commit c1cc2ada80edc303b0135b318790c3b23b602e60
Merge: 061805b 8e4b1ad
Author: wangjin 30021900 <wangjin167@huawei.com>
Date:   Fri Oct 14 16:27:36 2022 +0800
    Merge branch 'master' of ssh://codehub-dg-y.huawei.com:2222/bd_as/udm/tianjin-mobile-iop/IOPTianjinMobile_V3R5C76
    * 'master' of ssh://codehub-dg-y.huawei.com:2222/bd_as/udm/tianjin-mobile-iop/IOPTianjinMobile_V3R5C76:
......
commit 061805b288513a0b508adf56ce328147fed48a1f
Author: wangjin 30021900 <wangjin167@huawei.com>
Date:   Fri Oct 14 16:27:11 2022 +0800
    【修改内容/原因】 sql脚本和打包升级脚本
    【DTS|IR|SR|US号】 #issue 3
    【修改人】 汪津 30021900
    【审核人】 NA
    ......

# 2.执行git reset命令,且保留本地修改记录
$ git reset --soft 061805b288513a0b508adf56ce328147fed48a1f
$ git push --force #将本地的版本号强制覆盖远端个人仓的版本号，删除已push的提交

# 3. 本地修改完成后重新执行commit 、 push 操作。
```



### git-lfs(huawei)

LFS（Large File Storage） 是大文件的存储方案，而非 BFS 二进制文件的存储方案

如果这个二进制文件不是很大、也不会频繁更新及 Add，那么可以直接入 Git 仓库；如果这个二进制文件较大（如>50M），而且还会经常频繁更新及 Add，那么建议不要把该二进制文件直接入 Git 仓库，建议使用 Git-LFS 进行托管

#### 安装

##### 自动安装

```sh
#windows
#开始菜单 -> 搜索 Git Bash -> 右键 管理员运行
$ curl -k https://cmc-szver-artifactory.cmc.tools.huawei.com/artifactory/cmc-software-release/CodeHub/git-lfs/release.v2/git_lfs_autoinstall.sh -o git_lfs_autoinstall.sh &&
sh git_lfs_autoinstall.sh

#linux
$ curl -k https://cmc-szver-artifactory.cmc.tools.huawei.com/artifactory/cmc-software-release/CodeHub/git-lfs/release.v2/git_lfs_autoinstall.sh -o git_lfs_autoinstall.sh &&
sudo sh git_lfs_autoinstall.sh && (git lfs uninstall; git lfs install)
```

##### 手动安装

1. 下载：选择适合平台的正确版本，解开压缩包。

2. 对于 Linux 平台，执行 `install.sh` 将 `git-lfs` 自动复制到路径 `/usr/bin` 下。

   如果之前手动将老版本的 `git-lfs` 使用`whereis git-lfs` ，查找git-lfs位置然后手工删除老版本的 `git-lfs`。

3. 对于 Windows 平台，将 `git-lfs-windows-amd64.exe` 或者 `git-lfs-windows-386.exe` 复制到 `GIT安装目录\usr\bin\git-lfs.exe` (替换git默认安装的git-lfs.exe）。

4. 安装完毕后，执行下面命令完成初始化：`$ git lfs install`

   这个命令会自动修改用户主目录下的 `.gitconfig` 配置文件（即 `~/.gitconfig`），添加 Git-LFS 的过滤器启动（filter driver）配置。

   查看 `~/.gitconfig` ，如果显示 lfs 过滤器的信息则表明正确初始化了 lfs：

   ```
   $ cat ~/.gitconfig
   [filter "lfs"]
       clean = git-lfs clean -- %f
       smudge = git-lfs smudge -- %f
       process = git-lfs filter-process
       required = true
   ```

   至此就完成了 `git-lfs` 的安装和设置。

#### 卸载

```sh
$ git lfs uninstall

#找到git lfs二进制文件存放位置,删除git-lfs.exe二进制文件     
$ where git-lfs
```

#### 使用

```sh
#使用命令，查看仓库中那些文件大于50M
$ find ./ -path "./.git" -prune -o -type f -size +50M -print | cut -b 3-

#指定大文件用LFS管理，如下：large_file.pdf为大文件的名称，文件名用track跟踪时需要用仓库的相对路径
$ git lfs track large_file.pdf

#文件托管后会生成.gitattributes隐藏文件，将这个隐藏文件和被追踪的文件 large_file.pdf 一并提交到仓库
$ git add .gitattributes large_file.pdf 

$ git commit -s -m "提交信息，自己填写" 

$ git push
```

#### 检查

##### 确认Git lfs版本是否正确

```sh
#需要确认已经安装了 Git 客户端，且需要 1.8.2 或更高版本的 Git 客户端版本
$ git version

#执行如下命令查看lfs安装的版本，下面括号里必须显示Huawei才表示安装正确
$ git lfs version                                                               
git-lfs/2.13.3.huawei.p1 (Huawei; windows amd64; go 1.16.6; git 207b2155)
```

##### 文件是否正确转换为Git-LFS 

```sh
#输出文件的 oid 信息
$ git lfs ls-files -l
ec19e2d68fb3a44242b99555479d9886876dc54d8e1950b7f3639a6545ed961d * foo.pdf

#输出文件的Git-LFS链接文件的内容, Git-LFS链接文件包含 version, oid 和 size 三个信息
$ git show HEAD:foo.pdf
version https://git-lfs.github.com/spec/v1
oid sha256:ec19e2d68fb3a44242b99555479d9886876dc54d8e1950b7f3639a6545ed961d
size 3780462
```

[git-lfs](http://rnd-isourceb.huawei.com/iSource/git-lfs/overviews)

#### 问题

> push大文件报错 remote: GitLab: LFS objects are missing. Ensure LFS is properly set up or try a manual "git lfs push --all".   

打开gitbash，执行如下命令后，再push 

```sh
$ git lfs push --all 仓库地址
#或
$ git lfs push --all origin master
```



> 提交大文件报错

```sh
$ git log	#查看commit提交记录
$ git reset --mixed commit_id  #回退到提交之前
```

### 大小写不敏感

Windows 下 git 默认配置是对文件/文件夹名称的大小写不敏感

```sh
# 对文件的重命名，git 会将其识别为 Rename 的变更类型，然后正常提交推送就能同步到远程仓库
$ git mv test.txt TEST.txt

# 把文件夹命名成其他名称，然后再命名为大写
$ git mv test-dir tmp
$ git mv tmp TEST-DIR
```



## 常用git命令

### 新建代码库

```sh
#在当前目录新建一个Git代码库
$ git init

#新建一个目录，将其初始化为Git代码库
$ git init [project-name]

#下载一个项目和它的整个代码历史
$ git clone [url]
```



### 配置

```sh
# 显示当前的Git配置
$ git config --list

# 编辑Git配置文件
$ git config -e [--global]

# 设置提交代码时的用户信息
$ git config [--global] user.name "[name]"
$ git config [--global] user.email "[email address]"
```



### 增加/删除文件

```sh
#添加文件夹及所有包含内容
$ git add 文件夹/

#添加当前目录下所有此文件类型的文件
$ git add *.文件类型

#添加指定文件到暂存区
$ git add [file1] [file2] ...

#添加指定目录到暂存区，包括子目录
$ git add [dir]

#添加当前目录的所有目录文件到暂存区
$ git add .

#添加每个变化前，都会要求确认,对于同一个文件的多处变化，可以实现分次提交
$ git add -p

#删除工作区文件，并且将这次删除放入暂存区
$ git rm [file1] [file2] ...

#停止追踪指定文件，但该文件会保留在工作区
$ git rm --cached [file]

#改名文件，并且将这个改名放入暂存区
$ git mv [file-original] [file-renamed]
```

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

```sh
# 修改远程仓库地址
$ git remote set-url origin <remote-url>

# 仓库路径查询查询
$ git remote -v

# 增加一个新的远程仓库，并命名为origin 
$ git remote add origin <你的项目地址> #注:项目地址形式为:https://gitee.com/xxx/xxx.git或者 git@gitee.com:xxx/xxx.git

# 删除指定的远程仓库
$ git remote rm origin

# 下载远程仓库的所有变动
$ git fetch [remote]

# 显示某个远程仓库的信息
$ git remote show [remote]

# 取回远程仓库的变化，并与本地分支合并
$ git pull [remote] [local-branch]

# 上传本地指定分支到远程仓库
$ git push [remote] [local-branch]

# 如果当前分支与多个分支存在追踪关系，-u 指定[remote] [local-branch]为默认分支，后面就可以不加任何参数使用 git push
$ git push -u [remote] [local-branch]
如：
$ git push -u origin master

# 强行推送当前分支到远程仓库，即使有冲突
$ git push [remote] --force

# 推送所有分支到远程仓库
$ git push [remote] --all

# 将本地分支推送到远程分支上，若无远程分支则自动创建
$ git push origin 远程分支名:远程分支名
```



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

```sh
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

```sh
#新建本地分支localserverfix，并且拉取并跟踪远程分支origin/serverfix
$ git checkout -b localserverfix origin/serverfix

#新建本地分支serverfix，并且拉取并跟踪远程分支origin/serverfix
$ git checkout --track origin/serverfix

#已有本地分支拉取并跟踪远程分支origin/serverfix
$ git branch -u/--set-upstream-to origin/serverfix
```

[Git 分支 - 远程分支](https://git-scm.com/book/zh/v2/Git-%E5%88%86%E6%94%AF-%E8%BF%9C%E7%A8%8B%E5%88%86%E6%94%AF)

```sh
# 列出所有本地分支，当前分支前面会添加一个星号
$ git branch

# 列出所有远程分支
$ git branch -r

# 列出所有本地分支和远程分支
$ git branch -a

# 新建一个分支，但依然停留在当前分支
$ git branch [branch-name]

# 新建一个分支，并切换到该分支
$ git checkout -b [branch]

# 将远程git仓库里的指定分支拉取到本地，同时在本地新建了一个分支，并和指定的远程分支关联了起来。
$ git checkout -b 本地分支名 origin/远程分支名

# 新建一个分支，指向指定commit
$ git branch [branch] [commit]

# 新建一个分支，与指定的远程分支建立追踪关系
$ git branch --track [branch] [remote-branch]

# 切换到指定分支，并更新工作区
$ git checkout [branch-name]

# 切换到上一个分支
$ git checkout -

# 建立追踪关系，在现有分支与指定的远程分支之间
$ git branch --set-upstream [remote-branch] [local-branch]

# 合并指定分支到当前分支
$ git merge [branch]

# 选择一个commit，合并进当前分支
$ git cherry-pick [commit]

# 删除分支
$ git branch -d [branch-name]

# 删除远程分支
$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]

# 查看本地分支及追踪的分支，可以显示本地所有分支
$ git branch -vv
```



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
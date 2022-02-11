### ucxmanager

1. 云龙compile下载镜像包

2. 登录大数据云平台

   > 账号密码
   >
   > h84170495 h84170495 1qw2!QW@
   > x84163359	hanjun	1qw2!QW@

   高级管理-->云市场-->镜像中心

   > 删除ucx_1111仓库中的镜像包再上传

   服务-->我发布的服务-->发布服务

   > 1. 导入配置文件Service_zh.yml，修改服务名称，点击服务订购免订购
   > 2. 修改镜相报broker和console，注意是https请求
   > 3. 高级配置中选择租户级服务
   > 4. 点击确定

   高级管理-->云市场-->我发布的服务

   > 查看服务ID

   查看《BDPAAS_v3r5c60spc200环境信息.xlsx》中sheet1，登录7.183.32.108 主节点查看日志

   > root/paas     Image0@Huawei123
   >
   > kubectl get pod -nbigdata |grep ****
   >
   > kubectl logs -f -nbigdata b-paas-bapizdh-o9i14-c99b898d4-55w2g



### Kerberos

> 10.247.75.222 :22
>
> root/bigdataPDU_123 

```sh
$ su - ilearning
$ cd /home/ilearning/hadoopclient
$ source bigdata_env
$ kinit -kt user.keytab xuexiao
$ beeline

#在kinit之后，获取指定location创建的数据库
$ hadoop fs -ls /tmp
```


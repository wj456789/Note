# Docker_inspect

```
docker container inspect重点字段介绍
State.OOMKilled：是否触发OOM(out of memory)
LogPath: 日志路径
RestartCount: 容器重启次数
Mounts.Source： 容器内挂载目录对应的宿主机路径
Mounts.Destination：容器内挂载路径
Config.ExposedPosrts：暴露的端口
Config.Env：容器内环境变量
Config.Cmd：指定字符串或字符串数组的运行命令。
Config.Entrypoint：entrypoint.sh脚本路径及参数
NetworkSettings.Networks.IPAddress：容器ip
NetworkSettings.Networks.Gateway：该网络的网关
HostConfig.Binds：该容器的卷绑定列表
HostConfig.NetworkMode：该容器的网络模型
HostConfig.PortBindings：容器与主机的端口映射关系
HostConfig.Privileged：特权模式
HostConfig.Sysctls：给容器设置内核参数

docker image inspect 字段说明
Cmd：容器启动时执行的命令
Config.ExposedPorts：镜像配置的暴露端口信息
Config.Env：镜像配置的环境变量
Architecture：镜像运行的CPU架构

docker volume inspect 字段说明
MountPoint：容器内挂载目录对应的宿主机路径
UsageData：卷使用详情
UsageData.Size：挂载卷已使用
UsageData.RefCount：该卷关联的容器数

docker network inspect 字段说明
IPAM.Config.Subnet：子网网段
IPAM.Config.Gateway：子网网关
Containers：关联的容器信息
```



| container                           |                                                              |
| ----------------------------------- | ------------------------------------------------------------ |
| 字段名                              | 字段说明                                                     |
| Id                                  | 容器完整id                                                   |
| State.status                        | 容器运行状态                                                 |
| State.OOMKilled                     | 是否出发OOM(out   of memory)                                 |
| State.pid                           | 该容器本身进程pid                                            |
| Image                               | 容器所用镜像id                                               |
| ResolvConfPath                      | 容器内resolv.conf文件在宿主机上的路径                        |
| HostnamePath                        | 容器内hostname文件在宿主机上的路径                           |
| HostsPath                           | 容器内hosts文件在宿主机上的路径                              |
| LogPath                             | 日志路径                                                     |
| Name                                | 容器名                                                       |
| RestartCount                        | 容器重启次数                                                 |
| Driver                              | 所用引擎                                                     |
| Mounts.Type                         | 挂载类型                                                     |
| Mounts.Name                         | 挂载卷id                                                     |
| Mounts.Source                       | 容器内挂载目录对应的宿主机路径                               |
| Mounts.Destination                  | 容器内挂载路径                                               |
| Mounts.RW                           | 可读写                                                       |
| Config.Hostname                     | 容器内主机名                                                 |
| Config.Domainname                   | 容器内域名                                                   |
| Config.User                         | 工作用户                                                     |
| Config.ExposedPosrts                | 暴露的端口                                                   |
| Config.Env                          | 容器内环境变量                                               |
| Config.Cmd                          | 指定字符串或字符串数组的运行命令。                           |
| Config.WorksDir                     | 工作路径                                                     |
| Config.Entrypoint                   | entrypoint.sh脚本路径及参数                                  |
| Config.Healthcheck.test             | 执行测试                                                     |
| Config.Healthcheck.Interval         | 检查间隔                                                     |
| Config.Healthcheck.Timeout          | 检查超时时间                                                 |
| Config.Healthcheck.Retries          | 重试次数                                                     |
| Config.Healthcheck.StartPeriod      | 开始检测之前等待容器初始化的时间                             |
| NetworkSettings.Bridge              | 桥接网络的名称                                               |
| NetworkSettings.Ports               | 容器端口映射关系                                             |
| NetworkSettings.IPAddress           | 容器ip                                                       |
| NetworkSettings.IPPrefixLen         | 掩码长度                                                     |
| NetworkSettings.MacAddress          | 容器物理地址                                                 |
| NetworkSettings.Networks.IPAddress  | 容器ip                                                       |
| NetworkSettings.Networks.Gateway    | 该网络的网关                                                 |
| NetworkSettings.Networks.MacAddress | 该网络的物理地址                                             |
| Path                                | 运行命令的路径                                               |
| HostConfig.Cpushares                | 该容器的cpu权重值                                            |
| HostConfig.Memory                   | 内存限制                                                     |
| HostConfig.BlkioWeight              | 块设备id权重(相对权重)                                       |
| HostConfig.BlkioWeightDevice        | 块设备id权重(相对设备权重)                                   |
| HostConfig.BlkioDeviceReadBps       | 限制读取速度                                                 |
| HostConfig.BlkioDeviceWriteBps      | 限制写速度                                                   |
| HostConfig.BlkioDeviceReadIOps      | 限制读IO速度                                                 |
| HostConfig.BlkioDeviceWriteIOps     | 限制写Io速度                                                 |
| HostConfig.CpuPeriod                | 容器使用CPU的时间周期                                        |
| HostConfig.CpuQuota                 | 时间周期内该容器能获取到的cpu时间                            |
| HostConfig.CpusetCpus               | 允许使用的cpu核数                                            |
| HostConfig.KernelMemoryTCP          | 内核   TCP 缓冲区内存的硬限制（以字节为单位）                |
| HostConfig.MemoryReservation        | 内存软限制                                                   |
| HostConfig.MemorySwap               | 总内存限制（内存   + 交换）。设置为 -1 以启用swap无限制      |
| HostConfig.MemorySwappiness         | 调整容器的内存交换行为。接受介于   0 和 100 之间的整数。     |
| HostConfig.OomKillDisable           | 是否禁用OOM                                                  |
| HostConfig.PidsLimit                | 调整容器的   PID 限制。将 0 或 -1 设置为无限制，或将 null 设置为不更改。 |
| HostConfig.Ulimits                  | 设置容器的资源限制列表                                       |
| HostConfig.Binds                    | 该容器的卷绑定列表                                           |
| HostConfig.LogConfig                | 该容器的日志配置                                             |
| HostConfig.NetworkMode              | 该容器的网络模型                                             |
| HostConfig.PortBindings             | 容器与主机的端口映射关系                                     |
| HostConfig.RestartPolicy            | 容器重启策略                                                 |
| HostConfig.VolumeDriver             | 容器挂载卷使用的驱动                                         |
| HostConfig.VolumesFrom              | 卷来自其他容器                                               |
| HostConfig.CapAdd                   | 允许的内核功能列表                                           |
| HostConfig.CapDrop                  | 禁用的内核功能列表                                           |
| HostConfig.Dns                      | dns服务器列表                                                |
| HostConfig.DnsSearch                | 搜索域列表                                                   |
| HostConfig.DnsOptions               | dns配置选项                                                  |
| HostConfig.ExtraHosts               | 增加到容器/etc/hosts文件中的ip与hostname的映射               |
| HostConfig.PidMode                  | 容器pid命名空间的模式                                        |
| HostConfig.Privileged               | 特权模式                                                     |
| HostConfig.ReadonlyRootfs           | 容器根文件系统以只读挂载                                     |
| HostConfig.Sysctls                  | 给容器设置内核参数                                           |
| HostConfig.Runtime                  | 容器运行时                                                   |
| GraphDriver.Name                    | 镜像存储引擎所使用的文件系统名                               |
| GraphDriver.Data.LowerDir           | 镜像层文件目录                                               |
| GraphDriver.Data.UpperDir           | 容器层文件目录                                               |
| GraphDriver.Data.WorkDir            | 修改镜像层文件是拷贝的临时目录                               |
| GraphDriver.Data.MergedDir          | 镜像层和容器层文件系统合并之后的最终状态                     |





| images              |                                    |
| ------------------- | ---------------------------------- |
| 字段名              | 字段说明                           |
| Id                  | 镜像完整id                         |
| RepoTags            | 镜像tag                            |
| Cmd                 | 容器启动时执行的命令               |
| Container           | 用来创建该镜像的容器id             |
| Config              | 在主机之间可移植的容器配置         |
| Config.ExposedPorts | 镜像配置的暴露端口信息             |
| Config.Env          | 镜像配置的环境变量                 |
| Config.Cmd          | 指定字符串或字符串数组的运行命令。 |
| Config.Image        | 上层镜像id                         |
| Architecture        | 镜像运行的CPU架构                  |
| Os                  | 镜像系统类型                       |
| Size                | 镜像大小                           |
| GraphDriver         | 镜像引擎信息                       |
| RootFS              | 根文件系统信息                     |
| Parent              | 下层镜像的id                       |







| volume             |                                |
| ------------------ | ------------------------------ |
| 字段名             | 字段说明                       |
| Driver             | 使用的卷驱动名称               |
| Labels             | 卷标签                         |
| MountPoint         | 容器内挂载目录对应的宿主机路径 |
| Name               | 卷名称                         |
| UsageData          | 卷使用详情                     |
| UsageData.Size     | 挂载卷已使用                   |
| UsageData.RefCount | 该卷关联的容器数               |







| network             |                 |
| ------------------- | --------------- |
| 字段名              | 字段说明        |
| Name                | network名称     |
| Id                  | network的完整id |
| Driver              | network类型     |
| IPAM.Config.Subnet  | 子网网段        |
| IPAM.Config.Gateway | 子网网关        |
| Containers          | 关联的容器信息  |





```sh
$ docker inspect a42d7bf373b9
[
    {
        "Id": "a42d7bf373b9578c1c0595989f13ee9f67022de085eaab1633b3bef357b848a5",
        "Created": "2022-09-26T15:16:05.335005358Z",
        "Path": "/bin/bash",
        "Args": [
            "/opt/redis-service/redis_entry.sh"
        ],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 55783,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2022-09-26T15:16:07.195217973Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:ae7a7f5d607be1ec6740b2065684f0122c30bb6eb4590e38faada150fc5999f0",
        "ResolvConfPath": "/opt/docker/containers/38204481ec92a3f8035f7592e2073b60f6e8b30463cf848198e32c39a6bdc305/resolv.conf",
        "HostnamePath": "/opt/docker/containers/38204481ec92a3f8035f7592e2073b60f6e8b30463cf848198e32c39a6bdc305/hostname",
        "HostsPath": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/etc-hosts",
        "LogPath": "/opt/docker/containers/a42d7bf373b9578c1c0595989f13ee9f67022de085eaab1633b3bef357b848a5/a42d7bf373b9578c1c0595989f13ee9f67022de085eaab1633b3bef357b848a5-json.log",
        "Name": "/k8s_redisserver_redis-server-0_kube-system_b4a1b345-2b1b-45e1-8023-e653313a7a2d_0",
        "RestartCount": 0,
        "Driver": "devicemapper",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": [
            "b6974c5308096b2872611175939c7205f0624da6b65c4454bac36c2741746946"
        ],
        "HostConfig": {
            "Binds": [
                "/opt/dspredis/redis/data:/opt/redis/data",
                "/var/paas/sys/log/redisservice/redis/server:/opt/redis/log",
                "/var/paas/sys/log/SrvBackupAgentLog/redisservice/redis:/opt/redis/backuplog",
                "/opt/dspredis/redis/conf:/opt/redis/conf",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/cipher-key-secret:/opt/redis/key/manage_key:ro",
                "/etc/localtime:/etc/localtime",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/redis-cipher-secret:/opt/redis/cipher:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/apm-config:/opt/redis/redis_alarm/configmap:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/apm-opr-key-secret:/opt/redis/redis_alarm/secret:ro",
                "/opt/backupagent/redis:/opt/redis/backupagent",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/redis-pwd-secret:/opt/redis/passwd:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/xms-sdk-certfiles:/opt/redis/xms_sdk_certfiles:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/xms-workspace:/opt/redis/xms_workspace:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/redis-server-crl-cfg:/opt/redis/crl:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/redis-log-level-configmap:/opt/redis/log_configmap:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~empty-dir/shared-data:/opt/install",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/middleware-agent-console-certfiles:/opt/redis-service/middleware/certs:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/middleware-agent-console-config:/opt/redis-service/middleware/config:ro",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/etc-hosts:/etc/hosts",
                "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/containers/redisserver/40aa07cb:/dev/termination-log"
            ],
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {
                    "max-file": "5",
                    "max-size": "10m"
                }
            },
            "NetworkMode": "container:38204481ec92a3f8035f7592e2073b60f6e8b30463cf848198e32c39a6bdc305",
            "PortBindings": null,
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "Dns": null,
            "DnsOptions": null,
            "DnsSearch": null,
            "ExtraHosts": null,
            "GroupAdd": [
                "10070"
            ],
            "IpcMode": "container:38204481ec92a3f8035f7592e2073b60f6e8b30463cf848198e32c39a6bdc305",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 996,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": [
                "seccomp=unconfined"
            ],
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "HookSpec": "",
            "CpuShares": 102,
            "Memory": 4294967296,
            "NanoCpus": 0,
            "CgroupParent": "/kubepods/burstable/podb4a1b345-2b1b-45e1-8023-e653313a7a2d",
            "BlkioWeight": 0,
            "BlkioWeightDevice": null,
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 100000,
            "CpuQuota": 200000,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "0-7",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DiskQuota": 0,
            "KernelMemory": 0,
            "MemoryReservation": 0,
            "MemorySwap": 4294967296,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": 0,
            "FilesLimit": 0,
            "Ulimits": [
                {
                    "Name": "nofile",
                    "Hard": 1000000,
                    "Soft": 1000000
                },
                {
                    "Name": "nproc",
                    "Hard": 20000,
                    "Soft": 10000
                },
                {
                    "Name": "memlock",
                    "Hard": -1,
                    "Soft": -1
                }
            ],
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "Hugetlbs": [],
            "MaskedPaths": [
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/asound",
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "DeviceId": "70",
                "DeviceName": "docker-253:4-4849687-40f8b55f6a7086af294b9ee9aaaa647dd0306729aec2ed11ffc3f5dc413932e5",
                "DeviceSize": "10737418240"
            },
            "Name": "devicemapper"
        },
        "Mounts": [
            {
                "Type": "bind",
                "Source": "/var/paas/sys/log/redisservice/redis/server",
                "Destination": "/opt/redis/log",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/cipher-key-secret",
                "Destination": "/opt/redis/key/manage_key",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/redis-cipher-secret",
                "Destination": "/opt/redis/cipher",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/etc-hosts",
                "Destination": "/etc/hosts",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/opt/dspredis/redis/conf",
                "Destination": "/opt/redis/conf",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/etc/localtime",
                "Destination": "/etc/localtime",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/apm-config",
                "Destination": "/opt/redis/redis_alarm/configmap",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/apm-opr-key-secret",
                "Destination": "/opt/redis/redis_alarm/secret",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/middleware-agent-console-config",
                "Destination": "/opt/redis-service/middleware/config",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/containers/redisserver/40aa07cb",
                "Destination": "/dev/termination-log",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/opt/dspredis/redis/data",
                "Destination": "/opt/redis/data",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/opt/backupagent/redis",
                "Destination": "/opt/redis/backupagent",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/xms-sdk-certfiles",
                "Destination": "/opt/redis/xms_sdk_certfiles",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/xms-workspace",
                "Destination": "/opt/redis/xms_workspace",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~empty-dir/shared-data",
                "Destination": "/opt/install",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/middleware-agent-console-certfiles",
                "Destination": "/opt/redis-service/middleware/certs",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/paas/sys/log/SrvBackupAgentLog/redisservice/redis",
                "Destination": "/opt/redis/backuplog",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~secret/redis-pwd-secret",
                "Destination": "/opt/redis/passwd",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/redis-server-crl-cfg",
                "Destination": "/opt/redis/crl",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            },
            {
                "Type": "bind",
                "Source": "/var/lib/kubelet/pods/b4a1b345-2b1b-45e1-8023-e653313a7a2d/volumes/kubernetes.io~configmap/redis-log-level-configmap",
                "Destination": "/opt/redis/log_configmap",
                "Mode": "ro",
                "RW": false,
                "Propagation": "rprivate"
            }
        ],
        "Config": {
            "Hostname": "redis-server-0",
            "Domainname": "",
            "User": "10070",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "16379/tcp": {},
                "26379/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "memory=4",
                "update_parameters=update:true",
                "BUSINESS_PASSWORD_SECRET_PATH=/opt/redis/passwd",
                "XMS_SDK_SECRET_PATH=/opt/redis/xms_sdk_certfiles",
                "LOG_LEVEL_CONFIGMAP_PATH=/opt/redis/log_configmap",
                "stsname=redis",
                "sentinel_port=26379",
                "username=ossdbuser",
                "health_interval=15",
                "aof_mode=off",
                "REDIS_POD_NAME=redis-server-0",
                "XMS_SERVICE_URL_PATH=/opt/redis/redis_alarm/configmap/xms_service_url",
                "KUBE_POD_CLUSTERID=11111111-1111-1111-1111-111111111111",
                "opensource_mode=off",
                "docker_mode=on",
                "KEY_PATH=/opt/redis/key/manage_key",
                "KUBE_POD_NODE_NAME=kube-system-group1-node1",
                "KUBE_POD_NAMESPACE=kube-system",
                "db_name=sysmgrrdb",
                "admin_username=admin",
                "aof_rewrite_min_size=1024",
                "redis_service_name=redis-cluster",
                "memory_limit=0.45",
                "maxmemory_policy_mode=noeviction",
                "APM_OPR_KEY_SECRET_DIR=/opt/redis/redis_alarm/secret",
                "install_path=/opt",
                "server_replicas=1",
                "sentinel_group=redissentinel",
                "REDIS_NODE_IP=7.220.28.204",
                "MIDDLEWARE_CONSOLE_PATH=/opt/redis-service/middleware",
                "isStatic=yes",
                "read_username=readdbuser",
                "sentinel_replicas=1",
                "XMS_WORKSPACE_DIR=/opt/redis/xms_workspace",
                "XMS_CRL_PATH=/opt/redis/crl/xms-crl.crl",
                "stateful_name=redis-server",
                "redis_port=16379",
                "redis_path=/opt/dspredis/redis",
                "ZOOKEEPER_SVC_PORT_4096_TCP_PORT=4096",
                "IAM_ER_PORT_32943_TCP_PROTO=tcp",
                "SMGR_SERVICE_PORT=30208",
                "SMGR_SERVICE_HOST=10.247.133.63",
                "LB_SERVICE_MANAGE_PORT_18443_TCP_PROTO=tcp",
                "IAM_PORT_31943_TCP_PORT=31943",
                "GAUSSDB_PRIMARY_6ECE93BB_PORT_15432_TCP_PORT=15432",
                "IAM_BER_PORT_28330_TCP_ADDR=10.247.92.111",
                "ALM_ALMRECEIVER_PORT=tcp://10.247.5.185:8065",
                "LICENSE_PROXY_PORT_9450_TCP_PROTO=tcp",
                "KUBE_DNS_SERVICE_PORT=53",
                "LB_SERVICE_EXTERNAL_PORT_38080_TCP_PROTO=tcp",
                "AMS_ACCESS_PORT=tcp://10.247.116.187:8149",
                "LICENSE_PROXY_SERVICE_PORT_LICENSE_PROXY=9450",
                "SMGR_PORT_30208_TCP=tcp://10.247.133.63:30208",
                "KUBE_DNS_SERVICE_PORT_DNS_TCP=53",
                "AMS_ACCESS_PORT_8149_TCP=tcp://10.247.116.187:8149",
                "AMS_ACCESS_PORT_8149_TCP_ADDR=10.247.116.187",
                "AOS_PORT=tcp://10.247.47.13:31800",
                "AMS_ACCESS_SERVICE_HOST=10.247.116.187",
                "IAM_BER_SERVICE_PORT=28330",
                "IAM_PORT=tcp://10.247.69.221:31943",
                "AMS_ACCESS_SERVICE_PORT_AMS_ACCESS=8149",
                "LB_SERVICE_MANAGE_SERVICE_PORT=18443",
                "KUBE_DNS_PORT_53_UDP_PROTO=udp",
                "IAM_PORT_31943_TCP=tcp://10.247.69.221:31943",
                "LB_SERVICE_MANAGE_PORT=tcp://10.247.64.189:18443",
                "LB_SERVICE_MANAGE_PORT_18443_TCP=tcp://10.247.64.189:18443",
                "IAM_SERVICE_PORT=31943",
                "ZOOKEEPER_SVC_PORT_4096_TCP=tcp://10.247.90.177:4096",
                "LICENSE_PROXY_PORT_9450_TCP_PORT=9450",
                "KUBERNETES_SERVICE_PORT=443",
                "KUBERNETES_PORT_443_TCP_PORT=443",
                "LB_SERVICE_MANAGE_PORT_18443_TCP_PORT=18443",
                "IAM_BER_PORT_28330_TCP_PROTO=tcp",
                "ZOOKEEPER_SVC_SERVICE_HOST=10.247.90.177",
                "KUBE_DNS_PORT_53_TCP_ADDR=10.247.0.10",
                "ALM_ALMRECEIVER_PORT_8065_TCP=tcp://10.247.5.185:8065",
                "AOS_SERVICE_PORT=31800",
                "IAM_PORT_31943_TCP_ADDR=10.247.69.221",
                "KUBERNETES_PORT_443_TCP_PROTO=tcp",
                "LB_SERVICE_EXTERNAL_PORT_38080_TCP=tcp://10.247.99.127:38080",
                "ALM_ALMRECEIVER_PORT_8065_TCP_PORT=8065",
                "IAM_ER_SERVICE_PORT_IAM_ER=32943",
                "LICENSE_PROXY_SERVICE_HOST=10.247.71.68",
                "LICENSE_PROXY_PORT_9450_TCP_ADDR=10.247.71.68",
                "ZOOKEEPER_SVC_PORT=tcp://10.247.90.177:4096",
                "LB_SERVICE_EXTERNAL_PORT=tcp://10.247.99.127:38080",
                "IAM_SERVICE_HOST=10.247.69.221",
                "GAUSSDB_PRIMARY_6ECE93BB_PORT_15432_TCP_PROTO=tcp",
                "KUBE_DNS_PORT_53_TCP_PROTO=tcp",
                "IAM_BER_SERVICE_HOST=10.247.92.111",
                "IAM_ER_SERVICE_HOST=10.247.0.185",
                "KUBE_DNS_SERVICE_HOST=10.247.0.10",
                "AMS_ACCESS_PORT_8149_TCP_PORT=8149",
                "LB_SERVICE_EXTERNAL_SERVICE_PORT=38080",
                "LICENSE_PROXY_SERVICE_PORT=9450",
                "LICENSE_PROXY_PORT=tcp://10.247.71.68:9450",
                "LICENSE_PROXY_PORT_9450_TCP=tcp://10.247.71.68:9450",
                "KUBE_DNS_PORT_53_TCP_PORT=53",
                "ALM_ALMRECEIVER_PORT_8065_TCP_PROTO=tcp",
                "IAM_PORT_31943_TCP_PROTO=tcp",
                "GAUSSDB_PRIMARY_6ECE93BB_PORT=tcp://10.247.15.253:15432",
                "AOS_PORT_31800_TCP_PROTO=tcp",
                "KUBERNETES_SERVICE_HOST=10.247.0.2",
                "KUBERNETES_PORT_443_TCP=tcp://10.247.0.2:443",
                "LB_SERVICE_EXTERNAL_SERVICE_PORT_SERVICE_38080=38080",
                "GAUSSDB_PRIMARY_6ECE93BB_SERVICE_HOST=10.247.15.253",
                "IAM_ER_PORT=tcp://10.247.0.185:32943",
                "IAM_ER_PORT_32943_TCP_PORT=32943",
                "KUBE_DNS_PORT_53_TCP=tcp://10.247.0.10:53",
                "IAM_SERVICE_PORT_IAM=31943",
                "GAUSSDB_PRIMARY_6ECE93BB_PORT_15432_TCP=tcp://10.247.15.253:15432",
                "GAUSSDB_PRIMARY_6ECE93BB_PORT_15432_TCP_ADDR=10.247.15.253",
                "ZOOKEEPER_SVC_SERVICE_PORT=4096",
                "KUBERNETES_PORT_443_TCP_ADDR=10.247.0.2",
                "KUBERNETES_SERVICE_PORT_HTTPS=443",
                "IAM_BER_PORT_28330_TCP=tcp://10.247.92.111:28330",
                "KUBE_DNS_PORT_53_UDP_PORT=53",
                "ZOOKEEPER_SVC_PORT_4096_TCP_ADDR=10.247.90.177",
                "LB_SERVICE_MANAGE_SERVICE_HOST=10.247.64.189",
                "AOS_PORT_31800_TCP=tcp://10.247.47.13:31800",
                "ALM_ALMRECEIVER_PORT_8065_TCP_ADDR=10.247.5.185",
                "IAM_ER_PORT_32943_TCP=tcp://10.247.0.185:32943",
                "IAM_ER_PORT_32943_TCP_ADDR=10.247.0.185",
                "AMS_ACCESS_PORT_8149_TCP_PROTO=tcp",
                "ZOOKEEPER_SVC_PORT_4096_TCP_PROTO=tcp",
                "SMGR_PORT=tcp://10.247.133.63:30208",
                "SMGR_PORT_30208_TCP_ADDR=10.247.133.63",
                "AOS_PORT_31800_TCP_PORT=31800",
                "LB_SERVICE_EXTERNAL_SERVICE_HOST=10.247.99.127",
                "LB_SERVICE_EXTERNAL_PORT_38080_TCP_ADDR=10.247.99.127",
                "AOS_SERVICE_HOST=10.247.47.13",
                "AOS_SERVICE_PORT_AOS=31800",
                "AOS_PORT_31800_TCP_ADDR=10.247.47.13",
                "ALM_ALMRECEIVER_SERVICE_HOST=10.247.5.185",
                "ALM_ALMRECEIVER_SERVICE_PORT=8065",
                "KUBE_DNS_SERVICE_PORT_DNS=53",
                "SMGR_PORT_30208_TCP_PROTO=tcp",
                "SMGR_PORT_30208_TCP_PORT=30208",
                "SMGR_SERVICE_PORT_SMGR=30208",
                "KUBE_DNS_PORT_53_UDP_ADDR=10.247.0.10",
                "AMS_ACCESS_SERVICE_PORT=8149",
                "IAM_BER_SERVICE_PORT_IAM_BER=28330",
                "ALM_ALMRECEIVER_SERVICE_PORT_ALM_ALMRECEIVER=8065",
                "IAM_ER_SERVICE_PORT=32943",
                "ZOOKEEPER_SVC_SERVICE_PORT_CLIENT=4096",
                "KUBE_DNS_PORT_53_UDP=udp://10.247.0.10:53",
                "KUBERNETES_PORT=tcp://10.247.0.2:443",
                "LB_SERVICE_EXTERNAL_PORT_38080_TCP_PORT=38080",
                "LB_SERVICE_MANAGE_SERVICE_PORT_SERVER=18443",
                "LB_SERVICE_MANAGE_PORT_18443_TCP_ADDR=10.247.64.189",
                "IAM_BER_PORT_28330_TCP_PORT=28330",
                "KUBE_DNS_PORT=udp://10.247.0.10:53",
                "IAM_BER_PORT=tcp://10.247.92.111:28330",
                "GAUSSDB_PRIMARY_6ECE93BB_SERVICE_PORT=15432",
                "PAAS_POD_ID=b4a1b345-2b1b-45e1-8023-e653313a7a2d",
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/bash",
                "/opt/redis-service/redis_entry.sh"
            ],
            "Healthcheck": {
                "Test": [
                    "NONE"
                ]
            },
            "ArgsEscaped": true,
            "Image": "7.220.28.71:20202/kube-system/redis:2.1.15",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "annotation.io.kubernetes.container.hash": "35555996",
                "annotation.io.kubernetes.container.ports": "[{\"name\":\"redis\",\"containerPort\":16379,\"protocol\":\"TCP\"}]",
                "annotation.io.kubernetes.container.preStopHandler": "{\"exec\":{\"command\":[\"/bin/sh\",\"/opt/redis-service/prestop.sh\"]}}",
                "annotation.io.kubernetes.container.restartCount": "0",
                "annotation.io.kubernetes.container.terminationMessagePath": "/dev/termination-log",
                "annotation.io.kubernetes.container.terminationMessagePolicy": "File",
                "annotation.io.kubernetes.pod.terminationGracePeriod": "30",
                "annotation.ulimit": "[{\"Name\":\"nofile\",\"Hard\":1000000,\"Soft\":1000000},{\"Name\":\"nproc\",\"Hard\":20000,\"Soft\":10000}]",
                "from": "[euler_x86/eulerx86lib:2.1.1]",
                "io.kubernetes.container.logpath": "/var/log/pods/kube-system_redis-server-0_b4a1b345-2b1b-45e1-8023-e653313a7a2d/redisserver/0.log",
                "io.kubernetes.container.name": "redisserver",
                "io.kubernetes.docker.type": "container",
                "io.kubernetes.pod.name": "redis-server-0",
                "io.kubernetes.pod.namespace": "kube-system",
                "io.kubernetes.pod.uid": "b4a1b345-2b1b-45e1-8023-e653313a7a2d",
                "io.kubernetes.sandbox.id": "38204481ec92a3f8035f7592e2073b60f6e8b30463cf848198e32c39a6bdc305"
            },
            "Annotations": {
                "native.umask": "secure"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "",
            "Gateway": "",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "",
            "IPPrefixLen": 0,
            "IPv6Gateway": "",
            "MacAddress": "",
            "Networks": {}
        }
    }
]

```




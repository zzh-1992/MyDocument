# 1.0docker相关命令

## 1.1 from

- 基础镜像，当前镜像是基于哪个镜像的

## 1.2 maintainer

- 镜像维护者的姓名和邮箱地址

## 1.3 run

- 容器构建时需要允许的命令

## 1.4 expose

- 当前容器对外暴露的端口

## 1.5 workdir

- 指定在容器创建后，终端默认登陆的进来的工作目录，一个落脚点

## 1.6 env

- 用来构建镜像过程中设置环境变量

## 1.7 add

- 将宿主机目录下的文件拷贝进镜像并且add命令会自动处理url和解压tar压缩包

## 1.8 copy

- 类似add，拷贝文件和目录到镜像中。 将从构建上下文目录中<源路径>的文件/目录复制到新的一层的镜像内的<目标路径>位置

## 1.9 volume

- 容器数据卷，用于数据保存和持久化工作

## 1.10 cmd

- 指定一个容器启动时要运行的命令 DockerFile中可以有多个cmd命令，但只有最后一个生效，cmd会被docker run之后但参数替换

## 1.11 entrypoint

- 指定一个容器启动时要运行的命令

## 1.12 onbuild

- 当构建一个被继承的dockerfile时运行命令，父镜像在被子镜像继承后父镜像的onbulid被触发

# 2.0 docker安装及配置

## 2.1 安装docker

```shell
yum -y install docker
```

## 2.2 开启dockers

```shell
systemctl start docker
```

## 2.、设置开机自动开启docker

```shell
systemctl enable docker
```

## 2.4 检查dockers版本

```shell
docker version
```

## 2.5 idea远程连接docker（添加-H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock）

```shell
vim /usr/lib/systemd/system/docker.service(编辑)
ExecStart=/usr/bin/dockerd-current -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock \
```

- 重新加载配并重启docker：

```shell
systemctl daemon-reload && systemctl restart docker
```


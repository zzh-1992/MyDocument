# Arthas
## alibba官方链接:
https://arthas.aliyun.com/doc/

## 下载地址: arthas-bin.zip
https://github.com/alibaba/arthas/releases

## 使用方式:

1. 下载到本地后解压,在当前窗口开启终端
2. 启动jar包

```shell
java -jar arthas-boot.jar
```

若是机器上有多个java进程,可以使用以下命令查询对应到Java进程(格式:lsof -i:port)

```shell
lsof -i:3456

COMMAND   PID          USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    38516 zhihuangzhang   67u  IPv6 0xa1204f5c9fefd701      0t0  TCP *:vat (LISTEN)
java    38516 zhihuangzhang   78u  IPv6 0xa1204f5ca6593e01      0t0  TCP [::127.0.0.1]:vat->[::127.0.0.1]:56189 (CLOSED)
```

启动后显示如下信息

```shell
[INFO] arthas-boot version: 3.6.5
[INFO] Found existing java process, please choose one and input the serial number of the process, eg : 1. Then hit ENTER.
* [1]: 13536 com.install4j.runtime.launcher.MacLauncher
  [2]: 38513 org.jetbrains.jps.cmdline.Launcher
  [3]: 13505 com.grapefruit.consumer.B_consumer_8071_hystrix
  [4]: 13491 com.grapefruit.servercenter.A_server_8888
  [5]: 38516 com.grape.httptest.HttpTestApplication
  [6]: 4709 
  [7]: 13510 org.jetbrains.jps.cmdline.Launcher
  [8]: 13511 com.grapefruit.provider.C_provider_8082
  [9]: 6760 com.sun.tools.hat.Main
  [10]: 37982 org.jetbrains.idea.maven.server.RemoteMavenServer36
```

选择对应pid到序号(这边需要监听端口号为:3456,pid为38516)

```shell
5
```

成功后会显示如下信息

```shell
5
[INFO] arthas home: /Users/zhihuangzhang/Downloads/arthas-bin
[INFO] Try to attach process 38516
[INFO] Attach process 38516 success.
[INFO] arthas-client connect 127.0.0.1 3658
  ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.                           
 /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'                          
|  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.                          
|  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |                         
`--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'                          

wiki       https://arthas.aliyun.com/doc                                        
tutorials  https://arthas.aliyun.com/doc/arthas-tutorials.html                  
version    3.6.5                                                                
main_class com.grape.httptest.HttpTestApplication                               
pid        38516                                                                
time       2022-08-27 13:50:51                                                  

[arthas@38516]$ sc HttpTestApplication
Affect(row-cnt:0) cost in 34 ms.

```

使用trace命令,查看方法调用耗时(格式:trace 全类名 方法名)

```shell
trace com.grape.httptest.Health healthSleep
```

出现如下信息后就可以开始调用接口

```shell
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 88 ms, listenerId: 1
```

触发接口

```shell
curl --location --request GET 'http://127.0.0.1:3456/sleep?time=20&tryOOM=true&count=10'
```

```shell
`---ts=2022-08-27 13:52:00;thread_name=http-nio-3456-exec-2;id=18;is_daemon=true;priority=5;TCCL=org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader@1b822fcc
    `---[138.390862ms] com.grape.httptest.Health:healthSleep()
        +---[84.01% 116.267177ms ] com.grape.httptest.service.MyService:getDataFromDb() #46
        `---[1.34% 1.84855ms ] com.grape.httptest.utils.StringUtil:getStr() #47
```

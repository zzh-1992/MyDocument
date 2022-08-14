





# 1.查看tcp连接(统计各种状态的数量)

```shell
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'
```
```shell
LAST_ACK 24
CLOSE_WAIT 1704
ESTABLISHED 1712
FIN_WAIT2 1
```



# 2.linux查看指定端口tcp连接情况(结果可能比较多)

```shell
netstat -nat |grep  -i 8888
```


```shell
lsof -i:"port" 
lsof -i:8888
```

# 3.过滤出状态为CLOSE_WAIT的连接
```shell
netstat -antp | grep CLOSE_WAIT
```


## 4.生成java dump hprof文件的

test.hprof ：dump后的文件名

23933:java进程id（可以使用jps 或ps -ef | grep java查看）

```shell
jmap -dump:file=error.hprof,format=b 2038
```

# 5.查看JVM内存配置

```she
jmap -heap 16548    （进程id）
```
```shell
Attaching to process ID 16548, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.291-b10

using thread-local object allocation.
Mark Sweep Compact GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 482344960 (460.0MB)
   NewSize                  = 10485760 (10.0MB)
   MaxNewSize               = 160759808 (153.3125MB)
   OldSize                  = 20971520 (20.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 74514432 (71.0625MB)
   used     = 51462504 (49.078468322753906MB)
   free     = 23051928 (21.984031677246094MB)
   69.06380766614446% used
Eden Space:
   capacity = 66256896 (63.1875MB)
   used     = 51462504 (49.078468322753906MB)
   free     = 14794392 (14.109031677246094MB)
   77.67116648507047% used
From Space:
   capacity = 8257536 (7.875MB)
   used     = 0 (0.0MB)
   free     = 8257536 (7.875MB)
   0.0% used
To Space:
   capacity = 8257536 (7.875MB)
   used     = 0 (0.0MB)
   free     = 8257536 (7.875MB)
   0.0% used
tenured generation:
   capacity = 165453824 (157.7890625MB)
   used     = 99272248 (94.67339324951172MB)
   free     = 66181576 (63.11566925048828MB)
   59.99997195592167% used

14630 interned Strings occupying 1295576 bytes.
```

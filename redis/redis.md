
# redis:key-value 存储系统，是跨平台的非关系型数据库

## 1.0 数据类型
###1.1 Redis String
```shell
    普通设值
    > set mykey newval nx
    (nil)
    > set mykey newval xx
    OK

    原子增量

    > set counter 100
    OK
    > incr counter
    (integer) 101
    > incr counter
    (integer) 102
    > incrby counter 50
    (integer) 152
```


### 1.2 Redis Lists
```shell
 > rpush mylist A
    (integer) 1
    > rpush mylist B
    (integer) 2
    > lpush mylist first
    (integer) 3
    > lrange mylist 0 -1
    1) "first"
    2) "A"
    3) "B"

    > rpush mylist 1 2 3 4 5 "foo bar"
    (integer) 9
    > lrange mylist 0 -1
    1) "first"
    2) "A"
    3) "B"
    4) "1"
    5) "2"
    6) "3"
    7) "4"
    8) "5"
    9) "foo bar"
```
   

### 1.3 Redis Hashes
```shell
> hmset user:1000 username antirez birthyear 1977 verified 1
    OK
    > hget user:1000 username
    "antirez"
    > hget user:1000 birthyear
    "1977"
    > hgetall user:1000
    1) "username"
    2) "antirez"
    3) "birthyear"
    4) "1977"
    5) "verified"
    6) "1"
```

### 1.4 Redis Sets
```shell
 > sadd myset 1 2 3
    (integer) 3
    > smembers myset
    1. 3
    2. 1
    3. 2
```

### 1.5 Redis Sorted sets
```shell
    > zadd hackers 1940 "Alan Kay"
    (integer) 1
    > zadd hackers 1957 "Sophie Wilson"
    (integer) 1
    > zadd hackers 1953 "Richard Stallman"
    (integer) 1
    > zadd hackers 1949 "Anita Borg"
    (integer) 1
    > zadd hackers 1965 "Yukihiro Matsumoto"
    (integer) 1
    > zadd hackers 1914 "Hedy Lamarr"
    (integer) 1
    > zadd hackers 1916 "Claude Shannon"
    (integer) 1
    > zadd hackers 1969 "Linus Torvalds"
    (integer) 1
    > zadd hackers 1912 "Alan Turing"
    (integer) 1
```

## 2.0 集群

### 2.1 创建集群的命令
```shell
    redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 \
    127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
    --cluster-replicas 1
```

### 2.2 集群槽数16384
```shell
    There are 16384 hash slots in Redis Cluster, and to compute
    what is the hash slot of a given key, we simply take the 
    CRC16 of the key modulo 16384.
```


## 3.0 Redis 持久化
### RDB（Redis 数据库）
- RDB 持久性以指定的时间间隔执行数据集的时间点快照。
### AOF（Append Only File）
- AOF 持久化记录服务器收到的每个写操作，
        在服务器启动时会再次播放，重建原始数据集。命令使用与Redis协议本身相同的格式
        以仅附加的方式记录。当日志变得太大时，Redis 能够在后台重写日志。










# 保存墓碑（原文Tombstones to the rescue 墓碑营救?）

在Cassandra语境中，墓碑是一种特殊的数据和普通数据一样存储，一个删除操作，就是写入一个墓碑。
当Cassandra读取数据的时候，它会合并这些内存里或者磁盘上写入的数据行。然后使用一种最新写入胜出(LWW)
算法选择出正确的数据，不管它是个标准写入的数据，还是一个墓碑。

举例：

我们看下接下来的例子，背景是Cassandra 3.7集群，有3个节点(
通过ccm创建的,译者注：ccm是个脚本程序可以快速的删除创建一个小Cassandra集群,github地址是：https://github.com/pcmanus/ccm)

```cql
CREATE KEYSPACE tlp_lab WITH replication = {‘class’: ‘NetworkTopologyStrategy’, ‘datacenter1’ : 3};
CREATE TABLE tlp_lab.tombstones (fruit text, date text, crates set<int>, PRIMARY KEY (fruit, date));
```

插入一些数据，每天创建一些水果（译者注：表里的字段的含义），如下：

```cql
INSERT INTO tlp_lab.tombstones (fruit, date, crates) VALUES (‘apple’, ‘20160616’, {1,2,3,4,5});
INSERT INTO tlp_lab.tombstones (fruit, date, crates) VALUES (‘apple’, ‘20160617’, {1,2,3});
INSERT INTO tlp_lab.tombstones (fruit, date, crates) VALUES (‘pickles’, ‘20160616’, {6,7,8}) USING TTL 2592000;
```

下面就是我们存储的数据：

```cql
alain$ echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
———+———-+—————–
apple | 20160616 | {1, 2, 3, 4, 5}
apple | 20160617 | {1, 2, 3}
pickles | 20160616 | {6, 7, 8}
```

现在我们需要手动刷新数据（即写入一个磁盘文件，同时释放内存），在内存里的墓碑，更确切讲是在memtable
（译者注：数据在磁盘上的文件叫sstable，在内存里的数据结构叫memtable）里的墓碑，
会覆盖已经在内存里的数据，这和磁盘上的sstable还不一样.(
译者注：磁盘里写入以后，文件就不能改变了，内存里如果你刚刚写入，然后再去删除
，删除插入的墓碑可能会优化，覆盖之前的数据，最后合并后写入磁盘。这里执行一下刷新到磁盘，可以更方便你理解墓碑原理)。

刷新的操作：

```shell
nodetool -p 7100 flush
```

我们会在磁盘上看到：

```shell
alain$ ll /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/
total 72
drwxr-xr-x 11 alain staff 374 Jun 16 20:53 .
drwxr-xr-x 3 alain staff 102 Jun 16 20:25 ..
drwxr-xr-x 2 alain staff 68 Jun 16 17:05 backups
-rw-r–r– 1 alain staff 43 Jun 16 20:53 mb-5-big-CompressionInfo.db
-rw-r–r– 1 alain staff 127 Jun 16 20:53 mb-5-big-Data.db
-rw-r–r– 1 alain staff 10 Jun 16 20:53 mb-5-big-Digest.crc32
-rw-r–r– 1 alain staff 16 Jun 16 20:53 mb-5-big-Filter.db
-rw-r–r– 1 alain staff 20 Jun 16 20:53 mb-5-big-Index.db
-rw-r–r– 1 alain staff 4740 Jun 16 20:53 mb-5-big-Statistics.db
-rw-r–r– 1 alain staff 61 Jun 16 20:53 mb-5-big-Summary.db
-rw-r–r– 1 alain staff 92 Jun 16 20:53 mb-5-big-TOC.txt
```

为了得到可以方便阅读的格式，我们需要用SSTabledump工具对这个sstable文件进行个转换：

```shell
alain$ SSTabledump
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-5-big-Data.db
```

```json
[
  {
    "partition": {
      "key": [
        "apple"
      ],
      "position": 0
    },
    "rows": [
      {
        "type": "row",
        "position": 19,
        "clustering": [
          "20160616"
        ],
        "liveness_info": {
          "tstamp": "2016-06-16T18: 52: 41.900451Z"
        },
        "cells": [
          {
            "name": "crates",
            "deletion_info": {
              "marked_deleted": "2016-06-16T18: 52: 41.900450Z",
              "local_delete_time": "2016-06-16T18: 52: 41Z"
            }
          },
          {
            "name": "crates",
            "path": [
              "1"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "2"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "3"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "4"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "5"
            ],
            "value": ""
          }
        ]
      },
      {
        "type": "row",
        "position": 66,
        "clustering": [
          "20160617"
        ],
        "liveness_info": {
          "tstamp": "2016-06-16T18: 52: 41.902093Z"
        },
        "cells": [
          {
            "name": "crates",
            "deletion_info": {
              "marked_deleted": "2016-06-16T18: 52: 41.902092Z",
              "local_delete_time": "2016-06-16T18: 52: 41Z"
            }
          },
          {
            "name": "crates",
            "path": [
              "1"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "2"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "3"
            ],
            "value": ""
          }
        ]
      }
    ]
  },
  {
    "partition": {
      "key": [
        "pickles"
      ],
      "position": 104
    },
    "rows": [
      {
        "type": "row",
        "position": 125,
        "clustering": [
          "20160616"
        ],
        "liveness_info": {
          "tstamp": "2016-06-16T18: 52: 41.903751Z",
          "ttl": 2592000,
          "expires_at": "2016-07-16T18: 52: 41Z",
          "expired": false
        },
        "cells": [
          {
            "name": "crates",
            "deletion_info": {
              "marked_deleted": "2016-06-16T18: 52: 41.903750Z",
              "local_delete_time": "2016-06-16T18: 52: 41Z"
            }
          },
          {
            "name": "crates",
            "path": [
              "6"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "7"
            ],
            "value": ""
          },
          {
            "name": "crates",
            "path": [
              "8"
            ],
            "value": ""
          }
        ]
      }
    ]
  }
]
```

现在两个分区键数据 (3 行, 其中两行有共同的分区主键) 已经存到磁盘上了。

除非注明，赵岩的博客文章均为原创，转载请以链接形式标明本文地址
本文地址：https://zhaoyanblog.com/archives/969.html
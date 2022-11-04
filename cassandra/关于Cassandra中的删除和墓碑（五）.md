# 减轻墓碑带来的麻烦

好了，现在我们已经明白为什么我们要用墓碑，我们对墓碑也有一个大致的了解了。现在让我们看看墓碑会引起哪些潜在的麻烦，
我们可以采取哪些措施来减轻这些麻烦。

首先一个很显而易见的事情就是墓碑没有让数据被删掉，反而增加了存储。我们需要删除这些墓碑以腾出磁盘空间，
并且限制读出无用数据的大小，以降低时延和提高资源利用率。这个事情就发生在接下来你看到的压实的过程。

# 压实（Compactions）

当我们读取某一行数据的时候，为了读取到这一行数据的所有片段，我们翻阅的SSTables阅读，读时延就越大。
因此我们有必要把这些片段通过压实的过程把他们合并，以获得更低的读时延。这个过程包括把合适的目标也清除掉，
如我所愿的持续释放可用的空间。

压实的过程是通过合并来自多个sstable的row片段，去删除满足一定条件的墓碑。有些条件是在表的schema中指定的，
而且是可以优化可调节的，比如gc_grace_seconds参数，有些条件是cassandra内部的，代码里写死的，
这是为了保证数据持久化和一致性。要保证没有参与当前压实的sstable（重叠sstables）
里没有新的数据片段，这是防止墓碑被清掉以后，数据又出现成为僵尸数据的必要条件。

再看上面的例子，经过删除和flush以后，表数据目录大致如下：

```shell
alain$ ll /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/
total 360
drwxr-xr-x  43 alain  staff  1462 Jun 17 11:39 .
drwxr-xr-x   3 alain  staff   102 Jun 16 20:25 ..
drwxr-xr-x   2 alain  staff    68 Jun 16 17:05 backups
-rw-r–r–   1 alain  staff    43 Jun 17 11:13 mb-10-big-CompressionInfo.db
-rw-r–r–   1 alain  staff    43 Jun 17 11:13 mb-10-big-Data.db
-rw-r–r–   1 alain  staff    10 Jun 17 11:13 mb-10-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 11:13 mb-10-big-Filter.db
-rw-r–r–   1 alain  staff     9 Jun 17 11:13 mb-10-big-Index.db
-rw-r–r–   1 alain  staff  4701 Jun 17 11:13 mb-10-big-Statistics.db
-rw-r–r–   1 alain  staff    59 Jun 17 11:13 mb-10-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 11:13 mb-10-big-TOC.txt
-rw-r–r–   1 alain  staff    43 Jun 17 11:33 mb-11-big-CompressionInfo.db
-rw-r–r–   1 alain  staff    53 Jun 17 11:33 mb-11-big-Data.db
-rw-r–r–   1 alain  staff     9 Jun 17 11:33 mb-11-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 11:33 mb-11-big-Filter.db
-rw-r–r–   1 alain  staff     9 Jun 17 11:33 mb-11-big-Index.db
-rw-r–r–   1 alain  staff  4611 Jun 17 11:33 mb-11-big-Statistics.db
-rw-r–r–   1 alain  staff    59 Jun 17 11:33 mb-11-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 11:33 mb-11-big-TOC.txt
-rw-r–r–   1 alain  staff    43 Jun 17 11:33 mb-12-big-CompressionInfo.db
-rw-r–r–   1 alain  staff    42 Jun 17 11:33 mb-12-big-Data.db
-rw-r–r–   1 alain  staff    10 Jun 17 11:33 mb-12-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 11:33 mb-12-big-Filter.db
-rw-r–r–   1 alain  staff     9 Jun 17 11:33 mb-12-big-Index.db
-rw-r–r–   1 alain  staff  4611 Jun 17 11:33 mb-12-big-Statistics.db
-rw-r–r–   1 alain  staff    59 Jun 17 11:33 mb-12-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 11:33 mb-12-big-TOC.txt
-rw-r–r–   1 alain  staff    43 Jun 17 11:39 mb-13-big-CompressionInfo.db
-rw-r–r–   1 alain  staff    32 Jun 17 11:39 mb-13-big-Data.db
-rw-r–r–   1 alain  staff     9 Jun 17 11:39 mb-13-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 11:39 mb-13-big-Filter.db
-rw-r–r–   1 alain  staff    11 Jun 17 11:39 mb-13-big-Index.db
-rw-r–r–   1 alain  staff  4591 Jun 17 11:39 mb-13-big-Statistics.db
-rw-r–r–   1 alain  staff    65 Jun 17 11:39 mb-13-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 11:39 mb-13-big-TOC.txt
-rw-r–r–   1 alain  staff    43 Jun 17 11:12 mb-9-big-CompressionInfo.db
-rw-r–r–   1 alain  staff   127 Jun 17 11:12 mb-9-big-Data.db
-rw-r–r–   1 alain  staff    10 Jun 17 11:12 mb-9-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 11:12 mb-9-big-Filter.db
-rw-r–r–   1 alain  staff    20 Jun 17 11:12 mb-9-big-Index.db
-rw-r–r–   1 alain  staff  4740 Jun 17 11:12 mb-9-big-Statistics.db
-rw-r–r–   1 alain  staff    61 Jun 17 11:12 mb-9-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 11:12 mb-9-big-TOC.txt
```

你的sstable数量可能大多数和上面的例子不太一样，但是插入和删除操作都是一样的。
我们可以看到这个含有墓碑的表其实实际上已经空了。存在磁盘上的文件只有墓碑和那些已经被删掉的条目数据。从读取数据的角度看，是空的结果：

```shell

echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
——-+——+——–

(0 rows)
```

在这时候，我们触发一个major级别的压实，压实所有的sstable。 压实操作通常是自动运行的，禁止自动压实，
而手动执行一个major压实很少情况是使用，这里只是为了完成教学的目的。

```shell
nodetool -p 7100 compact
```

现在所有的sstable已经合并成一个了：

```shell
alain$ ll /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/
total 72
drwxr-xr-x  11 alain  staff   374 Jun 17 14:50 .
drwxr-xr-x   3 alain  staff   102 Jun 16 20:25 ..
drwxr-xr-x   2 alain  staff    68 Jun 16 17:05 backups
-rw-r–r–   1 alain  staff    51 Jun 17 14:50 mb-14-big-CompressionInfo.db
-rw-r–r–   1 alain  staff   105 Jun 17 14:50 mb-14-big-Data.db
-rw-r–r–   1 alain  staff    10 Jun 17 14:50 mb-14-big-Digest.crc32
-rw-r–r–   1 alain  staff    16 Jun 17 14:50 mb-14-big-Filter.db
-rw-r–r–   1 alain  staff    20 Jun 17 14:50 mb-14-big-Index.db
-rw-r–r–   1 alain  staff  4737 Jun 17 14:50 mb-14-big-Statistics.db
-rw-r–r–   1 alain  staff    61 Jun 17 14:50 mb-14-big-Summary.db
-rw-r–r–   1 alain  staff    92 Jun 17 14:50 mb-14-big-TOC.txt
```

下面是这个sstable的内容，包含所有的墓碑，已经合并到同一个文件同一个数据结构中了：

```shell
alain$ SSTabledump /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-14-big-Data.db
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
        "type": "range_tombstone_bound",
        "start": {
          "type": "exclusive",
          "clustering": [
            "20160615"
          ],
          "deletion_info": {
            "marked_deleted": "2016-06-17T09:14:11.697040Z",
            "local_delete_time": "2016-06-17T09:14:11Z"
          }
        }
      },
      {
        "type": "row",
        "position": 40,
        "clustering": [
          "20160617"
        ],
        "deletion_info": {
          "marked_deleted": "2016-06-17T09:33:56.367859Z",
          "local_delete_time": "2016-06-17T09:33:56Z"
        },
        "cells": []
      },
      {
        "type": "range_tombstone_bound",
        "end": {
          "type": "inclusive",
          "deletion_info": {
            "marked_deleted": "2016-06-17T09:14:11.697040Z",
            "local_delete_time": "2016-06-17T09:14:11Z"
          }
        }
      }
    ]
  },
  {
    "partition": {
      "key": [
        "pickles"
      ],
      "position": 73,
      "deletion_info": {
        "marked_deleted": "2016-06-17T09:38:52.550841Z",
        "local_delete_time": "2016-06-17T09:38:52Z"
      }
    }
  }
]
```

注意到, 那些已经墓碑化了的数据（译者注：被删掉的数据，不是指墓碑）已经在压实过程中直接被干掉了。
然而正如我们前面讨论的，我们要仍然存储这个墓碑标记在磁盘上，因为我们要保持一个这样的删除记录，
以便有有效的传递这个删除操作在整个集群中。而数据实际的值我们没必要保留，因为这不影响数据一致性。

除非注明，赵岩的博客文章均为原创，转载请以链接形式标明本文地址
本文地址：https://zhaoyanblog.com/archives/972.html
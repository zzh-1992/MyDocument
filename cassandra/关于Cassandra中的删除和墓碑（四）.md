让我们现在看看各种类型的删除：

# cell删除

在cassandra存储引擎里，一指定行里面的一列就叫做cell。

删除某一行的某一个cell如下：

```cqlsh
DELETE crates FROM tlp_lab.tombstones WHERE fruit=’apple’ AND date =’20160617′;
```

这一行的crates列就会显示为"null"：

```shell
alain$ echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
———+———-+—————–
apple | 20160616 | {1, 2, 3, 4, 5}
apple | 20160617 | null
pickles | 20160616 | {6, 7, 8}

(3 rows)
```

执行flush以后，我们会得到一个新的sstable在磁盘上: mb-6-big

```shell
alain$ ll /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/
total 144
drwxr-xr-x 19 alain staff 646 Jun 16 21:12 .
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
-rw-r–r– 1 alain staff 43 Jun 16 21:12 mb-6-big-CompressionInfo.db
-rw-r–r– 1 alain staff 43 Jun 16 21:12 mb-6-big-Data.db
-rw-r–r– 1 alain staff 10 Jun 16 21:12 mb-6-big-Digest.crc32
-rw-r–r– 1 alain staff 16 Jun 16 21:12 mb-6-big-Filter.db
-rw-r–r– 1 alain staff 9 Jun 16 21:12 mb-6-big-Index.db
-rw-r–r– 1 alain staff 4701 Jun 16 21:12 mb-6-big-Statistics.db
-rw-r–r– 1 alain staff 59 Jun 16 21:12 mb-6-big-Summary.db
-rw-r–r– 1 alain staff 92 Jun 16 21:12 mb-6-big-TOC.txt
```

下面是mb-6-big的内容：

```shell
alain$ SSTabledump /Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-6-big-Data.db
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
          "20160617"
        ],
        "cells": [
          {
            "name": "crates",
            "deletion_info": {
              "marked_deleted": "2016-06-16T19:10:53.267240Z",
              "local_delete_time": "2016-06-16T19:10:53Z"
            }
          }
        ]
      }
    ]
  }
]
```

看这个删除cell的目标和插入的一行cells长的多像，分区信息partition，row和cells都有，只是没有liveness_info。
删除信息的时间戳也记录了下来（deletion_info记录在cell级别）。对了，这就是一个cell墓碑。

行删除（Row delete）

从一个分区删除一行用下面的语句：

```json
DELETE FROM tlp_lab.tombstones WHERE fruit=’apple’ AND date =’20160617′;
```

删除以后，这一行和想的一样就查不到了：

```shell
alain$ echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
———+———-+—————–
apple | 20160616 | {1, 2, 3, 4, 5}
pickles | 20160616 | {6, 7, 8}

(2 rows)
```

flush以后得到另外一个SSTable在磁盘上：mb-7-big，内容如下：

```shell
alain$ SSTabledump
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-7-big-Data.db
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
          "20160617"
        ],
        "deletion_info": {
          "marked_deleted": "2016-06-16T19:31:41.142454Z",
          "local_delete_time": "2016-06-16T19:31:41Z"
        },
        "cells": []
      }
    ]
  }
]
```

花点时间看看cells（或者columns）的值是空的数组。这就是一个row级别的墓碑，没有cells，同样没有liveness_info信息。删除信息（deletion_info）和期望的一样记录在row级别。

# 范围删除（Range delete）

从一个分区中删除一个范围（即多行）使用下面的语句：

```cqlsh
DELETE FROM tlp_lab.tombstones WHERE fruit=’apple’ AND date > ‘20160615’;
```

这个分区的数据就不会返回了，因为没有rows存在了，如果我们有小于20160616的数据，我们就会得到下面的内容：

```shell
echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
———+———-+———–
pickles | 20160616 | {6, 7, 8}

(1 rows)
```

flush以后得到mb-8-big，内容如下：

```shell
alain$ SSTabledump
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-8-big-Data.db
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
            "marked_deleted": "2016-06-16T19:53:21.133300Z",
            "local_delete_time": "2016-06-16T19:53:21Z"
          }
        }
      },
      {
        "type": "range_tombstone_bound",
        "end": {
          "type": "inclusive",
          "deletion_info": {
            "marked_deleted": "2016-06-16T19:53:21.133300Z",
            "local_delete_time": "2016-06-16T19:53:21Z"
          }
        }
      }
    ]
  }
]
```

就像我们看到的，现在我们看到一个新的特别的写入，不再是一个row类型，
而是range_tombstone_bound，有一个start和end。从clustering key=20160615(排除)
到无穷。这个range_tombstone_bound类型的墓碑属于分区key为apple的。所以从磁盘空间的角度来看，
一个删除范围是非常有效的手段，我们不需要把没个cell的删除信息都写入，而是只写删除边界。

# 分区删除（Partition delete）

删除整个分区使用：

```cqlsh
DELETE FROM tlp_lab.tombstones WHERE fruit=’pickles’;
```

删除以后，这个分区的数据就不会再显示了，现在这个表空了：

```shell
alain$ echo "SELECT * FROM tlp_lab.tombstones LIMIT 100;" | cqlsh

fruit | date | crates
——-+——+——–

(0 rows)
```

flush以后得到一个新的sstable：mb-9-big，内容如下：

```shell
alain$ SSTabledump
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-9-big-Data.db
```

```json


[
  {
    "partition": {
      "key": [
        "pickles"
      ],
      "position": 0,
      "deletion_info": {
        "marked_deleted": "2016-06-17T09:38:52.550841Z",
        "local_delete_time": "2016-06-17T09:38:52Z"
      }
    }
  }
]
```

然后同样，我们插入一个特殊标记，一个partition类型的墓碑，有一个deletion_info，没有rows信息。

注意：当使用集合类型，当你INSERT或者UPDATE整个集合而不是部分更新的时候都会产生了一个range墓碑，
插入一个已经集合覆盖之前的数据，而不是追加和更新里面的item，会导致一个range墓碑插入在新数据插入之前。
因为这个删除操作是隐藏暗含的操作，所以会导致奇怪的、让人沮丧的墓碑问题。

使用SSTabledump，我们再仔细看看。插入一条上面说的集合数据，不做任何删除，你会看到里面已经有一个墓碑了：

```yaml
"cells": [
  { "name": "crates", "deletion_info": { "marked_deleted": "2016-06-16T18:52:41.900450Z", "local_delete_time": "
2016-06-16T18: 52: 41Z" } },
  {
    "name": "crates", "path": [ "1" ], "value": ""
  },
  {
    "name": "crates", "path": [ "2" ], "value": ""
  },
  {
    "name": "crates", "path": [ "3" ], "value": ""
  },
  {
    "name": "crates", "path": [ "4" ], "value": ""
  },
  {
    "name": "crates", "path": [ "5" ], "value": "" }
]
```

再邮件列表里，我发现James
Ravn同学已经指出了这个问题，使用了list类型做例子，其实对于所有的集合类型都是这样的。
所以我不打算展开更多细节，我这里只想指出这一点，因为这个特性比较奇怪。
参考 http://www.jsravn.com/2015/05/13/cassandra-tombstones-collections.html#lists

除非注明，赵岩的博客文章均为原创，转载请以链接形式标明本文地址
本文地址：https://zhaoyanblog.com/archives/971.html
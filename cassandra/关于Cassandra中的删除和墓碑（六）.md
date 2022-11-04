# 墓碑清除

只有在local_delete_time + gc_grace_seconds以后墓碑才会在压实的时候完全清除掉。记住，
这是假定所有节点都在gc_grace_seconds时间内repair过了，以保证墓碑在所有节点上都正确分布，
这是为了防止删除的数据再出现，上面已经说过了。

gc_grace_seconds参数是墓碑在磁盘上存在的最短时间。我们需要保证所有的副本都收到了这个删除操作，
并且写入了墓碑，从而避免僵尸数据的问题。为了达到这个目的，我们唯一的方法就是全量repair。在gc_grace_seconds以后，
墓碑最终会请清除，如果其中一个节点没有写入这个墓碑，我们就会进入上面描述的数据又出现的境地。
TTL没有影响，因为没有节点可以保留数据而错过ttl，它是一个原子操作，数据和ttl是一条记录。
任何有数据的节点都知道什么时候必须删除数据。

另外，为了删除数据和墓碑，还有一些安全守则需要Cassandra节点必须遵守。
我们需要一行数据或者一个分区key的所有的片段数据以及墓碑都要在同一个压实中。
假设一个压实操作包含1-4个文件，如果一些数据在文件5上面，墓碑被清除后，我们仍需要留下一个标记数据（译者注:
墓碑）,表示文件5里的数据被清除了，否则文件5里的数据又会回来了（成为僵尸数据）。

这些条件有时候让删除墓碑成为一件很复杂的事情，它经常给Cassandra的使用者带来麻烦。
墓碑不被清除意味着占用更多的磁盘，更慢的读，以及更多的repair工作，高概率的GC压力，更多的资源利用等等。
当你的sstable的墓碑占到一个很高的的比率（90%的数据都是墓碑），读取一个值或者一段相关的数据会变的相当困难，
存储的成本也越来越高。这些问题最终会导致磁盘空间耗尽。

很多使用情况下会导致数据删除（TTL或者delete操作），作为Cassandra的使用者我们必须克制，控制这些事情。

再次回到我们这个例子，我重启了这个节点在很多天以后（>10天，gc_grace_seconds的默认值）。
Cassandra重新打开压实过的mb-14-big文件，它立马又进行了压实操作。

```shell
MacBook-Pro:tombstones alain$ grep ‘mb-14-big’ /Users/alain/.ccm/Cassa-3.7/node1/logs/system.log

DEBUG [SSTableBatchOpen:1] 2016-06-28 15:56:17,947 SSTableReader.java:482 – Opening
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-14-big (0.103KiB)
DEBUG [CompactionExecutor:2] 2016-06-28 15:56:18,525 CompactionTask.java:150 – Compacting (
166f61c0-3d38-11e6-bfe3-e9e451310a18) [/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-14-big-Data.db:level=0, ]
```

此时，gc_grace_seconds已经过去了，墓碑有条件被清除了，所以所有的墓碑都被清除了，最后表里没有任何数据了，数据目录最终也是空的：

```shell
MacBook-Pro:tombstones alain$ ll
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/
total 0
drwxr-xr-x 3 alain staff 102 Jun 28 15:56 .
drwxr-xr-x 3 alain staff 102 Jun 16 20:25 ..
MacBook-Pro:tombstones alain$
```

如果墓碑在所有副本都都正确存在，我们就会有完全一致的删除操作，删除的数据就不会再出现。而且我们还可以释放一些磁盘空间，
让其他数据的读变的更容易，尽管为了证明这个事情，我的例子有点傻，但最后这个表是完全变空了。

监控墓碑比率和到期时间

因为Cassandra的设计，当我们删除数据或者使用ttl的时候很正常的就产生了墓碑。当然这个是我们必须要控制的。

使用sstablemetadata我们可以指定一个sstable的墓碑占有率，以及一个大概的墓碑清除时间分布情况。

```shell


alain$ SSTablemetadata
/Users/alain/.ccm/Cassa-3.7/node1/data/tlp_lab/tombstones-c379952033d311e6aa4261d6a7221ccb/mb-14-big-Data.db

—
Estimated droppable tombstones: 2.0
—
Estimated tombstone drop times:
1466154851:         2
1466156036:         1
1466156332:         1
—
```

上面的比率很可能是错误的，因为这个特别的状态，我们把所有的数据都清除了，在mb-14-big文件中，
只剩下墓碑了，没有有用的数据。但是对于墓碑而言这个通常是显示sstable状态是否良好的一个很有用的指标。

同时，Cassandra为每个表提供了一个叫TombstoneScannedHistogram的统计值，可以通过jmx获取到。
再说明一下，命令里scope=tombstones就是指表的名字。

org.apache.cassandra.metrics:type=Table,keyspace=tlp_lab,scope=tombstones,name=TombstoneScannedHistogram

这个值很有必要用监控工具监控起来，比如Graphite / Grafana, Datadog, New Relic等等。

上面的例子，在墓碑被压实清除之前，使用jconsole输出来是这样的：

cassandra tombstone jconsole

![alt ](http://zhaoyanblog.com/wp-content/uploads/2017/03/zhaoyanblog-2017-03-24-19-11-53.png)

除非注明，赵岩的博客文章均为原创，转载请以链接形式标明本文地址
本文地址：https://zhaoyanblog.com/archives/975.html


# 单SSTable压实

单SSTable压实是在cassandra1.2引入的，是由Jonathan Ellis在CASSANDRA-3442最新提出来的：

在Size压实模式下，你可能会产生很大的SSTable，很少被压实，但是有很多过期的数据在里面，
我们在这种情况下可能浪费了大量的磁盘空间。

就像我们上面提到的，压实的目的就是墓碑的剔除，在一定的场景下，压实操作并没有很好的剔除墓碑。
不仅仅是这里提到的Size模式的压实（STCS），所有的压实模式都有这种情况。
一些SSTable文件很久才会被压一次或者很长时间都会有重叠的SSTables（译者注：表示不能合并ROW）。
这也是为什么，时至今天，每个压实模式都会有一堆的配置项用于调节墓碑的剔除。

tombstone_threshod: 这个配置项的作用就是Jonathan Ellis 在2011年提出的：

如果一个SSTable在它的元数据信息里保存它含有的ttl记录的统计数据，我们就可以当过期数据超过20%的时候，进行一次单SSatble压实操作。

所以当墓碑占有率超过这个值(默认等于0.2，也就是20%）的时候，这个选项就会触发一次单SStable压实。
需要注意的是，那些真正可以被剔除的墓碑经常小于估值，因为计算这个墓碑占有率的时候，并未考虑gc_grace_secods参数。

tombstone_compaction_interval:
这个选项是在CASSANDRA-4781中被引入，目的是为了解决一个死循环问题，
当一个SSTable文件的墓碑占有率达到了触发一次单SStable压实的操作，
但是由于和别的SStable有重叠，导致无法清除墓碑。因为我们要删除一个文件的所有碎片，以防止僵尸数据产生。
这种情况下一些SStable的压实操作可能无休止的进行下去。既然一个SStable里还有的墓碑率是个估值，
那么我这个选项就用于限制两次单SStable压实操作的最小间隔时间，默认是1天。

unchecked_tombstone_compaction：是Paulo
Motta在CASSANDRA-6563引入的。在这个问题单里他描述了单个SSTable压实的历史和他引入这个参数的原因，
非常有趣。我是无法更好的去解释它。（译者注：意思是你可以直接去看问题单。）

注意此选项配置为true以后，就会出发一个SStable一天一次的压实（tombstone_compaction_interval默认），
只要墓碑占有率（估计值）高于0.2（20%是墓碑，这个是tombstone_threshold默认值），
最坏的情况就是即便是没有任何墓碑是可以被剔除的也会执行一次压实。

所以最好是多投入一些资源（多加一些机器？）希望让墓碑的剔除更好的进行。

推荐做法：当一个DC已经在删除墓碑上有麻烦了，立即使用下这个参数，应该是值得的。我在使用这个选项上，
已经有一些很成功的经验以及一些不是很糟糕的经验。相反的也有一些很少场景，这个选项实际上并没有什么作用。
我甚至有一次设置这个选项为true除非手动压缩。把一些磁盘已经100%
马上挂掉的机器恢复了正常。

要修改这些配置的时候，先查看表的描述。然后重新指定整个压实策略，以避免一些意外。
假设我想修改tlp_lab库里tombstones表的压实参数，我会这样做：

```shell
MacBook-Pro:~ alain$ echo "DESCRIBE TABLE tlp_lab.tombstones;" | cqlsh
CREATE TABLE tlp_lab.tombstones (
fruit text,
date text,
crates set,
PRIMARY KEY (fruit, date)
) WITH CLUSTERING ORDER BY (date ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class':
'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy',
'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class':
'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';
```

然后我会复制这些压实选项，并修改它：

```shell
echo "ALTER TABLE tlp_lab.tombstones WITH compaction = {'class': '
org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4', '
unchecked_tombstone_compaction': 'true', 'tombstone_threshold': '0.1'};" | cqlsh
```

或者把它复制到一个文件里去执行：

```shell

ALTER TABLE tlp_lab.tombstones WITH compaction = {'class': '
org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4', '
unchecked_tombstone_compaction': 'true', 'tombstone_threshold': '0.1'};

cqlsh -f myfile.cql
```

或者是使用-e参数去执行

```cqlsh

cqlsh -e "ALTER TABLE tlp_lab.tombstones WITH compaction = {'class': '
org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4', '
unchecked_tombstone_compaction': 'true', 'tombstone_threshold': '0.1'};"
```

注意：我会使用上面的这些方法，而不是选择进入一个cqlsh控制台。因为上面这些方法很容易使用管道输出进行处理研究，
特别是在查询数据和查看表描述的时候，就更有意义了。想象下你想查看所有表的read_repair_chance参数：

```shell
MacBook-Pro:~ alain$ echo "DESCRIBE TABLE tlp_lab.tombstones;" | cqlsh | grep -e TABLE -e read_repair_chance
CREATE TABLE tlp_lab.tombstones (
AND dclocal_read_repair_chance = 0.1
AND read_repair_chance = 0.0
CREATE TABLE tlp_lab.foo (
AND dclocal_read_repair_chance = 0.0
AND read_repair_chance = 0.1
CREATE TABLE tlp_lab.bar (
AND dclocal_read_repair_chance = 0.0
AND read_repair_chance = 0.0
```

# 手动剔除墓碑

有时候SSTable文件包含了95%的墓碑，仍然没有触发任何压实，这可能因为压实参数，重叠的sstables，
或者就是实际上单个sstable的压实就比常规的压实优先级的低的原因导致的。
很重要的一点是我们可以手动强制cassandra进行压实操作。为了实现这点，
我们需要可以通过JMX给cassandra发送指令。我这里考虑使用jmxterm， 你可以使用你喜欢的工具。

# 安装Jmxterm

```shell
wget http://sourceforge.net/projects/cyclops-group/files/jmxterm/1.0-alpha-4/jmxterm-1.0-alpha-4-uber.jar
```

执行一个JMX命令，比如强制执行一个压实操作：

```shell
echo "run -b org.apache.cassandra.db:type=CompactionManager forceUserDefinedCompaction
myks-mytable-marker-sstablenumber-Data.db" | java -jar jmxterm-1.0-alpha-4-uber.jar -l localhost:7199
```

有时候，我会先用sstablemetadata工具先去查看文件的墓碑占用情况，结合这些去执行压实操作的命令，效果很不错。

结论

删除操作一直是分布式系统的一项棘手操作，尤其是同时要考虑可用性、一致性、持久化等问题。

像cassandra这样的分布式系统使用墓碑来处理删除是比较聪明的一种方式，
但是也引入了一些特别要注意的地方。我们需要用非直观的方式来思考，
因为当删除的时候实际上是插入一些数据，确实不是一件自然的事情。然后还要理解墓碑的声明周期，
这又是很不平凡的事情。 然而，当我们去了解墓碑的行为并使用适当的工具来帮助我们解决墓碑问题时，会更容易理解它。

Cassandra是一个快速发展演进的系统，这里有许多围绕墓碑开放的的讨论，你可能会感兴趣：

开放的讨论贴：

CASSANDRA-7019：改进墓碑压实（主要解决重叠SStable的墓碑剔除，通过智能的选择多个SStable进行压实）
CASSANDRA-8527：无论我们在哪里产生的墓碑都会产生范围墓碑，好像对于范围墓碑，
在代码的多个地方我们没有好好考虑。CASSANDRA-11166 和 CASSANDRA-9617也指出了这个问题。

除非注明，赵岩的博客文章均为原创，转载请以链接形式标明本文地址
本文地址：https://zhaoyanblog.com/archives/987.html
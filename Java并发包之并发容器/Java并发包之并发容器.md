# 4.1 链表

## 4.1.1 基本的链表

## 4.1.2 优先级链表

## 4.1.3 跳表（SkipList）

# 4.2 BlockingQueue（阻塞队列）

## 4.2.1 ArrayBlockQueue

## 4.2.2 PriorityBlockQueue(优先级队列)

## 4.2.3 LinkedBlockingQueue

## 4.2.4 DelayQueue

## 4.2.5 SynchronousQueue

## 4.2.6 LinkedBlockingDeque(基于链表的实现的双向阻塞队列)

## 4.2.7 LinkedTransferQueue

# 4.3 ConcurrentQueue(并发队列)

## 4.3.1 并发队列的性能

## 4.3.2 并发队列在使用中需要注意的问题

## 4.3.3 并发队列总结

# 4.4 ConcurrentMap（并发映射）

## 4.4.1 ConcurrentHashMap

1.JDK 1.8版本以前的ConcurrentHashMap内部结构

- 在JDK1.6、1.7版本中，ConcurrentHashMap采用分段锁的机制(可以确保线程安全的同时最小化锁的粒度)实现并发的更新操作，在
  ConcurrentHashMap中包含两个核心的静态内部类Segment和HashEntry，前者是一个实现ReentrantLock的显示锁，每一个Segment
  锁对象均可用于同步每个散列映射表的若干个桶（HahsEntry），后者主要用于存储映射表的键值对。与此同时，若干个HashEntry通过
  链表结构形成类HashBucket，而最终的ConcurrentHashMap则由若干个（默认是16个）Segment对象数组组成。
- Segment可用于实现减小锁粒度，ConcurrentHahsMap被分割成若干个Segmeng，在put的时候只需要锁住一个Segmeng即可，而get时候 干脆不加锁，而是使用volatile属性保证被其他线程同时修改后的可见性。

2.JDK1.8版本ConcurrentHashMap的内部结构 在JDK1.8版本中直接用table数组存储键值对，在JDK1.6中，每个bucket中键值对的组织方式都是单向链表，查找复杂度是O(n),
JDK1.8中当链表长度超过8时，链表转换为红黑树，查询复杂度可以降低到O(log n),改进了性能。利用CAS + Synchronized可以保证并发 更新的安全性，底层采用数组 + 链表 + 红黑树（提高检索效率）的存储结构。

## 4.4.2 ConcurrentSkipListMap

## 4.4.3 并发映射总结

## 4.5 写时拷贝算法(Copy On Write)

- 并发容器 CopyOnWrite容器 COW
  所有线程对该容器对读取操作将不会对数据集合产生加锁对动作，从而使得高并发高吞吐量对读取操作变得高效，但是当有线程对该容器对数据集合进行删除或者增加等写操作时才会对整个数据集合进行加锁操作，然后将容器中对数据集合拷贝一份，并且基于最新对复制进行删除或增加写
  操作，当写操作执行结束后，将最新复制的数据集合引用指向原有的数据集合，进而达到读写分离最终一致性的目的。

- CopyOnWrite容器是一种读写分离的思想，读和写不同的容器，因此不会存在读写冲突，而写写之间的冲突则是由全局的显示Lock来进行防护，因此CopyOnWrite常常被应用与读操作远远高于写操作的应用场景。

### 4.5.3 CopyOnWrite总结

缺陷

- 数组复制带来的内存开销
- 并不能保证数据实时的一致性：CopyOnWrite容器只能保证数据最终的一致性，但不能保证数据但实时一直性。




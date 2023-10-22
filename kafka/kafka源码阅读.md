## kafka源码阅读

## 1、acks配置
[kafka简介-可靠性保证-acks机制](https://github.com/zzh-1992/kafka/blob/master/Kafka%E7%AE%80%E4%BB%8B.md#%E5%8F%AF%E9%9D%A0%E6%80%A7%E4%BF%9D%E8%AF%81-acks%E6%9C%BA%E5%88%B6)
```java
       private static final String ACKS_DOC = "The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the "
        + " durability of records that are sent. The following settings are allowed: "
        + " <ul>"
        + " <li><code>acks=0</code> If set to zero then the producer will not wait for any acknowledgment from the"
        + " server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be"
        + " made that the server has received the record in this case, and the <code>retries</code> configuration will not"
        + " take effect (as the client won't generally know of any failures). The offset given back for each record will"
        + " always be set to <code>-1</code>."
        + " <li><code>acks=1</code> This will mean the leader will write the record to its local log but will respond"
        + " without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after"
        + " acknowledging the record but before the followers have replicated it then the record will be lost."
        + " <li><code>acks=all</code> This means the leader will wait for the full set of in-sync replicas to"
        + " acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica"
        + " remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting."
        + "</ul>";

    // class org.apache.kafka.clients.producer.ProducerConfig  position:446
    
    private static String parseAcks(String acksString) {
        try {
            return acksString.trim().equalsIgnoreCase("all") ? "-1" : Short.parseShort(acksString.trim()) + "";
        } catch (NumberFormatException e) {
            throw new ConfigException("Invalid configuration value for 'acks': " + acksString);
        }
    }
```

## 2、幂等性
### 2.0 官方文档说明(如何实现幂等)
- 原文:To achieve this, the broker assigns each producer an ID and deduplicates messages using a sequence number that is sent by the producer along with every message
- 译文:为了实现这一点，代理为每个生产者分配一个ID，并使用生产者随每个消息一起发送的序列号来删除重复的消息

    Prior to 0.11.0.0, if a producer failed to receive a response indicating that a message was committed, it had little choice but to resend the message. This provides at-least-once delivery semantics since the
    message may be written to the log again during resending if the original request had in fact succeeded. Since 0.11.0.0, the Kafka producer also supports an idempotent delivery option which guarantees that resending
    will not result in duplicate entries in the log. <font color = yellow size=5 face="STCAIYUN">  To achieve this, the broker assigns each producer an ID and deduplicates messages using a sequence number that is sent by the producer along with every message.</font>
    Also beginning with 0.11.0.0, the producer supports the ability to send messages to multiple topic partitions using transaction-like semantics: i.e. either all messages are successfully written or none of them are.
    The main use case for this is exactly-once processing between Kafka topics (described below).

### 2.1 源码注释-幂等生产者、事物生产者
```html
  From Kafka 0.11, the KafkaProducer supports two additional modes: the idempotent producer and the transactional producer.
  The idempotent producer strengthens Kafka's delivery semantics from at least once to exactly once delivery. In particular
  producer retries will no longer introduce duplicates. The transactional producer allows an application to send messages
  to multiple partitions (and topics!) atomically.
```
### 2.2 源码注释-幂等配置说明

  To enable idempotence, the <font color = yellow>enable.idempotence</font> configuration must be set to true. If set, the
  <font color = yellow>retries</font> config will default to <font color = yellow>Integer.MAX_VALUE</font> and the <font color = yellow>acks</font> config will
  default to <font color = yellow>all</font>. There are no API changes for the idempotent producer, so existing applications will
  not need to be modified to take advantage of this feature.	


### 2.2 源码注释-幂等配置检查
```java
    private static short configureAcks(ProducerConfig config, Logger log) {
        // 判断是否存在acks等key(是否存在acks配置)
        boolean userConfiguredAcks = config.originals().containsKey(ProducerConfig.ACKS_CONFIG);
        short acks = Short.parseShort(config.getString(ProducerConfig.ACKS_CONFIG));

        // 如果配置了幂等性处理
        if (config.idempotenceEnabled()) {
            if (!userConfiguredAcks)
                log.info("Overriding the default {} to all since idempotence is enabled.", ProducerConfig.ACKS_CONFIG);
            else if (acks != -1)
                // 当开启幂等 && acks != all/-1 提示异常信息
                throw new ConfigException("Must set " + ProducerConfig.ACKS_CONFIG + " to all in order to use the idempotent " +
                        "producer. Otherwise we cannot guarantee idempotence.");
        }
        return acks;
    }
```


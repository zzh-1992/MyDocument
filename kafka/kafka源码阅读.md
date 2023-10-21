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


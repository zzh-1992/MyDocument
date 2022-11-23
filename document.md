
# 1.0 Mysql
## 1.1 多数据源配置
https://github.com/zzh-1992/mysql/commit/880587d881edced359935d7c8b0a750929e0480b

## 1.2 Mysql执行计划

https://github.com/zzh-1992/MyDocument/commit/a29cfdaf12f1e00da229adab89b4e6e6ad2d5402

## 1.3 Mysql相关命令

https://github.com/zzh-1992/MyDocument/commit/7ba71200d25fa7bc1fc6e55aef39d8e740e6e08a

## 1.4 Mysql索引

https://github.com/zzh-1992/MyDocument/commit/851fe844301fd0b64f74fa7e128ce8249b2a459e

## 1.5 mysql事务、隔离级别

https://github.com/zzh-1992/MyDocument/commit/24922458e10e78912d0e04e95eb29e58e58eae4c

## 1.5 手动提交/回滚事务

```java
// class: DataSourceTransactionManager(org.springframework:spring-jdbc:5.3.7)
protected void doBegin(Object transaction, TransactionDefinition definition)
// 说明设置自动提交=false(开启事务)
con.setAutoCommit(false);
```

https://github.com/zzh-1992/mysql/commit/310b556a215cb11157965c820d1577465bf8566b


# 2.0 SpringMVC

## 2.1 添加全局异常处理及请求体参数校验
https://github.com/zzh-1992/mysql/commit/21cebed93185c2cf92cbbb9c1f870da4e3c48836

## 2.2 SpringMVC执行流程

https://github.com/zzh-1992/MyDocument/commit/4a4631971e53223a8e1fcfdd79a9c9d212b4b8d9

## 2.3 SpringMVC全局异常

https://github.com/zzh-1992/MyDocument/commit/2a1110fa479bca36b0f4f8904ea30834bb29d1ac

## 2.4 Spring拦截器-洋葱模型

https://github.com/zzh-1992/MyDocument/commit/9e58e4ca55c7628bcda8c4ad7e41f83c50c6dcbd

## 2.5 后端跨域处理(CORS)
- mozilla 文档 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/CORS
- spring 文档 https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-cors
- 后端代码 https://github.com/zzh-1992/rear/blob/master/src/main/java/com/grapefruit/javaimage/config/CorsHandlerFilter.java


# 3.0 mybatis
## 3.0 mybatis官网
https://mybatis.org/mybatis-3/zh/index.html

## 3.1 添加Config配置类(扫描mapper)
https://github.com/zzh-1992/mysql/commit/7f817a5d0078a62989da7c053d1d3b17f8aaf697

## 3.2 取消一级和二级的缓存使用
https://github.com/zzh-1992/mybatis/commit/1decfd0bf6b6645fd1c34f701cd41fd135a78d41

## 3.3 注册自定义的TypeHandler
https://github.com/zzh-1992/mybatis/commit/04df363f5f9e805bf275b5c16b946969d9eb2f3a

## 3.4 自定义拦截器(打印sql日志)

https://github.com/zzh-1992/mybatis/commit/9b68c07157dd2c82a91b3872b3b92fe306021d85
mybatis插件
https://mybatis.org/mybatis-3/zh/configuration.html#plugins

## 3.5 spring事务流程图、mybatis插件流程图
https://github.com/zzh-1992/mybatis/commit/7e297b248b0a9236fe633bdfd80f9b0d51c4fd5d

## 3.6 mybatis association and collection(mybatis关联查询)
https://github.com/zzh-1992/mybatis/commit/9efd8cdaac1790a5c074c17abedab29fbea1b9dc

## 3.7 添加mybatis_spring和mybatis_starter的官方介绍(搬运) 

https://github.com/zzh-1992/mybatis/commit/bce7bd6d8b53bb984b9b0a0d835e64b9c6c488fc

## 3.8 mybatis 动态 SQL
https://github.com/zzh-1992/mysql/commit/4dd89dc2bab1ff25f79561cfc5e5caa5b2625915

## 3.9 Mybatis plus官网教程_快速开始
https://github.com/zzh-1992/mysql/commit/b535f8091638016810466a8281518cf871a26085

## 3.10 使用注解方式编写sql(批量插入语句-脚本)
https://github.com/zzh-1992/mysql/commit/83355d4dfe21853f5585a5dc997111205fef495e


# 4.0 Redis
## 4.1 添加redisonlock依赖及简单示例
https://github.com/zzh-1992/RedissonLock/commit/c523cd0b37a1e008f3ca7921cbd8d55b5d0d2c7d

## 4.2 redis数据类型、创建集群命令 

https://github.com/zzh-1992/MyDocument/commit/9f824acdd070c1a301c38dbc97e208f4136bf8b0

## 4.3 自定义redis lock starter(分布式锁启动器)

https://github.com/zzh-1992/redislockstarter/commit/6155c924f9ee9ba638bf5ca6824c7ad051d8341c

## 4.4 RedissonLock代码解读
https://github.com/zzh-1992/RedissonLock/blob/master/src/main/java/com/grapefruit/redisson/Lock.java

# 5.0 Spring 
## 5.1 Bean生命周期
https://github.com/zzh-1992/MyDocument/commit/3797c271ea8b1ed15e63fa7c83733619c04287a2

## 5.2 


## 5.3

## 5.4



# 6.0 Springboot

## 6.1 自定义banner 

https://github.com/zzh-1992/rear/commit/d757b7c2604f7ea0e2fa2ba66907df902b97fdab

## 6.2 springboot的run方法



## 6.3 Creating Your Own Starter(创建自定义启动器) 

- spring官网文档
  https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.custom-starter
- github个人仓库
  https://github.com/zzh-1992/MyDocument/commit/69bfac36cfd3b3e691ecd62c280a3008bc0b46b1



#  7.0 文件读取工具

## 7.1 excel单元格操作

https://poi.apache.org/components/spreadsheet/quick-guide.html

https://github.com/zzh-1992/excel/commit/b7154f2b846ac2a07a06a6cc87a8e88b1cb44d7d

## 7.2 读取yaml文件

https://github.com/zzh-1992/excel/commit/a395f358e13fdc7540a7a2ec377d9c6df4c89c5f

## 7.3 读取xml文件

https://github.com/zzh-1992/excel/commit/1d84de6508673ffc1dafe7a36cd7ada548dc9394

# 8.0 Docker

## 8.1 Docker命令、安装及配置

https://github.com/zzh-1992/MyDocument/commit/bdc69c0f01c186aea8936d04e10d11d17c8c8801

# 9.0 Vue

## 9.1 vue配置环境变量

- vue指导文档
  https://cli.vuejs.org/zh/guide/mode-and-env.html#%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F
- 个人github仓库
  https://github.com/zzh-1992/MyDocument/commit/507995016c99b522b5bb00bbcbd6b389b349d1f2



# 10.0 Kafka 

## 10.1 添加Kafka简介(组件、功能、消息)

https://github.com/zzh-1992/kafka/commit/98ce290a9eee230a2d5d931de66a33c2a08751f5

## 10.2 自定义kafka启动器

https://github.com/zzh-1992/kafka/commit/6d8ab2817869cb5c5d1746735448847fea885526

## 10.3 添加官网示例代码的链接(api)

kafka官方示例


- 官网"消费者"链接 http://kafka.apache.org/documentation/#consumerapi
- api(java代码) http://kafka.apache.org/26/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html

- 官网"生产者"链接 http://kafka.apache.org/documentation/#producerapi
- api(java代码) http://kafka.apache.org/26/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html


个人github仓库

https://github.com/zzh-1992/kafka/commit/f702a4e8316ff06aa0e5e91abb483824aab32c87



# 11.0 加密

## 11.1 AES

https://github.com/zzh-1992/utils/blob/master/src/main/java/com/grapefruit/utils/security/AesGcm.java

## 11.2 RSA

https://github.com/zzh-1992/utils/blob/master/src/main/java/com/grapefruit/utils/security/RSAUtils.java

## 11.3 Token

https://github.com/zzh-1992/utils/blob/master/src/main/java/com/grapefruit/utils/security/TokenUtils.java



# 12.0 JAVA

## 12.1 Java并发包之并发容器

https://github.com/zzh-1992/MyDocument/blob/master/Java%E5%B9%B6%E5%8F%91%E5%8C%85%E4%B9%8B%E5%B9%B6%E5%8F%91%E5%AE%B9%E5%99%A8/Java%E5%B9%B6%E5%8F%91%E5%8C%85%E4%B9%8B%E5%B9%B6%E5%8F%91%E5%AE%B9%E5%99%A8.md



# 13.0 大数据

## 13.1 WordCount示例代码

author: [jmaister](https://github.com/jmaister/wordcount/commits?author=jmaister)  https://github.com/jmaister/wordcount

Myself: https://github.com/zzh-1992/spark/tree/master/src/main/java/com/grapefruit/spark/wordcount



## 13.2 Spark Sql示例代码

https://github.com/zzh-1992/spark/blob/master/src/main/java/com/grapefruit/spark/collectfemaleinfo








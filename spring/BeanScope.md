## Bean scopes
[spring bean范围](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html)

- singleton
  (Default) Scopes a single bean definition to a single object instance for each Spring IoC container.
  （单例）：在整个IoC容器中，只会创建一个Bean的实例。这是默认的作用范围。

- prototype
  Scopes a single bean definition to any number of object instances.
  在整个IoC容器中，每次获取Bean的对象时，都会创建一个新的Bean实例。

- request
  Scopes a single bean definition to the lifecycle of a single HTTP request.
  That is, each HTTP request has its own instance of a bean created off the back of a single bean definition.
  Only valid in the context of a web-aware Spring ApplicationContext.
  在Web项目中，每一次HTTP请求，都会创建一个新的Bean实例。

- session
  Scopes a single bean definition to the lifecycle of an HTTP Session.
  Only valid in the context of a web-aware Spring ApplicationContext
  在Web项目中，每一次HTTP会话，都会创建一个新的Bean实例。

- application
  Scopes a single bean definition to the lifecycle of a ServletContext.
  Only valid in the context of a web-aware Spring ApplicationContext.

- websocket
  Scopes a single bean definition to the lifecycle of a WebSocket.
  Only valid in the context of a web-aware Spring ApplicationContext



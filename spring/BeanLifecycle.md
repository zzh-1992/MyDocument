
#Bean生命周期接口
## 1.0 aware接口
```java
    BeanNameAware setBeanName
    BeanClassLoaderAware  setBeanClassLoader
    BeanFactoryAware setBeanFactory
    EnvironmentAware setEnvironment
    EmbeddedValueResolverAware setEmbeddedValueResolver
    ResourceLoaderAware setResourceLoader
    ApplicationEventPublisherAware setApplicationEventPublisher
    MessageSourceAware setMessageSource
    ApplicationContextAware setApplicationContext
    ServletContextAware setServletContext
```

## 1.1 BeanPostProcessor 前置处理
```java
    // 前置方法
    postProcessBeforeInitialization methods of BeanPostProcessor
```
## 1.2 InitializingBean afterPropertiesSet
    // 自定义初始化方法
    a custom init-method definition
```java

```
## 1.3 BeanPostProcessor 后置处理
```java
    // 后置方法
    postProcessAfterInitialization methods of BeanPostProcessor
```
## 1.4 DisposableBean 销毁接口
```java
    // 原生销毁方法及自定义的销毁方法
    postProcessBeforeDestruction methods of DestructionAwareBeanPostProcessor
    a custom destroy-method definition
```
![img_5.png](img_5.png)

# AbstractAutowireCapableBeanFactory
## 具体方法
```java
	protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
		if (System.getSecurityManager() != null) {
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
				invokeAwareMethods(beanName, bean);
				return null;
			}, getAccessControlContext());
		}
		else {
		    // 对应 1.0 实现aware接口中的方法
			invokeAwareMethods(beanName, bean);
		}

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
		    // 对应 1.1 BeanPostProcessor 执行前置处理方法
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
        // 对应1.2 InitializingBean afterPropertiesSet 执行init/初始化方法
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}
		if (mbd == null || !mbd.isSynthetic()) {
		    // 对应 1.3 BeanPostProcessor 执行后置处理方法
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}

		return wrappedBean;
	}
```



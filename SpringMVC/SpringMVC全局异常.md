
# MVC全局异常
## 1.0 容器启动时
### 1.1 执行afterPropertiesSet方法
class: ExceptionHandlerExceptionResolver 实现InitializingBean
```java
	@Override
	public void afterPropertiesSet() {
		// Do this first, it may add ResponseBodyAdvice beans
		initExceptionHandlerAdviceCache();

		if (this.argumentResolvers == null) {
			List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
			this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
		}
		if (this.returnValueHandlers == null) {
			List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
		}
	}
```

### 1.2 扫描对应当全局异常类,并将结果存入成员变量:exceptionHandlerAdviceCache
```java
	private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
			new LinkedHashMap<>();
```
```java
	private void initExceptionHandlerAdviceCache() {
        if (getApplicationContext() == null) {
            return;
        }

        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
            if (ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
                this.responseBodyAdvice.add(adviceBean);
            }
        }

            if (logger.isDebugEnabled()) {
                int handlerSize = this.exceptionHandlerAdviceCache.size();
                int adviceSize = this.responseBodyAdvice.size();
                if (handlerSize == 0 && adviceSize == 0) {
                    logger.debug("ControllerAdvice beans: none");
                }
            else {
                logger.debug("ControllerAdvice beans: " +
                handlerSize + " @ExceptionHandler, " + adviceSize + " ResponseBodyAdvice");
            }
        }
    }
```
### 1.3 查找带有@ControllerAdvice注解的类
```java
// 这行代码会找带有@ControllerAdvice注解当类
ControllerAdvice controllerAdvice = context.findAnnotationOnBean(name, ControllerAdvice.class);

List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
```

## 2.0 容器启动后
### 2.1 视图/异常处理 processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
```java
    if (exception != null) {
        if (exception instanceof ModelAndViewDefiningException) {
            logger.debug("ModelAndViewDefiningException encountered", exception);
            mv = ((ModelAndViewDefiningException) exception).getModelAndView();
        }
        else {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
            errorView = (mv != null);
        }
    }
```

### 2.2 异常处理(class:DispatcherServlet)
```java
	/** List of HandlerExceptionResolvers used by this servlet. */
	@Nullable
	private List<HandlerExceptionResolver> handlerExceptionResolvers;
	
	// 当异常处理器不为空当时候
    if (this.handlerExceptionResolvers != null) {
        for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
        exMv = resolver.resolveException(request, response, handler, ex);
        if (exMv != null) {
            break;
        }
    }

```

### 2.3 找到自定义当全局异常处理类
```java
		for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            //获取全局异常处理的bean
			ControllerAdviceBean advice = entry.getKey();
			// 判断异常处理类是否适合
			if (advice.isApplicableToBeanType(handlerType)) {
				ExceptionHandlerMethodResolver resolver = entry.getValue();
				Method method = resolver.resolveMethod(exception);
				if (method != null) {
					return new ServletInvocableHandlerMethod(advice.resolveBean(), method);
				}
			}
		}
```






# 拦截器

## 1 注册拦截器
- spring 官方链接
  https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-config-interceptors

```java

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new ThemeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
    }
}
```


### 2 doDispatch方法中的拦截器方法
```java

    // 执行前置方法
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
        return;
    }

    // 执行业务逻辑
    // Actually invoke the handler.
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

    if (asyncManager.isConcurrentHandlingStarted()) {
        return;
    }

    applyDefaultViewName(processedRequest, mv);
    // 执行后置方法
    mappedHandler.applyPostHandle(processedRequest, response, mv);
```

- 这段逻辑类型洋葱模型

- 前置方法(先序遍历拦截器)
```java
	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = 0; i < interceptors.length; i++) {
				HandlerInterceptor interceptor = interceptors[i];
				if (!interceptor.preHandle(request, response, this.handler)) {
					triggerAfterCompletion(request, response, null);
					return false;
				}
				this.interceptorIndex = i;
			}
		}
		return true;
	}
```
- 后置方法(后续遍历拦截)
```java
	void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv)
			throws Exception {
		HandlerInterceptor[] interceptors = getInterceptors();
		if (!ObjectUtils.isEmpty(interceptors)) {
			for (int i = interceptors.length - 1; i >= 0; i--) {
				HandlerInterceptor interceptor = interceptors[i];
				interceptor.postHandle(request, response, this.handler, mv);
			}
		}
	}
```


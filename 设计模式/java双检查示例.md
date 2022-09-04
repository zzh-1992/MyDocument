
## java 双检查
## 1. 单利模式-双检查
https://github.com/zzh-1992/MyDocument/blob/ff0cc20fbb5eed5128087753282e77ef6586e779/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/src/SingletonLazy.java

## 2. AbstractBeanFactory
```java
	/**
     * Mark the specified bean as already created (or about to be created).
     * <p>This allows the bean factory to optimize its caching for repeated
     * creation of the specified bean.
     * @param beanName the name of the bean
     */
	protected void markBeanAsCreated(String beanName) {
		if (!this.alreadyCreated.contains(beanName)) {
			synchronized (this.mergedBeanDefinitions) {
				if (!this.alreadyCreated.contains(beanName)) {
					// Let the bean definition get re-merged now that we're actually creating
					// the bean... just in case some of its metadata changed in the meantime.
					clearMergedBeanDefinition(beanName);
					this.alreadyCreated.add(beanName);
				}
			}
		}
	}
```
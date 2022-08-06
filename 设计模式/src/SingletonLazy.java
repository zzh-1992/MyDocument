/*
 *Copyright @2022 Grapefruit. All rights reserved.
 */

/**
 * 懒汉式-volatile double check(懒汉式就是使用类实例的时候再去创建，这样就可以避免类在初始化时提前创建)
 *
 * @Author ZhangZhihuang
 * @Date 2022/8/6 14:55
 * @Version 1.0
 */
// final 不允许被继承
public final class SingletonLazy {
    // 实例变量
    private byte[] data = new byte[1024];

    // 在定义实例对象的时候直接初始化(volatile可以防止指令重排)
    private volatile static SingletonLazy instance = null;

    // 私有化构造函数，不允许外部new
    private SingletonLazy() {

    }

    public static SingletonLazy getInstance() {
        // 当instance为null时，进入同步代码块，同时该判断避免了每次都需要进入同步代码块，可以提高效率
        if (null == instance) {
            // 只有一个线程能够获得SingletonHungry.class关联都monitor
            synchronized (SingletonHungry.class) {
                // 判断如果instance为null时则创建
                if (null == instance) {
                    return new SingletonLazy();
                }
            }
        }
        return instance;
    }
}

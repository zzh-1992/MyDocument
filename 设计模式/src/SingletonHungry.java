/*
 *Copyright @2022 Grapefruit. All rights reserved.
 */

/**
 * 饿汉式
 *
 * @Author ZhangZhihuang
 * @Date 2022/8/6 14:55
 * @Version 1.0
 */
// final 不允许被继承
public final class SingletonHungry {
    // 实例变量
    private byte[] data = new byte[1024];

    // 在定义实例对象的时候直接初始化
    private static SingletonHungry instance = new SingletonHungry();

    // 私有化构造函数，不允许外部new
    private SingletonHungry() {

    }

    public static SingletonHungry getInstance() {
        return instance;
    }
}

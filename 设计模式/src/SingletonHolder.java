/*
 *Copyright @2022 Grapefruit. All rights reserved.
 */

/**
 * Holder
 *
 * @Author ZhangZhihuang
 * @Date 2022/8/6 14:55
 * @Version 1.0
 */
// final 不允许被继承
public final class SingletonHolder {
    // 实例变量
    private byte[] data = new byte[1024];

    // 在定义实例对象的时候直接初始化
    private static SingletonHolder instance = new SingletonHolder();

    // 私有化构造函数，不允许外部new
    private SingletonHolder() {

    }

    // 在静态内部类中持有SingletonHolder的实例，并且可被直接实例化
    private static class Holder {
        private static final SingletonHolder INSTANCE = new SingletonHolder();
    }

    // 调用getInstance方法，实际上获取Holder的instance静态属性
    public static SingletonHolder getInstance() {
        return Holder.INSTANCE;
    }
}

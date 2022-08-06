/*
 *Copyright @2022 Grapefruit. All rights reserved.
 */

/**
 * 枚举方式
 *
 * @Author ZhangZhihuang
 * @Date 2022/8/6 14:55
 * @Version 1.0
 */
// 枚举类型本身是final,不允许被继承
public enum SingletonEnum {
    INSTANCE;

    // 实例变量
    private byte[] data = new byte[1024];

    SingletonEnum() {
        System.out.println("Instance will be initialized immediately");
    }

    public static void method() {
        // 调用该方法则会主动使用SingletonEnum，INSTANCE将会被实例化
    }

    public static SingletonEnum getInstance() {
        return INSTANCE;
    }
}

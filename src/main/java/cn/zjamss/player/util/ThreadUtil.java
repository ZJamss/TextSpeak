package cn.zjamss.player.util;

/**
 * @author ZJamss
 * @date 2024/10/3
 * @description 阻塞条件工具类
 */
public class ThreadUtil {

    public static void wait(Object lock) {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

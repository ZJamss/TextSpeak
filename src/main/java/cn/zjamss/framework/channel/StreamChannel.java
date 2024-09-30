package cn.zjamss.framework.channel;

import java.io.File;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 流传输通道
 */
public interface StreamChannel<T> {

    /**
     * data(read) ... => handler
     * channel.open().register(handler)
     */


    /**
     * 打开
     *
     * @param filePath
     * @return
     * @date 2024/9/30
     */
    StreamChannel<T> open(String filePath);

    /**
     * 打开文件
     *
     * @param file
     * @return
     * @date 2024/9/30
     */
    StreamChannel<T> open(File file);


    /**
     * 注册流处理器
     *
     * @param handler
     * @return
     * @date 2024/9/30
     */
    StreamChannel<T> register(Consumer<T> handler);

    void stopOn(Function<T, Boolean> func);
}

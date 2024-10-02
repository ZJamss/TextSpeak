package cn.zjamss.framework.channel;

import cn.zjamss.framework.channel.data.provider.DataProvider;

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
     * 指定数据提供者
     *
     * @param provider
     * @return
     * @date 2024/10/2
     */
    StreamChannel<T> provider(DataProvider<T> provider);


    /**
     * 注册流处理器
     *
     * @param handler
     * @date 2024/9/30
     */
    void register(Consumer<T> handler);

    /**
     * 线程停止条件
     *
     * @param func
     * @return
     * @date 2024/10/2
     */
    StreamChannel<T> stopOn(Function<T, Boolean> func);
}

package cn.zjamss.framework.channel;

import cn.hutool.core.util.ObjectUtil;
import cn.zjamss.framework.channel.data.provider.DataProvider;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 文件流读取抽象类
 */
public abstract class AbstractStreamChannel<T> implements StreamChannel<T>, Converter<T> {

    /**
     * 阻塞队列，存放流数据
     */
    protected final ArrayBlockingQueue<T> blockingQueue = new ArrayBlockingQueue<T>(1);

    /**
     * 2线程，一个发布一个订阅
     */
    protected final ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * 是否已经添加过任务
     */
    protected volatile boolean working = false;

    /**
     * 数据提供器
     */

    protected DataProvider<T> dataProvider;

    /**
     * 线程停止条件
     */
    private Function<T, Boolean> stopConditionFunction;


    @Override
    public StreamChannel<T> provider(DataProvider<T> provider) {
        this.dataProvider = provider;
        return this;
    }

    @Override
    public StreamChannel<T> stopOn(Function<T, Boolean> func) {
        this.stopConditionFunction = func;
        return this;
    }

    @Override
    public void register(Consumer<T> handler) {
        if (working) {
            throw new RuntimeException("已经注册过任务且正在执行");
        }
        if (ObjectUtil.isNull(dataProvider)) {
            throw new RuntimeException("终止条件为空");
        }
        if (ObjectUtil.isNull(stopConditionFunction)) {
            throw new RuntimeException("终止条件为空");
        }
        executorService.execute(() -> {
            working = true;
            // TODO 提供数据 在这怪怪的
            provideData();
            try {
                T data;
                // 判断是否需要终止
                do {
                    // TODO 可能有没数据了还在阻塞的风险，脑子清醒了盘盘逻辑
                    data = blockingQueue.take();
                    handler.accept(data);
                    afterHandled(data);
                } while (!stopConditionFunction.apply(data) && working);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("consumer thread exited");
                working = false;
                // TODO 线程池两个线程都结束了为什么也需要手动shutdown吗，那可能需要好好判断一下条件，后续也可以改一下不要结束
                executorService.shutdown();
            }
        });
    }

    /**
     * 提供数据
     *
     * @param
     * @return
     * @date 2024/9/30
     */
    protected abstract void provideData();


    /**
     * 处理完数据钩子
     *
     * @param data
     * @return
     * @date 2024/10/2
     */
    protected abstract void afterHandled(T data);
}

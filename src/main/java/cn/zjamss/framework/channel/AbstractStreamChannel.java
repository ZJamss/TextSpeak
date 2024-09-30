package cn.zjamss.framework.channel;

import cn.hutool.core.util.ObjectUtil;
import cn.zjamss.framework.channel.data.Converter;

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
     TODO 一条一条来索引下标不会出错，可以持久化？
     */
    protected final ArrayBlockingQueue<T> blockingQueue = new ArrayBlockingQueue<T>(1);

    /**
     * 2线程池，一个发布一个订阅
     */
    // TODO 流！
    protected final ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * 是否已经添加过任务
     */
    private volatile boolean working = false;

    /**
     * 线程停止条件
     */
    private Function<T, Boolean> stopConditionFunction;


    @Override
    public void register(Consumer<T> handler) {
        if (working) {
            throw new RuntimeException("已经注册过任务且正在执行");
        }
        if (ObjectUtil.isNull(stopConditionFunction)) {
            throw new RuntimeException("终止条件为空");
        }
        executorService.execute(() -> {
            try {
                T data = blockingQueue.take();
                // 判断是否需要终止
                while (!stopConditionFunction.apply(data)) {
                    data = blockingQueue.take();
                    System.out.println(data);
                    handler.accept(data);
                }
                working = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        working = true;
    }

    @Override
    public StreamChannel<T> stopOn(Function<T, Boolean> func) {
        this.stopConditionFunction = func;
        return this;
    }

    /**
     * 提供数据
     *
     * @param
     * @return
     * @date 2024/9/30
     */
    protected abstract void provideData();
}

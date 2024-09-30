package cn.zjamss.framework.channel;

import cn.zjamss.framework.channel.data.Converter;

import java.io.File;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 文件流读取抽象类
 */
public abstract class AbstractFileStreamChannel<T> implements StreamChannel<T> {

    protected final ArrayBlockingQueue<T> blockingQueue = new ArrayBlockingQueue<T>(100);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * 是否已经添加过任务
     */
    private volatile boolean working = false;

    private Function<T, Boolean> stopConditionFunction;

    @Override
    public StreamChannel<T> open(String filePath) {
        return this;
    }

    @Override
    public StreamChannel<T> open(File file) {
        return this;
    }

    @Override
    public StreamChannel<T> register(Consumer<T> handler) {
        if (working) {
            throw new RuntimeException("已经注册过任务且正在执行");
        }
        if (stopConditionFunction == null) {
            throw new RuntimeException("未配置流终止条件");
        }
        executorService.execute(() -> {
            try {
                T data = blockingQueue.take();
                // 判断是否需要终止
                while (stopConditionFunction.apply(data)) {
                    data = blockingQueue.take();
                    handler.accept(data);
                }
                working = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        working = true;
        return this;
    }

    @Override
    public void stopOn(Function<T, Boolean> func) {
        this.stopConditionFunction = func;
    }
}

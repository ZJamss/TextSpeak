package cn.zjamss.framework.channel.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zjamss.framework.channel.AbstractStreamChannel;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description String流channel
 */
public class StringStreamChannel extends AbstractStreamChannel<String> {

    @Override
    protected void provideData() {
        executorService.execute(() -> {
            String data = null;
            while (ObjectUtil.isNotNull(data = dataProvider.acquireLastData())) {
                String dataConverted = convert(data);
                if (StrUtil.isBlank(dataConverted)) {
                    continue;
                }
                try {
                    blockingQueue.put(dataConverted);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void afterHandled(String data) {
        // 处理数据完毕再更新索引，避免进度问题
        dataProvider.updateIndex();
    }

    @Override
    public String convert(Object data) {
        return (String) data;
    }
}

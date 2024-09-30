package cn.zjamss.framework.channel.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zjamss.framework.channel.AbstractStreamChannel;
import cn.zjamss.framework.channel.StreamChannel;
import cn.zjamss.framework.channel.data.index.Index;
import cn.zjamss.framework.channel.data.index.IndexFactory;
import cn.zjamss.framework.channel.data.provider.DataProvider;
import cn.zjamss.framework.channel.data.provider.impl.FileLineDataProvider;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 从file读取String流channel
 */
public class FileStringLineStreamChannel extends AbstractStreamChannel<String> {

    private DataProvider<String> provider;

    private volatile boolean opened = false;

    private File file;

    @Override
    public StreamChannel<String> open(String filePath, Charset charset) {
        if (this.opened) {
            return this;
        }
        File openFile = new File(filePath);
        return open(openFile, charset);
    }

    // TODO charset判断
    @Override
    public StreamChannel<String> open(File file, Charset charset) {
        if (this.opened) {
            return this;
        }
        this.file = file;
        if (ObjectUtil.isNull(file)) {
            throw new RuntimeException("file为空");
        }
        provider = new FileLineDataProvider(file, charset);
        this.opened = true;
        // TODO 提供数据
        provideData();
        return this;
    }

    @Override
    protected void provideData() {
        // TODO 感觉或许索引方式很怪？provider创建索引维护file，我这边为什么还需要
        // TODO 流式传输
        executorService.execute(() -> {
            Index index = IndexFactory.getIndex(file.getAbsolutePath());
            String data = null;
            while (ObjectUtil.isNotNull(data = provider.acquireLastData(index))) {
                String dataConverted = convert(data);
                if(StrUtil.isBlank(dataConverted)) {
                    continue;
                }
                try {
                    blockingQueue.put(dataConverted);
                    index = IndexFactory.getIndex(file.getAbsolutePath());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public String convert(Object data) {
        return (String) data;
    }
}

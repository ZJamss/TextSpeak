package cn.zjamss.framework.channel.data.provider.impl;

import cn.zjamss.framework.channel.data.index.Index;
import cn.zjamss.framework.channel.data.index.IndexFactory;
import cn.zjamss.framework.channel.data.provider.DataProvider;

import java.io.File;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 文件数据提供
 */
public class FileDataProvider implements DataProvider {

    public FileDataProvider(File file) {
        initialIndex(file.getAbsolutePath());
    }

    @Override
    public void initialIndex(String eigenvalue) {

    }

    @Override
    public void acquireLastData(Index index) {

    }
}

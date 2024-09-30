package cn.zjamss.framework.channel.data.provider;

import cn.zjamss.framework.channel.data.index.Index;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 数据提供器
 */
public interface DataProvider<T> {

    /**
     * 根据索引获取最新数据
     */
    T acquireLastData(Index index);

    /**
     * 根据特征值是初始化索引
     */
    void initialIndex();
}

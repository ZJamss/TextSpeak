package cn.zjamss.framework.channel.data.provider;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 数据提供器
 */
public interface DataProvider<T> {

    /**
     * 根据索引获取最新数据
     */
    T acquireLastData();

    /**
     * 根据特征值是初始化索引
     */
    void initialIndex();

    /**
     * 更新索引动作
     * @return
     * @date 2024/10/2
     */
    void updateIndex();
}

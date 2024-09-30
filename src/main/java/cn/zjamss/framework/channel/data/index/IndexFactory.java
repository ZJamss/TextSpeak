package cn.zjamss.framework.channel.data.index;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 索引工厂
 */
public class IndexFactory {

    private static final Map<String, Index> indexKeeper = new HashMap<>();

    /**
     * 根据特征值获取索引
     *
     * @param eigenvalue
     * @return
     * @date 2024/9/30
     */
    public static Index getIndex(String eigenvalue) {
        String key = decode(eigenvalue);
        return indexKeeper.get(key);
    }

    /**
     * 创建新的索引
     *
     * @param eigenvalue
     * @param currentIndex
     * @param totalLength
     * @return
     * @date 2024/9/30
     */
    public static Index newIndexInstance(String eigenvalue, Integer currentIndex, Integer totalLength) {
        String key = decode(eigenvalue);
        return new Index(key, currentIndex, totalLength);
    }

    /**
     * 添加index
     *
     * @param eigenvalue
     * @param index
     * @return
     * @date 2024/9/30
     */
    public static void addIndex(String eigenvalue, Index index) {
        String key = decode(eigenvalue);
        indexKeeper.put(key, index);
    }

    /**
     * 更新index
     * @param index
     * @return
     * @date 2024/9/30
     */
    public static void updateIndex(Index index) {
        indexKeeper.put(index.getFd(), index);
    }


    /**
     * 判断index是否存在
     *
     * @param eigenvalue
     * @return
     * @date 2024/9/30
     */
    public static boolean exist(String eigenvalue) {
        String key = decode(eigenvalue);
        return indexKeeper.containsKey(key);
    }

    /**
     * 特征值转码
     *
     * @param eigenvalue
     * @return
     * @date 2024/9/30
     */
    private static String decode(String eigenvalue) {
        // TODO 特征值转码
        return eigenvalue;
    }
}

package cn.zjamss.framework.channel.data.index;

import cn.hutool.core.util.ObjectUtil;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 索引工厂
 */
@SuppressWarnings("unchecked")
public class IndexFactory {

    /**
     * 索引保存器
     */
    private static Map<String, Index> indexKeeper;

    /**
     * 索引保存路径
     */
    private static final String SAVE_PATH = "C:\\Users\\Administrator\\Code\\project\\readText\\src\\main\\resources\\index\\index.list";

    static {
        try {
            Path path = Paths.get(SAVE_PATH);
            indexKeeper = new HashMap<>();

            // 检查文件是否存在
            if (Files.exists(path)) {
                // 恢复索引
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
                    Object object = ois.readObject();
                    if (ObjectUtil.isNotNull(object)) {
                        indexKeeper = (HashMap<String, Index>) object;
                    }
                } catch (EOFException e) {
                    // 文件为空时捕获EOFException并跳过
                    System.out.println("EOFException: 文件为空，跳过索引恢复");
                }
            } else {
                // 如果文件不存在，创建新文件
                Files.createFile(path);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
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
        save();

    }

    /**
     * 更新index
     *
     * @param index
     * @return
     * @date 2024/9/30
     */
    public static void updateIndex(Index index) {
        indexKeeper.put(index.getFd(), index);
        save();
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

    /**
     * 持久化索引
     */
    private static void save() {
        Path path = Paths.get(SAVE_PATH);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(indexKeeper);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

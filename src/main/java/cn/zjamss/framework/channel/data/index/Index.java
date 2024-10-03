package cn.zjamss.framework.channel.data.index;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 数据索引
 */
@Data
@ToString
public class Index implements Serializable, Cloneable {

    public Index(String fd, String eigenvalue, Integer currentIndex, Integer length) {
        this.fd = fd;
        this.eigenvalue = eigenvalue;
        this.currentIndex = currentIndex;
        this.length = length;
    }

    /**
     * 文件描述id
     */
    private String fd;

    /**
     * 特征值，可以是文件绝对路径或者url等等
     */
    private String eigenvalue;

    /**
     * 当前数据索引位置
     */
    private Integer currentIndex;

    /**
     * 文件数据分片总长度
     */
    private Integer length;
}

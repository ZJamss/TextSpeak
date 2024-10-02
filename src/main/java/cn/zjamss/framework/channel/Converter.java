package cn.zjamss.framework.channel;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 流数据转换器
 */
public interface Converter<T> {

    /**
     * 转换流数据
     *
     * @param data
     * @return
     * @date 2024/9/30
     */
    T convert(Object data);
}

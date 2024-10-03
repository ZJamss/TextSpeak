package cn.zjamss.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZJamss
 * @date 2024/10/3
 * @description 命令类型枚举
 */
public enum CommandType {
    /**
     * 文本来源
     */
    SRC("src", true, 0),
    /**
     * 朗读速度 -10 - 10
     */
    RATE("rate", false, "3"),
    /**
     * 音量 0-100
     */
    VOLUME("volume", false, "100"),
    ;

    /**
     * 命令名称
     */
    final String value;
    /**
     * 是否必须
     */
    final boolean required;
    /**
     * 命令默认值
     */
    final Object defaultValue;

    CommandType(String value, boolean required, Object defaultValue) {
        this.value = value;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public static List<String> getRequiredCommands() {
        return Arrays.stream(values()).
                filter(commandType -> commandType.required)
                .map(commandType -> commandType.value)
                .collect(Collectors.toList());
    }

    public String getValue() {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}

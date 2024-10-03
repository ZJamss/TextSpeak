package cn.zjamss.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZJamss
 * @date 2024/10/3
 * @description 命令解析工具类
 */
public class CommandUtil {

    public static Map<String, Object> parse(String[] args) {
        List<String> requiredCommands = CommandType.getRequiredCommands();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            for (String arg : args) {
                String[] kv = arg.split("=");
                if (kv.length != 2) {
                    continue;
                }
                // 移除已配置项
                requiredCommands.remove(kv[0]);
                params.put(kv[0], kv[1]);
            }
            // 检查必须配置项
            if (!requiredCommands.isEmpty()) {
                throw new RuntimeException("必要参数未配置: " + requiredCommands.toString());
            }
            // 配置默认值
            for (CommandType type : CommandType.values()) {
                params.putIfAbsent(type.getValue(), type.defaultValue);
            }
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("参数配置错误");
        }
    }
}

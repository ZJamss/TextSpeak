package cn.zjamss.command;

/**
 * @author ZJamss
 * @date 2024/10/3
 * @description
 */
public enum Command {
    FILE_PATH("file_path", true);

    final String value;
    final boolean required;

    Command(String value, boolean required) {
        this.value = value;
        this.required = required;
    }
}

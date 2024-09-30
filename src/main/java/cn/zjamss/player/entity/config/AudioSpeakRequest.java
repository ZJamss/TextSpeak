package cn.zjamss.player.entity.config;

import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description
 */
@Data
@Builder
public class AudioSpeakRequest {

    /**
     * 朗读文本
     */
    @NotNull
    private String text;

    /**
     * 播放速度 -10 - 10
     */
    private Integer rate = 1;
    /**
     * 音量 0 - 100
     */
    private Integer volume = 100;
    /**
     * 是否需要转文件
     */
    private boolean requireFile = false;
    /**
     * 音频文件格式 TODO 22:mp3?
     */
    private Integer audioType = 22;
    /**
     * 文件保存地址
     */
    private String filePath;

    private AudioSpeakRequest(String text, Integer rate, Integer volume, boolean requireFile, Integer audioType, String filePath) {
        this.text = text;
        this.rate = rate;
        this.volume = volume;
        this.requireFile = requireFile;
        this.audioType = audioType;
        this.filePath = filePath;
    }

    public static AudioSpeakRequest newRequest(String text) {
        return new AudioSpeakRequest(text, 1, 100, false, 22, null);
    }

    public static AudioSpeakRequest newRequest(String text, Integer rate) {
        return new AudioSpeakRequest(text, rate, 100, false, 22, null);
    }

    public static AudioSpeakRequest newRequest(String text, Integer rate, Integer volume) {
        return new AudioSpeakRequest(text, rate, volume, false, 22, null);
    }
}

package cn.zjamss.player.util;

import cn.zjamss.player.entity.config.AudioSpeakRequest;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 播放工具类，System32文件夹内需要放置jacob-1.18-x64/x86.dll
 */
public class SpeakUtil {

    private static ActiveXComponent ax;
    private static Dispatch audio;

    static {
        ax = new ActiveXComponent("Sapi.SpVoice");
        audio = ax.getObject();
    }

    public static void speak(AudioSpeakRequest request) {
        Dispatch.put(audio, "Volume", new Variant(request.getVolume()));
        Dispatch.put(audio, "Rate", new Variant(request.getRate()));

        if (request.isRequireFile()) {
            speak2File(request);
        } else {
            Dispatch.call(audio, "Speak", new Variant(request.getText()));
        }
    }

    public static void speak2File(AudioSpeakRequest request) {
        if (!request.isRequireFile()) {
            return;
        }
        ax = new ActiveXComponent("Sapi.SpFileStream");
        Dispatch spFileStream = ax.getObject();

        ax = new ActiveXComponent("Sapi.SpAudioFormat");
        Dispatch spAudioFormat = ax.getObject();
        // 设置音频流格式
        Dispatch.put(spAudioFormat, "Type", new Variant(request.getAudioType()));
        // 设置文件输出流格式
        Dispatch.putRef(spFileStream, "Format", spAudioFormat);
        // 调用输出 文件流打开方法，创建一个.wav文件
        Dispatch.call(spFileStream, "Open", new Variant(request.getFilePath()), new Variant(3), new Variant(true));
        // 设置声音对象的音频输出流为输出文件对象
        Dispatch.putRef(audio, "AudioOutputStream", spFileStream);
        Dispatch.call(audio, "Speak", new Variant(request.getText()));

        // 关闭输出文件
        Dispatch.call(spFileStream, "Close");
        Dispatch.putRef(audio, "AudioOutputStream", null);

        spAudioFormat.safeRelease();
        spFileStream.safeRelease();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        audio.safeRelease();
        ax.safeRelease();
    }
}

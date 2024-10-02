package cn.zjamss.player;

import cn.zjamss.framework.channel.StreamChannel;
import cn.zjamss.framework.channel.impl.FileStringLineStreamChannel;
import cn.zjamss.player.entity.config.AudioSpeakRequest;
import cn.zjamss.player.util.SpeakUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description
 */
public class Main {
    public static void main(String[] args) throws IOException {
        URL resource = Main.class.getClassLoader().getResource("txt.txt");
        String filePath = resource.getPath();
        StreamChannel<String> channel = new FileStringLineStreamChannel();
        channel.open(filePath, Charset.forName("GB18030"))
                .stopOn(Objects::isNull)
                .register(data -> SpeakUtil.speak(AudioSpeakRequest.newRequest(data, 2)));
    }
}

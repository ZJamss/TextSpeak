package cn.zjamss.player;

import cn.zjamss.framework.channel.StreamChannel;
import cn.zjamss.framework.channel.data.provider.impl.FileLineDataProvider;
import cn.zjamss.framework.channel.impl.StringStreamChannel;
import cn.zjamss.player.entity.config.AudioSpeakRequest;
import cn.zjamss.player.util.SpeakUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description
 */
public class Main {
    public static void main(String[] args) throws IOException {
        URL resource = Main.class.getClassLoader().getResource("a.txt");
        String filePath = resource.getPath();
        StreamChannel<String> channel = new StringStreamChannel();
        channel.provider(FileLineDataProvider.newInstance(new File(filePath),
                        StandardCharsets.UTF_8))
                .stopOn(Objects::isNull)
                .register(data -> SpeakUtil.speak(AudioSpeakRequest.newRequest(data, 2)));
    }
}

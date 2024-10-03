package cn.zjamss.player;

import cn.zjamss.command.CommandType;
import cn.zjamss.command.CommandUtil;
import cn.zjamss.framework.channel.StreamChannel;
import cn.zjamss.framework.channel.data.provider.impl.FileLineDataProvider;
import cn.zjamss.framework.channel.impl.StringStreamChannel;
import cn.zjamss.player.entity.config.AudioSpeakRequest;
import cn.zjamss.player.util.SpeakUtil;
import cn.zjamss.player.util.ThreadUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // 解析参数
        Map<String, Object> params = CommandUtil.parse(args);
        String src = (String) params.get(CommandType.SRC.getValue());
        Integer rate = Integer.valueOf((String) params.get(CommandType.RATE.getValue()));
        Integer volume = Integer.valueOf((String) params.get(CommandType.VOLUME.getValue()));

        // 暂停事件处理
        Object pauseLock = new Object();
        AtomicBoolean pause = new AtomicBoolean(false);

        // 线程监听输入暂停或退出命令
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("q")) {
                    // 退出
                    System.exit(0);
                } else {
                    synchronized (pauseLock) {
                        pause.set(!pause.get());
                        if (!pause.get()) {
                            // 唤醒线程
                            pauseLock.notify();
                        }
                    }
                }
            }
        }).start();

        // 获取流数据
        StreamChannel<String> channel = new StringStreamChannel();
        channel.provider(FileLineDataProvider.newInstance(new File(src), Charset.forName("GB18030")))
                .stopOn(Objects::isNull)
                .register(data -> {
                    // 检查暂停状态
                    synchronized (pauseLock) {
                        while (pause.get()) {
                            ThreadUtil.wait(pauseLock);
                        }
                    }
                    SpeakUtil.speak(AudioSpeakRequest.newRequest(data, rate, volume));
                });

    }
}

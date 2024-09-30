package cn.zjamss.player;

import cn.zjamss.player.entity.config.AudioSpeakRequest;
import cn.zjamss.player.util.SpeakUtil;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description
 */
public class Main {
    public static void main(String[] args) throws IOException {
        URL resource = Main.class.getClassLoader().getResource("txt.txt");
        String filePath = resource.getPath();
        int ski = 1000000;
        try (
                FileInputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, "GB18030");
                BufferedReader br = new BufferedReader(isr);
        ) {
            br.lines().skip(ski).forEach(line -> {
                System.out.println(line);
                SpeakUtil.speak(AudioSpeakRequest.newRequest(line, 10));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

# 摸鱼小工具-文本朗读

### 背景

想着写一个读小说的小工具上班偷偷摸鱼，结果架构好像写得有点开放了

### 特性

- 提供channel机制，通过自定义不同的DataProvider和Channel实现不同的数据流
- 提供SpeakUtil工具类，文字阅读或者转音频

### 使用

- 将dll中的jacob-xxx.dll放入System32文件夹中

> MacOS不清楚，反正系统能识别到就行

- 运行Main方法:

```java
public static void main(String[] args) throws IOException {
    URL resource = Main.class.getClassLoader().getResource("txt.txt");
    String filePath = resource.getPath();
    // 选择字符串流实现
    StreamChannel<String> channel = new StringStreamChannel();
    // 选择文件行数据提供器
    channel.provider(FileLineDataProvider.newInstance(new File(filePath), Charset.forName("GB18030")))
            // 当数据为null时停止获取流
            .stopOn(Objects::isNull)
            // 传入回调方法,注册worker获取流数据
            .register(data -> SpeakUtil.speak(AudioSpeakRequest.newRequest(data, 2)));
}
```
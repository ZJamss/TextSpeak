# 摸鱼小工具-文本朗读

### 背景

想着写一个读小说的小工具上班偷偷摸鱼，结果架构好像写得有点开放了

### 特性

#### 架构

- 提供channel机制，通过自定义不同的DataProvider和Channel实现不同的数据流
- 提供SpeakUtil工具类，文字阅读或者转音频
- 提供索引机制，自定义进度保存

#### 功能

- 自动保存朗读进度，下次启动从最新进度开始播放
- 输入任意字符暂停，输入q退出

> 因为是引入第三方语音库的原因，暂停也只能等当前行播放完才会停止

### 运行

- 将dll中的jacob-xxx.dll放入System32文件夹中

> MacOS不清楚，反正系统能识别到就行

#### jar方式

- 下载Release中的jar包
- (默认已有jre环境)
  运行 `java -jar .\novel2audio-0.1.jar src=文本路径 [ rate=播放速度(-10 - 10) volume=音量(0 - 100)](可选)
  `

> 索引文件默认与jar包同目录保存，文本文件注意编码，默认是GB18030，想要使用其他编码以代码方式运行

#### 代码方式，更自由

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
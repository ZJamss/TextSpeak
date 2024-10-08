# 听小说

文本转音频自动播放，提供底层流的设计实现，可以通过自定义`DataProvider`和`StreamChannel`做到其余想做的事，包括但不限于播放其他数据源文本，同时通过`Index`机制保存数据进度

#### 架构
- 提供channel机制，通过自定义不同的DataProvider和Channel实现不同的数据流
- 提供SpeakUtil工具类，文字阅读或者转音频
- 提供索引机制，自定义进度保存

#### 功能
- [x] 朗读本地文本文件
- [ ] 朗读网络文件（url）
- [ ] 支持多种语音SDK
- [x] 音量，语速设置 
- [x] 自动保存朗读进度，下次启动从最新进度开始播放
- [x] 输入任意字符暂停，输入q退出

> 因为是引入第三方语音库的原因，暂停也只能等当前行播放完才会停止

### 运行

- 将dll中的jacob-xxx.dll放入System32文件夹中

> MacOS不清楚，反正系统能识别到就行

#### jar方式

- 下载Releases中的jar包
- (默认已有jre 1.8环境)
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

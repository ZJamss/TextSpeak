package cn.zjamss.framework.channel.data.provider.impl;

import cn.zjamss.framework.channel.data.index.Index;
import cn.zjamss.framework.channel.data.index.IndexFactory;
import cn.zjamss.framework.channel.data.provider.DataProvider;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author ZJamss
 * @date 2024/9/30
 * @description 文件数据提供
 */
public class FileLineDataProvider implements DataProvider<String> {

    private final File file;
    private final LineNumberReader reader;
    private Charset charset = null;
    private int initialLineNumber = 0;

    public FileLineDataProvider(File file) {
        this.file = file;
        try {
            this.reader = new LineNumberReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        initialIndex();
    }

    public FileLineDataProvider(File file, Charset charset) {
        this.file = file;
        try {
            this.reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), charset));
            // TODO charset判断
            this.charset = charset;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        initialIndex();
    }

    @Override
    public void initialIndex() {
        if (IndexFactory.exist(file.getAbsolutePath())) {
            return;
        }
        try (Stream<String> lines = Files.lines(Paths.get(file.toURI()), this.charset);) {
            Long lineCount = lines.count();
            Index index = IndexFactory.newIndexInstance(file.getAbsolutePath(), 0, lineCount.intValue());
            IndexFactory.addIndex(file.getAbsolutePath(), index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO 流传输？
    @Override
    public String acquireLastData(Index index) {
        Integer currentIndex = index.getCurrentIndex();
        if (currentIndex < 0 || currentIndex > index.getLength()) {
            throw new RuntimeException("错误的currentIndex");
        }
        try {
            while (initialLineNumber++ < currentIndex - 1) {
                System.out.println(reader.readLine());
            }
            reader.setLineNumber(currentIndex);
            String data = reader.readLine();
            index.setCurrentIndex(currentIndex + 1);
            IndexFactory.updateIndex(index);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

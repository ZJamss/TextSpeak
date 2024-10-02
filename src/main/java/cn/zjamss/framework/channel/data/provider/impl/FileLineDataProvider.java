package cn.zjamss.framework.channel.data.provider.impl;

import cn.hutool.core.util.ObjectUtil;
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
 * @description 文件行数据提供
 */
public class FileLineDataProvider implements DataProvider<String> {

    /**
     * 文件实例
     */
    private final File file;
    /**
     * reader
     */
    private final LineNumberReader reader;
    /**
     * 文件编码
     */
    private Charset charset = Charset.defaultCharset();
    /**
     * 文件读取起始行
     */
    private int initialLineNumber = 1;

    private FileLineDataProvider(File file, Charset charset) {
        this.file = file;
        try {
            this.reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), charset));
            if (ObjectUtil.isNotNull(charset)) {
                this.charset = charset;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        initialIndex();
    }


    public static FileLineDataProvider newInstance(File file, Charset charset) {
        return new FileLineDataProvider(file, charset);
    }


    @Override
    public void initialIndex() {
        if (IndexFactory.exist(file.getAbsolutePath())) {
            return;
        }
        // 新建索引
        try (Stream<String> lines = Files.lines(Paths.get(file.toURI()), this.charset);) {
            Long lineCount = lines.count();
            Index index = IndexFactory.newIndexInstance(file.getAbsolutePath(), 0, lineCount.intValue());
            IndexFactory.addIndex(file.getAbsolutePath(), index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String acquireData() {
        Index index = IndexFactory.getIndex(file.getAbsolutePath());
        Integer currentIndex = index.getCurrentIndex();
        if (currentIndex < 0 || currentIndex > index.getLength()) {
            throw new RuntimeException("错误的currentIndex");
        }
        // TODO 有没有其他读取指定行的方法？
        try {
            while (initialLineNumber++ < currentIndex) {
                reader.readLine();
            }
            reader.setLineNumber(currentIndex);
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIndex() {
        Index index = IndexFactory.getIndex(file.getAbsolutePath());
        index.setCurrentIndex(index.getCurrentIndex() + 1);
        IndexFactory.updateIndex(index);
    }
}

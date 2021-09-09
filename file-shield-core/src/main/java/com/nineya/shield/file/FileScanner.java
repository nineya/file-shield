package com.nineya.shield.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
public class FileScanner {
    private File rootPath;
    private FileFilter filter;

    public FileScanner(String rootPath) {
        this.rootPath = new File(rootPath);
    }

    public FileScanner(File rootPath) {
        this.rootPath = rootPath;
    }

    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    public File getRootPath() {
        return rootPath;
    }

    public void execute(FileScannerCallback callback) throws IOException {
        doScanner(rootPath, callback);
    }

    /**
     * 递归执行扫描程序
     *
     * @param path
     * @param callback
     */
    private void doScanner(File path, FileScannerCallback callback) throws IOException {
        File[] files = path.listFiles(filter);
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    callback.file(file);
                } else {
                    doScanner(file, callback);
                }
            }
        }
    }

    /**
     * 扫描到文件后的回调接口
     */
    @FunctionalInterface
    public interface FileScannerCallback {
        /**
         * 回调执行操作
         *
         * @param file
         */
        void file(File file) throws IOException;
    }
}

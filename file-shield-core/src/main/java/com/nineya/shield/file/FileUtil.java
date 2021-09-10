package com.nineya.shield.file;

import java.io.File;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
public class FileUtil {

    /**
     * 文件重命名
     *
     * @param file
     */
    public static void fileRename(String file, String newFileName) {
        File f = new File(file);
        if (f.exists()) {
            f.renameTo(new File(f.getParent(), newFileName));
        }
    }

    /**
     * 取得路径长度，包含分隔符
     *
     * @param file
     */
    public static int getPathSize(File file) {
        String path = file.getAbsolutePath();
        int size = path.length();
        // 如果不是已经携带了分隔符，就需要添加一个分隔符，应对根目录的特殊情况
        if (!File.separator.equals(String.valueOf(path.charAt(size - 1)))) {
            size++;
        }
        return size;
    }
}

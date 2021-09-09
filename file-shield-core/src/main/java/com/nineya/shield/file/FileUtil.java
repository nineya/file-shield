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
}

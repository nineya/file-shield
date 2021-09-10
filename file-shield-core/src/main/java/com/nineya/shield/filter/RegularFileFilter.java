package com.nineya.shield.filter;

import com.nineya.shield.file.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 * 正则过滤文件
 */
public class RegularFileFilter implements FileFilter {
    private boolean include;
    private Set<String> pathRules = new HashSet<>();
    private int baseFileSize;

    /**
     * 正则过滤文件
     *
     * @param include   true：通过符合规则的文件，false：通过不符合规则的文件
     * @param pathRules 正则规则列表
     */
    public RegularFileFilter(boolean include, Collection<String> pathRules) {
        this.include = include;
        addRules(pathRules);
    }

    /**
     * 添加正则规则
     *
     * @param pathRules
     */
    public void addRules(Collection<String> pathRules) {
        if (pathRules != null) {
            this.pathRules.addAll(pathRules);
        }
    }

    /**
     * 基础路径不参与正则过滤
     *
     * @param file
     */
    public void setBaseFile(File file) {
        this.baseFileSize = FileUtil.getPathSize(file);
    }

    @Override
    public boolean accept(File f) {
        return isRegular(f.getAbsolutePath().substring(baseFileSize));
    }

    public boolean isRegular(String path) {
        return pathRules.stream().anyMatch(rule -> {
            Pattern p = Pattern.compile(rule);
            return p.matcher(path).matches();
        }) == include;
    }
}

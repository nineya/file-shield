package com.nineya.shield.config.properties;

import com.nineya.shield.model.enums.ModeEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 */
@Data
public class ShieldProperties {
    /**
     * 模式
     */
    private ModeEnum mode;
    /**
     * 选择的摘要算法
     */
    private String algo;
    /**
     * 摘要文件路径，
     */
    private String digestPath;
    /**
     * 文件扫描路径配置
     */
    private Map<String, ShieldPathProperties> paths;
    /**
     * 文件url过滤（正则）
     * 不包含的url，全局所有路径有效
     */
    private Set<String> filter;
}

package com.nineya.shield.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 * path 参数配置
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShieldPathProperties {
    /**
     * 路径对应的名称
     */
    @EqualsAndHashCode.Include
    private String name;
    /**
     * 扫描的url根路径
     */
    private String url;
    /**
     * 当前路径特定的filter过滤（正则）
     */
    private Set<String> filter;

    public ShieldPathProperties(String name, String url) {
        this.name = name;
        this.url = url;
    }
}

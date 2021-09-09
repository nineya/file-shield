package com.nineya.shield.execute;

import com.nineya.shield.config.properties.ShieldProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
@Slf4j
public class ShieldExecute {
    private ShieldProperties properties;

    public ShieldExecute(ShieldProperties properties) {
        log.info("start execute shield.");
        this.properties = properties;
    }

    /**
     * 创建不同模式的执行程序
     *
     * @return
     */
    public DigestExecute buildMode() {
        switch (properties.getMode()) {
            case Check:
                return new CheckDigestExecute(properties);
            case Build:
            default:
                return new BuildDigestExecute(properties);
        }
    }

    /**
     * 执行程序
     */
    public void execute() {
        DigestExecute execute = buildMode();
        execute.execute();
    }
}

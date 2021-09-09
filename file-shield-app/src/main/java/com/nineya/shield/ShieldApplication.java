package com.nineya.shield;

import com.nineya.shield.config.ShieldConfiguration;
import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.execute.ShieldExecute;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 殇雪话诀别
 * 2021/9/6
 */
@Slf4j
public class ShieldApplication {
    public static void main(String[] args) {
        run(args);
    }

    /**
     * 运行程序，进行全局异常捕获
     *
     * @param args
     */
    public static void run(String[] args) {
        try {
            doRun(args);
        } catch (Throwable e) {
            log.error("running error", e);
        }
    }

    /**
     * 运行程序
     *
     * @param args
     */
    public static void doRun(String[] args) {
        log.info("start shield.");
        ShieldProperties properties = ShieldConfiguration.instance(args).config();
        ShieldExecute execute = new ShieldExecute(properties);
        execute.execute();
        log.info("shield success.");
    }
}

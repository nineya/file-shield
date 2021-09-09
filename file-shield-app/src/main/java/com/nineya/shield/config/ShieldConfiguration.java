package com.nineya.shield.config;

import com.nineya.shield.config.properties.ShieldProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 */
@Slf4j
public class ShieldConfiguration {
    private CommandConfiguration commandConfiguration;
    private YamlConfiguration yamlConfiguration;

    private ShieldConfiguration(String[] args) {
        log.info("start load config.");
        commandConfiguration = CommandConfiguration.instance(args);
        yamlConfiguration = YamlConfiguration.instance(commandConfiguration.configPath());
    }

    public static ShieldConfiguration instance(String[] args) {
        return new ShieldConfiguration(args);
    }

    /**
     * 加载配置文件和命令行的配置信息
     *
     * @return
     */
    public ShieldProperties config() {
        ShieldProperties properties = commandConfiguration.config(yamlConfiguration.config());
        log.info("complete load config.");
        return properties;
    }
}

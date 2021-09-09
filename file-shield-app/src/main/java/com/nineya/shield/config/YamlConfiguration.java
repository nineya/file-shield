package com.nineya.shield.config;

import com.nineya.shield.config.properties.ShieldPathProperties;
import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.model.enums.ModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 */
@Slf4j
public class YamlConfiguration {
    private final Map<String, Object> params = new LinkedHashMap<>();

    private YamlConfiguration(String configPath) {
        Yaml yaml = new Yaml();
        try {
            params.putAll(yaml.load(new FileInputStream(configPath)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("配置文件不存在", e);
        }
    }

    public static YamlConfiguration instance(String configPath) {
        return new YamlConfiguration(configPath);
    }

    /**
     * 加载配置文件中 filter 过滤器的信息
     *
     * @param params
     * @return
     */
    private Set<String> loadPathFilter(Map<String, Object> params) {
        Object o = params.get("filter");
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            return new LinkedHashSet<String>((List) o);
        }
        throw new RuntimeException("filter params: not string list");
    }

    /**
     * 加载 yaml 文件中 paths 路径信息
     *
     * @param properties
     */
    private void loadPathProperties(ShieldProperties properties) {
        Object o = params.get("paths");
        Set<ShieldPathProperties> pathProperties = new HashSet<>();
        if (!(o instanceof List)) {
            pathProperties.add(new ShieldPathProperties(".", "default"));
            properties.setPaths(pathProperties);
            return;
        }
        List<Map<String, Object>> paths = (List<Map<String, Object>>) o;
        for (Map<String, Object> path : paths) {
            String url = String.valueOf(path.get("url"));
            String name = String.valueOf(path.get("name"));
            if (url == null || name == null) {
                throw new RuntimeException("paths params: url or name not null");
            }
            pathProperties.add(new ShieldPathProperties(name, url, loadPathFilter(path)));
        }
        properties.setPaths(pathProperties);
    }

    /**
     * 加载yaml文件信息
     *
     * @param properties
     */
    private void loadShieldProperties(ShieldProperties properties) {
        properties.setAlgo(String.valueOf(params.getOrDefault("algo", "sha-256")));
        properties.setMode(ModeEnum.value(String.valueOf(params.getOrDefault("mode", "Build"))));
        properties.setDigestPath(String.valueOf(params.get("digestPath")));
        properties.setFilter(loadPathFilter(params));
        loadPathProperties(properties);
    }

    /**
     * 加载yaml文件的配置信息
     *
     * @return
     */
    public ShieldProperties config() {
        ShieldProperties properties = new ShieldProperties();
        loadShieldProperties(properties);
        return properties;
    }
}

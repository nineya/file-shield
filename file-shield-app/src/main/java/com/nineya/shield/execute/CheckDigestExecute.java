package com.nineya.shield.execute;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.nineya.shield.config.properties.ShieldPathProperties;
import com.nineya.shield.config.properties.ShieldProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
@Slf4j
public class CheckDigestExecute extends DigestExecute {
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private ShieldProperties digestProperties;

    public CheckDigestExecute(ShieldProperties properties) {
        super(properties);
        try {
            fileReader = new FileReader(properties.getDigestPath());
            bufferedReader = new BufferedReader(fileReader);
            loadConfig(bufferedReader.readLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("digest file not found", e);
        } catch (IOException e) {
            throw new RuntimeException("digest file read error", e);
        }
    }

    /**
     * 加载初始配置，打印配置信息
     *
     * @param config
     */
    public void loadConfig(String config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        digestProperties = gson.fromJson(config, ShieldProperties.class);
        log.info("digest config info:\n" + gson.toJson(digestProperties));
    }

    @Override
    protected void notFoundFile(FileNotFoundException e) {
        log.error("history file not found", e);
    }

    /**
     * 根据name获取baseUrl，进行拼接获取完整的url
     * 先查询当前配置，如果当前配置不存在name则从digest配置中获取name
     *
     * @param name
     * @param path
     * @return
     */
    public String splicePath(String name, String path) {
        ShieldPathProperties pathProperties = properties.getPaths().get(name);
        if (pathProperties == null) {
            pathProperties = digestProperties.getPaths().get(name);
        }
        return pathProperties.getUrl() + File.separator + path;
    }

    @Override
    public void execute() {
        try {
            String line;
            int errorNum = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] digest = line.split(":");
                if (digest.length != 3) {
                    throw new RuntimeException("analysis digest file error");
                }
                System.out.print(line + "\r");
                String path = splicePath(digest[0], digest[1]);
                String hash = hash(path);
                if (!digest[2].equalsIgnoreCase(hash)) {
                    errorNum++;
                    log.error("hash error: digestHash = {}, currentHash = {}, filePAth = {}", digest[2], hash, path);
                }
            }
            log.info("check complete, error num {} !", errorNum);
        } catch (FileNotFoundException e) {
            log.error("digest file not found", e);
        } catch (IOException e) {
            log.error("digest file read error", e);
        } finally {
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                log.error("file reader close error", e);
            }
        }
    }
}

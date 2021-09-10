package com.nineya.shield.execute;

import com.google.gson.Gson;
import com.nineya.shield.config.properties.ShieldPathProperties;
import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.file.FileScanner;
import com.nineya.shield.file.FileUtil;
import com.nineya.shield.filter.RegularFileFilter;

import java.io.*;
import java.util.Map;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
public class BuildDigestExecute extends DigestExecute {
    private static final String DIGEST_NAME = "digest.bat";
    private static final String DIGEST_NAME_FORMAT = "digest-%s.bat";

    public BuildDigestExecute(ShieldProperties properties) {
        super(properties);
    }

    @Override
    protected void notFoundFile(FileNotFoundException e) {

    }

    private FileScanner buildFileScanner(ShieldPathProperties pathProperties) {
        File baseFile = new File(pathProperties.getUrl());
        FileScanner scanner = new FileScanner(baseFile);

        RegularFileFilter fileFilter = new RegularFileFilter(false, properties.getFilter());
        fileFilter.addRules(pathProperties.getFilter());
        fileFilter.setBaseFile(baseFile);
        scanner.setFilter(fileFilter);
        return scanner;
    }

    @Override
    public void execute() {
        Map<String, ShieldPathProperties> pathProperties = properties.getPaths();
        FileUtil.fileRename(DIGEST_NAME, String.format(DIGEST_NAME_FORMAT, System.currentTimeMillis()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIGEST_NAME))) {
            bw.write(new Gson().toJson(properties));
            bw.newLine();
            int num = 1;
            int count = pathProperties.size();
            for (Map.Entry<String, ShieldPathProperties> entry : pathProperties.entrySet()) {
                FileScanner scanner = buildFileScanner(entry.getValue());
                String name = entry.getKey();
                int basePathSize = FileUtil.getPathSize(scanner.getRootPath());
                int finalNum = num;
                scanner.execute((file) -> {
                    String path = file.getAbsolutePath().substring(basePathSize);
                    System.out.print(String.format("(%d/%d)%s:%s\r", finalNum, count, name, path));
                    bw.write(String.format("%s:%s:%s", name, path, hash(file)));
                    bw.newLine();
                });
                num++;
            }
        } catch (IOException e) {
            throw new RuntimeException("file io error", e);
        }
    }
}

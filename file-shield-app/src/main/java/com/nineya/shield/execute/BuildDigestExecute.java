package com.nineya.shield.execute;

import com.nineya.shield.config.properties.ShieldPathProperties;
import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.file.FileScanner;
import com.nineya.shield.file.FileUtil;
import com.nineya.shield.filter.RegularFileFilter;

import java.io.*;
import java.util.Set;

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
        Set<ShieldPathProperties> pathProperties = properties.getPaths();
        FileUtil.fileRename(DIGEST_NAME, String.format(DIGEST_NAME_FORMAT, System.currentTimeMillis()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIGEST_NAME))) {
            for (ShieldPathProperties spp : pathProperties) {
                FileScanner scanner = buildFileScanner(spp);
                String name = spp.getName();
                int basePathSize = scanner.getRootPath().getAbsolutePath().length() + 1;
                scanner.execute((file) -> {
                    bw.write(String.format("%s:%s:%s", name, file.getAbsolutePath().substring(basePathSize), hash(file)));
                    bw.newLine();
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("file io error", e);
        }
    }
}

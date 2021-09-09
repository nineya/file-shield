package com.nineya.shield.digest;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
@Slf4j
public abstract class DigestService {
    private MessageDigest digest;

    public DigestService(String algo) {
        try {
            digest = MessageDigest.getInstance(algo);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no such algorithm: " + algo, e);
        }
    }


    /**
     * 计算文件对应的hash
     *
     * @param file
     * @return
     */
    public String hash(String file) {
        return hash(new File(file));
    }

    /**
     * 计算文件对应的hash
     *
     * @param file
     * @return
     */
    public String hash(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, length);
            }
            return HexBin.encode(digest.digest());
        } catch (FileNotFoundException e) {
            notFoundFile(e);
        } catch (IOException e) {
            log.error("read file error: " + file, e);
        }
        return null;
    }

    /**
     * 处理文件不存在异常
     *
     * @param e
     */
    protected abstract void notFoundFile(FileNotFoundException e);
}

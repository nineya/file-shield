package com.nineya.shield.execute;

import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.digest.DigestService;

/**
 * @author 殇雪话诀别
 * 2021/9/9
 */
public abstract class DigestExecute extends DigestService implements Execute {
    protected ShieldProperties properties;

    public DigestExecute(ShieldProperties properties) {
        super(properties.getAlgo());
        this.properties = properties;
    }
}

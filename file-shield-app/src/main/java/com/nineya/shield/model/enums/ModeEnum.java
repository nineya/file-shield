package com.nineya.shield.model.enums;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 * 扫描模式
 */
public enum ModeEnum {
    /**
     * 生成摘要文件
     */
    Build("生成摘要文件"),
    /**
     * 校验文件
     */
    Check("校验文件");

    private final String info;

    ModeEnum(String info) {
        this.info = info;
    }

    public String info() {
        return info;
    }

    public static ModeEnum value(String name) {
        for (ModeEnum modeEnum : values()) {
            if (modeEnum.name().equalsIgnoreCase(name)) {
                return modeEnum;
            }
        }
        return Build;
    }
}

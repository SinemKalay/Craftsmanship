package com.xebialabs.craftsmanship.helper;

public enum SalvoTypeEnum {

    HIT("hit"), MISS("miss"), KILL("kill");

    private String desc;

    SalvoTypeEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
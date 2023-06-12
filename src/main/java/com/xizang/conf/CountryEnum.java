package com.xizang.conf;

import java.time.ZoneId;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/12 11:37
 */
public enum CountryEnum {
    cn(143465, "UTC+8"),
    us(143441, "America/Los_Angeles");

    private int storeFrontId;
    private ZoneId zoneId;

    CountryEnum(int storeFrontId, String timeZoneId) {
        this.storeFrontId = storeFrontId;
        this.zoneId = ZoneId.of(timeZoneId);
    }
    public int getStoreFrontId() {
        return storeFrontId;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }
}
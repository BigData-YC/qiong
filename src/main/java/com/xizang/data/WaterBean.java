package com.xizang.data;

import com.xizang.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/12 11:19
 */
@Data
@AllArgsConstructor
public class WaterBean {
    private String name;
    private String code;
    private LocalDateTime time;
    private double high;

    private double check;

    private double sub;

    private double newHigh;

    public WaterBean(String name, String code, LocalDateTime time, double high) {
        this.name = name;
        this.code = code;
        this.time = time;
        this.high = high;
    }

    @Override
    public String toString() {
        return "WaterBean{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", time=" + time.format(DateUtils.normalFormatter) +
                ", high=" + high +
                '}';
    }
}

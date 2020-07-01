package com.xizang.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author 剑雨江湖
 * @version 1.0
 * @date 2020/6/29 17:47
 * @desc TODO
 */
@Getter
@Setter
public class DataBean {
    private Long lineNum;
    private String date;
    private String time;
    private double waterHight;
    private String desc;

    public DataBean(){}

    public DataBean(Long lineNum, String date, String time, double waterHight, String desc) {
        this.lineNum = lineNum;
        this.date = date;
        this.time = time;
        this.waterHight = waterHight;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "{" +
                "行数=" + lineNum +
                ",  " + date + '\'' +
                ",  " + time + '\'' +
                ",  " + waterHight +
                ",  '" + desc + '\'' +
                '}';
    }
}

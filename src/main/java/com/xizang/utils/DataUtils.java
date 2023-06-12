package com.xizang.utils;

import com.xizang.data.WaterBean;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.xizang.utils.DateUtils.parseStr;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/12 11:16
 */
public class DataUtils {

    public static void show(List<List<WaterBean>> list) {
        list.forEach(s -> s.forEach(System.out::println));
    }

    /**
     *  转化数据
     * @param data
     * @return
     */
    public static List<List<WaterBean>> parseData(List<List<String>> data) {
        List<List<WaterBean>> list = new ArrayList<>();
        List<WaterBean> temp = new ArrayList<>();
        String dateKey = null;
        for (int i = 0; i < data.size(); i++) {
            List<String> line = data.get(i);
            WaterBean bean;
            if ((bean=parse(line)) == null) continue;
            LocalDateTime time = bean.getTime();
            String currentDate = time.format(DateUtils.formatter);
            if (dateKey != null && !currentDate.equals(dateKey)) {
                list.add(getData(temp));
                temp = new ArrayList<>();
                temp.add(bean);
            }
            temp.add(bean);
            dateKey = currentDate;
        }
        return list;
    }

    public static List<WaterBean> getData(List<WaterBean> data) {
        if (CollectionUtils.isEmpty(data)) return null;

        WaterBean maxBean = data.stream().max(Comparator.comparingDouble(WaterBean::getHigh)).get();
        WaterBean minBean = data.stream().min(Comparator.comparingDouble(WaterBean::getHigh)).get();
        return data.stream()
                .filter(Objects::nonNull)
                .filter(x -> {
                    LocalDateTime time = x.getTime();
                    if (time.equals(maxBean.getTime())) return true;
                    if (time.equals(minBean.getTime())) return true;
                    String dateTime = time.format(DateUtils.normalFormatter);
                    if (dateTime.endsWith("08:00:00") || dateTime.endsWith("00:00:00") ||
                        dateTime.endsWith("14:00:00") || dateTime.endsWith("20:00:00")) {
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(WaterBean::getTime))
        .collect(Collectors.toList());


    }
    public static WaterBean parse(List<String> line) {
        // 编码、名称、时间、水位
        if (CollectionUtils.isEmpty(line) || line.size() < 3) return null;
        try {
            double high = Double.parseDouble(line.get(3));
            LocalDateTime localDateTime = parseStr(line.get(2));
            return new WaterBean(line.get(1), line.get(0), localDateTime, high);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}

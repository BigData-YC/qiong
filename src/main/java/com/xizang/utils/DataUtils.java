package com.xizang.utils;

import com.xizang.data.WaterBean;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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
     * @param check
     * @return
     */
    public static List<List<WaterBean>> parseData(List<List<String>> data, List<List<String>> check) {
        Map<String, Double> checkKey = check.stream().collect(Collectors.toMap(x -> x.get(0), x -> Double.valueOf(x.get(1))));

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
                list.add(getData(temp, checkKey));
                temp = new ArrayList<>();
                temp.add(bean);
            }
            temp.add(bean);
            dateKey = currentDate;
        }
        return list;
    }

    public static List<WaterBean> getData(List<WaterBean> data, Map<String, Double> checkKey) {
        if (CollectionUtils.isEmpty(data)) return null;

        WaterBean maxBean = data.stream().max(Comparator.comparingDouble(WaterBean::getHigh)).get();
        WaterBean minBean = data.stream().min(Comparator.comparingDouble(WaterBean::getHigh)).get();
        List<Double> sub2 = new ArrayList<>();

        List<WaterBean> newList = data.stream()
                .filter(Objects::nonNull)
                .filter(x -> {
                    LocalDateTime time = x.getTime();
                    if (time.equals(maxBean.getTime())) return true;
                    if (time.equals(minBean.getTime())) return true;
                    String dateTime = time.format(DateUtils.normalFormatter);
                    if (dateTime.endsWith("08:00:00") || dateTime.endsWith("00:00:00") ||
                        dateTime.endsWith("14:00:00") || dateTime.endsWith("20:00:00")) {
                        //处理校验
                        if (dateTime.endsWith("08:00:00") || dateTime.endsWith("20:00:00")) {
                            double orDefault = checkKey.getOrDefault(dateTime, x.getHigh());
                            double sub = subtraction(orDefault, x.getHigh());
                            x.setSub(sub);
                            x.setCheck(orDefault);
                            sub2.add(sub);
                        }
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(WaterBean::getTime))
        .collect(Collectors.toList());

        double s1 = newList.stream().filter(x -> x.getTime().format(DateUtils.normalFormatter).endsWith("08:00:00"))
                .mapToDouble(WaterBean::getSub).max().orElse(0D);
        double s2 = newList.stream().filter(x -> x.getTime().format(DateUtils.normalFormatter).endsWith("20:00:00"))
                .mapToDouble(WaterBean::getSub).max().orElse(0D);


        if (s1 < -0.03 || s1 > 0.03 || s2 < -0.03 || s2 > 0.03) {
            double a = division(subtraction(s2, s1), 12D, 3);
            newList.forEach(x -> {
                int hour = x.getTime().getHour();
                double sub = subtraction(s2, (20 - hour) * a, 2);
                double newHigh = add(x.getHigh(), sub, 2);
                x.setSub(sub);
                x.setNewHigh(newHigh);
            });
        } else {
            newList.forEach(x -> x.setNewHigh(x.getHigh()));
        }

        return newList;

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

    /**
     * 四舍五入计算
     *
     * @param big   被除数
     * @param small 除数
     * @param scale 小数位
     * @return
     */
    public static double division(long big, long small, int scale) {
        return division(big + "", small + "", scale);
    }
    public static double division(double big, double small, int scale) {
        return division(big + "", small + "", scale);
    }

    public static double division(String big, String small, int scale) {
        try {
            if (Integer.valueOf(small) == 0) return 0D;
            BigDecimal b = new BigDecimal(big);
            BigDecimal s = new BigDecimal(small);
            return b.divide(s, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return 0D;
        }
    }

    public static double division(long big, long small) {
        return division(big, small, 4);
    }
    public static double multiplication(long big, long small) {
        BigDecimal bigDecimal = new BigDecimal(big);
        return division(big, small, 4);
    }

    public static double add(double big, double small) {
        if (big == 0D) return small;
        if (small == 0D) return big;
        return new BigDecimal(big + "").add(new BigDecimal(small + "")).doubleValue();
    }

    public static double add(double big, double small, int scala) {
        if (big == 0D) return small;
        if (small == 0D) return big;
        return new BigDecimal(big + "")
                .add(new BigDecimal(small + ""))
                .setScale(scala, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    public static double subtraction(double big, double small) {
        return new BigDecimal(big + "").subtract(new BigDecimal(small + "")).doubleValue();
    }

    public static double subtraction(double big, double small, int scala) {
        return new BigDecimal(big + "").subtract(new BigDecimal(small + "")).setScale(scala, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}

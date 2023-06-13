package com.xizang.utils;


import com.xizang.conf.CountryEnum;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author yangchong
 * @Date 2021/3/3 3:42 下午
 * @Version 1.0
 * @Desc 关于时间的处理
 */
public class DateUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    public static DateTimeFormatter birthdayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    public static DateTimeFormatter monthFormatter_ = DateTimeFormatter.ofPattern("yyyy-MM");
    public static DateTimeFormatter normalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter flinkFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static DateTimeFormatter excelFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    public static DateTimeFormatter utcFormatter = DateTimeFormatter.ISO_INSTANT;
    public static long MILLI_SECOND = 1000_000_000_000L;

    public static long getTodayTimeStamp() {
        return ZonedDateTime.now().toEpochSecond();
    }

    public static String getToday(DateTimeFormatter formatter) {
        return ZonedDateTime.now().format(formatter);
    }

    public static String getToday(){
        return getToday(normalFormatter);
    }

    public static String getYesterday(){
        return ZonedDateTime.now().plusDays(-1).format(formatter);
    }

    public static long getStartOfToday() {
        return ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toEpochSecond();
    }

    public static long getStartOfYesterday() {
        return ZonedDateTime.now().plusDays(-1).truncatedTo(ChronoUnit.DAYS).toEpochSecond();
    }

    public static long getStartOfDay(int days) {
        return ZonedDateTime.now().plusDays(days).truncatedTo(ChronoUnit.DAYS).toEpochSecond();
    }
    /**
     * 将任意时间戳转化为凌晨0点的时间戳
     *
     * @param timeStamp 秒级时间戳
     * @return
     */
    public static long getStartOfDay(long timeStamp, CountryEnum country) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), country.getZoneId()).toLocalDate().atStartOfDay(country.getZoneId()).toEpochSecond();
    }

    public static long getStartOfDay(long timeStamp) {
        return getStartOfDay(timeStamp, CountryEnum.cn);
    }

    public static long getOneDaySeconds(String day, CountryEnum country) {
        return LocalDate.parse(day, formatter).atStartOfDay(country.getZoneId()).toEpochSecond();
    }

    public static long getOneDaySeconds(String day, DateTimeFormatter formatter) {
        return LocalDate.parse(day, formatter).atStartOfDay(CountryEnum.cn.getZoneId()).toEpochSecond();
    }

    public static String getOtherDay(String date, int days) {
        return LocalDate.parse(date, formatter).plusDays(days).format(formatter);
    }

    public static long getOneDaySeconds(String day) {
        return getOneDaySeconds(day, CountryEnum.cn);
    }

    public static String getDate(long timeStamp, DateTimeFormatter formatter) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), CountryEnum.cn.getZoneId()).format(formatter);
    }

    public static String getDate(long timeStamp) {
        return getDate(timeStamp, formatter);
    }

    public static String getDateTime(long timeStamp) {
        return getDate(timeStamp, normalFormatter);
    }

    public static String getDate(long timeStamp, DateTimeFormatter formatter, int plusHours) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), CountryEnum.cn.getZoneId()).plusHours(plusHours).format(formatter);
    }

    public static String getDateTime(String day, DateTimeFormatter flinkFormatter, int plusHours) {
        final LocalDateTime parse = LocalDateTime.parse(day, flinkFormatter);
        return parse.plusHours(plusHours).format(flinkFormatter);
    }

    public static String getDateTime(String day, int plusHours) {
        return getDateTime(day, flinkFormatter, plusHours);
    }

    public static String getDateTime(long timeStamp, int plusHours) {
        return getDate(timeStamp, normalFormatter, plusHours);
    }

    public static long getTimeStamps(ZonedDateTime zonedDateTime, ChronoUnit dateType) {
        return zonedDateTime.truncatedTo(dateType).toEpochSecond();
    }

    public static long getDayTimeStamps(ZonedDateTime zonedDateTime) {
        return getTimeStamps(zonedDateTime, ChronoUnit.DAYS);
    }

    public static long getDayTimeStamps(long timeStamp) {
        return Instant.ofEpochSecond(timeStamp).atZone(CountryEnum.cn.getZoneId())
                .truncatedTo(ChronoUnit.DAYS).toEpochSecond();
    }

    public static long getHourTimeStamps(ZonedDateTime zonedDateTime) {
        return getTimeStamps(zonedDateTime, ChronoUnit.HOURS);
    }

    public static long getHourTimeStamps(long timeStamp) {
        return Instant.ofEpochSecond(timeStamp).atZone(CountryEnum.cn.getZoneId())
                .truncatedTo(ChronoUnit.HOURS).toEpochSecond();
    }

    public static long getTimeStamps(ZonedDateTime zonedDateTime, ChronoUnit dateType, int min) {
        long hourTimeStamp = zonedDateTime.truncatedTo(dateType).toEpochSecond();
        final int minute = zonedDateTime.getMinute();
        return hourTimeStamp + ((minute / min) * min);
    }

    public static String changeDateType(String date, DateTimeFormatter form, DateTimeFormatter to) {
        return LocalDate.parse(date, formatter).format(formatter);
    }

    public static long getMinu20TimeStamps(ZonedDateTime zonedDateTime) {
        return getTimeStamps(zonedDateTime, ChronoUnit.HOURS, 20);
    }

    public static long getMinu20TimeStamps(long timeStamp) {
        ZonedDateTime zonedDateTime = Instant.ofEpochSecond(timeStamp).atZone(CountryEnum.cn.getZoneId());
        long hourTimeStamp = zonedDateTime.truncatedTo(ChronoUnit.HOURS).toEpochSecond();
        final int minute = zonedDateTime.getMinute();
        return hourTimeStamp + ((minute / 20) * 20);
    }

    /**
     *
     * @param millisecond   毫秒
     * @return  季度：202201-03
     */
    public static String get3Month(long millisecond) {
        LocalDate localDate = millisecond > MILLI_SECOND ?
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), CountryEnum.cn.getZoneId()).toLocalDate():
            LocalDateTime.ofInstant(Instant.ofEpochSecond(millisecond), CountryEnum.cn.getZoneId()).toLocalDate();

        final int month = localDate.getMonthValue();
        int mo = (month - 1) % 3;
        LocalDate startMonth = localDate.minusMonths(mo);
        int endMonth = startMonth.plusMonths(2).getMonthValue();
        String start = startMonth.format(monthFormatter);
        String end = endMonth < 10 ? "-0" + endMonth : "-"+ endMonth;
        return start + end;
    }

    public static LocalDateTime parseStr(String date) {
        return LocalDateTime.parse(date, excelFormatter);
    }

    public static void main(String[] args) {
        String s = "2023-04-01 00:00:05.9";
        LocalDateTime parse = LocalDateTime.parse(s, excelFormatter);

        System.out.println(parse);
    }

}

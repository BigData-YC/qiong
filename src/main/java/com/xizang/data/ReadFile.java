package com.xizang.data;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 剑雨江湖
 * @version 1.0
 * @date 2020/6/29 17:45
 * @desc TODO
 */
public class ReadFile {
    private String fileName;
    private List<DataBean> data = new ArrayList<>();

    public ReadFile(){}
    public ReadFile(String fileName){
        this.fileName = fileName;
    }

    public void getBigOrSmallDate() throws IOException {
        File file = new File(fileName);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"GBK");
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        Long lineNum = 1l;
        // 判断同一天
        String tmpDate = "2019";
        DataBean maxData = new DataBean();
        DataBean minData = new DataBean();
        // 同一天最大值
        double max = 0l;
        // 同一天最小值
        double min = 10000l;
        line = br.readLine();
        while(line != null) {
            if(!line.startsWith("20")){
                line = br.readLine();
                continue;
            }
            // System.out.println(line);
            String[] fields = line.split(",");
            double waterHight = Double.valueOf(fields[2]);
            if(waterHight>max){
                max = waterHight;
                maxData.setLineNum(lineNum);
                maxData.setDate(fields[0]);
                maxData.setTime(fields[1]);
                maxData.setWaterHight(max);
                maxData.setDesc("最高水位");
            }
            if(waterHight<min){
                min = waterHight;
                minData.setLineNum(lineNum);
                minData.setDate(fields[0]);
                minData.setTime(fields[1]);
                minData.setWaterHight(min);
                minData.setDesc("最低水位");
            }

            if(!tmpDate.equals(fields[0]) ){
                if(!tmpDate.equals("2019")){
                    data.add(maxData);
                    data.add(minData);
                    maxData = new DataBean();
                    minData = new DataBean();
                    max = 0l;
                    min = 10000l;
                }
                tmpDate = fields[0];
            }
            DataBean date = getDate(lineNum, fields);
            if(date!=null){
                data.add(date);
            }

            line = br.readLine();
            lineNum += 1;
        }
        if(br!=null){
            br.close();
        }
        if(reader!=null){
            reader.close();
        }
    }


    public DataBean getDate(long num,String[]fields) {
        if(fields.length<3||num<0){
            return null;
        }
        String time = fields[1].trim() ;
        Date date = null;
        try {
             date = new SimpleDateFormat("HH:mm").parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch ( String.valueOf(date.getTime())){
            case "0":
                return new DataBean(num,fields[0],fields[1],Double.valueOf(fields[2]),"8点的水位");
            case "43200000":
                return new DataBean(num,fields[0],fields[1],Double.valueOf(fields[2]),"20点的水位");
            case "-28800000":
                return new DataBean(num,fields[0],fields[1],Double.valueOf(fields[2]),"24点的水位");
            default:
                return null;
        }
    }

    public static void main(String[] args) throws IOException {
        ReadFile readFile = new ReadFile("C:\\Users\\32384\\Desktop\\2019年类乌齐完整自记数据.csv");
        readFile.getBigOrSmallDate();
        List<DataBean> list = readFile.data;
        System.out.println(list.size());
        for(DataBean dataBean:list){
            System.out.println(dataBean.toString());

        }
        //readFile.data.forEach(any->System.out.println(any.toString()));
        //String str = "ad\ds\dsfa";
    }
}

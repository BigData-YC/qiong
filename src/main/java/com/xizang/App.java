package com.xizang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException {
        Date date = new SimpleDateFormat("HH:mm").parse("20:00:25");
        System.out.println( String.valueOf(date.getTime()) );
    }
}

package com.newland.property.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式化规则
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M");
        // 格式化日期并返回
        System.out.println(currentDate.format(formatter));
    }
}

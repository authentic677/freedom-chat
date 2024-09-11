package xyz677123.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static LocalDateTime now(String zone){
        //设置正确而统一的时区
        ZonedDateTime zonedDateTime=ZonedDateTime.now();
        ZonedDateTime zonedDateTime1 = zonedDateTime.withZoneSameInstant(ZoneId.of(zone));
        return zonedDateTime1.toLocalDateTime();
    }

    public static String getCurrentYearMonth() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化当前日期并返回
        return currentDate.format(formatter);
    }
}

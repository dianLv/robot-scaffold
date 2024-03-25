package code.dianlv.robot.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils
{
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
    
    public static String time(int epochSecond)
    {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        LocalDateTime now = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return now.format(FORMATTER);
    }
    
    public static String time(int epochSecond, int zoneId)
    {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        LocalDateTime now = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(zoneId));
        return now.format(FORMATTER);
    }
    
    public static String now()
    {
        return LocalDateTime.now().format(FORMATTER);
    }
}

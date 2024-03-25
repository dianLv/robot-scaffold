package code.dianlv.robot.utils;

public class SleepUtils
{
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void sleep(int sec)
    {
        sleep(sec * 1000L);
    }
    
    public static void randomSleep(int sec)
    {
        sleep(RUtils.range(sec));
    }
    
    public static void randomSleep(int secMin, int secMax)
    {
        sleep(RUtils.range(secMin, secMax));
    }
}

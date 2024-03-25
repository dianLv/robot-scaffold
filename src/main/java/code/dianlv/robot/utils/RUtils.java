package code.dianlv.robot.utils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * a simple random utils
 */
public class RUtils
{
    public static Random random()
    {
        return ThreadLocalRandom.current();
    }
    
    public static int number(int bound)
    {
        return random().nextInt(bound);
    }
    
    /**
     * @return between boundMin ~ boundMax
     */
    public static int range(int boundMin, int boundMax)
    {
        return boundMin + number(boundMax);
    }
    
    /**
     * @return between 1 ~ bound
     */
    public static int range(int bound)
    {
        return range(1, bound);
    }
    
    public static boolean bool()
    {
        return random().nextBoolean();
    }
    
    @SafeVarargs
    private static <T> T randomOne(T... ts)
    {
        if (ts == null || ts.length == 0) throw new IllegalArgumentException("args is null or empty");
        
        return ts[number(ts.length)];
    }
    
    public static <T> T randomOne(List<T> list)
    {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException("args is null or empty");
        
        return list.get(number(list.size()));
    }
    
    public static String randomString(String... more)
    {
        return randomOne(more);
    }
    
    public static int randomNumber(Integer... more)
    {
        return randomOne(more);
    }
}

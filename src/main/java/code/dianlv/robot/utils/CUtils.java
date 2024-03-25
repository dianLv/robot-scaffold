package code.dianlv.robot.utils;

import java.util.Collection;

/**
 * base utils
 */
public class CUtils
{
    public static boolean isNotEmpty(String string)
    {
        return string != null && !string.isEmpty();
    }
    
    public static boolean isNotEmpty(Collection<?> collections)
    {
        return collections != null && !collections.isEmpty();
    }
    
    /**
     * Capitalize first letter
     */
    public static String upperCase(String string)
    {
        if (string == null) throw new IllegalArgumentException("args must be not null");
        if (string.isEmpty()) return string;
        
        char[] chars = string.toCharArray();
        
        if (chars[0] >= 'a' && chars[0] <= 'z')
        {
            chars[0] -= 32;
        }
        
        return new String(chars);
    }
}

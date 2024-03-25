package code.dianlv.robot.utils;

/**
 * about math
 */
public class MathUtils
{
    /**
     * How many decimal places
     */
    public static int statDigit(int number)
    {
        if (number == 0) return 1;
        int _num = Math.abs(number);
        int count = 0;
        while (_num > 0)
        {
            _num /= 10;
            count += 1;
        }
        return count;
    }
    
    public static double angle(double sx, double sy, double tx, double ty)
    {
        double x = tx - sx;
        double y = ty - sy;
        
        double sqrt = Math.sqrt(x*x + y*y);
        
        if (sqrt == 0) return Math.toDegrees(0);
        
        if (y >= 0)
        {
            return Math.toDegrees(Math.acos(x / sqrt));
        }
        else
        {
            return Math.toDegrees(Math.PI * 2 - Math.acos(x / sqrt));
        }
    }
    
    public static double distance(double sx, double sy, double tx, double ty)
    {
        double x = sx - tx;
        double y = sy - ty;
        
        return Math.sqrt(x*x + y*y);
    }
}

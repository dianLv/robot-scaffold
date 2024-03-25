package code.dianlv.robot.protocol;

public class ProtocolConverts
{
    /**
     * @param t Protocol
     * @return bytes
     */
    public static <T extends Protocol> byte[] serialize(T t)
    {
        throw new AbstractMethodError();
    }
    
    /**
     * @param bytes Protocol Bytes
     * @param clazz Protocol implement's class
     * @return protocol
     */
    public static <T extends Protocol> T deserialize(byte[] bytes, Class<T> clazz)
    {
        throw new AbstractMethodError();
    }
}

package code.dianlv.robot.connection;

/**
 * This object is used to represent a Message|Packet|Telegram.<br>
 * A message typically consists of a header and a body.<br>
 *  Header(include message_id, messsage_version, user_info, gateway_id etc.)<br>
 *  Body, The bytes obtained from protocol conversion.<br>
 * @see ./codec/SimpleCodec.java
 */
public class Message
{
    /**
     * message_id: request/response id
     */
    private int id;
    
    /**
     * message content or packet data
     */
    private byte[] payload;
    
    public Message()
    {
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public byte[] getPayload()
    {
        return payload;
    }
    
    public void setPayload(byte[] payload)
    {
        this.payload = payload;
    }
}

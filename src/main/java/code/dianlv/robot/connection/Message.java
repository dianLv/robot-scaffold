package code.dianlv.robot.connection;

/**
 * a (Message|Packet|Telegram) object
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

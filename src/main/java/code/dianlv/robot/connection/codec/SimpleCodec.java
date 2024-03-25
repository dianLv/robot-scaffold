package code.dianlv.robot.connection.codec;

import code.dianlv.robot.connection.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * a simple codec, <br>
 * send ByteBuf(message_length, message_id, message_body(payload))<br>
 */
public class SimpleCodec implements Codec
{
    private static final int MESSAGE_MAX_LENGTH = 65535;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int MESSAGE_LENGTH_FIELD_LENGTH = 4;
    /**
     * message_len:int id:int
     */
    private static final int DEFAULT_MESSAGE_LENGTH = 8;
    
    @Override
    public MessageToByteEncoder<Message> newEncoder()
    {
        return new MessageToByteEncoder<>()
        {
            @Override
            protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception
            {
                if (msg != null)
                {
                    int length = DEFAULT_MESSAGE_LENGTH;
                    int id = msg.getId();
                    byte[] payload = msg.getPayload();
                    
                    if (payload != null && payload.length > 0)
                    {
                        // payload = encryption(payload)
                        length += payload.length;
                    }
                    
                    out.writeInt(length);
                    out.writeInt(id);
                    
                    if (payload != null && payload.length > 0) out.writeBytes(payload);
                }
            }
        };
    }
    
    @Override
    public LengthFieldBasedFrameDecoder newDecoder()
    {
        return new LengthFieldBasedFrameDecoder(MESSAGE_MAX_LENGTH, LENGTH_FIELD_OFFSET, MESSAGE_LENGTH_FIELD_LENGTH, -MESSAGE_LENGTH_FIELD_LENGTH, MESSAGE_LENGTH_FIELD_LENGTH)
        {
            @Override
            protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
            {
                ByteBuf frame = (ByteBuf) super.decode(ctx, in);
                
                if (frame == null) return null;
                
                try
                {
                    Message message = new Message();
                    message.setId(frame.readInt());
                    int len = frame.readableBytes();
                    
                    if (len > 0)
                    {
                        byte[] buf = new byte[len];
                        frame.readBytes(buf);
                        //  buf = decrypt(buf)
                        message.setPayload(buf);
                    }
                    
                    return message;
                }
                finally
                {
                    // prevent memory leak
                    frame.release();
                }
            }
        };
    }
}

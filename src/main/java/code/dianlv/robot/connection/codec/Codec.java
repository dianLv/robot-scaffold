package code.dianlv.robot.connection.codec;

import code.dianlv.robot.connection.Message;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public interface Codec
{
    MessageToByteEncoder<Message> newEncoder();
    
    ByteToMessageDecoder newDecoder();
}

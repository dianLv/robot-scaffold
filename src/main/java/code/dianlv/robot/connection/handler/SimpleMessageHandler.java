package code.dianlv.robot.connection.handler;

import code.dianlv.robot.connection.Connection;
import code.dianlv.robot.connection.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class SimpleMessageHandler extends SimpleChannelInboundHandler<Message>
{
    @Override
    public void channelActive(ChannelHandlerContext ctx ) throws Exception
    {
        // ...
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        ctx.channel().attr(Connection.CONNECTION).get().onRecv(msg);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // super.channelInactive(ctx);
        
        ctx.channel().attr(Connection.CONNECTION).get().release();
    }
}

package code.dianlv.example.client;

import code.dianlv.robot.connection.Connection;
import code.dianlv.robot.connection.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class SimpleGameClientHandler extends SimpleChannelInboundHandler<Message>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        System.out.println("server " + new String(msg.getPayload()));
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);
        
        ctx.channel().close();
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent stateEvent)
        {
            if (stateEvent.state() == IdleState.WRITER_IDLE)
            {
                Message message = new Message();
                message.setId(1000);
                ctx.channel().writeAndFlush(message);
            }
        }
    }
}

package code.dianlv.robot.connection.handler;

import code.dianlv.robot.connection.Connection;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class SimpleHeartbeatHandler extends ChannelDuplexHandler
{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent stateEvent)
        {
            if (stateEvent.state() == IdleState.WRITER_IDLE)
            {
                ctx.channel().attr(Connection.CONNECTION).get().keepalive();
            }
            
            // other codes ...
        }
    }
}

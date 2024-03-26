package code.dianlv.example.origin.server;

import code.dianlv.robot.connection.Message;
import code.dianlv.robot.utils.TimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class SimpleGameServerHandler extends SimpleChannelInboundHandler<Message>
{
    private static final Map<Channel, String> users = new ConcurrentHashMap<>();
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        
        ctx.channel().eventLoop().scheduleAtFixedRate(() ->
        {
            Message message = new Message();
            message.setId(9999);
            message.setPayload(TimeUtils.now().getBytes());
            ctx.channel().writeAndFlush(message);
        }, 60, 60, TimeUnit.SECONDS);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        switch (msg.getId())
        {
            case 1000:
                onKeepalive(ctx, msg);
                break;
                
            case 1001:
                onLogin(ctx, msg);
                break;
                
            case 1002:
                onTalk(ctx, msg);
                break;
                
            default:
        }
    }
    
    private void onTalk(ChannelHandlerContext ctx, Message msg)
    {
        byte[] payload = msg.getPayload();
        if (payload == null || payload.length == 0)
        {
            Message newMessage = new Message();
            newMessage.setId(2001);
            newMessage.setPayload("Error, Not Content!".getBytes());
            ctx.channel().writeAndFlush(newMessage);
        }
        else
        {
            String content = new String(payload);
            String userName = users.get(ctx.channel());
            
            Message newMessage = new Message();
            newMessage.setId(2001);
            newMessage.setPayload((userName + ": " + content).getBytes());
            
            users.keySet().forEach(channel -> channel.writeAndFlush(newMessage));
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if (cause instanceof SocketException)
        {
            users.remove(ctx.channel());
            ctx.channel().close();
        }
        else
        {
            super.exceptionCaught(ctx, cause);
        }
    }
    
    private void onLogin(ChannelHandlerContext ctx, Message msg)
    {
        byte[] payload = msg.getPayload();
        String userName = new String(payload);
        
        users.put(ctx.channel(), userName);
        
        Message newMessage = new Message();
        newMessage.setId(2001);
        newMessage.setPayload((userName + " login success.").getBytes());
        
        ctx.channel().writeAndFlush(newMessage);
    }
    
    private void onKeepalive(ChannelHandlerContext ctx, Message msg)
    {
        Message newMessage = new Message();
        newMessage.setId(2000);
        newMessage.setPayload("server keep connect".getBytes());
        ctx.channel().writeAndFlush(newMessage);
    }
}

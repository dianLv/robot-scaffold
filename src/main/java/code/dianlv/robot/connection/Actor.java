package code.dianlv.robot.connection;

import code.dianlv.robot.client.CommonHandler;
import code.dianlv.robot.protocol.Protocol;
import io.netty.channel.Channel;

/**
 * Client Behavior Simulation
 */
public class Actor implements Connection
{
    private Channel channel;
    
    private MessageDispatcher dispatcher;
    
    // Client Behavior and Data ...
    // public final CommonHandler commonHandler =
    //     new CommonHandler(this);
    
    public Actor(Channel channel)
    {
        this.channel = channel;
        this.channel.attr(CONNECTION).set(this);
    }
    
    public MessageDispatcher getDispatcher()
    {
        return dispatcher;
    }
    
    public void setDispatcher(MessageDispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }
    
    @Override
    public void onRecv(Message message)
    {
        if (dispatcher != null)
        {
            dispatcher.dispatch(this, message);
        }
        
        // unlock
    }
    
    @Override
    public void send(int requestId, byte[] payload)
    {
        if (!isConnecting()) throw new IllegalStateException("connect is Disconnect");
        // build a message
        Message message = new Message();
        message.setId(requestId);
        message.setPayload(payload);
        // send
        channel.writeAndFlush(message);
    }
    
    public void send(int requestId, Protocol protocol)
    {
        // ...
    }
    
    // send message 1001, 1002, 1003...
    //                |     |     |
    //  wait server 2001, 2002, 2003...
    // use lock+Condition
    //  1. send 1001 message and lock current send thread
    //  2. wait server response some message
    //  3. receive 2001 message unlock
    // use status(idle, waiting, ...)
    //  0. check current state
    //  1. send 1001 message, state idle -> waiting
    //  2. wait server response some message
    //  3. receive 2001 message, state waiting -> idle
    public void sendSync(int requestId, int responseId, byte[] payload)
    {
        // if(!isIdle()) return;
        // lock on
        send(requestId, payload);
        // await responseId
        // lock off
    }
    
    public void sendSync(int requestId, int responseId, Protocol protocol)
    {
        // ...
    }
    
    @Override
    public Channel getChannel()
    {
        return channel;
    }
    
    @Override
    public boolean isConnecting()
    {
        return this.channel != null && this.channel.isActive();
    }
    
    // public boolean isOnline()
    // {
    //     return false;
    // }
    
    
    // @Override
    // public void keepalive()
    // {
    //     if (commonHandler != null) commonHandler.keepalive();
    // }
    
    @Override
    public void release()
    {
        if (this.channel != null)
        {
            this.channel.close();
        }
    }
    
    // about Reusable
    // public void init(Channel channel)
    // {
    // }
    //
    // public void reset()
    // {
    //     release();
    //     // clear local data
    // }
}

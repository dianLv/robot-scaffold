package code.dianlv.robot.connection;

import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.exception.ChannelCreateException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DefaultActorFactory implements ActorFactory
{
    private static final EventLoopGroup workerGroup =
            new NioEventLoopGroup();
    private final Codec codec;
    private final ChannelHandler heartbeatHandler;
    private final ChannelHandler handler;
    protected Bootstrap bootstrap;
    private boolean first;  // initialized
    
    public DefaultActorFactory(Codec codec, ChannelHandler heartbeatHandler, ChannelHandler handler)
    {
        if (codec == null) throw new IllegalArgumentException("");
        if (handler == null) throw new IllegalArgumentException("");
        
        this.codec = codec;
        this.heartbeatHandler = heartbeatHandler;
        this.handler = handler;
    }
    
    @Override
    public void init()
    {
        if (first) throw new IllegalStateException("actorFactory initialized");
        
        // build bootstrap
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(ConnectConfig.SOCKET_CHANNEL)
                .option(ChannelOption.TCP_NODELAY, ConnectConfig.TCP_NODELAY)
                .option(ChannelOption.SO_KEEPALIVE, ConnectConfig.TCP_SO_KEEPALIVE)
                .option(ChannelOption.SO_REUSEADDR, ConnectConfig.TCP_SO_REUSEADDR)
                .option(ChannelOption.SO_SNDBUF, ConnectConfig.TCP_SO_SNDBUF)
                .option(ChannelOption.SO_RCVBUF, ConnectConfig.TCP_SO_RCVBUF);
    
        initFlowControl();
        
        bootstrap.handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception
            {
                ChannelPipeline pipeline = channel.pipeline();
                
                pipeline.addLast("decoder", codec.newDecoder());
                pipeline.addLast("encoder", codec.newEncoder());
                
                if (heartbeatHandler != null)
                {
                    pipeline.addLast("idleStateHandler",
                            new IdleStateHandler(ConnectConfig.READ_SECONDS,
                                    ConnectConfig.SEND_SECONDS,
                                    ConnectConfig.READ_SEND_SECONDS));
                    pipeline.addLast("heartbeatHandler", heartbeatHandler);
                }
                
                pipeline.addLast("handler", handler);
            }
        });
        
        first = true;
    }
    
    /**
     * about flow control code
     */
    private void initFlowControl()
    {
        // ...
    }
    
    @Override
    public Actor createActor(String targetIp, int targetPort) throws ChannelCreateException
    {
        Channel channel = buildConnection(targetIp, targetPort, 0, false);
        
        return new Actor(channel);
    }
    
    @Override
    public Actor createActor(String targetIp, int targetPort, int timeout) throws ChannelCreateException
    {
        Channel channel = buildConnection(targetIp, targetPort, timeout, true);
        
        return new Actor(channel);
    }
    
    @Override
    public List<Actor> createActors(String targetIp, int targetPort, int amount)
    {
        if (!first) throw new IllegalStateException("actorFactory uninitialized");
        
        CountDownLatch latch = new CountDownLatch(amount);
        List<Actor> actors = new CopyOnWriteArrayList<>();
        
        bootstrap.connect(targetIp, targetPort)
                .addListener((ChannelFutureListener)future ->
                {
                    if (future.isSuccess())
                    {
                        Channel channel = future.channel();
                        actors.add(new Actor(channel));
                        latch.countDown();
                    }
                });
        
        try
        {
            boolean awaited = latch.await(ConnectConfig.DEFAULT_BATCH_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            
            if (!awaited) throw new IllegalStateException("connects timeout");
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        
        return actors;
    }
    
    private Channel buildConnection(String targetIP, int targetPort, int timeout, boolean checkTimeout) throws ChannelCreateException
    {
        if (!first) throw new IllegalStateException("actorFactory uninitialized");
    
        if (checkTimeout)
        {
            if (timeout <= 0) throw new IllegalArgumentException("illegal timeout");
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        }
        
        ChannelFuture future = bootstrap.connect(targetIP, targetPort);
        future.awaitUninterruptibly();
        
        Channel channel = future.channel();
        if (!channel.isActive())
        {
            throw new ChannelCreateException("create channel failed");
        }
        return channel;
    }
}

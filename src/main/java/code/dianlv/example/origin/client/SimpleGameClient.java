package code.dianlv.example.origin.client;

import code.dianlv.robot.connection.ConnectConfig;
import code.dianlv.robot.connection.Message;
import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.codec.SimpleCodec;
import code.dianlv.robot.utils.RUtils;
import code.dianlv.robot.utils.SleepUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleGameClient
{
    public static void main(String[] args)
    {
        final String targetIp = "127.0.0.1";
        final int targetPort = 8080;
        
        final int timeSeconds = 15;
        
        Codec codec = new SimpleCodec();
        
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(ConnectConfig.SOCKET_CHANNEL)
                .option(ChannelOption.TCP_NODELAY, ConnectConfig.TCP_NODELAY)
                .option(ChannelOption.SO_KEEPALIVE, ConnectConfig.TCP_SO_KEEPALIVE)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ch.pipeline().addLast(codec.newDecoder());
                        ch.pipeline().addLast(codec.newEncoder());
                        ch.pipeline().addLast(new IdleStateHandler(0, timeSeconds, 0));
                        ch.pipeline().addLast(new SimpleGameClientHandler());
                    }
                });
        
        String[] users = {"AA", "BB", "CC", "DD", "EE"};
        String[] msg = {"Hello", "Hi", "哈喽", "Bonjour", "Hola", "こんにちは", ""};
        ExecutorService service = Executors.newFixedThreadPool(users.length);
        
        for (String user : users)
        {
            service.execute(() ->
            {
                ChannelFuture channelFuture = bootstrap.connect(targetIp, targetPort);
                channelFuture.awaitUninterruptibly();
                
                Channel channel = channelFuture.channel();
                Message loginMessage = new Message();
                loginMessage.setId(1001);
                loginMessage.setPayload(user.getBytes());
                channel.writeAndFlush(loginMessage);
                
                while (true)
                {
                    Message talkMessage = new Message();
                    talkMessage.setId(1002);
                    talkMessage.setPayload(RUtils.randomString(msg).getBytes());
                    
                    channel.writeAndFlush(talkMessage);
                    SleepUtils.randomSleep(30);
                }
            });
        }
    }
}

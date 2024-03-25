package code.dianlv.example.client;

import code.dianlv.robot.connection.ConnectConfig;
import code.dianlv.robot.connection.Message;
import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.codec.SimpleCodec;
import code.dianlv.robot.utils.SleepUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Scanner;

public class SimpleGameClient
{
    public static void main(String[] args)
    {
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
                        ch.pipeline().addLast(new IdleStateHandler(0, 15, 0));
                        ch.pipeline().addLast(new SimpleGameClientHandler());
                    }
                });
        
        try
        {
            Channel channel = bootstrap.connect("127.0.0.1", 8080).sync().channel();
            
            Scanner scanner = new Scanner(System.in);
            
            // login
            Message loginMessage = new Message();
            loginMessage.setId(1001);
            System.out.println("login");
            loginMessage.setPayload(scanner.nextLine().getBytes());
            channel.writeAndFlush(loginMessage);
            
            while (true)
            {
                Message talkMessage = new Message();
                talkMessage.setId(1002);
                // talkMessage.setPayload(scanner.nextLine().getBytes());
                channel.writeAndFlush(talkMessage);
                
                SleepUtils.randomSleep(5);
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            group.shutdownGracefully();
        }
    }
}

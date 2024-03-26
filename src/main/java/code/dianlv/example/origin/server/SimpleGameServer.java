package code.dianlv.example.origin.server;

import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.codec.SimpleCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleGameServer
{
    public static void main(String[] args)
    {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        
        Codec codec = new SimpleCodec();
        final int port = 8080;
        
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
        
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, false)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(codec.newDecoder());
                            pipeline.addLast(codec.newEncoder());
                            pipeline.addLast(new SimpleGameServerHandler());
                        }
                    });
            
            ChannelFuture future = bootstrap.bind(port).addListener(new ChannelFutureListener()
            {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception
                {
                    if (future.isSuccess())
                    {
                        System.out.println("Server open in port: " + port);
                    }
                }
            }).sync();
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

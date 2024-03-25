package code.dianlv.robot.connection;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectConfig
{
    // ----------- Connect -----------
    public static final Class<? extends Channel> SOCKET_CHANNEL = NioSocketChannel.class;
    public static final Boolean TCP_NODELAY = true;
    public static Boolean TCP_SO_REUSEADDR = false;
    public static Boolean TCP_SO_KEEPALIVE = true;
    public static Integer TCP_SO_SNDBUF = 65535;
    public static Integer TCP_SO_RCVBUF = 65535;
    
    public static final int DEFAULT_BATCH_CONNECT_TIMEOUT = 60;     // 60s
    
    // ----------- Heartbeat -----------
    public final static int READ_SECONDS = 0;
    public final static int SEND_SECONDS = 120;
    public final static int READ_SEND_SECONDS = 0;
}

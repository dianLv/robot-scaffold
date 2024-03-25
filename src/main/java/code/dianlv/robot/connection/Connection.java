package code.dianlv.robot.connection;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * client <-> server
 *  1. client send request, server response, client receive
 *      example: client send 1001 and wait 2002, server responds with 2001, client continue
 *  2.1. client send request, server record and no response
 *      example: The client sends multiple requests and the server responds.
 *  2.2. server notify message, client receive and trigger on event
 *      example: Activity open, server time now, ...
 */
public interface Connection
{
    // about this
    AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");
    
    /**
     * receive a message from server side
     */
    void onRecv(Message message);
    
    /**
     * build message(requestId, payload), and send to server side
     */
    void send(int requestId, byte[] payload);
    
    /**
     * @return current connection bind's channel
     */
    Channel getChannel();
    
    /**
     * send a keepalive message, keep heartbeat
     */
    default void keepalive() {}
    
    /**
     * release channel
     */
    default void release() {}
    
    /**
     * check channel
     */
    boolean isConnecting();
}

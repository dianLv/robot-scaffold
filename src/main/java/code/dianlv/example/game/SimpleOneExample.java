package code.dianlv.example.game;

import code.dianlv.robot.client.CommonHandler;
import code.dianlv.robot.connection.Actor;
import code.dianlv.robot.connection.DefaultActorFactory;
import code.dianlv.robot.connection.MessageDispatcher;
import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.codec.SimpleCodec;
import code.dianlv.robot.connection.exception.ChannelCreateException;
import code.dianlv.robot.connection.handler.SimpleHeartbeatHandler;
import code.dianlv.robot.connection.handler.SimpleMessageHandler;

public class SimpleOneExample
{
    public static void main(String[] args) throws ChannelCreateException
    {
        MessageDispatcher dispatcher = new MessageDispatcher();
        
        Codec codec = new SimpleCodec();
        SimpleHeartbeatHandler heartbeatHandler = new SimpleHeartbeatHandler();
        SimpleMessageHandler messageHandler = new SimpleMessageHandler();
        
        DefaultActorFactory factory = new DefaultActorFactory(codec, heartbeatHandler, messageHandler);
        Actor actor = factory.createActor("127.0.0.1", 8080);
        actor.setDispatcher(dispatcher);
        
        // Specific game code ...
        CommonHandler common = new CommonHandler(actor);
        common.login("Hello, World");
        
    }
}

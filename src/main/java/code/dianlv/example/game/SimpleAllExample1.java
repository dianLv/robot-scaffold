package code.dianlv.example.game;

import code.dianlv.robot.connection.Actor;
import code.dianlv.robot.connection.DefaultActorFactory;
import code.dianlv.robot.connection.MessageDispatcher;
import code.dianlv.robot.connection.codec.Codec;
import code.dianlv.robot.connection.codec.SimpleCodec;
import code.dianlv.robot.connection.exception.ChannelCreateException;
import code.dianlv.robot.connection.handler.SimpleHeartbeatHandler;
import code.dianlv.robot.connection.handler.SimpleMessageHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleAllExample1
{
    public static void main(String[] args) throws ChannelCreateException
    {
        final String targetIp = "127.0.0.1";
        final int targetPort = 8080;
        final int actorAmount = 80;
        
        MessageDispatcher dispatcher = new MessageDispatcher();
        
        Codec codec = new SimpleCodec();
        SimpleHeartbeatHandler heartbeatHandler = new SimpleHeartbeatHandler();
        SimpleMessageHandler messageHandler = new SimpleMessageHandler();
        
        DefaultActorFactory factory = new DefaultActorFactory(codec, heartbeatHandler, messageHandler);
        List<Actor> actors = factory.createActors(targetIp, targetPort, actorAmount);
        
        // Specific game code ...
        ExecutorService service = Executors.newFixedThreadPool(actors.size());
        for (Actor actor : actors)
        {
            actor.setDispatcher(dispatcher);
            service.execute(() ->
            {
                // 1. actor.login();
                // 2. ...
                // or   
                // 1. set sync points
                // 2. all actor send request
            });
        }
    }
}

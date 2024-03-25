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

public class SimpleAllExample2
{
    public static void main(String[] args) throws ChannelCreateException
    {
        MessageDispatcher dispatcher = new MessageDispatcher();
        
        Codec codec = new SimpleCodec();
        SimpleHeartbeatHandler heartbeatHandler = new SimpleHeartbeatHandler();
        SimpleMessageHandler messageHandler = new SimpleMessageHandler();
        
        DefaultActorFactory factory = new DefaultActorFactory(codec, heartbeatHandler, messageHandler);
        List<Actor> actors = factory.createActors("127.0.0.1", 8080, 100);
        // bind dispatcher, process message
        for (Actor actor : actors)
        {
            actor.setDispatcher(dispatcher);
        }
        
        // polling all actor
        // while (true)
        // {
        //     for (Actor actor : actors)
        //     {
        //         // actor state: idle, waiting event, working
        //         // login, enter game, play
        //     }
        // }
    }
}

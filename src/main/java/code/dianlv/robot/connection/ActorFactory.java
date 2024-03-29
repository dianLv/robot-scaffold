package code.dianlv.robot.connection;

import code.dianlv.robot.connection.exception.ChannelCreateException;

import java.util.List;

/**
 * build actor factory
 */
public interface ActorFactory
{
    void init();
    
    Actor createActor(String targetIp, int targetPort) throws ChannelCreateException;
    
    Actor createActor(String targetIp, int targetPort, int timeout) throws ChannelCreateException;
    
    /**
     *  a batch of actors
     */
    List<Actor> createActors(String targetIp, int targetPort, int amount);
}

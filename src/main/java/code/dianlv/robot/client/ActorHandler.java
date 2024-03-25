package code.dianlv.robot.client;

import code.dianlv.robot.connection.Actor;

public abstract class ActorHandler
{
    protected final Actor actor;
    
    public ActorHandler(Actor actor)
    {
        this.actor = actor;
    }
    
    /**
     * reset data filed
     */
    public void reset()
    {
    }
}

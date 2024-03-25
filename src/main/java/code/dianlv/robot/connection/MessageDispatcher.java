package code.dianlv.robot.connection;

import code.dianlv.robot.protocol.Protocol;
import code.dianlv.robot.protocol.ProtocolConverts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * How to appropriately handle messages sent by the server.<br>
 * 1. build a mapper, include (message_id, Processor)<br>
 * 2. Processor consists of (ProcessorHandler's Instance, process_method, method_param)<br>
 * 3. ProcessorHandler example:
 * <pre>
 * class ProcessorHandler_1 {
 *  &#064;Mapping(10001)
 *  public void handle(Protocol p) {
 *      Actor actor = ActorMgr.getActor();
 *      ...
 *  }
 *
 *  ...
 * }
 * or
 * class ProcessorHandler_2 {
 *  &#064;Mapping(10001)
 *  public void handle(Actor a, Protocol p) {
 *      ...
 *  }
 *
 *  ...
 * }
 * 4. Utilize reflection to establish connections among these.
 * 5. scanner and find ProcessorHandler class
 * </pre>
 */
public class MessageDispatcher
{
    Map<Integer, Processor> mapper = new HashMap<>();
    
    public void dispatch(Actor actor, Message message)
    {
        // currentActor = actor;
        Optional.ofNullable(mapper.get(message.getId())).ifPresent(processor ->
        {
            byte[] payload = message.getPayload();
            // deserialize protocol
            Protocol protocol = ProtocolConverts.deserialize(payload, processor.param);
            processor.process(protocol);
            //or processor.process(actor, protocol);
        });
    }
    
    record Processor(Object instance, Method method, Class<? extends Protocol> param)
        {
            public void process(Protocol protocol)
            {
                try
                {
                    method.invoke(instance, protocol);
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            }
            
            public void process(Actor actor ,Protocol protocol)
            {
                try
                {
                    method.invoke(instance, actor, protocol);
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
}

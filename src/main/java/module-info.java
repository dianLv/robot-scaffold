module code.dianlv
{
    requires io.netty.codec;
    requires io.netty.common;
    requires io.netty.buffer;
    requires io.netty.transport;
    requires io.netty.handler;
    
    // netty require sun.misc.Unsafe
    requires jdk.unsupported;
}
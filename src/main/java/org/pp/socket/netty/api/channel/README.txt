循环依赖关系~~~  查看组件关系图吧！
之所以要搞出这么多关系，是因为解耦，领域划分

Channel接口
Channel：连接数据源/数据汇与缓冲区的抽象接口
Channel注册到EventLoop的多路复用器上，用于处理IO事件，通过eventloop方法可以获取到所注册的EventLoop

在继承体系中最终服务端nio channel：NioServerSocketChannel，在ServerBootstrap bind地址时它通过ReflectiveChannelFactory实例化
① NioServerSocketChannel实例化时创建java nio ServerSocketChannel并传递（未注册）ACCEPT事件到AbstractNioChannel --- method:newSocket
② 同时关键字super调用，调用父类构造，直到AbstractChannel这个超级父类时会完成进一步的初始化工作
    protected AbstractChannel(Channel parent) {
        this.parent = parent;
        id = newId();
        unsafe = newUnsafe(); // 子类实现NioServerSocketChannel这边是继承AbstractNioMessageChannel实现创建Unsafe（返回AbstractNioUnsafe类型）
        pipeline = newChannelPipeline();  // 自身实现
    }
protected DefaultChannelPipeline newChannelPipeline() {
    return new DefaultChannelPipeline(this); // 这里通过构造注入，将channel与pipeline关联
}


Unsafe接口时Channel内部的一个接口，它是一个辅助接口，用于实际的数据传输，这些方法并须从I/O线程操作，以下几个方法除外：
    * <ul>
    *   <li>{@link #localAddress()}</li>
    *   <li>{@link #remoteAddress()}</li>
    *   <li>{@link #closeForcibly()}</li>
    *   <li>{@link #register(EventLoop, ChannelPromise)}</li>
    *   <li>{@link #deregister(ChannelPromise)}</li>
    *   <li>{@link #voidPromise()}</li>
    * </ul>

FAQ. 这些例外方法是无法从I/O线程操作中拆分还是没有必要？？这里的IO线程指EventLoop（线程池）中的线程
------------------------------------------------------------------------------------------------------------------------
ChannelHandler接口
    处理I/O事件或拦截I/O操作，然后将其转发到其ChannelPipeline中的下一个处理程序。

ChannelPipeline接口
    元素为java.util.Map.Entry<String, ChannelHandler>的双端队列（类似LinkedList）
    继承了ChannelInboundInvoker, ChannelOutboundInvoker
public interface ChannelPipeline
        extends ChannelInboundInvoker, ChannelOutboundInvoker, Iterable<Entry<String, ChannelHandler>>

ChannelHandlerContext接口
    从类图可以看到ChannelInboundInvoker, ChannelOutboundInvoker依赖ChannelHandlerContext
public interface ChannelHandlerContext extends AttributeMap, ChannelInboundInvoker, ChannelOutboundInvoker
ChannelInboundInvoker--Channel入站绑定调用抽象  看UML主要用于事件注册（就看你在什么上注册Pipeline还是HandlerContext上注册）
    具体什么必要条件、什么时候触发留到启动流程分析
ChannelOutboundInvoker--出站抽象  方法基本返回异步接口，这里就可以看出（read\write\bind\connect等）操作是异步的。

ChannelPipeline与ChannelHandlerContext是互相依赖的，它们看起来结构很相似
ChannelHandlerContext用于管理Handler和pipeline。通过唯一抽象类实现就可以看到：
    AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor,
                                  String name, Class<? extends ChannelHandler> handlerClass) {
        this.name = ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
        this.executor = executor;
        this.executionMask = mask(handlerClass);
        // Its ordered if its driven by the EventLoop or the given Executor is an instanceof OrderedEventExecutor.
        ordered = executor == null || executor instanceof OrderedEventExecutor;
    }


------------------------------------------------------------------------------------------------------------------------
ChannelInitializer---以Channel接口为泛型上限，依赖Channel，同时继承ChannelInboundHandlerAdapter，是一个特殊的ChannelHandler。
它为注册到EventLoop的Channel初始化
@Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter

ChannelOutBoundInvoker的方法返回值基本都是ChannelFuture接口，它是一个异步接口。
查看AbstractChannelHandlerContext中的方法，事件操作都是newPromise() 这里可以看出异步，事件驱动。



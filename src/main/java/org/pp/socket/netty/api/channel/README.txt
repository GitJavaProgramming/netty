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

Unsafe接口时Channel内部的一个接口，它是一个辅助接口，用于实际的数据传输，并须从I/O线程操作，以下几个方法除外：
    * <ul>
    *   <li>{@link #localAddress()}</li>
    *   <li>{@link #remoteAddress()}</li>
    *   <li>{@link #closeForcibly()}</li>
    *   <li>{@link #register(EventLoop, ChannelPromise)}</li>
    *   <li>{@link #deregister(ChannelPromise)}</li>
    *   <li>{@link #voidPromise()}</li>
    * </ul>

FAQ. 这些例外方法是无法从I/O线程操作中拆分还是没有必要？？这里的IO线程指EventLoop（线程池）中的线程


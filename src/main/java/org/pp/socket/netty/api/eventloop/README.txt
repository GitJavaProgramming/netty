循环依赖关系~~~  查看组件关系图吧！
之所以要搞出这么多关系，是因为解耦，领域划分

Netty线程模型---几张图

    DefaultThreadFactory.png
    EventExecutorGroup继承体系.png
    EventLoop体系.png
    Executor体系.png
    NioEventLoopGroup继承体系.png
    SingleThreadEventLoop详细图.png

Bootstrap绑定时，会将Channel注册到eventloop
步骤如下：
1 final ChannelFuture regFuture = initAndRegister();
2 channel = channelFactory.newChannel(); // 绑定pipeline
3 ChannelFuture regFuture = config().group().register(channel); // 绑定eventloop，异步操作，initAndRegister返回

绑定pipeline
protected DefaultChannelPipeline(Channel channel) {
    this.channel = ObjectUtil.checkNotNull(channel, "channel");
    succeededFuture = new SucceededChannelFuture(channel, null);
    voidPromise =  new VoidChannelPromise(channel, true);
    tail = new TailContext(this); // auto complete
    head = new HeadContext(this); // auto complete
    head.next = tail;
    tail.prev = head;
}

绑定EventLoop，实现迭代器，next循环遍历绑定
SingleThreadEventLoop中的方法如下：
@Override
public ChannelFuture register(Channel channel) {
    return register(new DefaultChannelPromise(channel, this));
}
@Override
public ChannelFuture register(final ChannelPromise promise) {
    promise.channel().unsafe().register(this, promise);
    return promise;
}
// 过时方法
@Deprecated
@Override
public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
    channel.unsafe().register(this, promise);
    return promise;
}
register最后在AbstractNioChannel中将javaChannel注册到Selector上并注册一个接收事件，源码中说这里有一个JDK bug！！
register之后pipeline触发事件 pipeline.fireChannelRegistered(); registered事件触发交给HandlerContext的头节点去处理
final AbstractChannelHandlerContext head;

之后判断服务器是否自动读数据，如果没有注册读事件就注册相应事件
final int interestOps = selectionKey.interestOps(); // 获取此键的 interest 集合。
if ((interestOps & readInterestOp) == 0) {
    selectionKey.interestOps(interestOps | readInterestOp);
}





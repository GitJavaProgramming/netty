# Redisson锁
1. 使用方式
```
    RLock lock = redisson.getLock("foobar"); // 1.获得锁对象实例
    lock.lock(); // 2.获取分布式锁
    try {
        // ...
    } finally {
        lock.unlock(); // 3.释放锁
    }
```
2. 配置redisson并连接redis，获取锁  
```
1. 加载配置，连接redis，返回抽象RedissonClient
Config config = Config.fromYAML(new File("config-file.yaml")); // 从文件加载配置，各项含义查看文档
// Redisson代表集群管理器实例，一个集群生成一个统一的RedissonClient。
RedissonClient redisson = Redisson.create(config); // 默认setAddress("redis://127.0.0.1:6379");
2. 获取Redisson锁 每次get都new一个锁 RedissonLock实现了RedissonExpirable锁持有时间有限会过期的
// FAQ. 会不会在同一个redis集群上获得两把锁执行两次相同的命令，造成脏数据，getLock会不会有并发问题？？
public RLock getLock(String name) {
    return new RedissonLock(connectionManager.getCommandExecutor(), name);
}
// 根据配置的集群方式决定ConnectionManager，从构造方法和提供的调用方法可以看出ConnectionManager主要功能就是管理Config
connectionManager = ConfigSupport.createConnectionManager(configCopy);
// 命令执行器，实现异步执行接口，在加锁时判断命令是否执行完成
this.commandExecutor = new CommandSyncService(this);
```
3. 加锁  
```
调用栈如下：
// 默认锁 lock(-1, null, false); 不可中断
private void lock(long leaseTime, TimeUnit unit, boolean interruptibly) throws InterruptedException {
    // ...
    Long ttl = tryAcquire(leaseTime, unit, threadId);
    if (ttl == null) {
        return;
    }
    RFuture<RedissonLockEntry> future = subscribe(threadId); // 订阅锁释放的消息 threadId参数没用到？？
    // ...
}
private <T> RFuture<Long> tryAcquireAsync(long leaseTime, TimeUnit unit, long threadId) {
    if (leaseTime != -1) {
        return tryLockInnerAsync(leaseTime, unit, threadId, RedisCommands.EVAL_LONG);
    }
    // ...
}
// 从代码看到 默认使用的lua脚本作为执行器执行的命令，源码中经过加密和异步调用封装最后把这串命令发送到redis执行，等待异步通知
// 异步通知返回结果用到了netty中的异步接口ChannelFuture
// command.getName = EVAL
<T> RFuture<T> tryLockInnerAsync(long leaseTime, TimeUnit unit, long threadId, RedisStrictCommand<T> command) {
    internalLockLeaseTime = unit.toMillis(leaseTime);

    return commandExecutor.evalWriteAsync(getName(), LongCodec.INSTANCE, command,
              "if (redis.call('exists', KEYS[1]) == 0) then " +
                  "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                  "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              "return redis.call('pttl', KEYS[1]);",
                Collections.<Object>singletonList(getName()), internalLockLeaseTime, getLockName(threadId));
}
```
4. 释放锁

# 参考资料
* https://www.jianshu.com/p/a8b3473f9c24
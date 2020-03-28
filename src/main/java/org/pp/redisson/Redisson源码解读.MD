# redisson
    起源与目标
    Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid），是一个java操作redis的工具库。
    Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上。
    
    项目地址  https://github.com/redisson/redisson
    中文文档（很详细）  https://github.com/redisson/redisson/wiki/目录
    redis两小时入门 https://github.com/GitJavaProgramming/springboot_mybatis/blob/master/redis.md
    redis中文文档 http://www.redis.cn/documentation.html
    研究版本： 3.11.6 
## 配置
    1. 集群模式
        负载均衡与主从模式，主节点对命令的复制工作发生在返回命令回复之后，必须在性能和一致性之间做出权衡。
    2. 哨兵模式
        主节点和从节点读写分离。主节点宕机，竞选新主节点。竞选机制的实现依赖于在系统中启动一个sentinel进程。sentinel也可以
        集群，部署多个哨兵，sentinel可以通过发布与订阅来自动发现Redis集群上的其它sentinel。
    3. 主从模式    
        主节点和从节点读写分离。必须保证主节点不能宕机，主节点宕机时，从节点不会竞争产生新的主节点，Redis将会丧失写能力。
## 分布式对象与集合
    每个Redisson对象实例都会有一个与之对应的Redis数据实例，可以通过调用getName方法来取得Redis数据实例的名称（key）。
    
    查看RList UML，RList通过接口继承来实现功能扩展，数据结构是java.util.List，其他接口也是类似。
    RExpirable:提供数据过期机制,所有集合都实现这个接口，通过继承默认抽象子类RedissonExpirable扩展。
    RandomAccess:随机访问接口，用于有索引的结构提供随机访问提高性能。
    RListAsync:为RList添加异步操作功能如getAsync、setAsync、readAllAsync等。
    RSortable:List可排序，提供多种排序算法，必须指定SortOrder(ASC, DESC)
    
    分布式锁与队列同步器
    从RLock的UML图可以看到RLock接口扩展了java.util.concurrent.locks.Lock，提供很多种锁的实现
    Lock是java中锁的抽象，提供lock(), unlock()等方法来控制线程对资源的占用。
    关于同步器实现原理和工具类见另一个库  https://github.com/GitJavaProgramming/new-Java
    
    关于分布式锁具体使用要看redis集群使用哪种配置模式。
    
# 参考资料
* https://blog.csdn.net/qq_28410283/article/details/89197156
* https://juejin.im/post/5da287b55188256f4e70752b#heading-5
* http://www.imooc.com/article/284859


    
    


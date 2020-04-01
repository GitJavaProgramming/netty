# 分布式理论
    CAP
    Consistency             一致性
    Availability            可用性
    Partition Tolerance     分区容错性
    
    为什么分布式的分区容错性可以实现：网络具有共享、连通特性
    
    BASE
    Basically Available(基本可用)
        分布式系统在出现故障的时候，允许损失部分可用性，即保证核心可用。
    Soft state（软状态）
        允许系统中的数据存在中间状态,并认为该状态不影响系统的整体可用性,即允许系统在多个不同节点的数据副本存在数据延迟。
    Eventually consistent（最终一致性）
        尽可能保证在某个时间级别（比如秒级别）之后，可以让数据达到一致性状态。
# 分布式事务
    概念与方式
    访问由多个服务器管理的对象的平面事务或嵌套事务称为分布式事务（distributed transaction） 。
    FAQ. 平面事务，嵌套事务？？
    当一个分布式事务结束时，事务的原子特性要求所有参与该事务的服务器必须全部提交或全部放弃该事务。为了实现这一点，需要其中一个
    服务器承担协调者（coordinater）的角色来进行控制，协调者的工作方式取决于他选用的协议。
    
    原子提交协议
        两阶段提交
            1. 是否全部参与者准备好提交 2. 全部提交或放弃
        嵌入式事务的两阶段提交
            嵌套事务树  顶层事务--子事务1 子事务11 子事务12 子事务2 子事务21 子事务22 ...  
            整个事务是否提交由顶层事务决定，子事务临时提交（本地状态的改变，可以不用备份到持久化存储）。
            
            层次化两阶段提交
            顶层事务协调者按树的层次结构依次向子事务协调者发送canCommit消息。每个参与者首先收集其后代事务的应答，然后再应答
            自己的父事务，父事务决定子事务是否提交或放弃。
            
            平面化两阶段提交
                顶层事务协调者向临时提交列表中所有子事务的协调者发送canCommit消息，每个参与者都引用顶层事务TID来引用事务
                每个参与者都查找自己的事务列表，寻找匹配那个TID的事务或子事务。
                TID: 事务协调者标识符id
    分布式事务并发控制
        加锁与死锁（死锁检测：事务等待图中寻找环路）
        乐观锁：事务提交前验证
        
# zookeeper是什么
    为什么你会这么问？什么是什么典型的形式科学思维。。。
    
        ZooKeeper是维护配置信息、命名、提供分布式同步和组服务于一体的服务，这些服务都以某种形式被分布式应用程序使用。
    每次zookeeper被(分布式应用)发现时，都会进行大量工作来修复一些漏洞和竟态条件。zookeeper实现复杂，这使得它们在发生改变时
    变得很脆弱并且难于管理。即使正常运行了zookeeper，在部署应用时，zookeeper服务的不同实现也会导致管理的复杂性。
    
    配置信息：zookeeper服务器节点注册
    命名：节点命名和查找
    分布式同步：并发控制
    
    FAQ. 软件脆弱性？？
         
     ZooKeeper的目的是将这些不同服务的本质提炼成一个非常简单的界面，以实现集中式协调服务。服务本身是分布式的，并且高度可靠。
     共识，组管理和状态协议将由服务实现，因此应用程序不需要自己实现它们。
     
     FAQ. zookeeper(服务)的实现方式？？
     zookeeper使用Atomic Broadcast(ZAB, ZooKeeper原子消息广播协议)协议作为其数据一致性的核心算法。ZAB协议不像Paxos算法那样
     是一种通用的分布式一致性算法，它是一种特别为zookeeper设计的崩溃可恢复的原子消息广播算法。基于该协议，zookeeper实现了一种
     主备模式的系统架构来保持集群中副本之间数据的一致性。
# Paxos算法
    Paxos算法-wiki描述
    Paxos是一个共识（consensus）算法。首先将议员的角色分为 proposers，acceptors，和 learners（允许身兼数职）。
    proposers 提出提案，提案信息包括提案编号和提议的 value；acceptor 收到提案后可以接受（accept）提案，若提案获得
    多数派（majority）的 acceptors 的接受，则称该提案被批准（chosen）；learners 只能“学习”被批准的提案。
    
    为什么一定能达成共识（找到批准提案），这里摘取wiki中已被数学归纳证明一段话：
        P2c：如果一个编号为 n 的提案具有 value v，那么存在一个多数派，要么他们中所有人都没有接受（accept）编号小于 n 
        的任何提案，要么他们已经接受（accept）的所有编号小于 n 的提案中编号最大的那个提案具有 value v。
        
    FAQ. 每个提案需要有不同的编号，且编号间要存在全序关系，全序关系具有传递性。这个是必要前提吗，如果编号间不具有全序关系就不能
    确保达成共识？？
    
# ZAB协议
    ZAB协议运行过程中，所有的客户端更新都发往Leader，Leader写入本地日志后再复制到所有的Follower节点。
    一旦Leader节点故障无法工作，ZAB协议能够自动从Follower节点中重新选择出一个合适的替代者，这个过程被称为选主，选主也是
    ZAB协议中最为重要和复杂的过程。
# zookeeper的java客户端
    ZooKeeper字段和属性
        字段：类数据成员，一个数据项，一般私有，java反射破坏了私有性，字段的概念来源于关系型数据库 表--记录--字段
            protected final ClientCnxn cnxn;  // socket连接，多次封装后的client
            protected final HostProvider hostProvider;
            protected final ZKWatchManager watchManager; // zookeeper连接状态或节点事件监控程序管理
        属性：发布的字段，具有数据操作权限，类可以根据本身数据的抽象情况决定是否将访问属性抽象为成员变量（字段）
            public ZKClientConfig getClientConfig() // zookeeper字段（数据项，需要设置值，查看构造方法），只读属性
            public List<String> getEphemerals() // 同步获取此会话创建的所有临时节点。   
            由字段ClientCnxn提供的只读属性
                public ZooKeeperSaslClient getSaslClient()
                public States getState()
                public int getSessionTimeout()
                public long getSessionId()
                public byte[] getSessionPasswd()
    ZooKeeper构造方法 有多个重载构造，其中一个如下
        public ZooKeeper(
                String connectString,
                int sessionTimeout,
                Watcher watcher,
                boolean canBeReadOnly,
                HostProvider aHostProvider,
                ZKClientConfig clientConfig)
        各参数具体含义参考从Paxos到Zookeeper 表5-2
# zookeeper服务端 
    源码环境搭建略
    
    先假定你对zookeeper一无所知，从main方法开始，来看java怎么实现一个zookeeper服务。
    
    服务端主线程入口：QuorumPeerMain
        1. 解析命令行参数
            读取并解析进程配置文件，zookeeper不像spring对资源的加载和解析并没有做过多的封装，使用QuorumPeerConfig
        2. 启动清理任务线程
            由于配置文件会指定一些数据文件目录，在启动进程时需要尽心清理，确保进程启动唯一占用这些文件不至产生脏数据
        3. 判断zookeeper的运行模式：单机、分布式
```            
            if (args.length == 1 && config.isDistributed()) {
               runFromConfig(config);
            } else {
               // there is only server in the quorum -- run as standalone
               ZooKeeperServerMain.main(args); // 静态方法调用
            }
```
        
# 应用场景与如何实现       
      
# 参考资料
* 分布式系统：概念与设计 原书第5版
* 从Paxos到Zookeeper--分布式一致性原理与实践
* https://zookeeper.apache.org/
* https://cwiki.apache.org/confluence/display/ZOOKEEPER/Index
* 系统编程  分布式应用的设计与开发
* ZooKeeper分布式过程协同技术详解
* https://zh.wikipedia.org/zh-hans/Paxos算法
* https://zhuanlan.zhihu.com/p/27335748




































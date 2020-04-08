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
    为什么你会这么问？什么是什么典型的形式科学思维，这是一个关于存在的终极问题，目前仍然没有答案。。。
    
        ZooKeeper是维护配置信息、命名、提供分布式同步和组服务于一体的服务，这些服务都以某种形式被分布式应用程序使用。
    每次zookeeper被发现时，都会进行大量工作来修复一些漏洞和竟态条件。zookeeper实现复杂，这使得它们在发生改变时
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
        属性：发布的字段，具有（类内/类外）数据操作权限，类可以根据本身数据的抽象情况决定是否将访问属性抽象为成员变量（字段）
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
## 源码环境搭建
    这里直接使用maven自动下载的源码这里使用版本3.6.0，需要在本机建立数据目录并且需要添加相关依赖
    依赖见pom.xml
    配置文件见./src_run_local_config/**  
    注意集群运行需要设置idea RUN/DEBUG为允许并行运行，启动参数需要带上配置文件路径，如D:\tmp\zoo3.cfg
    注意日志等级控制，在resources文件夹下log4j.properties，方便调试
    特别注意：本例由于历史原理之前使用netty4.1.43研究过netty源码 zookeeper V3.6.0依赖netty4.1.45 不过maven管理没出问题！
    
    先假定你对zookeeper一无所知，从main方法开始，来看java怎么实现一个zookeeper服务。
    
## 服务端主线程入口：QuorumPeerMain
        1. 解析命令行参数
            读取并解析进程配置文件，zookeeper不像spring对资源的加载和解析并没有做过多的封装，使用QuorumPeerConfig
        2. 启动清理任务线程
            由于配置文件会指定一些数据文件目录，在启动进程时需要尽心清理，确保文件中只生成占用它的进程的数据。
        3. 判断zookeeper的运行模式：单机、分布式
```            
            if (args.length == 1 && config.isDistributed()) {
               runFromConfig(config);
            } else {
               // there is only server in the quorum -- run as standalone
               ZooKeeperServerMain.main(args); // 静态方法调用
            }
```
### 1. 确定一个leader
        ZooKeeper分布式模式能保证了CAP理论中的CP，即一致性。通过leader对整个分布式系统(socket网)进行协调，最终使整个
        集群中的数据一致，由于分布式系统的复杂性，为便于维护管理，zookeeper集群（如启动时）确定leader前不对外提供服务。
        
        FAQ. 如果所有客户端连接的都是非leader服务器，可能吗？？此时如何进行一致性协调？？
        
        1. 第一步
            解析配置文件、创建两个NIOServerCnxnFactory并将它们作为属性注入QuorumPeer
                quorumPeer.setCnxnFactory(cnxnFactory);
                quorumPeer.setSecureCnxnFactory(secureCnxnFactory);
            QuorumPeer：仲裁协议管理类，当前服务器会有三种状态：
                领导者选举，最初服务器都建议自己担任领导者
                Follower服务器与领导者同步并复制任何交易
                领导者服务器处理请求并转发给Follower（请求都有leader接收处理吗？？）
                
                仲裁是一个法律术语，是指由双方当事人协议将争议提交（具有公认地位的）第三者，由该第三者对争议的是非曲直进行
                评判并作出裁决的一种解决争议的方法。
            quorumPeer.start(); 开始仲裁 start()方法如下：这里先只关注Leader election
            @Override
            public synchronized void start() {
                // 选举前准备工作：数据（）、socket连接... 
                startLeaderElection();
                // ...
                super.start();  // 真正线程启动Thread.start()->执行run() 线程启动 NEW->Runnable  
            }

            // startLeaderElection(Args) -> createElectionAlgorithm(Args)
            // 最终在QuorumPeerConfig类中找到默认的electionAlgorithm => protected int electionAlg = 3;
            @SuppressWarnings("deprecation")
            protected Election createElectionAlgorithm(int electionAlgorithm) {
                // ...
                case 3:
                    QuorumCnxManager qcm = createCnxnManager();
                    // ...
                    QuorumCnxManager.Listener listener = qcm.listener;
                    if (listener != null) {
                        listener.start();
                        FastLeaderElection fle = new FastLeaderElection(this, qcm);
                        fle.start();
                        // ...
                    }
                // ...
            }
            
        集群配置
            server.X=A:B:C 其中X是一个数字, 表示这是第几号server. A是该server所在的IP地址. 
            B配置该server和集群中的leader交换消息所使用的端口. C配置选举leader时所使用的端口.
            
        2. 数据封装、通信封装(作为公共模块？？)
            
            数据封装：ToSend 只有数据，没有行为方法，其中一些数据由Vote提供，服务器自身Vote作为QuorumPeer的属性被管理 参考UML
            
            FastLeaderElection实例化，初始化数据成员，从构造方法看出它聚合了QuorumPeer, QuorumCnxManager同时结合自身数据
            提供更多的功能。其中比较重要的成员：
                sendqueue = new LinkedBlockingQueue<ToSend>();
                recvqueue = new LinkedBlockingQueue<Notification>();
                this.messenger = new Messenger(manager);
            FastLeaderElection start() 实际调用内部类Messenger start()
                void start() {
                    this.wsThread.start();
                    this.wrThread.start();
                }
            Messenger会开启两个后台线程执行两个任务WorkerSender、WorkerReceiver，这两个任务组合QuorumCnxManager因为需要
            QuorumCnxManager进行数据收发统一管理，从UML Messenger中可以看到。
            1. WorkerSender从sendqueue.poll出一个ToSend并将ToSend转换成NIO ByteBuffer然后交给QuorumCnxManager发送；
               WorkerReceiver从QuorumCnxManager.recvQueue poll出Message;向FastLeaderElection.recvqueue中放Notification
                   if (self.getPeerState() == QuorumPeer.ServerState.LOOKING) {
                        recvqueue.offer(n);
                   }
            2. QuorumCnxManager.toSend()收到ByteBuffer会进行逻辑处理，并进行socket连接，阻塞等待连接建立，最后在
               startConnection(Socket sock, Long sid)方法处进行数据发送.
               数据收发用到流：DataOutputStream、DataInputStream、BufferedOutputStream
               
               QuorumCnxManager主要功能：维护映射，用于管理集群中每个服务器已经建立的连接和发送的的数据buffer队列。
                    final ConcurrentHashMap<Long, SendWorker> senderWorkerMap;
                    final ConcurrentHashMap<Long, BlockingQueue<ByteBuffer>> queueSendMap; // 发送给每个服务器的缓冲队列
                    public final BlockingQueue<Message> recvQueue;  // 本地接收队列
            
            注意：此时ToSend是还没有构造的，ToSend的构造会在QuorumPeer线程正式运行run()方法执行时调用，此时线程QuorumPeer
            尚未真正启动
         
        3. 选举leader
            在线程启动前数据和通信的封装完成之后，线程start启动会执行run() 进行真正的leader选举逻辑: QuorumPeer.run()
            
            线程逻辑首先会初始化一些MBean对java对象进行内存管理，这部分内容参考JMX规范，在spring、tomcat中都由运用。
            
            主循环
            QuorumPeer中默认的state private ServerState state = ServerState.LOOKING;  // 对象构造时字段初始值
            case LOOKING、OBSERVING、FOLLOWING、LEADING:
            
            LOOKING
                Read Only Mode Server : (Java system property: readonlymode.enabled) New in 3.4.0
                设置当前选票
                setCurrentVote(makeLEStrategy().lookForLeader()); // 这里可能是想使用策略模式对应选举算法不同实现
                接口Election当前只有FastLeaderElection一个子类
         
         总结
            从宏观上看，zookeeper作为一个java应用，它的通信模型、业务逻辑并没有很好的解耦。不过从代码现状来看，作者应该是想
            要做一个完全解耦的通信模型即底层通信、业务通信可以分别对应不同模型，然而由于复杂性并没有很好的实现。
            从降低复杂性的角度考虑，业务模型应该是依赖通信模型的，业务作为通信的扩展功能提供接口，这样就可以很好的抽象了
            代码见：./server/my/**
            
        到此要实现zookeeper还剩下三个问题：数据与存储、socket连接建立、leader选举逻辑
        选举逻辑主要判断选票Vote的各个成员状态 参考UML
            version             通知版本，更新只出现在leader服务器中，设置Notification.CURRENTVERSION = 0x2
            id                  被推举的leader的SID值
            zxid                被推举的leader的事务ID
            electionEpoch       逻辑时钟，每一轮选举更新提案都会自增1
            peerEpoch           被选举的leader的时期
            state               当前服务器的状态
        领导者状态变更只在两处：
        1. QuorumPeer.updateServerState() 
            updateServerState() 有三处调用都在QuorumPeer.run()的主循环中 FOLLOWING, LEADING, OBSERVING
        2. FastLeeaderElection.Messenger.WorkerReveiver.run() {
            int rstate = response.buffer.getInt();
            // ...
            case 2:
                ackstate = QuorumPeer.ServerState.LEADING;  // 选出leader
                break;
            // ...
        }
        QuorumPeer.run()的主循环中leader选出之后更新服务器状态ServerState--产生三种角色：FOLLOWING, LEADING, OBSERVING
            observer.observeLeader();
            follower.followLeader();
            leader.lead();
        
        Leader服务器是整个集群工作机制中的核心，其主要工作有以下两个：
            1. 事务请求的唯一调度和处理者，保证集群事务处理的顺序性（并行->串行）
            2. 集群内部各服务器的调度者
            事务请求包含一个全局唯一的事务ID，所有的请求交给leader调度--ZAB
        lead() 做一些初始化工作，然后 startZkServer(); // line 684
        eg. -> new LearnerCnxAcceptor().run() -> 等待LearnerCnxAcceptorHandler.run()执行acceptConnections()
            -> new LearnerHandler(socket, is, Leader.this).start 以Leader作为learnerMaster
        最后在顶层父类ZooKeeperServer处调用startup()做了很多工作如：setupRequestProcessors()在子类LeaderZooKeeperServer
        重写进行动态调用。
        
### 2. 集群中系统间通信
### 3. 客户端与集群的通信
        服务端对于会话创建的处理，大体可以分为请求接收、会话创建、预处理、事务处理、事务应用和会话响应6大环节。
        客户端与服务端的所有通信都由NIOServerCnxn负责--负责统一接收客户端的所有请求，并将请求内容从底层网络I/O读取出来。
        服务端同时实现了监视点管理器管理当前已被注册的监视点列表，并负责触发它们。
        
        总体模型：
            系统间通信：leader选举通信、事务请求调度
            客户端服务器：连接监视与请求处理
        
        再看分布式系统：
            透明性--提供facade，屏蔽内部复杂性（访问透明、位置、复制、并发、迁移、故障、规模扩展、性能、分布和实现）
            名称服务、域名系统--分布式系统名称的有效性
            时间服务--物理时钟和逻辑时钟的同步
            选举算法--崩溃可恢复的主从模式，领导者协调分布式
            中间件：进程间的通信，系统间的透明性
            分布式计算机系统的确定性和不确定性
        
              
    zookeeper的配置文件参数配置：
        https://zookeeper.apache.org/doc/r3.6.0/zookeeperAdmin.html#sc_configuration
               
# zookeeper分布式锁
    见代码
    
      
# 参考资料
* 分布式系统：概念与设计 原书第5版
* 从Paxos到Zookeeper--分布式一致性原理与实践
* https://zookeeper.apache.org/
* https://cwiki.apache.org/confluence/display/ZOOKEEPER/Index
* 系统编程  分布式应用的设计与开发
* ZooKeeper分布式过程协同技术详解
* https://zh.wikipedia.org/zh-hans/Paxos算法
* https://zhuanlan.zhihu.com/p/27335748
* https://stephenzhou.net/2020/01/13/zookeeper-src-build/
* http://www.imooc.com/article/284956
* https://www.cnblogs.com/leeSmall/p/9614601.html




































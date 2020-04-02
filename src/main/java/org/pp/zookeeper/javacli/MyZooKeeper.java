package org.pp.zookeeper.javacli;

import org.apache.zookeeper.Watcher;

public class MyZooKeeper {
    private final String connectConfig;
    private final MyClientCnxn cnxn; // async client and event wrapper

    private final Watcher watcher; // use zookeeper watcher interface

    public MyZooKeeper(String connectConfig, Watcher watcher) {
        this.connectConfig = connectConfig;
        this.watcher = watcher;

        this.cnxn = createCli();
        cnxn.start();
    }

    private MyClientCnxn createCli() {
        return new MyClientCnxn(connectConfig, watcher);
    }
}

package com.qq.servers;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-12
 * Time: 下午1:12
 */
public class WordSegServer {
    private final int port;

    public WordSegServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 8083;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new WordSegServer(port).run();
    }

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new HttpWordSegPipelineFactory());

        bootstrap.bind(new InetSocketAddress(port));

    }

}

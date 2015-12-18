package com.lwk.avro.hello;

import com.lwk.avro.hello.impl.HelloWorldImpl;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;

import java.net.InetSocketAddress;

/**
 * 远程调用测试
 * Created by dell-pc on 2015/12/17.
 */
public class TestHello {

    public static void main(String[] args) throws Exception{
        //启动服务
        Server server=new NettyServer(
                new SpecificResponder(HelloWorld.class,new HelloWorldImpl()),
                new InetSocketAddress(8080));
        server.start();
        NettyTransceiver client=new NettyTransceiver(new InetSocketAddress(8080));
        Greeting greeting=new Greeting();
        greeting.setMessage("妹妹:哈喽哥哥你在吗? ");

        HelloWorld helloWorld= (HelloWorld)SpecificRequestor.getClient(HelloWorld.class,client);
        Greeting greetingNew=helloWorld.hello(greeting);

        System.out.println(greetingNew.getMessage());
        server.close();
        client.close();

    }
}

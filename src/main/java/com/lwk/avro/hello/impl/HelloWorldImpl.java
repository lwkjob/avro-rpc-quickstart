package com.lwk.avro.hello.impl;

import com.lwk.avro.hello.Curse;
import com.lwk.avro.hello.Greeting;
import com.lwk.avro.hello.HelloWorld;
import org.apache.avro.AvroRemoteException;

/**
 * Created by dell-pc on 2015/12/17.
 */
public class HelloWorldImpl implements HelloWorld {

    @Override
    public Greeting hello(Greeting greeting) throws AvroRemoteException, Curse {
               greeting.setMessage(greeting.getMessage()+" 哥哥:妹妹,你猜我在不在");
        return greeting;
    }
}

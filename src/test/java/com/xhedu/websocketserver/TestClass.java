package com.xhedu.websocketserver;

import com.xhedu.entity.SocketEntity;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class TestClass {
    @Test
    public void test1(){
        SocketEntity entity1 = new SocketEntity("chy");
        SocketEntity entity2 = new SocketEntity("chy");
        System.out.println(entity1==entity2);
        ConcurrentHashMap<SocketEntity,String> map = new ConcurrentHashMap<>();
        map.put(entity1,"a");
        map.put(entity2,"b");
        System.out.println(map);
    }
}

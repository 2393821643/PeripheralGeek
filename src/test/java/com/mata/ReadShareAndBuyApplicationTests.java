package com.mata;

import com.mata.utils.RedisCommonKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class ReadShareAndBuyApplicationTests {
    @Autowired
    @Qualifier("userBloom")
    private RBloomFilter<Integer> userBloom;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
        String s = stringRedisTemplate.opsForValue().get("test");
        System.out.println(s);
    }

    @Test
    void testWriteLock(){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("RWL");
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock(15, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set("test","test3");
        }finally {
             rLock.unlock();
        }
    }

    @Test
    void testReadLock(){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("RWL");
        RLock rLock = readWriteLock.readLock();
        try {
            rLock.lock(15, TimeUnit.SECONDS);
            String s = stringRedisTemplate.opsForValue().get("test");
            System.out.println(s);
        }finally {
             // rLock.unlock();
        }
    }

    @Test
    void testWriteLock1(){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("RWL");
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock(15, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set("test","test1");
            System.out.println("test1");
        }finally {
            //rLock.unlock();
        }
    }

    @Test
    void testWriteLock2(){
        Long goodId = Long.valueOf("12345");
        System.out.println(goodId);
    }

    @Test
    void testLock() throws InterruptedException {
        boolean add = userBloom.add(10001);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    void suggestTest(){
        Message message = MessageBuilder
                .withBody("test".getBytes(StandardCharsets.UTF_8)) //设置消息内容
                .setHeader("x-delay", 1000) // 设置消息延迟时间 5分钟
                .build();
        rabbitTemplate.convertAndSend("UpdateCountExchange", "updateCountKey", message);
    }

    @Test
    public void testL(){
        RLock updateLock = redissonClient.getLock(RedisCommonKey.GOODS_COUNT_UPDATE_PRE_KEY+123);
        try {
            boolean isLock = updateLock.tryLock(0, RedisCommonKey.GOODS_COUNT_UPDATE_TIME, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

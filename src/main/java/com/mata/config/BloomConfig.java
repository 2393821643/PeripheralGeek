package com.mata.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomConfig {
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 订单id的bloom过滤器
     */
    @Bean("orderBloom")
    public RBloomFilter<Long> orderBloom(){
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter("orderIdBloom");
        bloomFilter.tryInit(1000000,0.03);
        return bloomFilter;
    }

    /**
     * 评论点赞的布隆过滤器
     */
    @Bean("commentGoodCountBloom")
    public RBloomFilter<String> commentGoodCountBloom(){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("commentGoodCountBloom");
        bloomFilter.tryInit(100000000,0.03);
        return bloomFilter;
    }

}

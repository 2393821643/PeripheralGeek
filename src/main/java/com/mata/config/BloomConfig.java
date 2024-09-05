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
     * 用户id的bloom过滤器
     */
    @Bean("userBloom")
    public RBloomFilter<Integer> userBloom(){
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter("userIdBloom");
        bloomFilter.tryInit(1000000,0.03);
        return bloomFilter;
    }

    /**
     * 商品id的bloom过滤器
     */
    @Bean("goodsBloom")
    public RBloomFilter<Long> goodsBloom(){
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter("goodsIdBloom");
        bloomFilter.tryInit(1000000,0.03);
        return bloomFilter;
    }

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
     * 文章的bloom过滤器
     */
    @Bean("articleBloom")
    public RBloomFilter<Long> articleBloom(){
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter("articleIdBloom");
        bloomFilter.tryInit(1000000,0.03);
        return bloomFilter;
    }
}

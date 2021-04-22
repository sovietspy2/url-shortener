package com.example.urlshortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${redisHost}")
    private String redisHost;

    @Value("${redisPort}")
    private Integer redisPort;

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
    }
}

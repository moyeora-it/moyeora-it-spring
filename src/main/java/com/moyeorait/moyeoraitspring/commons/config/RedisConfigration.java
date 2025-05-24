package com.moyeorait.moyeoraitspring.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfigration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // Redis를 연결합니다.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // Hash Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        // 기본적으로 직렬화를 수행합니다.
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }


    public ListOperations<String, Object> getListOperations() {
        return this.redisTemplate().opsForList();
    }

    public ValueOperations<String, Object> getValueOperations() {
        return this.redisTemplate().opsForValue();
    }


    public int executeOperation(Runnable operation) {
        try {
            operation.run();
            return 1;
        } catch (Exception e) {
            System.out.println("Redis 작업 오류 발생 :: " + e.getMessage());
            return 0;
        }
    }
}

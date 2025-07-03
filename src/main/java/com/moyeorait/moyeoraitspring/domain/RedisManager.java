package com.moyeorait.moyeoraitspring.domain;

import com.moyeorait.moyeoraitspring.commons.RedisHandler;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisManager {
    private final RedisHandler redisHandler;

    public void saveValue(String key, Object value) {
        redisHandler.executeOperation(() ->
                redisHandler.getValueOperations().set(key, value)
        );
    }

    public Object getValue(String key) {
        return redisHandler.getValueOperations().get(key);
    }

    public void deleteKey(String key) {
        redisHandler.executeOperation(() ->
                redisHandler.getRedisTemplate().delete(key)
        );
    }

    public void pushToList(String key, Object value) {
        redisHandler.executeOperation(() ->
                redisHandler.getListOperations().rightPush(key, value)
        );
    }

    public Object popFromList(String key) {
        return redisHandler.getListOperations().leftPop(key);
    }

    public void saveValueWithTTL(String key, UserInfo value, Duration ttl) {
        redisHandler.executeOperation(() ->
                redisHandler.getValueOperations().set(key, value, ttl)
        );
    }
}

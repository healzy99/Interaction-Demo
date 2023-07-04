package com.example.demo.service.impl;

import com.example.demo.service.ICache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: heal
 * @Date: 2023/7/4 10:21
 */
@Service
public class CacheServiceImpl implements ICache {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean rightPushSessionId(String ocId, String sessionId) {
        Long push = redisTemplate.opsForList().rightPush(ocId + ":Session", sessionId);
        return push != null && push > 0;
    }

    @Override
    public boolean rightPushAccountId(String ocId, String accountId) {
        Long push = redisTemplate.opsForList().rightPush(ocId + ":Account", accountId);
        return push != null && push > 0;
    }
}

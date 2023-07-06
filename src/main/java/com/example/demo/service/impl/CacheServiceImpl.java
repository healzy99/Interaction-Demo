package com.example.demo.service.impl;

import com.example.demo.service.ICache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

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
        Long push = redisTemplate.opsForList().rightPush("Session:" + ocId, sessionId);
        return push != null && push > 0;
    }

    @Override
    public String leftPopSessionId(String ocId) {
        return redisTemplate.opsForList().leftPop("Session:" + ocId);
    }

    @Override
    public boolean addInvalidSession(String sessionId) {
        Long session = redisTemplate.opsForSet().add("InvalidSession", sessionId);
        return session != null && session > 0;
    }

    @Override
    public void removeInvalidSession(String sessionId) {
        redisTemplate.opsForSet().remove("InvalidSession", sessionId);
    }

    @Override
    public boolean isMemberInvalidSession(String sessionId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("InvalidSession", sessionId));
    }

    @Override
    public void putIfAbsentUnusedAccount(String ocId, Long accountId) {
        redisTemplate.opsForHash().put("Unused:" + ocId, accountId.toString(), 1);
    }

    @Override
    public Long getUnusedAccount(String ocId) {
        ocId = "Unused:" + ocId;
        // 获取运营中心的空闲坐席个数
        Long size = redisTemplate.opsForHash().size( ocId);
        if (size == 0) return 0L;
        // 获取所有的空闲坐席
        Set<Object> keys = redisTemplate.opsForHash().keys(ocId);
        // 获取第一个坐席ID
        Optional<Object> first = keys.stream().findFirst();
        if (!first.isPresent()) return 0L;
        // 存在则删除该坐席并返回
        Long accountId = Long.parseLong(String.valueOf(first.get()));
        return deleteUnusedAccount(ocId, accountId) ? accountId : 0L;
    }

    @Override
    public boolean deleteUnusedAccount(String ocId, Long accountId) {
        Long delete = redisTemplate.opsForHash().delete("Unused:" + ocId, accountId.toString());
        return delete > 0;
    }

    @Override
    public boolean rightPushAccountId(String ocId, Long accountId) {
        Long push = redisTemplate.opsForList().rightPush("Account:" + ocId, accountId.toString());
        return push != null && push > 0;
    }
}

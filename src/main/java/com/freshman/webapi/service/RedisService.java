package com.freshman.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis服务类
 */
@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置value
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setValue(Object key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public Object getValue(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key
     * @param key
     */
    public void deleteKey(Object key) {
        redisTemplate.delete(key);
    }
}

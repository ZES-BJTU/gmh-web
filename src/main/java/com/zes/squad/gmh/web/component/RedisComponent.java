package com.zes.squad.gmh.web.component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr.Unit;
import com.zes.squad.gmh.common.util.JsonUtils;

@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T extends Serializable> T get(String key, Class<T> clazz) {
        String jsonData = (String) redisTemplate.opsForValue().get(key);
        T obj = JsonUtils.parseJson(jsonData, clazz);
        return obj;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long expiredTime) {
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
    }

}

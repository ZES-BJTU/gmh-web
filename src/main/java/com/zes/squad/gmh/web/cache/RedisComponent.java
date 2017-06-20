package com.zes.squad.gmh.web.cache;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zes.squad.gmh.common.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 判断缓存是否可用
     * 
     * @return
     */
    public boolean isValid() {
        boolean valid = false;
        try {
            redisTemplate.opsForValue().get("test");
            valid = true;
        } catch (Exception e) {
            log.error("当前缓存不可用", e);
        }
        return valid;
    }

    /**
     * 存储单个值
     * 
     * @param key
     * @param value
     */
    public void put(String key, Serializable value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存储列表值
     * 
     * @param key
     * @param values
     */
    public <K extends Serializable> void putList(String key, List<K> values) {
        String jsonData = JsonUtils.toJson(values);
        redisTemplate.opsForValue().set(key, jsonData);
    }

    /**
     * 存储单个值, 带过期
     * 
     * @param key
     * @param value
     */
    public void put(String key, Serializable value, long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 存储列表值, 带过期
     * 
     * @param key
     * @param values
     */
    public <V extends Serializable> void putList(String key, List<V> values, long expireSeconds) {
        String jsonData = JsonUtils.toJson(values);
        redisTemplate.opsForValue().set(key, jsonData, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 获取单个对象
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取单个对象(泛型)
     * 
     * @param key
     * @param classOfValue
     * @return
     */
    public <V extends Serializable> V get(String key, Class<V> classOfValue) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        V v = classOfValue.cast(value);
        return v;
    }

    /**
     * 获取列表对象(泛型)
     * 
     * @param key
     * @param classOfValue
     * @return
     */
    public <V extends Serializable> List<V> getList(String key, Class<V> classOfValue) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        String jsonData = value.toString();
        List<V> values = Lists.newArrayList();
        JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
        for (JsonElement element : array) {
            values.add(new Gson().fromJson(element, classOfValue));
        }
        return values;
    }

    /**
     * 删除redis存储对象
     * 
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除
     * 
     * @param keys
     */
    public void batchDelete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 设置过期
     * 
     * @param key
     * @param expireSeconds
     */
    public void expire(String key, int expireSeconds) {
        redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
    }

}

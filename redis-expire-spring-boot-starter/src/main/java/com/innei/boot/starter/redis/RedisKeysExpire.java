package com.innei.boot.starter.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LIUMI969 on 2017/5/18.
 */
public class RedisKeysExpire {

    private Map<String,Long> keysExpireMap = new HashMap<>();

    public void addKeyExpire(String name,Long expire){
        keysExpireMap.put(name,expire);
    }

    public Map<String,Long> getKeysExpireMap(){
        return keysExpireMap;
    }
}

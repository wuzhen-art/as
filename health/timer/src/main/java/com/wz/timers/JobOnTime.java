package com.wz.timers;

import com.wz.constants.RedisConstant;
import com.wz.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class JobOnTime {


    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){

//        计算db和上传的图片差值
        Set<String> files = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

//        遍历files
        for (String file : files) {
//            删除七牛云图片
            QiniuUtils.deleteFileFromQiniu(file);
//            删除redit图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,file);

        }

    }
}

package com.ccdown.blockdemo.dao;

import org.apache.logging.log4j.util.Strings;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
@Component
@Slf4j
public class LevelDbDao {

    private DB levelDb;

    @PostConstruct
    public void init() {
        try {

            DBFactory levelDbFactory = new Iq80DBFactory();
            Options options = new Options();
            options.createIfMissing(true);
            //LevelDB存储目录
            String path = ".//leveldb";
            levelDb = levelDbFactory.open(new File(path), options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //插入数据
    public void put(String key, String value) {
        if (Strings.isBlank(key)) {
            return;
        }
        levelDb.put(Iq80DBFactory.bytes(key), Iq80DBFactory.bytes(value));
    }

    //获取数据
    public String get(String key) {
        if (Strings.isBlank(key)) {
            return null;
        }

        byte[] valueBytes = levelDb.get(Iq80DBFactory.bytes(key));

        return Iq80DBFactory.asString(valueBytes);

    }

    //删除数据
    public void delete(String key) {
        if (Strings.isBlank(key)) {
            return;
        }

        levelDb.delete(Iq80DBFactory.bytes(key));
    }

    //遍历所有数据
    public Map<String, String> traverseAllDatas() {
        DBIterator iterator = levelDb.iterator();
        HashMap<String,String> result = new HashMap<>();

        while (iterator.hasNext()){
            Map.Entry<byte[],byte[]> next = iterator.next();
            String key = Iq80DBFactory.asString(next.getKey());
            String value = Iq80DBFactory.asString(next.getValue());
            result.put(key,value);
        }

        return result;
    }

}

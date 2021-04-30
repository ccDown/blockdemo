package com.ccdown.blockdemo.dao;

import org.apache.logging.log4j.util.Strings;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
@Slf4j
//@Component
public class RocksdbDao {

    @Resource
    RocksDB rocksDB;

    private static final String CHARSET = "utf-8";

    public void put(String key,String value) throws UnsupportedEncodingException, RocksDBException {
        if (Strings.isBlank(key)){
            return;
        }

        rocksDB.put(key.getBytes(CHARSET),value.getBytes(CHARSET));
    }

    public String get(String key) throws UnsupportedEncodingException, RocksDBException {
        if (Strings.isBlank(key)){
            return null;
        }

        byte[] bytes = rocksDB.get(key.getBytes(CHARSET));
        return new String(bytes);
    }

    public void delete(String key) throws UnsupportedEncodingException, RocksDBException {
        if (Strings.isBlank(key)){
            return;
        }

        rocksDB.delete(rocksDB.get(key.getBytes(CHARSET)));
    }


}

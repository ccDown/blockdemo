package com.ccdown.blockdemo.pojo.po;

import com.ccdown.blockdemo.utils.crypto.sm2.Sm2CryptoUtils;

import lombok.Data;

/**
 * @author kuan
 * Created on 21/5/10.
 * @description
 */

@Data
public class Block {
    private BlockHeader blockHeader;
    private BlockBody blockBody;
    private String blockHash;

    /**
     * @Date 13:54 21/5/10
     * @Description 计算所有内容的hash
     * @Param []
     * @return java.lang.String
     **/
    public String getBlockHash(){
        String content = blockHeader.toString() + blockBody.toString();
        return Sm2CryptoUtils.hash(content.getBytes());
    }
}

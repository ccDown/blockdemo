package com.ccdown.blockdemo.pojo.po;

import java.util.List;

import lombok.Data;

/**
 * @author kuan
 * Created on 21/5/10.
 * @description
 */
@Data
public class BlockHeader {
    //版本号
    private int version;
    //上一区块hash
    private String previousBlockHash;
    //Nerkle树根节点hash
    private String merkleRootHash;
    // 生成该区块的公钥
    private String publicKey;
    //区块的序号
    private int number;
    //时间戳
    private long timeStamp;
    //32位随机数
    private long nonce;
    // 该区块里每条加以信息的hash集合，按顺序来，通过该hash集合能算出根节点hash
    private List<String> hashList;

}

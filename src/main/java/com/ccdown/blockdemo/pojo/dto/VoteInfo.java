package com.ccdown.blockdemo.pojo.dto;

import java.util.List;

import lombok.Data;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */

@Data
public class VoteInfo {
    // 投票状态码
    private int code;
    // 待写入区块内容
    private List<String> list;
    //待写入区块的内容的 Merkle 树根节点哈希值
    private String hash;




}

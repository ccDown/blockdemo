package com.ccdown.blockdemo.pojo.po;

import lombok.Data;

/**
 * @author kuan
 * Created on 21/5/10.
 * @description
 */
@Data
public class ContentInfo {
    //新的json内容
    private String jsonContent;
    //时间戳
    private Long timeStamp;
    //公钥
    private String publicKey;
    //签名
    private String sign;
    //该操作的hash
    private String hash;
}

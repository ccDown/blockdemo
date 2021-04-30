package com.ccdown.blockdemo.pojo.dto;

import com.alibaba.fastjson.JSON;
import com.ccdown.blockdemo.enums.MessageEnum;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
public class PingMessage extends BaseMessage {
    public PingMessage(){
        this.messageType = MessageEnum.PING.getCode();
    }

    public PingMessage(String message){
        this.messageType = MessageEnum.PING.getCode();
        this.message = JSON.toJSONBytes(message);
    }
}

package com.ccdown.blockdemo.pojo.dto;

import com.alibaba.fastjson.JSON;
import com.ccdown.blockdemo.enums.MessageEnum;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
public class PongMessage extends BaseMessage {
    public PongMessage(){
        this.messageType = MessageEnum.PONG.getCode();
    }

    public PongMessage(String message){
        this.messageType = MessageEnum.PONG.getCode();
        this.message = JSON.toJSONBytes(message);
    }
}

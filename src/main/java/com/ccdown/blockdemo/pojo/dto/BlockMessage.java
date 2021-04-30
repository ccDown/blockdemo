package com.ccdown.blockdemo.pojo.dto;


import com.ccdown.blockdemo.enums.MessageEnum;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
public class BlockMessage extends BaseMessage {

    public BlockMessage(){
        this.messageType = MessageEnum.BLOCK.getCode();
    }

}

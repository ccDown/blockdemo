package com.ccdown.blockdemo.enums;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
public enum MessageEnum {

    BLOCK("区块",1),
    CONSENSUS("共识",2),
    PING("PING",3),
    PONG("PONG",4)
    ;

    private String desc;
    private int code;

    MessageEnum(String desc,int code){
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

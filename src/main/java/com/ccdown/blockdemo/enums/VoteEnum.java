package com.ccdown.blockdemo.enums;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */
public enum VoteEnum {
    PREPREPARE("节点将自己生成BLOCK", 100),
    PREPARE("节点 到请求生成 Block的消息，进入准备状态，并对外广播该状态", 200),
    COMMIT("每个节点收到超过2f+l 不同节点的 commit 消息后，则认为该区块已经达成 ，即进入Commit并将其持久化到区块链数据库中", 400);


    private String msg;
    private int code;

    VoteEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static VoteEnum findCode(int code) {
        for (VoteEnum voteEnum :
                VoteEnum.values()) {
            if (voteEnum.code == code) {
                return voteEnum;
            }
        }
        return null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

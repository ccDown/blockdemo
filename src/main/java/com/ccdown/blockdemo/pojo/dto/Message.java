package com.ccdown.blockdemo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuan
 * Created on 21/4/28.
 * @description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Message {
    protected int messageType;
    protected byte[] message;
}

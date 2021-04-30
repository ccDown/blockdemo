package com.ccdown.blockdemo.utils;


import com.ccdown.blockdemo.utils.crypto.sm2.Sm2CryptoUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author kuan
 * Created on 21/4/27.
 * @description
 */
public class SimpleMerkleTree {
    public static String getRootHash(List<String> hashList) {
        if (null == hashList || hashList.size() == 0) {
            return null;
        }

        if (hashList.size() != 1) {
            hashList = getMerkleTreeNodeHash(hashList);
        }

        return hashList.get(0);
    }

    private static ArrayList<String> getMerkleTreeNodeHash(List<String> contentList) {

        ArrayList<String> merKleNodeList = new ArrayList<>();

        int index = 0, length = contentList.size();
        while (index < length) {
            //获取左孩子节点数据
            String left = contentList.get(index);
            ++index;
            //获取右孩子节点数据
            String right = "";
            if (index < length) {
                right = contentList.get(index);
                ++index;
                //计算左右孩子节点的父节点的哈希值
                String sha2HexValue = Base64.getEncoder().encodeToString(Sm2CryptoUtils.hash((left + right).getBytes(StandardCharsets.UTF_8)));
                merKleNodeList.add(sha2HexValue);
                return merKleNodeList;
            }
        }
        return merKleNodeList;
    }
}
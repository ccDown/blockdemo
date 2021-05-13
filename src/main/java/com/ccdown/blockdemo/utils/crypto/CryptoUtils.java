package com.ccdown.blockdemo.utils.crypto;

import org.bouncycastle.crypto.CryptoException;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author kuan
 * Created on 21/4/27.
 * @description
 */
public abstract class CryptoUtils {

    public static byte[] sign(byte[] unsignedMsg, PrivateKey privateKey) throws CryptoException, IOException {
        return new byte[0];
    }

    public static boolean verify(byte[] signedMsg, byte[] unSignedMsg, PublicKey publicKey) throws IOException {
        return false;
    }

    public static byte[] enCode(byte[] msg, PrivateKey privateKey) {
        return new byte[0];
    }

    public static byte[] deCode(byte[] enCodeMsg, PublicKey publicKey) {
        return new byte[0];
    }

    public static String hash(byte[] msg) {
        return "";
    }

}

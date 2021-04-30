package com.ccdown.blockdemo.utils.crypto.sm2;


import com.ccdown.blockdemo.utils.crypto.CryptoUtils;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author kuan
 * Created on 21/4/27.
 * @description
 */
public class Sm2CryptoUtils extends CryptoUtils {

    private static final byte[] SM2_ID = {
            (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38,
            (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38
    };

    public static byte[] sign(byte[] unsignedMsg, PrivateKey privateKey) throws CryptoException, IOException {
        AsymmetricKeyParameter ecParam = PrivateKeyFactory.createKey(privateKey.getEncoded());
        SM2Signer sm2Signer = new SM2Signer();
        sm2Signer.init(true, new ParametersWithID(ecParam, SM2_ID));
        sm2Signer.update(unsignedMsg, 0, unsignedMsg.length);
        return sm2Signer.generateSignature();
    }


    public static boolean verify(byte[] signedMsg, byte[] unSignedMsg, PublicKey publicKey) throws IOException {
        AsymmetricKeyParameter ecParam = PublicKeyFactory.createKey(publicKey.getEncoded());
        SM2Signer sm2Signer = new SM2Signer();
        sm2Signer.init(false, new ParametersWithID(ecParam, SM2_ID));
        sm2Signer.update(unSignedMsg, 0, unSignedMsg.length);
        return sm2Signer.verifySignature(signedMsg);
    }

    public static byte[] enCode(byte[] msg, PrivateKey privateKey) {
        return new byte[0];
    }

    public static byte[] deCode(byte[] enCodeMsg, PublicKey publicKey) {
        return new byte[0];
    }

    public static byte[] hash(byte[] msg) {
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg, 0, msg.length);
        byte[] out = new byte[32];
        sm3.doFinal(out, 0);
        return out;
    }
}

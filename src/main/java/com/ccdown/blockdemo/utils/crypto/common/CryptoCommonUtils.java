package com.ccdown.blockdemo.utils.crypto.common;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

/**
 * @author kuan
 * Created on 21/4/27.
 * @description
 */
public class CryptoCommonUtils {
    /**
     * @return java.security.PrivateKey
     * @Description 通过key获取 PrivateKey
     * @Param [pemKey]
     **/
    public static PrivateKey getPrivateKey(byte[] pemKey) throws CryptoException {
        PrivateKey privateKey;
        try {
            PemReader pr = new PemReader(new StringReader(new String(pemKey)));
            PEMParser pem = new PEMParser(new StringReader(new String(pemKey)));
            PemObject po = pr.readPemObject();
            Object obj = pem.readObject();
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
            if ("PRIVATE KEY".equals(po.getType())) {
                privateKey = jcaPEMKeyConverter.getPrivateKey((PrivateKeyInfo) obj);
            } else {
                privateKey = jcaPEMKeyConverter.getPrivateKey(((PEMKeyPair) obj).getPrivateKeyInfo());
            }
        } catch (Exception e) {
            throw new CryptoException("转换私钥失败：%s", e);
        }
        return privateKey;
    }

    /**
     * @return java.security.PublicKey
     * @Description 通过key获取PublicKey
     * @Param [pemKey]
     **/
    public static PublicKey getPublicKey(byte[] pemKey) throws CryptoException, NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey;
        try {
            PemReader pr = new PemReader(new StringReader(new String(pemKey)));
            PEMParser pem = new PEMParser(new StringReader(new String(pemKey)));
            PemObject po = pr.readPemObject();
            Object obj = pem.readObject();
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
            if ("PUBLIC KEY".equals(po.getType())) {
                publicKey = jcaPEMKeyConverter.getPublicKey((SubjectPublicKeyInfo) obj);
            } else {
                publicKey = jcaPEMKeyConverter.getPublicKey(((PEMKeyPair) obj).getPublicKeyInfo());
            }
        } catch (Exception e) {
            throw new CryptoException("转换公钥失败：%s", e);
        }
        return publicKey;
    }

    /**
     * @return java.security.PublicKey
     * @Description 通过pem证书获取公钥
     * @Param [pemPath]
     **/
    private static PublicKey getPublicKeyFromPem(String pemPath) throws IOException, CertificateException, NoSuchProviderException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
        FileInputStream fileInputStream = new FileInputStream(pemPath);
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        return x509Certificate.getPublicKey();
    }

    /**
     * @param filePath 文件路径
     * @return java.lang.String 文件信息
     * @description：读文件
     */
    private static String readFile(String filePath) {
        StringBuilder info = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String tempString;
            while ((tempString = br.readLine()) != null) {
                info.append(tempString).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return info.toString();
        }
    }
}

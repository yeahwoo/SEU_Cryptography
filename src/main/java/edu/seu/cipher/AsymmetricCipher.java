package edu.seu.cipher;

import edu.seu.algorithm.RSA;
import edu.seu.algorithm.RSAKey;

import static edu.seu.enums.RSAMode.*;

public class AsymmetricCipher {
    // 如需拓展其他算法，可使用以下字段标记算法，并在各方法中通过判断此字段调用不同算法中的方法
    // private String algorithm;
    // 密钥对
    private final RSAKey.KeyPair keyPair;

    /**
     * 构造函数，初始化密钥对
     * @param nSize 模数长度
     */
    public AsymmetricCipher(int nSize) {
        this.keyPair = new RSAKey(nSize).getKeyPair();
    }

    /**
     * 公钥加密
     * @param plainText 明文
     * @return 密文
     * @throws Exception process抛出的异常
     */
    public byte[] encrypt(byte[] plainText) throws Exception {
        return new RSA(ENCRYPT,keyPair.getPublicKey()).process(plainText);
    }

    /**
     * 私钥解密
     * @param cipherText 密文
     * @return 明文
     * @throws Exception process抛出的异常
     */
    public byte[] decrypt(byte[] cipherText) throws Exception {
        return new RSA(DECRYPT,keyPair.getPrivateKey()).process(cipherText);
    }

    /**
     * 公钥验证
     * @param signature 签名
     * @return 数据摘要
     * @throws Exception process抛出的异常
     */
    public byte[] verify(byte[] signature) throws Exception {
        return new RSA(VERIFY,keyPair.getPublicKey()).process(signature);
    }

    /**
     * 私钥签名
     * @param digest 数据摘要
     * @return 签名
     * @throws Exception process抛出的异常
     */
    public byte[] sign(byte[] digest) throws Exception {
        return new RSA(SIGN,keyPair.getPrivateKey()).process(digest);
    }

    /**
     * 获取密钥对
     * @return 密钥对
     */
    public RSAKey.KeyPair getKeyPair() {
        return keyPair;
    }
}

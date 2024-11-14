package edu.seu.cipher;

import edu.seu.algorithm.AES;
import edu.seu.algorithm.DES;
import edu.seu.enums.Algorithm;

import java.security.SecureRandom;

public class SymmetricCipher {
    // 对称加密算法
    private final Algorithm algorithm;
    // 数据分块大小（分块加密）
    private final int blockSize;
    // 对称密钥种子
    private final String symmetricKeySeed;
    // 加密密钥
    private byte[] symmetricKey;

    /**
     * 构造函数
     *
     * @param algorithm        对称加密算法
     * @param symmetricKeySeed 对称密钥种子
     */
    public SymmetricCipher(Algorithm algorithm, String symmetricKeySeed) {
        this.algorithm = algorithm;
        this.symmetricKeySeed = symmetricKeySeed;
        if (algorithm == Algorithm.DES) {
            blockSize = 8;
        } else {
            blockSize = 16;
        }
        ;
        this.symmetricKey = new byte[blockSize];
        generateKey();
    }

    /**
     * 默认使用AES算法
     *
     * @param symmetricKeySeed 对称密钥种子
     */
    public SymmetricCipher(String symmetricKeySeed) {
        this.algorithm = Algorithm.AES;
        this.symmetricKeySeed = symmetricKeySeed;
        blockSize = 16;
        this.symmetricKey = new byte[blockSize];
        generateKey();
    }

    /**
     * 对称加密
     *
     * @param plainText 明文
     * @return 加密后的密文
     */
    public byte[] encrypt(byte[] plainText) {
        if (algorithm == Algorithm.DES) {
            return new DES(symmetricKey).encrypt(plainText);
        } else {
            return new AES(symmetricKey).encrypt(plainText);
        }
    }

    /**
     * 对称解密
     *
     * @param cipherText 密文
     * @return 解密后的明文
     */
    public byte[] decrypt(byte[] cipherText) {
        if (algorithm == Algorithm.DES) {
            return new DES(symmetricKey).decrypt(cipherText);
        } else {
            return new AES(symmetricKey).decrypt(cipherText);
        }
    }

    /**
     * 获取对称密钥
     *
     * @return 密钥
     */
    public byte[] getKey() {
        return symmetricKey;
    }

    /**
     * 生成对称密钥
     */
    private void generateKey() {
        byte[] key = new byte[blockSize];
        // 随机生成种子
        // 实例化随机数生成器
        SecureRandom random;
        if (symmetricKeySeed == null) {
            random = new SecureRandom();
            // 用这个生成器来生成一个随机数赋值给symKey
        } else {
            // 用提供的种子实例化随机数生成器
            random = new SecureRandom(symmetricKeySeed.getBytes());
        }
        random.nextBytes(key);
        this.symmetricKey = key;
    }

    /**
     * 获取分块大小
     *
     * @return blockSize
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * 获取加密算法
     * @return String
     */
    public String getAlgorithm() {
        return algorithm.name();
    }
}

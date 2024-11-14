package edu.seu.algorithm;

import edu.seu.enums.Algorithm;

/**
 * 哈希编码
 */
public class Hash {
    // 指定哈希算法
    private final Algorithm algorithm;

    /**
     * 构造函数
     * @param algorithm 哈希算法
     */
    public Hash(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 对原文进行哈希得到数据摘要
     * @param msg 原始数据消息
     * @return 数据摘要
     */
    public byte[] getDigest(byte[] msg) {
        byte[] digest = {};
        switch (algorithm) {
            case MD5:
                MD5 md5 = new MD5();
                digest = md5.getDigest(msg);
                break;
            case SHA224:
                SHA2.SHA224 sha224 = new SHA2.SHA224();
                digest = sha224.getDigest(msg);
                break;
            case SHA256:
                SHA2.SHA256 sha256 = new SHA2.SHA256();
                digest = sha256.getDigest(msg);
                break;
            case SHA384:
                SHA5.SHA384 sha384 = new SHA5.SHA384();
                digest = sha384.getDigest(msg);
                break;
            case SHA512:
                SHA5.SHA512 sha512 = new SHA5.SHA512();
                digest = sha512.getDigest(msg);
                break;
        }

        return digest;
    }

    /**
     * 获取哈希算法
     * @return String
     */
    public String getAlgorithm() {
        return algorithm.name();
    }
}

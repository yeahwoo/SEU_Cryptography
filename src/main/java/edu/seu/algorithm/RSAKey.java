package edu.seu.algorithm;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * 生成公钥和私钥
 * 默认的模数位数为2048
 */
public class RSAKey {
    // RSA算法中模数n的bit位数
    private final int nSize;

    // 密钥对
    private KeyPair keyPair;

    /**
     * @param nSize 模数n位数
     */
    public RSAKey(int nSize) {
        this.nSize = nSize;
        generateKeyPair();
    }

    /**
     * 默认模数为2048
     */
    public RSAKey() {
        this.nSize = 2048;
        generateKeyPair();
    }

    /**
     * 生成公私钥对
     */
    private void generateKeyPair() {
        // 计算大素数p和q的位数
        // p和q的长度之和为keySize
        // 一般需要保证两个大素数位数合适
        int pLength = nSize / 2;
        int qLength = nSize - pLength;

        // 公钥指数，使用OpenSSL提供的公钥指数
        BigInteger e = BigInteger.valueOf(65537);

        // 随机数生成器
        SecureRandom random = new SecureRandom();

        // 生成公私钥对
        while (true) {
            // 利用随机数生成指定位数的大素数p和q
            BigInteger p = BigInteger.probablePrime(pLength, random);
            BigInteger q, n;

            do {
                q = BigInteger.probablePrime(qLength, random);

                // 调整p和q使得p>q
                if (p.compareTo(q) < 0) {
                    BigInteger tmp = p;
                    p = q;
                    q = tmp;
                }

                // 生成一个足够大的模数n，直到n>=指定位数
                n = p.multiply(q);
            } while (n.bitLength() < nSize);

            // p1 = p-1
            // q1 = q-1
            // λ(n) = (p-1)*(q-1)
            BigInteger p1 = p.subtract(BigInteger.ONE);
            BigInteger q1 = q.subtract(BigInteger.ONE);
            BigInteger lambda = p1.multiply(q1);
            // 检查GCD(e,λ(n))=1是否满足，如不满足则重新计算，其中λ(n)=(p-1)*(q-1)
            if (!e.gcd(lambda).equals(BigInteger.ONE)) {
                continue;
            }

            BigInteger d = e.modInverse(lambda);

            // 得到公钥和私钥
            PublicKey publicKey = new PublicKey(n, e);
            PrivateKey privateKey = new PrivateKey(n, d);
            keyPair = new KeyPair(publicKey, privateKey);
            break;
        }
    }

    /**
     * getter方法：获取密钥对
     * @return 密钥对
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * 常量类：公钥
     */
    public static final class PublicKey {
        // 模数
        private final BigInteger n;
        // 公钥指数
        private final BigInteger e;

        public PublicKey(BigInteger n, BigInteger e) {
            this.n = n;
            this.e = e;
        }

        // 模数
        public BigInteger getModule() {
            return this.n;
        }

        // 公钥指数
        public BigInteger getExponent() {
            return this.e;
        }
    }

    /**
     * 常量类：私钥
     */
    public static final class PrivateKey {
        // 模数
        private final BigInteger n;
        // 私钥指数
        private final BigInteger d;

        public PrivateKey(BigInteger n, BigInteger d) {
            this.n = n;
            this.d = d;
        }

        public BigInteger getModule() {
            return this.n;
        }

        public BigInteger getExponent() {
            return this.d;
        }
    }

    /**
     * 常量类：密钥对
     */
    public static final class KeyPair {
        private final PublicKey publicKey;
        private final PrivateKey privateKey;

        public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }
    }
}

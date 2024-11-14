package edu.seu.algorithm;

import edu.seu.enums.RSAMode;
import edu.seu.utils.DataParser;

import java.math.BigInteger;
import java.security.SecureRandom;

import static edu.seu.enums.RSAMode.*;


// TODO:算法类只需要进行加密和解密操作即可，填充等等应该剥离出去
/**
 * RSA加密算法
 * 包括加密、解密、签名、验证操作
 */
public class RSA {
    private final RSAMode mode;
    private RSAKey.PublicKey publicKey;
    private RSAKey.PrivateKey privateKey;
    private final int blockSize;

    /**
     * 公钥构造（加密或者验证）
     *
     * @param mode 操作模式
     * @param publicKey 公钥
     */
    public RSA(RSAMode mode, RSAKey.PublicKey publicKey) {
        this.mode = mode;
        this.publicKey = publicKey;
        this.blockSize = DataParser.getByteLength(publicKey.getModule());
    }

    /**
     * 私钥构造（解密或者签名）
     *
     * @param mode 操作模式
     * @param privateKey 私钥
     */
    public RSA(RSAMode mode, RSAKey.PrivateKey privateKey) {
        this.mode = mode;
        this.privateKey = privateKey;
        this.blockSize = DataParser.getByteLength(privateKey.getModule());
    }

    /**
     * RSA加密
     * PKCS#1 v2.1标准中定义的RSAEP模式
     *
     * @param data 待加密的字节数组
     * @return 加密后的字节数组
     */
    private byte[] encrypt(byte[] data) {
        // 将字节数字转成一个大整数，1表示正数，作为明文
        BigInteger m = new BigInteger(1, data);
        // 计算m^e mod n，即加密后的密文
        BigInteger c = m.modPow(publicKey.getExponent(), publicKey.getModule());
        // 将密文c转为字节数组
        return DataParser.toByteArray(c, blockSize);
    }

    /**
     * RSA解密
     * DP（Decryption/Private）：私钥解密
     *
     * @param data 待解密数据
     * @return 解密完的数据
     */
    private byte[] decrypt(byte[] data) {
        BigInteger c = new BigInteger(1, data);
        BigInteger m = c.modPow(privateKey.getExponent(), privateKey.getModule());
        return DataParser.toByteArray(m, blockSize);
    }

    /**
     * RSA签名
     * SP模式（Signing/Private）：私钥签名
     *
     * @param data 待签名数据
     * @return 签名完的数据
     */
    private byte[] sign(byte[] data) {
        return decrypt(data);
    }

    /**
     * RSA验证
     * VP模式（Verification/Private）：公钥验证（签名验证）
     *
     * @param data 待验证数据
     * @return 验证完的数据
     */
    private byte[] verify(byte[] data) {
        return encrypt(data);
    }

    /**
     * 字节填充
     * 参考rfc2313-pkcs#1 v1.5
     * EB = 00 || BT || PS || 00 || D
     * <p>
     * EB(Encrypted Block): 加密块
     * 00(0x00): 填充的开始标志
     * BT(0x01或0x02): 填充类型，0x01表示解密，0x02表示
     * PS: 填充字节，长度为k - len(D) - 3，k为加密块长度，D为原始数据
     * 00(0x00): 填充的结束标志
     * D: 原始数据
     *
     * @param data      原始数据
     * @param dataBegin 原始数据开始位置
     * @param len       原始数据长度
     * @return 填充后的数据
     */
    private byte[] pad(byte[] data, int dataBegin, int len) {
        // 存储填充后的数据
        byte[] padded = new byte[blockSize];
        // 将原始数据data从dataBegin位置后len长度的数据复制到padded的末尾，在最前面留出空间来进行字节填充
        System.arraycopy(data, dataBegin, padded, blockSize - len, len);
        // 是为了给 00 || BT || 00 留出空间，即需要填充PS的字节数
        int psLen = blockSize - len - 3;
        int index = 0;
        // 填充开始标志00
        padded[index++] = 0x00;
        if (mode == DECRYPT) {
            // 如果是解密则BT填充为0x01
            padded[index++] = 0x01;
            // 其余字节全部填充为0xff
            while (psLen-- > 0) {
                padded[index++] = (byte) 0xff;
            }
            // 填充结束标志00
            padded[index] = 0x00;
        } else {
            // 最大重试次数，以避免随机生成的字节中有太多00，不够填充导致死循环
            int maxRetries = 5;
            int retries = 0;
            // 如果是加密则BT填充为0x02
            padded[index++] = 0x02;
            // 加密情况下随机填充字节
            SecureRandom random = new SecureRandom();
            while (psLen > 0 && retries < maxRetries) {
                // 多4个字节，避免随机生成的字节中有0x00，则不能用于填充，需要跳过
                byte[] r = new byte[psLen + 4];
                random.nextBytes(r);
                for (int i = 0; i < r.length && psLen > 0; i++) {
                    if (r[i] != 0) {
                        // 只有非0字节才能用于填充
                        padded[index++] = r[i];
                        psLen--;
                    }
                }
                // 如果还没有填充足够的非0字节，则重试
                if (psLen > 0) {
                    retries++;
                }
            }
            padded[index] = 0x00;
        }
        return padded;
    }

    /**
     * 去填充
     *
     * @param padded 填充后的字节数组
     * @return 去填充后的字节数组
     */
    private byte[] unpad(byte[] padded) throws Exception {
        // 记录填充的字节数
        int index = 2;
        // 找到真实数据的位置
        while (padded[index] != 0) {
            index++;
        }
        index++;
        // 帧数数据大小
        byte[] data = new byte[padded.length - index];
        // 恢复真实数据
        System.arraycopy(padded, index, data, 0, data.length);
        return data;
    }

    /**
     * 根据操作模式mode处理消息
     *
     * @param msg 待处理消息
     * @return 处理后的数据（字节数组）
     */
    public byte[] process(byte[] msg) throws Exception {
        // 数据分块数
        int numBlocks;
        // 实际加密的数据大小
        int dataSize;
        // 处理后数据的大小
        byte[] output;
        // 数据块计数器
        int blockCount;
        // 缓冲区，解密数据
        byte[] buffer;
        // 明文长度
        int mLength;

        switch (mode) {
            case ENCRYPT:
                // PKCS#1 v1.5 填充标准：填充11字节
                dataSize = blockSize - 11;
                // 加上 dateSize-1 是为了让数据块数向上取整
                numBlocks = (msg.length + dataSize - 1) / dataSize;
                output = new byte[numBlocks * blockSize];
                blockCount = 1;
                // blockCount * dateSize - 1表示当前已处理数据的最后一个字节的索引（数组从0开始）
                // 小于length表示还没处理完，还需要更多的块
                while (blockCount * dataSize - 1 < msg.length) {
                    // 填充数据
                    byte[] padded = pad(msg, (blockCount - 1) * dataSize, dataSize);
                    // 加密填充后的数据
                    byte[] encrypted = encrypt(padded);
                    // 保证填充长度为blockSize
                    System.arraycopy(encrypted, 0, output, blockCount * blockSize - encrypted.length, encrypted.length);
                    // 处理下一个数据块
                    blockCount++;
                }
                // 处理最后一个块（最后一个块是不完整的，不足之处都需要填充）
                if (blockCount == numBlocks) {
                    // 计算最后一个块实际需要填充的字节数
                    int remain = msg.length - (blockCount - 1) * dataSize;
                    // 填充数据
                    byte[] padded = pad(msg, (blockCount - 1) * dataSize, remain);
                    // 加密填充后的数据
                    byte[] encrypted = encrypt(padded);
                    System.arraycopy(encrypted, 0, output, blockCount * blockSize - encrypted.length, encrypted.length);
                }
                return output;

            case DECRYPT:
                if (msg.length % blockSize != 0) {
                    throw new Exception("Invalid ciphertext length");
                }
                numBlocks = msg.length / blockSize;
                // 解密后的数据最大的大小（去掉填充的11字节）
                buffer = new byte[numBlocks * (blockSize - 11)];
                // 记录实际明文长度
                mLength = 0;
                for (int i = 0; i < numBlocks; i++) {
                    // 分块解密
                    byte[] input = new byte[blockSize];
                    System.arraycopy(msg, i * blockSize, input, 0, blockSize);
                    byte[] decrypted = decrypt(input);
                    // 去掉填充
                    byte[] unpadded = unpad(decrypted);
                    mLength += unpadded.length;
                    // 将解密后的数据放到buffer中
                    System.arraycopy(unpadded, 0, buffer, i * (blockSize - 11), unpadded.length);
                }
                output = new byte[mLength];
                System.arraycopy(buffer, 0, output, 0, mLength);
                return output;

            case SIGN:
                dataSize = blockSize - 11;
                numBlocks = (msg.length + dataSize - 1) / dataSize;
                output = new byte[numBlocks * blockSize];
                blockCount = 1;
                while (blockCount * dataSize - 1 < msg.length) {
                    byte[] padded = pad(msg, (blockCount - 1) * dataSize, dataSize);
                    byte[] signed = sign(padded);
                    // 保证填充长度为blockSize
                    System.arraycopy(signed, 0, output, blockCount * blockSize - signed.length, signed.length);
                    blockCount++;
                }
                if (blockCount == numBlocks) {
                    int remainder = msg.length - (blockCount - 1) * dataSize;
                    byte[] padded = pad(msg, (blockCount - 1) * dataSize, remainder);
                    byte[] signed = sign(padded);
                    System.arraycopy(signed, 0, output, blockCount * blockSize - signed.length, signed.length);
                }
                return output;

            case VERIFY:
                if (msg.length % blockSize != 0) {
                    throw new Exception("Invalid ciphertext length");
                }
                numBlocks = msg.length / blockSize;
                buffer = new byte[numBlocks * (blockSize - 11)];
                mLength = 0;
                for (int i = 0; i < numBlocks; i++) {
                    byte[] input = new byte[blockSize];
                    System.arraycopy(msg, i * blockSize, input, 0, blockSize);
                    byte[] verified = verify(input);
                    byte[] unpadded = unpad(verified);
                    mLength += unpadded.length;
                    System.arraycopy(unpadded, 0, buffer, i * (blockSize - 11), unpadded.length);
                }
                output = new byte[mLength];
                System.arraycopy(buffer, 0, output, 0, mLength);
                return output;
        }
        return null;
    }
}

package edu.seu.utils;

import edu.seu.algorithm.*;
import edu.seu.cipher.AsymmetricCipher;
import edu.seu.cipher.SymmetricCipher;
import edu.seu.enums.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static edu.seu.enums.RSAMode.VERIFY;
import static edu.seu.constant.MessageConstant.*;

public class AlgUtils {

    // 对称加密工具
    private final SymmetricCipher symmetricCipher;
    // 发送方非对称加密工具
    private final AsymmetricCipher senderAsymmetricCipher;
    // 接收方非对称加密工具
    private final AsymmetricCipher receiverAsymmetricCipher;
    // 哈希工具
    private final Hash hash;

    /**
     * 构造函数
     * @param symmetricAlg 对称加密算法
     * @param hashAlg 哈希算法
     * @param symmetricKeySeed 对称密钥种子
     * @param nSizeSender 发送方非对称加密算法的模数
     * @param nSizeReceiver 接收方非对称加密算法的模数
     */
    public AlgUtils(Algorithm symmetricAlg, Algorithm hashAlg, String symmetricKeySeed, String nSizeSender, String nSizeReceiver) {
        symmetricCipher = new SymmetricCipher(symmetricAlg, symmetricKeySeed);
        senderAsymmetricCipher = new AsymmetricCipher(Integer.parseInt(nSizeSender));
        receiverAsymmetricCipher = new AsymmetricCipher(Integer.parseInt(nSizeReceiver));
        hash = new Hash(hashAlg);
    }

    /**
     * 发送方处理明文消息
     * 1、用哈希算法生成数据摘要:H(M)
     * 2、用私钥对数据摘要签名:E(RK1,H(M))
     * 3、用对方公钥对会话密钥加密:E(UK2,K)
     * 4、用对称密钥对明文以及签名后的摘要一起加密:E(K,M + E(RK1,H(M)))
     * 5、将加密后的会话密钥和加密后的数据一起发送:SEND:E(K,M + E(RK1,H(M))) + E(UK2,K)
     * 其中K为会话密钥，M为明文，RK1为发送方私钥，UK2为接收方公钥
     *
     * @param plainText 明文消息
     * @return 处理后待发送的消息
     */
    public Map<String, byte[]> senderProcess(byte[] plainText) throws Exception {
        // 用map封装数据
        Map<String, byte[]> data = new HashMap<>();
        data.put("rawData", plainText);

        // 哈希算法生成数据摘要
        byte[] digest = hash.getDigest(plainText);

        data.put("digest", digest);

        // 对数据摘要进行签名
        byte[] signedDigest = senderAsymmetricCipher.sign(digest);

        data.put("signature", signedDigest);

        // 获取对称密钥
        byte[] sessionKey = symmetricCipher.getKey();
        data.put("sessionKey", sessionKey);

        // 对会话密钥（对称密钥）进行加密
        byte[] encryptedSessionKey = receiverAsymmetricCipher.encrypt(sessionKey);
        data.put("encryptedSessionKey", sessionKey);

        // 将明文消息和签名拼接
        byte[] msgWithSignature = new byte[plainText.length + signedDigest.length];
        System.arraycopy(plainText, 0, msgWithSignature, 0, plainText.length);
        System.arraycopy(signedDigest, 0, msgWithSignature, plainText.length, signedDigest.length);

        // 对称密钥加密明文消息和签名后的摘要
        byte[] encryptedData = symmetricCipher.encrypt(msgWithSignature);
        data.put("encryptedData", encryptedData);

        // 将对称加密后的数据和非对称加密后的会话密钥拼接，即最终要发送的数据
        byte[] sendData = new byte[encryptedData.length + encryptedSessionKey.length];
        System.arraycopy(encryptedData, 0, sendData, 0, encryptedData.length);
        System.arraycopy(encryptedSessionKey, 0, sendData, encryptedData.length, encryptedSessionKey.length);
        data.put("cipherData", sendData);

        return data;
    }

    /**
     * 接收方处理密文消息
     * 1、用私钥解密会话密钥
     * 2、用会话密钥解密实际数据
     * 3、用对方公钥验证数字签名
     * 4、得到明文消息
     *
     * @param cipherData 接收到的加密数据
     * @return 解密后的明文消息
     */
    public Map<String, byte[]> receiverProcess(byte[] cipherData) throws Exception {
        Map<String, byte[]> data = new HashMap<>();
        if (cipherData.length == 0) {
            data.put("errorMessage", DECRYPT_ERR.getBytes());
            return data;
        }
        // 加密的会话密钥
        byte[] encryptedSessionKey = new byte[DataParser.getByteLength(receiverAsymmetricCipher.getKeyPair().getPrivateKey().getModule())];
        // 收到的数据包括密文和加密后的会话密钥，一定是比模数位数大的（非对称加密的对称密钥的位数应该和模数相等）
        if (cipherData.length < encryptedSessionKey.length) {
            data.put("errorMessage", DECRYPT_ERR.getBytes());
            return data;
        }
        // 将公钥加密的会话密钥复制到encryptedSessionKey中
        System.arraycopy(cipherData, cipherData.length - encryptedSessionKey.length, encryptedSessionKey, 0, encryptedSessionKey.length);

        // 用私钥解密会话密钥
        byte[] sessionKey;
        // 解密得到会话密钥
        sessionKey = receiverAsymmetricCipher.decrypt(encryptedSessionKey);
        data.put("sessionKey", sessionKey);

        // 分离被加密的数据
        byte[] encryptedData = new byte[cipherData.length - encryptedSessionKey.length];
        if (encryptedData.length % symmetricCipher.getBlockSize() != 0) {
            data.put("errorMessage", DECRYPT_ERR.getBytes());
            return data;
        }
        System.arraycopy(cipherData, 0, encryptedData, 0, encryptedData.length);

        // 用会话密钥解密得到实际数据
        byte[] decryptedData = symmetricCipher.decrypt(encryptedData);
        data.put("decryptedData", decryptedData);

        // 分离得到发送方数字签名（私钥加密，与模数位数相同）
        byte[] signature = new byte[DataParser.getByteLength(senderAsymmetricCipher.getKeyPair().getPublicKey().getModule())];
        System.arraycopy(decryptedData, decryptedData.length - signature.length, signature, 0, signature.length);
        data.put("signature", signature);

        // 公钥解密得到数据摘要
        byte[] digest;
        digest = senderAsymmetricCipher.verify(signature);
        data.put("digest", digest);

        // 分离得到消息本身
        byte[] plainText = new byte[decryptedData.length - signature.length];
        System.arraycopy(decryptedData, 0, plainText, 0, plainText.length);
        data.put("plainText", plainText);

        // 原始数据：包括消息本身、会话密钥、数字签名和数据摘要
        byte[] rawMsg = new byte[plainText.length + signature.length + sessionKey.length + digest.length];
        System.arraycopy(plainText, 0, rawMsg, 0, plainText.length);
        data.put("totalMessage", rawMsg);
        return data;
    }

    /**
     * 获取双方密钥的各项参数（包含提示信息）
     * 供密钥查看面板调用
     *
     * @return Map<String, String>
     */
    public Map<String, String> getParameterInfo() {
        Map<String, String> params = getParameter();
        Map<String, String> paramInfo = new HashMap<>();
        String symmetricKeyPara;
        String senderPublicKeyPara;
        String senderPrivateKeyPara;
        String receiverPublicKeyPara;
        String receiverPrivateKeyPara;

        StringBuilder builder = new StringBuilder();
        // 字符串，保存各密钥
        builder.append("Hash算法：").append(params.get("hashAlg")).append("\n");
        builder.append("对称加密算法：").append(params.get("symmetricAlg")).append("\n");
        builder.append("对称加密密钥：").append(params.get("symmetricKey")).append("\n");
        symmetricKeyPara = builder.toString();
        paramInfo.put("symmetricKey", symmetricKeyPara);

        builder = new StringBuilder();
        builder.append("发送方RSA公钥(n,e)：\n(").append(params.get("senderKeyM"))
                .append(",")
                .append(params.get("senderPublicKeyE"))
                .append(")\n");
        senderPublicKeyPara = builder.toString();
        paramInfo.put("senderPublicKey", senderPublicKeyPara);

        builder = new StringBuilder();
        builder.append("发送方RSA私钥(n,d)：\n(").append(params.get("senderKeyM"))
                .append(",")
                .append(params.get("senderPrivateKeyD"))
                .append(")\n");
        senderPrivateKeyPara = builder.toString();
        paramInfo.put("senderPrivateKey", senderPrivateKeyPara);

        builder = new StringBuilder();
        builder.append("接收方RSA公钥(n,e)：\n(").append(params.get("receiverKeyM"))
                .append(",")
                .append(params.get("receiverPublicKeyE"))
                .append(")\n");
        receiverPublicKeyPara = builder.toString();
        paramInfo.put("receiverPublicKey", receiverPublicKeyPara);

        builder = new StringBuilder();
        builder.append("接收方RSA私钥(n,d)：\n(").append(params.get("receiverKeyM"))
                .append(",")
                .append(params.get("receiverPrivateKeyD"))
                .append(")\n");
        receiverPrivateKeyPara = builder.toString();
        paramInfo.put("receiverPrivateKey", receiverPrivateKeyPara);
        return paramInfo;
    }

    /**
     * 获取双方密钥的各项参数（仅参数）
     * 供密钥查看面板调用
     *
     * @return Map<String, String>
     */
    public Map<String, String> getParameter() {
        Map<String, String> params = new HashMap<>();
        params.put("hashAlg",hash.getAlgorithm());
        params.put("symmetricKey",DataParser.byte2String(symmetricCipher.getKey()));
        params.put("symmetricAlg",symmetricCipher.getAlgorithm());
        params.put("senderKeyM",senderAsymmetricCipher.getKeyPair().getPublicKey().getModule().toString());
        params.put("senderPublicKeyE",senderAsymmetricCipher.getKeyPair().getPublicKey().getExponent().toString());
        params.put("senderPrivateKeyD",senderAsymmetricCipher.getKeyPair().getPrivateKey().getExponent().toString());

        params.put("receiverKeyM",receiverAsymmetricCipher.getKeyPair().getPublicKey().getModule().toString());
        params.put("receiverPublicKeyE",receiverAsymmetricCipher.getKeyPair().getPublicKey().getExponent().toString());
        params.put("receiverPrivateKeyD",receiverAsymmetricCipher.getKeyPair().getPrivateKey().getExponent().toString());
        return params;
    }

    /**
     * 获取发送方公钥
     * @return PublicKey
     */
    public RSAKey.PublicKey getSenderPublicKey(){
        return senderAsymmetricCipher.getKeyPair().getPublicKey();
    }

    /**
     * 身份验证
     * 用明文生成数据摘要与解密签名得到的数据摘要比对
     * 静态方法，不依赖于此类中定义的加密工具的参数
     *
     * @param key 发送方公钥
     * @param data 待验证数据
     * @param signature 数字签名
     * @param hashAlg 哈希算法
     * @return 是否通过验证
     */
    public static boolean authentication(RSAKey.PublicKey key, byte[] data, byte[] signature, Algorithm hashAlg) {
        // 用哈希算法得到数据摘要
        byte[] digest = new byte[0];
        switch (hashAlg) {
            case MD5:
                MD5 md5 = new MD5();
                digest = md5.getDigest(data);
                break;
            case SHA224:
                SHA2.SHA224 sha224 = new SHA2.SHA224();
                digest = sha224.getDigest(data);
                break;
            case SHA256:
                SHA2.SHA256 sha256 = new SHA2.SHA256();
                digest = sha256.getDigest(data);
                break;
            case SHA384:
                SHA5.SHA384 sha384 = new SHA5.SHA384();
                digest = sha384.getDigest(data);
                break;
            case SHA512:
                SHA5.SHA512 sha512 = new SHA5.SHA512();
                digest = sha512.getDigest(data);
                break;
        }

        // 解密数字签名得到数据摘要
        byte[] signatureDigest;
        try {
            signatureDigest = new RSA(VERIFY,key).process(signature);
        } catch (Exception e) {
            return false;
        }

        // 相同则验证成功，否则失败
        return Arrays.equals(digest, signatureDigest);
    }
}

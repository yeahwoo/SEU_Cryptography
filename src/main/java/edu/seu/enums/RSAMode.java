package edu.seu.enums;

/**
 * RSA操作模式
 */
public enum RSAMode {
    ENCRYPT(1),  // 加密
    DECRYPT(2),  // 解密
    SIGN(3),     // 签名
    VERIFY(4);   // 验证

    private final int code;

    RSAMode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

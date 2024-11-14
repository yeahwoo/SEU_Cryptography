package edu.seu.enums;

public enum Algorithm {
    AES(1),
    DES(2),
    MD5(3),
    SHA224(4),
    SHA256(5),
    SHA384(6),
    SHA512(7);

    private final int code;

    Algorithm(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

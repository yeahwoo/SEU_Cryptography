package edu.seu.constant;

public class MessageConstant {
    // 错误信息
    public static final String INFO_UNSET = "请先设置密钥参数！\n";
    public static final String DECRYPT_ERR = "数据被篡改，解密失败！\n";
    public static final String VERIFY_ERR = "数据被篡改，未通过验证！\n";
    public static final String NO_MESSAGE = "未收到消息！\n";
    public static final String DATA_NOT_ENCRYPT = "请先解密数据！\n";
    public static final String VERIFY_SUCCESS = "数据未被篡改，验证通过！\n";
    public static final String DATA_NOT_SEND = "请先发送消息！\n";
    public static final String DATA_NOT_INPUT = "请输入待加密数据！\n";
    public static final String SEND_SUCCESS = "消息发送成功！\n";
    public static final String READ_FILE_ERR = "文件读取错误！\n";
    public static final String INVALID_LENGTH = "模数长度允许的范围为1024-2048！\n";
    public static final String SEED_NOT_INPUT = "请输入对称密钥种子！\n";
    public static final String KEY_NOT_GEN = "请生成双方RSA密钥！\n";
    public static final String PATH_NOT_SELECT = "请选择密钥保存路径！\n";
    public static final String KEY_NOT_INPUT = "请加载公钥文件！\n";
    public static final String SIG_NOT_INPUT = "请加载签名信息！\n";
    public static final String MSG_NOT_INPUT = "请输入待验证明文\n";
    public static final String ERROR = "系统异常！";
}

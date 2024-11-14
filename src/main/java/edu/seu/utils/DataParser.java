package edu.seu.utils;

import java.math.BigInteger;

/**
 * 数据解析
 */
public class DataParser {

    /**
     * 将字节数组以16进制字符串的形式输出
     * @param bytes 输入的字节数组
     */
    public static String byte2String(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // 将字节转换为无符号整数表示，并用 0xFF 遮掩负值影响
            String hex = Integer.toHexString(b & 0xFF);
            // 单字节需要补0保证两位
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 将字符串反转为字节数组
     * @param string 输入的字节数组
     */
    public static byte[] string2Byte(String string) {
        // 确保输入的字符串长度是偶数
        if (string.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string length.");
        }

        byte[] bytes = new byte[string.length() / 2];

        for (int i = 0; i < string.length(); i += 2) {
            // 每次取两个字符，转换为一个字节
            String hexByte = string.substring(i, i + 2);
            // 将十六进制字符串解析为整数，并转换为字节
            bytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
        }

        return bytes;
    }

    /**
     * 计算大整数所占字节数
     *
     * @param bigInt 大整数
     * @return 所占字节数
     */
    public static int getByteLength(BigInteger bigInt) {
        // 返回bigInt二进制数的位数
        int bitLen = bigInt.bitLength();
        // (相当于+7)/8 得到bigInt所占向上取整的字节数
        return (bitLen + 7) >> 3;
    }

    /**
     * 大整数转字节数组
     * BigInteger转为byte array，处理加密后长度大于模数长度的情况，供加解密函数使用
     * 正常情况下加密长度大于模数长度只可能出现在最高位字节需补零以指定正负数的情况，因此在encrypt和decrypt中进行
     * byte array转BigInteger时必须指定正负号，因为在此处高位字节零可能被移除，导致数变为负数
     *
     * @param bigInt 大整数
     * @param len    转为的字节数组的长度
     * @return 转换后的字节数组
     */
    public static byte[] toByteArray(BigInteger bigInt, int len) {
        // 大整数转为字节数组
        byte[] bytes = bigInt.toByteArray();
        if (bytes.length == len) {
            return bytes;
            // 当大整数转为的字节数组小于len时会在最前面补0表示正数
            // 如果生成的大整数转为字节数组超出了长度，则需要把最前面的0去掉保留后面的实际数据（这里并不在乎数据的正负，只需要将字节数组保留下来即可）
        } else if ((bytes.length == len + 1) && (bytes[0] == 0x00)) {
            byte[] tmp = new byte[len];
            System.arraycopy(bytes, 1, tmp, 0, len);
            return tmp;
        } else {
            return bytes;
        }
    }

}

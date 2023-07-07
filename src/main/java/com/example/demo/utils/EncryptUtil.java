package com.example.demo.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: [core-master]
 * Package:     [com.zero.core.utils.EncryptUtil]
 * Description: 加密工具类
 * CreateDate:  2020/3/29 23:57
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("all")
public class EncryptUtil {
    private static Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String HMACM_D5 = "HmacMD5";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String DES = "DES";
    public static final String AES = "AES";

    /**
     * 编码格式；默认使用uft-8
     */
    public String charset = "utf-8";
    /**
     * DES
     */
    public int keySizeDES = 0;
    /**
     * AES
     */
    public int keySizeAES = 128;

    public static EncryptUtil me;

    /**
     * ASCII 自定义加密前缀
     */
    public static String ASCII_PREFIX = "";

    /**
     * ASCII 自定义加密长度
     */
    public static int ASCII_LENGTH = 30;

    private EncryptUtil() {
        //单例
    }

    //双重锁
    public static EncryptUtil getInstance() {
        if (me == null) {
            synchronized (EncryptUtil.class) {
                if (me == null) {
                    me = new EncryptUtil();
                }
            }
        }
        return me;
    }

    /**
     * 使用MessageDigest进行单向加密（无密码）
     *
     * @param res       被加密的文本
     * @param algorithm 加密算法名称
     * @return 密文
     */
    private String messageDigest(String res, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
            return base64(md.digest(resBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用KeyGenerator进行单向/双向加密（可设密码）
     *
     * @param res       被加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密使用的秘钥
     * @return 密文
     */
    private String keyGeneratorMac(String res, String algorithm, String key) {
        try {
            SecretKey sk;
            if (key == null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            byte[] result = mac.doFinal(res.getBytes());
            return base64(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res       加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密的秘钥
     * @param keysize   大小
     * @param isEncode  是否使用编码
     * @return 密文
     */
    private String keyGeneratorES(String res, String algorithm, String key, int keysize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keysize);
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String base64(byte[] res) {
        return Base64.encode(res);
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf 二进制数组
     * @return 16进制
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return String.valueOf(sb);
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return 二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 把中文字符串转换为十六进制Unicode编码字符串
     *
     * @param s 中文字符串
     * @return unicode编码
     */
    public static String stringToUnicode(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            if (ch > 255) {
                str.append("\\u").append(Integer.toHexString(ch));
            } else {
                str.append("\\").append(Integer.toHexString(ch));
            }
        }
        return str.toString();
    }

    /**
     * 把十六进制Unicode编码字符串转换为中文字符串, 将\u848B\u4ECB\u77F3转化成蒋介石，注意格式
     *
     * @param str eg:\u848B\u4ECB\u77F3
     * @return 蒋介石
     */
    public static String unicodeToString(String str) {
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");

        }
        return str;
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return 密文
     */
    public String MD5(String res) {
        return messageDigest(res, MD5);
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return 密文
     */
    public String MD5(String res, String key) {
        return keyGeneratorMac(res, HMACM_D5, key);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return 密文
     */
    public String SHA1(String res) {
        return messageDigest(res, SHA1);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return 密文
     */
    public String SHA1(String res, String key) {
        return keyGeneratorMac(res, HMAC_SHA1, key);
    }

    /**
     * 使用DES加密算法进行加密（可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return 密文
     */
    public String DESencode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, true);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return 密文
     */
    public String DESdecode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, false);
    }

    /**
     * 使用AES加密算法经行加密（可逆）
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return 密文
     */
    public String AESencode(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, true);
    }

    /**
     * 对使用AES加密算法的密文进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return 密文
     */
    public String AESdecode(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, false);
    }

    /**
     * 使用XO异或进行加密
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return 密文
     */
    public String XORencode(String res, String key) {
        byte[] bs = res.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return parseByte2HexStr(bs);
    }

    /**
     * 使用XO异或进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return 密文
     */
    public String XORdecode(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);
        if (bs == null) {
            return "";
        }
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return new String(bs);
    }

    /**
     * 直接使用异或（第一调用加密，第二次调用解密）
     *
     * @param res 密文
     * @param key 秘钥
     * @return 结果
     */
    public int XOR(int res, String key) {
        return res ^ key.hashCode();
    }

    /**
     * 使用Base64进行加密
     *
     * @param res 字符串
     * @return 密文
     */
    public static String Base64Encode(String res) {
        return Base64.encode(res.getBytes());
    }

    /**
     * 使用Base64进行解密
     *
     * @param res 密文
     * @return 字符串
     */
    public static String Base64Decode(String res) {
        return new String(Base64.decode(res));
    }

    /**
     * ASCIIEncode 加密
     *
     * @param res 明文
     * @return 密文
     */
    public static String ASCIIEncode(String res) {
        return ASCIIEncode(res, 0);
    }

    /**
     * ASCIIEncode 加密
     *
     * @param res       明文
     * @param maxLength 最大长度限制（0：不限制）
     * @return 密文
     */
    public static String ASCIIEncode(String res, int maxLength) {
        return ASCIIEncode(res, maxLength, ASCII_PREFIX);
    }

    /**
     * ASCIIEncode 加密
     *
     * @param res       明文
     * @param maxLength 最大长度限制（0：不限制）
     * @param prefix    加密前缀
     * @return 密文
     */
    public static String ASCIIEncode(String res, int maxLength, String prefix) {
        String resEncode = res;
        if (StringUtils.isNotBlank(resEncode)) {
            String[] strings = resEncode.split("_");
            // 长度平均分配
            if (strings.length > 0) {
                maxLength = maxLength - strings.length - 1;
                maxLength = maxLength / strings.length;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String o : strings) {
                stringBuilder.append(EncryptUtil.ASCIIEncodeOperate(o, maxLength, prefix)).append("_");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            resEncode = stringBuilder.toString();
        }
        return resEncode;
    }

    /**
     * ASCIIEncode 加密
     *
     * @param res       明文
     * @param maxLength 最大长度限制（0：不限制）
     * @param prefix    加密前缀
     * @return 密文
     */
    public static String ASCIIEncodeOperate(String res, int maxLength, String prefix) {
        //原文：String msg = "440 981 199 601 186 12";
        //偏移：String msg = "NNJ SRK KSS PJK KRP KL";
        //base64加密：String msg = "Tk5K U1JL S1NT UEpL S1JQ KL";
        StringBuffer stringBuffer = new StringBuffer();

        if (StringUtils.isBlank(res)) {
            new Exception("ASCIIEncode: 明文字符不能为空");
        }

        // 加密一：偏移量
        byte[] bytes = res.getBytes();
        for (byte b : bytes) {
            // 可显示字符范围：32 <= n <= 126， 可视范围+26
            if (32 <= b && b <= 100) {
                char c = (char) (b + 26);
                // 加密范围：58 <= c <= 152
                stringBuffer.append(c);
            }
        }

        log.warn("ASCIIEncode-偏移量：{}", stringBuffer);
        // 解密二：base64
        // 3个字符转换为4个字符
        int length = stringBuffer.length();
        int prefixLength = prefix.length();
        maxLength = maxLength - prefixLength;
        if (maxLength > 0 && length < maxLength) {
            // 可转换个数
            int number = length / 3;
            // 加密后最大长度
            int encodeMaxLength = length + number;
            int diff = maxLength - encodeMaxLength;
            // 如果超过最大长度则
            if (diff < 0) {
                // 更新可转换的个数
                number = number + diff;
            }

            // base64 二次转码
            for (int i = number - 1; i >= 0; i--) {
                int startIndex = i * 3;
                int endIndex = startIndex + 3;
                String s = stringBuffer.substring(startIndex, endIndex);
                String base64Decode = Base64Encode(s);
                log.warn("ASCIIEncode-数字{}替换操作：{}-{} ： {} == 》 {}", stringBuffer, startIndex, endIndex, s, base64Decode);
                stringBuffer = stringBuffer.replace(startIndex, endIndex, base64Decode);
                log.warn("ASCIIEncode-数字替换结果：{}", stringBuffer.toString());
            }
        }
        return stringBuffer.insert(0, prefix).toString();
    }

    /**
     * ASCII解密
     *
     * @param res 密文
     * @return 明文
     */
    public static String ASCIIDncode(String res) {
        return ASCIIDncode(res, 0);
    }

    /**
     * ASCII解密
     *
     * @param res       密文
     * @param maxLength 最大长度限制（0：不限制）
     * @return 明文
     */
    public static String ASCIIDncode(String res, int maxLength) {
        return ASCIIDncode(res, maxLength, ASCII_PREFIX);
    }

    /**
     * ASCII解密("_"作为分割符号)
     *
     * @param res       密文
     * @param maxLength 最大长度限制（0：不限制）
     * @param prefix    前缀
     * @return 明文
     */
    public static String ASCIIDncode(String res, int maxLength, String prefix) {
        String resDncode = res;
        if (StringUtils.isNotBlank(resDncode)) {
            String[] strings = resDncode.split("_");
            if (strings.length > 0) {
                maxLength = maxLength - strings.length - 1;
                maxLength = maxLength / strings.length;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String o : strings) {
                stringBuilder.append(EncryptUtil.ASCIIDncodeOperate(o, maxLength, prefix)).append("_");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            resDncode = stringBuilder.toString();
        }
        return resDncode;
    }

    /**
     * ASCII解密
     *
     * @param res       密文
     * @param maxLength 最大长度限制（0：不限制）
     * @param prefix    前缀
     * @return 明文
     */
    public static String ASCIIDncodeOperate(String res, int maxLength, String prefix) {
        if (StringUtils.isBlank(res)) {
            new Exception("ASCIIDncode: 密文字符不能为空");
        }

        // 前缀校验不一致则不解密
        if (!res.startsWith(prefix)) {
            return res;
        }
        res = res.replaceFirst(prefix, "");

        // 解密一：base64
        StringBuffer base64Buf = new StringBuffer(res);
        // 4个字符还原为3个字符
        int length = res.length();
        if (maxLength > 0 && length < maxLength) {
            // 可解密转换个数
            int number = length / 4;

            // base64 二次转码
            for (int i = number - 1; i >= 0; i--) {
                int startIndex = i * 4;
                int endIndex = startIndex + 4;
                String s = base64Buf.substring(startIndex, endIndex);
                String base64Decode = Base64Decode(s);
                log.warn("ASCIIDncode-数字{}替换操作：{}-{} ： {} == 》 {}", base64Buf, startIndex, endIndex, s, base64Decode);
                base64Buf = base64Buf.replace(startIndex, endIndex, base64Decode);
                log.warn("ASCIIDncode-数字替换结果：{}", base64Buf.toString());
            }
        }

        log.warn("ASCIIDncode-bese64解密：{}", base64Buf);

        res = base64Buf.toString();
        StringBuffer stringBuffer = new StringBuffer();
        // 解密二：偏移量
        byte[] bytes = res.getBytes();
        for (byte b : bytes) {
            // 加密后可显示字符范围：58 <= n <= 152， 可视范围-26
            if (58 <= b && b <= 152) {
                char c = (char) (b - 26);
                // 解密后字符范围：32 <= n <= 126
                stringBuffer.append(c);
            }
        }

        return stringBuffer.toString();
    }

//    public static void main(String[] args) {
//        String groupId = "TG440106199602031215_null";
//        String s = EncryptUtil.ASCIIEncode(groupId, EncryptUtil.ASCII_LENGTH);
//       /* String[] strings = groupId.split("_");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String o : strings) {
//            stringBuilder.append(EncryptUtil.ASCIIEncode(o, EncryptUtil.ASCII_LENGTH)).append("_");
//        }
//        if (stringBuilder.length() > 0) {
//            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
//        }
//        groupId = stringBuilder.toString();*/
//        String asciiEncode = EncryptUtil.ASCIIEncode(groupId, ASCII_LENGTH);
//        System.out.println(s);
//        System.out.println(asciiEncode);
//    }

    public static void main(String[] args) {
        String msg = "TG440106199602031215_null";
//        log.info("insert :{}", new StringBuilder(msg).insert(0, "abc").toString());
        //原文：String msg = "440 981 199 601 186 12";
        //偏移：String msg = "NNJ SRK KSS PJK KRP KL";
        //base64加密：String msg = "Tk5K U1JL S1NT UEpL S1JQ KL";
//        String result = "NDQwOTgxMTk5NjAxMTg2MTI=_OTE0NDAxMDFNQTU5UTRVUjlI";
//        String deSencode = new EncryptUtil().DESencode("44098119960118612", "1");
//        String aeSencode = new EncryptUtil().AESencode("44098119960118612", "1");
//        System.out.println(deSencode);
//        System.out.println(aeSencode);
        String asciiEncode = EncryptUtil.ASCIIEncode(msg, ASCII_LENGTH);
        log.info("加密：{}", asciiEncode);
        log.info("解密：{}", EncryptUtil.ASCIIDncode(asciiEncode, ASCII_LENGTH));
        log.info("解密：{}", EncryptUtil.ASCIIDncode("naNNJKJPKSSPJLJMKLKO_", ASCII_LENGTH));
        // ASNNOMRKKSSSJMKPLKNL_ASSKNNJKJKg[O]qlNrJg
/*        String roomId = EncryptUtil.ASCIIEncode("445381199903162142_91440101MA5CWR4X0M", ASCII_LENGTH);
//        String roomId = EncryptUtil.ASCIIEncode("91440101MA5CWR4X0M", ASCII_LENGTH);
        log.info("房间号加密：{}", roomId);
        roomId = EncryptUtil.ASCIIDncode(roomId, ASCII_LENGTH);
        log.info("房间号解密：{}", roomId);*/
    }
}

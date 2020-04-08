package pers.wesley.common.util;

import pers.wesley.common.exception.BaseException;
import pers.wesley.common.exception.ErrorCodeEnum;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Description : Des加/解密
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 15:39
 */
public abstract class DesUtils {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";

    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * des加密
     * @param desKey
     * @param content
     * @return
     */
    public static String encrypt(String desKey, String content) {
        try {
            final byte[] decryptByte = content.getBytes(CHARSET);
            return new String(Base64.getEncoder().encode(getCipher(Cipher.ENCRYPT_MODE, desKey, decryptByte)));
        } catch (Exception e) {
            throw new BaseException(ErrorCodeEnum.ENCRYPT, new String[] {"des"}, e);
        }
    }

    /**
     * des解密
     * @param desKey
     * @param content
     * @return
     */
    public static String decrypt(String desKey, String content) {
        try {
            final byte[] encryptByte = Base64.getDecoder().decode(content);
            return new String(getCipher(Cipher.DECRYPT_MODE, desKey, encryptByte), CHARSET);
        } catch (Exception e) {
            throw new BaseException(ErrorCodeEnum.DECRYPT, new String[] {"des"}, e);
        }
    }

    private static byte[] getCipher(int cryptMode, String desKey, byte[] content) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec desKeySpec = new DESKeySpec(String.format("%8.8s", desKey).getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        // Cipher对象实际完成解密操作
        // using DES in ECB mode
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(cryptMode, secretKey, random);
        return cipher.doFinal(content);
    }
}

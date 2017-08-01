package com.tww.utils.crypto;

import java.security.GeneralSecurityException;

import javax.crypto.spec.IvParameterSpec;

/** 
 * @ClassName: DesBase 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author taoww
 * @date 2017年3月13日 上午10:31:11
 * <p> 
 * 加密算法工具父类
 * <p>
 * 包涵一些常量和公共方法 <br>
 * http://tripledes.online-domain-tools.com/ <br> 
 */
public class DesBase {
	/**
	 * 8字节全0
	 */
	public static final byte[] DEF_IV = { (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00 };

	/**
	 * 0x80开始,后为全0
	 */
	public static final byte[] DEF_FILL = { (byte) 0x80, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00 };

	/**
	 * 自动填充数组
	 * <p>
	 * 以8000000000000000填充<br>
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] append80(byte[] data) {
		return append(data, DEF_FILL);
	}

	/**
	 * 自动填充数组
	 * 
	 * @param data
	 * @param fills
	 * @return
	 */
	public static byte[] append(byte[] data, byte[] fills) {
		byte[] result = new byte[data.length
				+ (data.length % 8 == 0 ? 8 : 8 - (data.length % 8))];
		System.arraycopy(data, 0, result, 0, data.length);
		System.arraycopy((fills == null ? DEF_FILL : fills), 0, result,
				data.length, result.length - data.length);
		return result;
	}

	/**
	 * 自动去除加密数据的补位
	 * <p>
	 * 去除以8000000000000000的填充<br>
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] cut80(byte[] data) {
		int index = 0;
		for (int i = data.length - 1; i > 0; i--) {
			if ((data[i] & 0xFF) == 0x80) {
				index = i;
				break;
			}
		}
		byte[] result = new byte[index];
		System.arraycopy(data, 0, result, 0, result.length);
		return result;
	}

	/**
	 * 补充成24字节完整密钥
	 * <p>
	 * 16字节时补充的8字节就是16字节密钥的前8位<br>
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] build3DesKey(byte[] key) {
		if (key == null || key.length == 0) {
			throw new IllegalArgumentException("密钥不能为空！");
		} else if (key.length == 24) {
			return key;
		} else if (key.length == 16) {
			// 补充的8字节就是16字节密钥的前8位
			byte[] km = new byte[24];
			System.arraycopy(key, 0, km, 0, 16);
			System.arraycopy(key, 0, km, 16, 8);
			return km;
		} else {
			throw new IllegalArgumentException("密钥长度不可控！");
		}
	}
	
	public static byte[] keyTo24Bytes(byte[] key) {
        if (key.length == 24) {
            return key;
        }
        byte[] padKey = new byte[24];
        if (key.length == 16) {
            // key before expansion
            // key = |-- 8-byte partA --|-- 8 byte part B --|

            // key after expansion:
            // |-- part A --||-- part B --|-- part A --|
            System.arraycopy(key, 0, padKey, 0, key.length);
            System.arraycopy(key, 0, padKey, key.length, 8);
        } else if (key.length == 8) {
            // key before expansion
            // key = |-- 8-byte partA --|

            // key after expansion:
            // |-- part A --||-- part A --|-- part A --|
            System.arraycopy(key, 0, padKey, 0, key.length);
            System.arraycopy(key, 0, padKey, key.length, key.length);
            System.arraycopy(key, 0, padKey, key.length << 1, key.length);
        } else {
            throw new IllegalArgumentException(
                    "invalid key length: " + key.length);
        }
        return padKey;
    }

	/**
	 * 生成向量对象
	 * 
	 * @param iv
	 * @return
	 * @throws GeneralSecurityException
	 */
	protected static IvParameterSpec ivGenerator(byte[] iv)
			throws GeneralSecurityException {
		IvParameterSpec IV;
		if (iv == null || iv.length == 0) {
			IV = new IvParameterSpec(DEF_IV);
		} else {
			IV = new IvParameterSpec(iv);
		}
		return IV;
	}

	/**
	 * byte数组转换为16进制字符串
	 * 
	 * @param data
	 * @return
	 */
	protected static String bytes2Hex(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String temp = Integer.toHexString(((int) data[i]) & 0xFF);
			for (int t = temp.length(); t < 2; t++) {
				sb.append("0");
			}
			sb.append(temp);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 
	 * 16进制转换为byte数组
	 * 
	 * @param hexStr
	 * @return
	 */
	protected static byte[] hex2Bytes(String hexStr) {
		byte[] bts = new byte[hexStr.length() / 2];
		for (int i = 0, j = 0; j < bts.length; j++) {
			bts[j] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
			i += 2;
		}
		return bts;
	}

}

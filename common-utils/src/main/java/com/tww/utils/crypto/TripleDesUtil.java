package com.tww.utils.crypto;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/** 
 * @ClassName: TripleDesUtil 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author taoww
 * @date 2017年3月13日 上午10:33:59 
 * <p>
 * 3DES工具类
 * <p>
 * 3DES（或称为Triple DES）是三重数据加密算法 <br>
 * <br>
 */
public class TripleDesUtil extends DesBase {
	/**
	 * 算法名称 DESede
	 */
	public static final String KEY_ALGORITHM = "DESede";

	// 算法名称/加密模式/填充方式
	public static final String CIPHER_ALGORITHM_ECB = "DESede/ECB/NoPadding";
	public static final String CIPHER_ALGORITHM_CBC = "DESede/CBC/NoPadding";
	public static final String CIPHER_ALGORITHM_ECB_CS5 = "DESede/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_CBC_CS5 = "DESede/CBC/PKCS5Padding";

	/**
	 * DESede/CBC/NoPadding加密算法
	 * <p>
	 * 默认向量IV全0
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static byte[] cbcEncrypt(byte[] key, byte[] data)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			GeneralSecurityException {
		return cbc(key, data, DEF_IV, true);
	}
	
	/**
	 * DESede/CBC/NoPadding加密算法
	 * <p>
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static byte[] cbcEncrypt(byte[] key, byte[] data, byte[] iv)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			GeneralSecurityException {
		return cbc(key, data, iv, true);
	}

	/**
	 * DESede/CBC/NoPadding加密算法
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param iv
	 *            向量
	 * @param encrypt
	 *            模式,true:加密,false:解密
	 * @return byte[]
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	public static byte[] cbc(byte[] key, byte[] data, byte[] iv, boolean encrypt)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			GeneralSecurityException {
		if (key.length != 16 && key.length != 8) {
            throw new IllegalArgumentException("key length must be 16 or 8");
        }
        if (data.length % 8 != 0) {
            throw new IllegalArgumentException(
                    "data length must be mutiple of 8");
        }
        byte[] key24 = keyTo24Bytes(key);
        
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
				keyGenerator(key24), ivGenerator(iv));
		//cipher.init(Cipher.ENCRYPT_MODE, keyGenerator(key24), ivGenerator(iv));
		byte[] result = cipher.doFinal(data);
		return result;
	}
	
	/**
	 * DESede/CBC/NoPadding解密算法<br>
	 * 向量默认全0
	 * 
	 * @param key
	 * @param data
	 * @return byte[]
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	public static byte[] cbcDecrypt(byte[] key, byte[] data)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			GeneralSecurityException {
		return cbc(key, data, DEF_IV, false);
	}
	
	/**
	 * DESede/CBC/NoPadding解密算法
	 * 
	 * @param key
	 * @param data
	 * @param iv
	 * @return byte[]
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	public static byte[] cbcDecrypt(byte[] key, byte[] data, byte[] iv)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			GeneralSecurityException {
		return cbc(key, data, iv, false);
	}


	/**
	 * DESede/ECB/NoPadding加密算法
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws GeneralSecurityException
	 * @throws InvalidKeyException
	 */
	public static byte[] ecbEncrypt(byte[] key, byte[] data)
			throws InvalidKeyException, GeneralSecurityException {
		return ecb(key, data, true);
	}
	
	/**
	 * DESede/ECB/NoPadding算法
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param encrypt
	 *            模式,true:加密,false:解密
	 * @return byte[]
	 * @throws GeneralSecurityException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	public static byte[] ecb(byte[] key, byte[] data, boolean encrypt)
			throws InvalidKeyException, GeneralSecurityException {
		if (key.length != 16 && key.length != 8) {
			throw new IllegalArgumentException("key length must be 16 or 8");
		}
		if (data.length % 8 != 0) {
			throw new IllegalArgumentException(
					"data length must be mutiple of 8");
		}
		byte[] key24 = keyTo24Bytes(key);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
				keyGenerator(key24));
		byte[] result = cipher.doFinal(data);
		return result;
	}
	

	/**
	 * DESede/ECB/NoPadding解密算法
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws GeneralSecurityException
	 * @throws InvalidKeyException
	 * @throws
	 */
	public static byte[] ecbDecrypt(byte[] key, byte[] data)
			throws InvalidKeyException, GeneralSecurityException {
		return ecb(key, data, false);
	}

	/**
	 * 
	 * 生成密钥key对象
	 * 
	 * @param KeyStr
	 *            密钥字符串
	 * @return 密钥对象
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	protected static SecretKey keyGenerator(byte[] key)
			throws GeneralSecurityException {
		DESedeKeySpec KeySpec = new DESedeKeySpec(key);
		SecretKeyFactory KeyFactory = SecretKeyFactory
				.getInstance(KEY_ALGORITHM);
		return KeyFactory.generateSecret(KeySpec);
	}

	public static void main(String[] args) throws Exception {
		// 即可动态的将提供者加入
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// Security.addProvider(new
		// org.bouncycastle.jce.provider.BouncyCastleProvider());

		String key = "404142434445464748494A4B4C4D4E4F4041424344454647";
		String data = "FFFFFFFFFFFFFF078069FFFFFFFFFFFF439CDDE3BE9A98C2";

		byte[] temp = cbcEncrypt(hex2Bytes(key), hex2Bytes(data));
		System.out.println("3encrypt:" + bytes2Hex(temp));

		temp = cbcDecrypt(hex2Bytes(key), temp);
		System.out.println("3decrypt:" + bytes2Hex(temp));

		temp = ecbEncrypt(hex2Bytes(key), hex2Bytes(data));
		System.out.println("3encrypt ecb:" + bytes2Hex(temp));

		temp = ecbDecrypt(hex2Bytes(key), temp);
		System.out.println("3decrypt ecb:" + bytes2Hex(temp));
	}
}

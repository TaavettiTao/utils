/**
 * Copyright Shanghai COS Software Co., Ltd.
 * All right reserved
 * RSAUtil.java
 * Created on 2015
 */

package com.tww.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * 
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * 
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2015
 * @author yangb
 * @version 1.0
 * @Create: 2015-8-11 下午02:40:34
 * 
 * @Modification History
 * @Date Author Version Description
 * @----------------------------------------------------
 * @2015-8-11 yangb 1.0 create
 */
public class RSAUtil {

	public enum AlgorithmEnum {

		RSA_NONE_PKCS1("RSA/None/PKCS1Padding"),

		RSA_NONE_NOPAD("RSA/None/NoPadding"),

		RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),

		RSA_ECB_NOPAD("RSA/ECB/NoPadding"),

		;
		private String code;

		private AlgorithmEnum(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	// public static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";

	// public static final String RSA_ECB_NOPADDING = "RSA/ECB/NoPadding";

	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @param keyLen
	 *            密钥长度,1024,1152..
	 * @return Map
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, Object> genKeyPair(int keyLen)
			throws NoSuchAlgorithmException {
		if (512 > keyLen || keyLen > 2048 || keyLen % 8 != 0) {
			throw new IllegalArgumentException("密钥长度错误");
		}

		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(keyLen);
		KeyPair keyPair = keyPairGen.generateKeyPair();

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对
	 * @return 私钥
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对
	 * @return 公钥
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 随机生成密钥对文件(keystore)
	 * 
	 * @Title: genKeyPair
	 * @param filePath
	 * @return void
	 */
	public static void genKeyPair(String filePath) {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		try {
			// 得到公钥字符串
			String publicKeyString = Base64Util.encode(publicKey.getEncoded());
			// 得到私钥字符串
			String privateKeyString = Base64Util
					.encode(privateKey.getEncoded());

			// 将密钥对写入到文件
			File file = FileUtil.create(filePath + "/publicKey.keystore", true);
			FileWriter pubfw = new FileWriter(file);

			File file2 = FileUtil.create(filePath + "/privateKey.keystore",
					true);
			FileWriter prifw = new FileWriter(file2);

			BufferedWriter pubbw = new BufferedWriter(pubfw);
			BufferedWriter pribw = new BufferedWriter(prifw);
			pubbw.write(publicKeyString);
			pribw.write(privateKeyString);

			pubbw.flush();
			pubbw.close();
			pubfw.close();
			pribw.flush();
			pribw.close();
			prifw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用模和指数还原RSA私钥<br>
	 * 使用N、d值还原私钥
	 * <p>
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            私钥模(hex)
	 * @param privateExponent
	 *            私钥指数 (hex)
	 * @param radix
	 *            基数(10,16)
	 * @return PrivateKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey loadPrivateKey(String modulus,
			String privateExponent, int radix) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus, radix);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent,
				radix);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 使用Base64值还原私钥
	 * 
	 * @param privateKeyBase64
	 *            Base64编码私钥
	 * @return PrivateKey 私钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey loadPrivateKey(String privateKeyBase64)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return loadPrivateKey(Base64Util.decode(privateKeyBase64));
	}

	/**
	 * 使用byte[]值还原私钥
	 * 
	 * @param privateKey
	 *            私钥数据
	 * @return PrivateKey 私钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey loadPrivateKey(byte[] privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);
		return priKey;
	}

	/**
	 * 使用模和指数还原RSA公钥 <br>
	 * 使用N、e值还原公钥
	 * <p>
	 * 注意： 【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/
	 * None/NoPadding】
	 * 
	 * @param modulus
	 *            公钥模(hex)
	 * @param publicExponent
	 *            公钥指数(hex)
	 * @param radix
	 *            基数(10,16)
	 * @return PublicKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey loadPublicKey(String modulus,
			String publicExponent, int radix) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus, radix);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent, radix);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 使用Base64值还原公钥
	 * 
	 * @param publicKeyBase64
	 *            Base64编码公钥
	 * @return PublicKey 公钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @return PublicKey
	 */
	public static PublicKey loadPublicKey(String publicKeyBase64)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return loadPublicKey(Base64Util.decode(publicKeyBase64));
	}

	/**
	 * 使用byte[]值还原公钥
	 * 
	 * @param publicKey
	 *            公钥数据
	 * @return PublicKey 公钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey loadPublicKey(byte[] publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		return pubKey;
	}
	

	// ---------------------------------------------

	/**
	 * 私钥数据, 加/解密
	 * 
	 * @param privateKeyBytes
	 *            私钥数据
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] privateKeyCrypt(byte[] privateKeyBytes,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		PrivateKey privateKey = loadPrivateKey(privateKeyBytes);
		return privateKeyCrypt(privateKey, algorithm, encrypt, data);
	}

	/**
	 * Base64编码私钥, 加/解密
	 * 
	 * @param privateKeyBase64
	 *            Base64编码私钥
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] privateKeyCrypt(String privateKeyBase64,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		PrivateKey privateKey = loadPrivateKey(privateKeyBase64);
		return privateKeyCrypt(privateKey, algorithm, encrypt, data);
	}

	/**
	 * 模(N)和指数(d)编码私钥, 加/解密
	 * 
	 * @param modulus
	 *            私钥模(hex)
	 * @param privateExponent
	 *            私钥指数 (hex)
	 * @param radix
	 *            基数(10,16)
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] privateKeyCrypt(String modulus,
			String privateExponent, int radix, AlgorithmEnum algorithm,
			boolean encrypt, byte[] data) throws Exception {
		PrivateKey privateKey = loadPrivateKey(modulus, privateExponent, radix);
		return privateKeyCrypt(privateKey, algorithm, encrypt, data);
	}

	/**
	 * 私钥, 加/解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] privateKeyCrypt(PrivateKey privateKey,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm.getCode());
		if (encrypt) {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		}
		return cipher.doFinal(data);
	}

	/**
	 * 公钥数据, 加/解密
	 * 
	 * @param publicKeyBytes
	 *            公钥数据
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] publicKeyCrypt(byte[] publicKeyBytes,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		PublicKey puboicKey = loadPublicKey(publicKeyBytes);
		return publicKeyCrypt(puboicKey, algorithm, encrypt, data);
	}

	/**
	 * Base64编码公钥, 加/解密
	 * 
	 * @param publicKeyBase64
	 *            Base64编码公钥
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] publicKeyCrypt(String publicKeyBase64,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		PublicKey puboicKey = loadPublicKey(publicKeyBase64);
		return publicKeyCrypt(puboicKey, algorithm, encrypt, data);
	}

	/**
	 * 模(N)和指数(d)编码公钥, 加/解密
	 * 
	 * @param modulus
	 *            公钥模(hex)
	 * @param publicExponent
	 *            公钥指数 (hex)
	 * @param radix
	 *            基数(10,16)
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] publicKeyCrypt(String modulus, String publicExponent,
			int radix, AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		PublicKey publicKey = loadPublicKey(modulus, publicExponent, radix);
		return publicKeyCrypt(publicKey, algorithm, encrypt, data);
	}

	/**
	 * 公钥, 加/解密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密模式,true:加密,false:解密
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] publicKeyCrypt(PublicKey publicKey,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm.getCode());
		if (encrypt) {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		}
		return cipher.doFinal(data);
	}

	/**
	 * 私钥加/解密操作
	 * 
	 * @param privateKey
	 *            私钥对象
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密
	 * @param data
	 *            数据
	 * @return byte[]
	 * @throws Exception
	 */
	@Deprecated
	private static byte[] privateKey(PrivateKey privateKey,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		// Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
		Cipher cipher = Cipher.getInstance(algorithm.getCode());
		int maxBlock = MAX_ENCRYPT_BLOCK;
		if (encrypt) {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} else {
			maxBlock = MAX_DECRYPT_BLOCK;
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		}

		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > maxBlock) {
				cache = cipher.doFinal(data, offSet, maxBlock);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * maxBlock;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * 公钥加/解密操作
	 * 
	 * @param publicKey
	 *            公钥对象
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加/解密
	 * @param data
	 *            数据
	 * @return byte[]
	 * @throws Exception
	 */
	@Deprecated
	private static byte[] publicKey(PublicKey publicKey,
			AlgorithmEnum algorithm, boolean encrypt, byte[] data)
			throws Exception {
		// Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
		Cipher cipher = Cipher.getInstance(algorithm.getCode());
		int maxBlock = MAX_ENCRYPT_BLOCK;
		if (encrypt) {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} else {
			maxBlock = MAX_DECRYPT_BLOCK;
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		}
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > maxBlock) {
				cache = cipher.doFinal(data, offSet, maxBlock);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * maxBlock;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * 使用KEY对明文进行加密
	 * 
	 * 
	 * @param key
	 *            Key
	 * @param algorithm
	 *            算法
	 * @param encrypt
	 *            加解密模式,true:加密,false:解密
	 * @param data
	 *            明文/密文数据
	 * @return byte[] 加密后的数据
	 */
	public static byte[] rsaCrypt(Key key, AlgorithmEnum algorithm,
			boolean encrypt, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm.getCode());
			if (encrypt) {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, key);
			}
			return cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA公钥加密
	 * 
	 * @param modulus
	 *            公钥模(hex)
	 * @param exponent
	 *            公钥指数 (hex)
	 * @param algorithm
	 *            算法
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] rsaEncrypt(String modulus, String exponent,
			AlgorithmEnum algorithm, byte[] data) throws Exception {
		PublicKey publicKey = loadPublicKey(modulus, exponent, 16);
		return rsaCrypt(publicKey, algorithm, true, data);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param modulus
	 *            私钥模(hex)
	 * @param exponent
	 *            私钥指数 (hex)
	 * @param algorithm
	 *            算法
	 * @param data
	 *            数据
	 * @return byte[] 计算后数据
	 * @throws Exception
	 */
	public static byte[] rsaDecrypt(String modulus, String exponent,
			AlgorithmEnum algorithm, byte[] data) throws Exception {
		try {
			PrivateKey privateKey = loadPrivateKey(modulus, exponent, 16);

			Cipher cipher = Cipher.getInstance(algorithm.getCode());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("no such algorithm : " + algorithm);
			throw e;
		} catch (InvalidKeyException e) {
			System.err.println("invalid key");
			throw e;
		} catch (InvalidKeySpecException e) {
			System.err.println("invalid key spec");
			throw e;
		} catch (NoSuchPaddingException e) {
			System.err.println("no such padding, algorithm : " + algorithm);
			throw e;
		} catch (BadPaddingException e) {
			System.err.println("bad padding, algorithm : " + algorithm);
			throw e;
		} catch (IllegalBlockSizeException e) {
			System.err.println("illegal block size");
			throw e;
		}
	}

	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param privateKeyBytes
	 *            私钥数据
	 * @param data
	 *            已加密数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] privateKeyBytes, byte[] data)
			throws Exception {
		// byte[] keyBytes = Base64Utils.decode(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
				privateKeyBytes);
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥匙对象
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		return sign(privateK, data);
	}

	public static byte[] sign(PrivateKey key, byte[] data) throws Exception {
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(key);
		signature.update(data);
		return signature.sign();
	}

	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param publicKeyBytes
	 *            公钥数据
	 * @param data
	 *            已加密数据
	 * @param sign
	 *            数字签名
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean verify(byte[] publicKeyBytes, byte[] data, byte[] sign)
			throws Exception {
		// byte[] keyBytes = Base64Utils.decode(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取公钥匙对象
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		// 验证签名是否正常
		return signature.verify(sign);
	}
	
	public static String getKeyString(Key key) {
		// String s = (new BASE64Encoder()).encode(key.getEncoded());
		String s = Base64Util.encode(key.getEncoded());
		return s;
	}
	
	
	/**
	 * RSA验签
	 * 
	 * @param txnInfo
	 * @param sign
	 * @param rsaModulus
	 * @param publicKeyExponent
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 */
	public static boolean verify(String txnInfo, byte[] sign,
			String rsaModulus, String publicKeyExponent)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidKeyException, SignatureException {
		int radix = 16;
		BigInteger bigIntModulus = new BigInteger(rsaModulus, radix);
		BigInteger bigIntPrivateExponent = new BigInteger(publicKeyExponent,
				radix);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		byte[] data = txnInfo.getBytes("UTF-8");

		return verify(data, sign, publicKey);
	}
	
	/**
	 * 用公钥进行验证签名
	 * 
	 * @param publicKey
	 * @param content
	 * @param signed
	 * @return boolean
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 */
	public static boolean verify(byte[] content, byte[] signed,
			PublicKey publicKey) throws NoSuchAlgorithmException,
			SignatureException, InvalidKeyException {
		java.security.Signature signature = java.security.Signature
				.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicKey);
		signature.update(content);
		// 验证签名是否正常
		if (signature.verify(signed)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * RSA签名
	 * 
	 * @param reqData
	 * @param rsaModulus
	 * @param privateKeyExponent
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 */
	public static byte[] sign(String reqData, String rsaModulus,
			String privateKeyExponent) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException {
		int radix = 16;
		BigInteger bigIntModulus = new BigInteger(rsaModulus, radix);
		BigInteger bigIntPrivateExponent = new BigInteger(privateKeyExponent,
				radix);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		byte[] data = reqData.getBytes("UTF-8");
		return sign(data, privateKey);
	}

	/**
	 * 用私钥进行签名
	 * 
	 * @param content
	 *            需签名数据
	 * @param privateKey
	 *            私钥
	 * @return byte[]
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] sign(byte[] content, PrivateKey privateKey)
			throws InvalidKeyException, SignatureException,
			NoSuchAlgorithmException {
		java.security.Signature signature = java.security.Signature
				.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateKey);
		signature.update(content);
		return signature.sign();
	}



	public static void main(String[] args) throws Exception {
		System.out.println("==========");

		/*genKeyPair("d:\\key");

		byte[] publicKey;
		byte[] privateKey;

		AlgorithmEnum algorithm = AlgorithmEnum.RSA_ECB_PKCS1;

		Map<String, Object> keyMap = RSAUtil.genKeyPair(1152);
		publicKey = RSAUtil.getPublicKey(keyMap);
		privateKey = RSAUtil.getPrivateKey(keyMap);

		System.err.println("公钥: \n\r" + BytesUtil.bytesToHex(publicKey));
		System.err.println("私钥： \n\r" + BytesUtil.bytesToHex(privateKey));

		System.err.println("公钥加密——私钥解密");
		String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
		System.out.println("加密前文字：\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtil.publicKeyCrypt(publicKey, algorithm, true,
				data);
		System.out.println("加密后文字：\n" + new String(encodedData));
		byte[] decodedData = RSAUtil.privateKeyCrypt(privateKey, algorithm,
				false, encodedData);
		String target = new String(decodedData);
		System.out.println("解密后文字: \n" + target);

		// ----------------------------------
		System.err.println();
		System.err.println("私钥加密——公钥解密");
		source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\n" + source);
		data = source.getBytes();

		encodedData = RSAUtil
				.privateKeyCrypt(privateKey, algorithm, true, data);
		System.out.println("加密后：\n" + new String(encodedData));
		decodedData = RSAUtil.publicKeyCrypt(publicKey, algorithm, false,
				encodedData);
		target = new String(decodedData);
		System.out.println("解密后: \n" + target);

		System.err.println("私钥签名——公钥验证签名");
		byte[] sign = RSAUtil.sign(privateKey, encodedData);
		System.out.println("签名:\n" + sign);
		boolean status = RSAUtil.verify(publicKey, encodedData, sign);
		System.out.println("验证结果:\n" + status);*/		
		
		//========================== 获取base64编码公钥串====================
		//base64公钥
		String key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx+yMLnBhVreFOITTNMosXs7tLVUvEqqvLJPaE+FhyouIUU5CZQnKGUV6zUhlUO9BAh22h2udExUgrKzvElrIcmUsIXyqXzTajOAnpBgikYY8J7tQretvk3Uj11Z0JaBz3pt6EXqcYxLQMu/cY0neX8BNYy1B81zrpjntsHbpBMhw1uI+BhjVQ9iXWxpL3bMeSW9H6R+/lztRvbGcnbVHrPKlkDxwwRBfmbAim5mrEovHS8hlwhynVPxWMZ8mlNU3TtWBe9AG7HQftFXeg1gKCv8Nd04EbFbm7NFv4OgPYDiM7pztH5vSuO+/sQZPkieb7xDK+aDrZZaeDIN8vU5ZBQIDAQAB"; 
		System.out.println(Base64.isBase64(key));
		  
		//转换成RSAPublicKey
		RSAPublicKey publicKey2=(RSAPublicKey)loadPublicKey(key);; //16进制模
		System.out.println(publicKey2.getModulus().toString(16)); //16进制指数
		System.out.println(publicKey2.getPublicExponent().toString(16));
		// 获取base64编码公钥串
		 System.out.println(getKeyString(publicKey2)); 

		
		
	}
}
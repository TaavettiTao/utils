package com.tww.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
* @ClassName: RSASignature 
* @Description: RSA签名、验签
* @author taoww
* @date 2017年3月3日 下午4:19:18 
*
 */
public class RSASignature {
	
	private static Logger logger = LoggerFactory
			.getLogger(RSASignature.class);

	/**
	 * 加密算法RSA
	 */
	public static final String RSA_ALGORITHM = "RSA";

	/**
	 * 签名算法(华为：SHA256WithRSA)
	 */
	public static final String SIGN_ALGORITHMS = "SHA256WithRSA";

	private static final String KEY_SIGNATURE = "sign";
	private static final String KEY_CHARSET = "charset";
	private static final String KEY_ALGORITHM = "signType";

	/**
	 * 生成签名
	 * <p>
	 * Generate signature.
	 * </p>
	 * 
	 * @param params
	 *            parameters, never be null
	 * @param modulusPrivate
	 *            模数
	 * @param exponentPrivate
	 *            指数
	 * @param radix
	 *            进制
	 * @return signature
	 * @throws UnsupportedEncodingException
	 *             if encounter illegal character encoding
	 * @see String#getBytes()
	 * @see #constructSignaturePayload(Map)
	 */
	public static byte[] genSign(Map<String, String> params,
			String modulusPrivate, String exponentPrivate, int radix)
			throws UnsupportedEncodingException {

		// 忽略sign_type和sign和charset参数
		params.remove(KEY_SIGNATURE);
		String charset = params.remove(KEY_CHARSET);
		if (null == charset) {
			charset = "UTF-8";
		}

		String payload = constructSignaturePayload(params);
		logger.info("接口协议签名字符串：{},modulusPrivate:{},exponentPrivate:{}",payload,modulusPrivate);
		logger.info("exponentPrivate:{}",exponentPrivate);

		byte[] source = payload.getBytes(charset);
		// System.out.println("接口协议生成签名数据:" + BytesUtil.bytesToHex(source));

		// 5. 将payload和key拼接后执行MD5算法，获取摘要
		return genSign(source, modulusPrivate, exponentPrivate, radix);
	}

	
	public static byte[] genSign(byte[] data, String modulusPrivate,
			String exponentPrivate, int radix) {
		try {
			PrivateKey privateKey = loadPrivateKey(modulusPrivate,
					exponentPrivate, radix);
			return sign(data, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 验证签名
	 * <p>
	 * Verify signature.
	 * </p>
	 * 
	 * @param params
	 *            parameters, never be null
	 * @param key
	 *            key, never be null
	 * @return true if OK, otherwise false
	 * @throws UnsupportedEncodingException
	 *             if encounter illegal character encoding
	 * @see String#getBytes(String)
	 * @see #generateSignature(Map, String, String, String)
	 */
	public static boolean verifySign(Map<String, String> params,
			String modulusHex, String exponentHex, int radix)
			throws UnsupportedEncodingException {
		
		System.out.println("modulusHex:"+modulusHex);
		
		String signature = params.get(KEY_SIGNATURE).toString();
		if (null == signature || signature.length() == 0) {
			// 1. 算法和签名非空
			return false;
		}

		// 2. 忽略sign_type和sign和charset参数
		Map<String, String> paramsForPayload = new HashMap<String, String>(
				params);
		paramsForPayload.remove(KEY_ALGORITHM);
		paramsForPayload.remove(KEY_SIGNATURE);
		paramsForPayload.remove(KEY_CHARSET);

		// 字符集
		String charset = params.get(KEY_CHARSET);
		if (null == charset) {
			charset = "UTF-8";
		}

		String payload = constructSignaturePayload(paramsForPayload);
		logger.info("验签数据：{}",payload);
		byte[] source = payload.getBytes(charset);
		
		byte[] bytes=Base64Util.decode(signature);
		logger.info("signnnnn:{}",StringUtil.byteArrayToHexString(bytes));
		
		/*boolean flag=verify(source,  hex2Bytes(signature), modulusHex, exponentHex,
				radix);*/
		boolean flag=verify(source, bytes, modulusHex, exponentHex,
				radix);
		logger.info("验签结果：{}",flag?"成功":"失败");
		return flag;
	}

	/**
	 * hex字符串转byte数组<br/>
	 * 2个hex转为一个byte
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] hex2Bytes1(String src) {
		byte[] res = new byte[src.length() / 2];
		char[] chs = src.toCharArray();
		int[] b = new int[2];

		for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
			for (int j = 0; j < 2; j++) {
				if (chs[i + j] >= '0' && chs[i + j] <= '9') {
					b[j] = (chs[i + j] - '0');
				} else if (chs[i + j] >= 'A' && chs[i + j] <= 'F') {
					b[j] = (chs[i + j] - 'A' + 10);
				} else if (chs[i + j] >= 'a' && chs[i + j] <= 'f') {
					b[j] = (chs[i + j] - 'a' + 10);
				}
			}
			b[0] = (b[0] & 0x0f) << 4;
			b[1] = (b[1] & 0x0f);
			res[c] = (byte) (b[0] | b[1]);
		}
		return res;
	}

	/**
	 * hex字符串转byte数组<br/>
	 * 2个hex转为一个byte
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] hex2Bytes(String src) {
		byte[] res = new byte[src.length() / 2];
		char[] chs = src.toCharArray();
		for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
			res[c] = (byte) (Integer.parseInt(new String(chs, i, 2), 16));
		}
		return res;
	}

	static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * byte数组转hex字符串<br/>
	 * 一个byte转为2个hex字符
	 * 
	 * @param src
	 * @return
	 */
	public static String bytes2Hex(byte[] src) {
		char[] res = new char[src.length * 2];
		for (int i = 0, j = 0; i < src.length; i++) {
			res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
			res[j++] = hexDigits[src[i] & 0x0f];
		}
		return new String(res);
	}

	public static boolean verify(byte[] content, byte[] signed,
			String modulusPublic, String exponentPublic, int radix) {
		try {
			PublicKey publicKey = loadPublicKey(modulusPublic, exponentPublic,
					radix);
			return verify(content, signed, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean verify(byte[] content, byte[] signed,
			PublicKey publicKey) throws NoSuchAlgorithmException,
			SignatureException, InvalidKeyException {
		java.security.Signature signature = java.security.Signature
				.getInstance(SIGN_ALGORITHMS);
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
	 * 构造签名payload
	 * <p>
	 * Construct signature payload.
	 * </p>
	 * 
	 * @param params
	 *            parameters, never be null
	 * @return payload
	 */
	public static String constructSignaturePayload(Map<String, String> params) {
		// 1. 前置条件 参数中未包含sign和sign_type这两个参数
		StringBuilder payloadBuilder = new StringBuilder();
		for (Map.Entry<String, String> pair : new TreeMap<String, String>(
				params).entrySet() /* 2. 按照参数名自然排序 */) {
			String value = pair.getValue();
			// 3. 忽略值为空的参数
			if (null == value /*|| value.length() == 0*/) {
				continue;
			}
			// 4. 构造一个类似请求字符串格式的内容 e.g foo=a&bar=b&baz=c
			payloadBuilder.append(pair.getKey());
			payloadBuilder.append('=');
			payloadBuilder.append(value);
			payloadBuilder.append('&');
		}
		// 移除末尾多余的&
		if (payloadBuilder.length() > 0) {
			payloadBuilder.deleteCharAt(payloadBuilder.length() - 1);
		}
		System.out.println("签名字符串:" + payloadBuilder.toString());
		return payloadBuilder.toString();
	}



	public static byte[] sign(byte[] content, String modulusHex,
			String exponentHex, int radix) {
		try {
			PrivateKey privateKey = loadPrivateKey(modulusHex, exponentHex,
					radix);
			return sign(content, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
				.getInstance(SIGN_ALGORITHMS);
		signature.initSign(privateKey);
		signature.update(content);
		return signature.sign();
	}

	/**
	 * 获取私钥对象
	 * @param modulus
	 *        模数
	 * @param privateExponent
	 *        指数
	 * @param radix
	 *        底数，如10进制、16进制等
	 * @return
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
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 获取公钥对象
	 * @param modulus
	 *        公钥模数
	 * @param publicExponent
	 *        指数
	 * @param radix
	 *        底数，如10进制、16进制等
	 * @return
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
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String algorithm = "MD5";
		String charset = "GBK";
		String key = "diy8kd3j80sxhikb9kdy3ldi3qplm3v8";

		Map<String, String> params = new HashMap<String, String>();
		params.put("service", "some_service");
		params.put("foo", "abc");
		params.put("bar", "");
		params.put("xxx", "yyy");
		params.put("zzz", "中文");
		
		
		// 注意：生成签名时sign和signType需要排除，此处为生成签名的演示
		// String s = genSign(params, algorithm, key, charset);
		// System.out.println("------Signature:" + s);
		// params.put(KEY_CHARSET, charset);
		// params.put(KEY_SIGNATURE, s);
		// params.put(KEY_ALGORITHM, algorithm);
		//
		// System.out.println("paramsMap=" + params);
		// System.out.println(verifySign(params, key));
	}
}

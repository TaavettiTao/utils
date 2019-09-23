package com.tww.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Signature {

	private static Logger logger = LoggerFactory.getLogger(MD5Signature.class);
	
	private static final String KEY_SIGNATURE = "sign";
	private static final String KEY_CHARSET = "charset";
	private static final String KEY_ALGORITHM = "signType";

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
	public static boolean verifySign(Map<String, String> params, String key)
			throws UnsupportedEncodingException {
		String signature = params.get(KEY_SIGNATURE).toString();
		String algorithm = params.get(KEY_ALGORITHM).toString();
		if (StringUtils.isBlank(algorithm) || StringUtils.isBlank(signature)) {
			// 1. 算法和签名非空
			return false;
		}

		
		Map<String, String> paramsForPayload = new HashMap<String, String>(
				params);
		// 2. 忽略sign_type和sign和charset参数
		paramsForPayload.remove(KEY_ALGORITHM);
		paramsForPayload.remove(KEY_SIGNATURE);
		paramsForPayload.remove(KEY_CHARSET);

		// 3. 因为MD5只是哈希算法，所以只要再次正向生成一次做对比就行
		String expectedSignature = genSign(paramsForPayload, algorithm, key,
				"UTF-8");
		logger.info("验签-生成签名：{}",expectedSignature);

		return expectedSignature.equalsIgnoreCase(signature);
	}

	/**
	 * 生成签名.
	 * <p>
	 * Generate signature.
	 * </p>
	 * 
	 * @param params
	 *            parameters, never be null
	 * @param algorithm
	 *            algorithm
	 * @param key
	 *            key
	 * @param charset
	 *            character encoding
	 * @return signature
	 * @throws UnsupportedEncodingException
	 *             if encounter illegal character encoding
	 * @see String#getBytes()
	 * @see #constructSignaturePayload(Map)
	 * @see DigestUtils#md5Hex(byte[])
	 */
	public static String genSign(Map<String, String> params, String algorithm,
			String key, String charset) throws UnsupportedEncodingException {
		if (!"MD5".equals(algorithm.toUpperCase())) {
			throw new IllegalStateException("unsupported signature algorithm ["
					+ algorithm + "]");
		}

		// 忽略sign_type和sign和charset参数
		params.remove(KEY_ALGORITHM);
		params.remove(KEY_SIGNATURE);
		params.remove(KEY_CHARSET);

		String payload = constructSignaturePayload(params);
		// System.out.println("接口协议生成签名串:" + payload);
        payload=payload + "&key=" + key;
        logger.info("待签名数据：{}",payload);

		byte[] source = (payload).getBytes(charset);
		// System.out.println("接口协议生成签名数据:" + BytesUtil.bytesToHex(source));

		// 5. 将payload和key拼接后执行MD5算法，获取摘要,并转换成大写
		return DigestUtils.md5Hex(source).toUpperCase();
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
			if (StringUtils.isEmpty(value)) {
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
		return payloadBuilder.toString();
	}
	
	public static  String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		/** 支付通渠道：微信公众号支付-银联 */
		/*Map<String, String> reqData = new HashMap<String, String>();
		//订单号:每个商户唯一
		reqData.put("txn_no", "201909201568945680");
		//商户号:1.2接口录入
		reqData.put("mcht_no", "M20190730000106063");
		//业务类型 (固定值): 3005-微信公众号支付 
		reqData.put("business_type", "3005");
		//交易金额(必传):整数 ,单位分 
		reqData.put("txn_amt", "1");
		//订单标题((必传)
		reqData.put("subject","消费");
		//商品描述(非必传)
		reqData.put("body","消费描述");
		//附加信息(非必传)：商户自定义，异步通知原样返回，异步通知接口需要校验
		reqData.put("attach","附加消息");
		//用户直接取消和失败都重定向fail_url，流程结束；用户支付，成功了，重定向success_url；
		//取消支付跳转网址（非必传）:取消支付时调整到商户网页	
		reqData.put("cancel_url","http://dwispp.51dianwo.com/ticket/test/fail");
        //支付成功后前端跳转到商户网页（不可作为交易成功判断）（非必传）	
		reqData.put("success_url","http://dwispp.51dianwo.com/ticket/test/success");
		reqData.put("notify_url","http://dwispp.51dianwo.com/ticket/test/codePublicNotify");
        //签名方式(必传)：目前只支持MD5
		reqData.put("sign_type","MD5");	
		//随机字符串(必传)：随机字符串，防止MD5密钥被猜到 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
		reqData.put("nonce_str","tcct2667r8vfy7z7ut1k57wmee1s0mor");			
		String s = genSign(reqData, "MD5", "c7946cd3389dfac88b2ea50c2f9aab06", "UTF-8");
		System.out.println("------Signature:" + s);*/
		
		String payload="attach=附加消息&body=测试&business_type=3005&cancel_url=https://dwispp.51dianwo.com/ticket/test/fail&mcht_no=M20190730000106063&nonce_str=47e0ff9611fb43cc98cbe0afc530939f&notify_url=http://devaggregation/v1/aggregation/payexpress/notify&sign_type=MD5&subject=测试&success_url=https://dwispp.51dianwo.com/ticket/test/success&txn_amt=100&txn_no=2019092010544400000000002095&key=c7946cd3389dfac88b2ea50c2f9aab06";

		byte[] source = (payload).getBytes("UTF-8");
		// System.out.println("接口协议生成签名数据:" + BytesUtil.bytesToHex(source));

		// 5. 将payload和key拼接后执行MD5算法，获取摘要,并转换成大写
		System.out.println(DigestUtils.md5Hex(source).toUpperCase());
			}
}

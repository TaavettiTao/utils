/**
 * Copyright Shanghai COS Software Co., Ltd.
 * All right reserved
 * Base64Util.java
 * Created on 2014
 */

package com.tww.utils;

/**
 * 实现base64的编码和解码
 * <p>
 * 不依赖任何类库实现<br>
 * 
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2014
 * @author Platform Development Group
 * @version 1.0
 * @Create: 2014-6-26 下午03:02:40
 * 
 * @Modification History
 * @Date Author Version Description
 * @----------------------------------------------------
 * @2014-6-26 yangb 1.0 create
 */
public class Base64Util {

	private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	private static int[] toInt = new int[128];

	static {
		for (int i = 0; i < ALPHABET.length; i++) {
			toInt[ALPHABET[i]] = i;
		}
	}

	public Base64Util() {
	}

	/**
	 * Base64编码
	 * Translates the specified byte array into Base64 string.
	 * 
	 * @param buf
	 *            the byte array (not null)
	 * @return the translated Base64 string (not null)
	 */
	public static String encode(byte[] buf) {
		int size = buf.length;
		char[] ar = new char[((size + 2) / 3) * 4];
		int a = 0;
		int i = 0;
		while (i < size) {
			byte b0 = buf[i++];
			byte b1 = (i < size) ? buf[i++] : 0;
			byte b2 = (i < size) ? buf[i++] : 0;

			int mask = 0x3F;
			ar[a++] = ALPHABET[(b0 >> 2) & mask];
			ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
			ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
			ar[a++] = ALPHABET[b2 & mask];
		}
		switch (size % 3) {
		case 1:
			ar[--a] = '=';
		case 2:
			ar[--a] = '=';
		}
		return new String(ar);
	}

	/**
	 * Base64解码
	 * Translates the specified Base64 string into a byte array.
	 * 
	 * @param s
	 *            the Base64 string (not null)
	 * @return the byte array (not null)
	 */
	public static byte[] decode(String s) {
		int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
		byte[] buffer = new byte[s.length() * 3 / 4 - delta];
		int mask = 0xFF;
		int index = 0;
		for (int i = 0; i < s.length(); i += 4) {
			int c0 = toInt[s.charAt(i)];
			int c1 = toInt[s.charAt(i + 1)];
			buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c2 = toInt[s.charAt(i + 2)];
			buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c3 = toInt[s.charAt(i + 3)];
			buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
		}
		return buffer;
	}

	public static void main(String[] args) {
		System.out.println("-------------Base64Util main ----------");
		System.out.println(encode("liao".getBytes()));
		System.out.println(new String(decode(encode("123456".getBytes()))));

		String str = "liao743321greAFEE3423423er";
		String temp = encode(str.getBytes());
		System.out.println(str.length() + "-----" + temp.length());
		System.out.println("-----:" + temp);
		System.out.println(new String(decode(temp)));
	}
}
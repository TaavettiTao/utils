/**
 * Copyright Shanghai COS Software Co., Ltd.
 * All right reserved
 */
package com.tww.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * 字节数组转换工具类<br>
 * 
 * <br>
 * 
 * @Description:
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2011
 * 
 * @author Platform Development Group
 * @version 1.0
 * 
 * @Modification History
 * @----------------------------------------------------
 * @2012-10-10 1.0 create
 * 
 * 
 */
public class BytesUtil {

	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	/**
	 * 工具类不可实例化
	 */
	private BytesUtil() {

	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0F;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0F;
		return (c - '0') & 0x0F;
	}

	/**
	 * 对数组做异或
	 * 
	 * @param src
	 *            数组1
	 * @param srcPos
	 *            数组1偏移
	 * @param mask
	 *            数组2
	 * @param maskPos
	 *            数组2偏移
	 * @param length
	 *            需做异或的长度
	 * @return byte[]
	 */
	public static byte[] xor(byte[] src, int srcPos, byte[] mask, int maskPos,
			int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) (src[srcPos + i] ^ mask[maskPos + i]);
		}
		return result;
	}

	/**
	 * 对数组做异或
	 * <p>
	 * 返回msak长度的异或后数组
	 * 
	 * @param src
	 * @param mask
	 * @return byte[]
	 */
	public static byte[] xor(byte[] src, byte[] mask) {
		if (null == src || null == mask) {
			throw new IllegalArgumentException("异或因子有空");
		}
		if (src.length != mask.length) {
			throw new IllegalArgumentException("异或因子长度不一致");
		}
		return xor(src, 0, mask, 0, mask.length);
	}

	/**
	 * 对HEX型字符串做异或
	 * 
	 * @param src
	 * @param mask
	 * @return
	 * @return byte[]
	 * @throws
	 */
	public static byte[] xor(String src, String mask) {
		return xor(BytesUtil.hexToBytes(src), BytesUtil.hexToBytes(mask));
	}

	/**
	 * 字节内容转换为LV(长度+数据)格式
	 * <p>
	 * 
	 * @param len
	 *            长度字节数
	 * @param data
	 *            数据
	 * @return
	 * @return byte[]
	 * @throws
	 */
	public static byte[] toLV(int lLen, byte[] data) {
		byte[] result = new byte[lLen + data.length];
		byte[] LBytes = toBytes(data.length, lLen);
		System.arraycopy(LBytes, 0, result, 0, lLen);
		System.arraycopy(data, 0, result, lLen, data.length);
		return result;
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 *            byte[]
	 * @param radix
	 *            基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，
	 *            超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * 字节内容转换为二进制数据0或1
	 * <p>
	 * bit
	 * 
	 * @param byte
	 * @return String 00100111
	 */
	public static String byteToBits(byte b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 8; i++)
			buf.append((int) (b >> (8 - (i + 1)) & 0x1));
		return buf.toString();
	}

	/**
	 * 字节数组内容转换为二进制数据0或1
	 * 
	 * @param bytes
	 * @return String 00100111...
	 */
	public static String byteToBits(byte[] bytes) {
		StringBuffer buf = new StringBuffer();
		for (byte b : bytes)
			for (int i = 0; i < 8; i++)
				buf.append((int) (b >> (8 - (i + 1)) & 0x0001));
		return buf.toString();
	}

	/**
	 * Bit转成Byte
	 * <p>
	 * 参数长度为4or8字符
	 * 
	 * @param byteStr
	 * @return
	 */
	public static byte bitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

	/**
	 * 功能：将整数值转换为字节。
	 * <p>
	 * Java 总是当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值<br>
	 * 
	 * @param value
	 *            要转换的值
	 * @return byte
	 */
	public static byte toByte(int value) {
		return (byte) (value & 0xFF);
	}

	/**
	 * 功能：将字节转换为整数值。
	 * <p>
	 * 保持最低字节中各个位不变，3个高字节全部用0填充<br>
	 * 也就是字节的无符号值,0~~255<br>
	 * 
	 * @param value
	 * @return
	 */
	public static int toInt(byte value) {
		return (value & 0xFF);
	}

	/**
	 * 功能：将数值转换为指定长度的数组。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的顺序。<br>
	 * 
	 * 转换出1~4个字节的数组<br>
	 * 最多只有转换出前4个字节的数组<br>
	 * 
	 * @param iSource
	 *            要转换的值
	 * @param iArrayLen
	 *            转换成数组的长度
	 * @return byte[]
	 */
	public static byte[] toBytes(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			// 从数组最后开始
			bLocalArr[iArrayLen - 1 - i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * 功能：将数值转换为指定长度的数组。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的顺序。<br>
	 * 
	 * 转换出1~8个字节的数组<br>
	 * 最多只有转换出前8个字节的数组<br>
	 * 
	 * @param iSource
	 *            要转换的值
	 * @param iArrayLen
	 *            转换成数组的长度
	 * @return byte[]
	 */
	public static byte[] toBytes(long iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 8) && (i < iArrayLen); i++) {
			// 从数组最后开始
			bLocalArr[iArrayLen - 1 - i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * 功能：将数值转换为指定长度的数组。
	 * <p>
	 * 本方法适用于(低位在前，Little-endian)的顺序。<br>
	 * 最多只有转换出4个字节的数组<br>
	 * 
	 * @param iSource
	 *            要转换的值
	 * @param iArrayLen
	 *            转换成数组的长度
	 * @return byte[]
	 */
	public static byte[] toBytesLittle(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			// 从数组前面开始
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @return int
	 */
	public static int toInt(byte[] bArray) {
		int iOutcome = 0;
		int len = bArray.length - 1;
		for (int i = 0; i < bArray.length; i++) {
			// 从后往前,以上两种写法结果一样
			// iOutcome += (bArray[len - i] & 0xFF) << (8 * i);
			iOutcome |= (bArray[len - i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(低位在前，Little-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @return int
	 */
	public static int toIntLittle(byte[] bArray) {
		int iOutcome = 0;
		for (int i = 0; i < bArray.length; i++) {
			// 从前往后
			iOutcome += (bArray[i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @param offset
	 *            起始偏移量
	 * @param len
	 *            要转换的长度
	 * @return
	 */
	public static int toInt(byte[] bArray, int offset, int len) {
		int lastIndex = offset + len - 1;
		int iOutcome = 0;
		for (int i = 0; i < len; i++) {
			// 从后往前
			iOutcome += (bArray[lastIndex - i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @return long
	 */
	public static long toLong(byte[] bArray) {
		long iOutcome = 0;
		int len = bArray.length - 1;
		for (int i = 0; i < bArray.length; i++) {
			// 从后往前,以上两种写法结果一样
			// iOutcome += (bArray[len - i] & 0xFF) << (8 * i);
			iOutcome |= (bArray[len - i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(高位在前，Big-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @param offset
	 *            起始偏移量
	 * 
	 * @return int
	 */
	public static int toInt(byte[] bArray, int offset) {
		int lastIndex = bArray.length - 1;
		int iOutcome = 0;
		for (int i = 0; i < bArray.length - offset; i++) {
			// 从后往前
			iOutcome += (bArray[lastIndex - i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 功能：将数组转换整数。
	 * <p>
	 * 本方法适用于(低位在前，Little-endian)的数组顺序。<br>
	 * <br>
	 * 
	 * @param bArray
	 *            要转换的数组
	 * @param offset
	 *            起始偏移量
	 * @param len
	 *            要转换的长度
	 */
	public static int toIntLittle(byte[] bArray, int offset, int len) {
		int iOutcome = 0;
		for (int i = 0; i < len; i++) {
			// 从前往后
			iOutcome += (bArray[offset + i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	// ------------Hex~Bytes---------------

	/**
	 * 功能：字节数组到十六进制字符串转换
	 * 
	 * @param bytes
	 * @return string
	 */
	public static String bytesToHex(byte[] bytes) {
		if (bytes == null)
			return "";
		byte[] buff = new byte[2 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			buff[2 * i] = hex[(bytes[i] >> 4) & 0x0F];
			buff[2 * i + 1] = hex[bytes[i] & 0x0F];
		}
		return new String(buff);
	}

	/**
	 * 功能：字节数组到十六进制字符串转换
	 * 
	 * @param data
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public static String bytesToHex(byte data[], int offset) {
		if (data == null)
			return null;
		byte[] buff = new byte[2 * (data.length - offset)];
		for (int i = offset, bufIndex = 0; i < data.length; i++, bufIndex++) {
			buff[2 * bufIndex] = hex[(data[i] >> 4) & 0x0f];
			buff[2 * bufIndex + 1] = hex[data[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 功能：字节数组到十六进制字符串转换
	 * 
	 * @param bytes
	 * @param offset
	 *            偏移量
	 * @param size
	 *            要转换的字节量
	 * @return
	 */
	public static String bytesToHex(byte bytes[], int offset, int size) {
		if (bytes == null)
			return null;
		byte[] buff = new byte[2 * size];
		for (int i = offset, bufIndex = 0; i < offset + size; i++, bufIndex++) {
			buff[2 * bufIndex] = hex[(bytes[i] >> 4) & 0x0f];
			buff[2 * bufIndex + 1] = hex[bytes[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 功能： 从十六进制字符串到字节数组转换
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexToBytes(String hexStr) {
		if (null == hexStr) {
			return null;
		}
		if (hexStr.trim().length() == 0) {
			return new byte[0];
		}
		byte[] b = new byte[hexStr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexStr.charAt(j++);
			char c1 = hexStr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * 功能： 从十六进制字符串到字节数组转换
	 * 
	 * @param hexStr
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public static byte[] hexToBytes(String hexStr, int offset) {
		if (null == hexStr || hexStr.trim().length() == 0) {
			return new byte[0];
		}
		byte[] result = new byte[(hexStr.length() - offset) / 2];
		int j = offset;
		for (int i = 0; i < result.length; i++) {
			char c0 = hexStr.charAt(j++);
			char c1 = hexStr.charAt(j++);
			result[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return result;
	}

	/**
	 * 功能： 从十六进制字符串到字节数组转换
	 * 
	 * @param hexstr
	 * @param offset
	 *            偏移量
	 * @param size
	 *            要转换的字符量(2的整倍数)
	 * @return byte[]
	 */
	public static byte[] hexToBytes(String hexstr, int offset, int size) {
		byte[] result = new byte[size / 2];
		int j = offset;
		for (int i = 0; i < result.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			result[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return result;
	}

	/**
	 * 功能：字节转换为十六进制字符串
	 * 
	 * @param b
	 * @return String
	 */
	public static String byteToHex(byte b) {
		byte[] buff = new byte[2];
		buff[0] = hex[(b >> 4) & 0x0f];
		buff[1] = hex[b & 0x0f];
		return new String(buff);
	}

	/**
	 * 功能： 两个十六进制字符串转换成字节
	 * 
	 * @param hexstr
	 *            2个Hex字符
	 * @return byte
	 */
	public static byte hexToByte(String hexstr) {
		char c0 = hexstr.charAt(0);
		char c1 = hexstr.charAt(1);
		return (byte) ((parse(c0) << 4) | parse(c1));
	}

	/**
	 * 比较两个数组内容是否一样
	 * 
	 * @param bytes1
	 *            数组1
	 * @param bytes2
	 *            数组2
	 * @return boolean
	 */
	public static boolean compareBytes(byte[] bytes1, byte[] bytes2) {
		if ((bytes1 == null) || (bytes2 == null))
			return false;
		if (bytes1.length != bytes2.length)
			return false;
		for (int i = 0; i < bytes1.length; i++)
			if (bytes1[i] != bytes2[i])
				return false;
		return true;
	}
	
	/**
	 * 字节数组转换为ASCII字符串
	 * @param bytes 字节数组
	 * @param offset 初始为值
	 * @param dateLen 数据长度
	 * @return
	 */
	public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
		if ((bytes == null) || (bytes.length == 0) || (offset < 0)
				|| (dateLen <= 0)) {
			return null;
		}
		if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
			return null;
		}

		String asciiStr = null;
		byte[] data = new byte[dateLen];
		System.arraycopy(bytes, offset, data, 0, dateLen);
		try {
			asciiStr = new String(data, "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
		}
		return asciiStr;
	}

	/**
	 * 字节数组转换为ASCII字符串
	 * @param bytes 字节数组
	 * @param dateLen 转换长度
	 * @return
	 */
	public static String bytesToAscii(byte[] bytes, int dateLen) {
		return bytesToAscii(bytes, 0, dateLen);
	}

	/**
	 * 字节数组转换为ASCII字符串
	 * @param bytes 字节数组
	 * @return
	 */
	public static String bytesToAscii(byte[] bytes) {
		return bytesToAscii(bytes, 0, bytes.length);
	}

	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();

		byte[] srcArray = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0xB2 };
		byte[] temp;
		int i;

		byte b = (byte) 0xA3;
		String s = byteToBits(b);
		System.out.println("--------byteToBits:" + s);
		System.out.println(b + "--------bitToByte:" + bitToByte(s));
		System.out.println("--------byteToBits:" + byteToBits(srcArray));

		System.out.println((byte) 300 + "--------toByte:" + toByte(300));
		System.out.println("--------toInt:" + toInt((byte) 0xFF));

		i = toInt(srcArray);
		System.out.println("--------toInt:" + i);
		temp = toBytes(i, 2);
		System.out.println(" --------toBytes:" + bytesToHex(temp));

		temp = toBytesLittle(i, 3);
		System.out.println("--------toBytes:" + bytesToHex(temp));

		i = toIntLittle(srcArray);
		System.out.println("--------toIntLittle:" + i);
		temp = toBytes(i, 2);
		System.out.println("--------toBytes:" + bytesToHex(temp));
		temp = toBytesLittle(i, 3);
		System.out.println("--------toBytes:" + bytesToHex(temp));

		i = toInt(srcArray, 2, 2);
		System.out.println(" --------toInt:" + i);

		i = toInt(srcArray, 2);
		System.out.println("--------toInt:" + i);

		// ---------------Hex--------------

		String src = "0102030405060708090A0B0C0D0E0F";
		String result;

		temp = hexToBytes(src);

		result = bytesToHex(temp);
		System.out.println("-Hex-" + src.equals(result));

		// 长度+数据
		temp = toLV(6, temp);
		result = bytesToHex(temp);
		System.out.println("-长度+数据:" + result);

		// 数据
		temp = hexToBytes(src, 6);
		result = bytesToHex(temp);
		System.out.println("SUB HEX:" + result);

		temp = hexToBytes(src);
		result = bytesToHex(temp, 8);
		System.out.println(src.substring(16) + "-Hex-sub-" + (result));

		result = bytesToHex(temp, 8, temp.length - 8);
		System.out.println(src.substring(16) + "-Hex-sub-" + (result));

		System.out.println(byteToHex((byte) 0xE2) + "---------"
				+ byteToHex(hexToByte("E2")));

		long end = System.currentTimeMillis();
		System.out.println(" time:" + ((end - begin) / 1000) + "s");
	}

}

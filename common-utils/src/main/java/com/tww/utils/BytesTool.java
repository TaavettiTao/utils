package com.tww.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;

/**
 * 字符串和字节数组的相互转换
 * <p>
 * <br>
 * 
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2015
 * @author Platform Development Group
 * @version 1.0
 * @Create: 2015-10-30 下午03:59:31
 * 
 * @Modification History
 * @Date Author Version Description
 * @----------------------------------------------------
 * @2015-10-30 yangb 1.0 create
 */
public class BytesTool {

	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0F;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0F;
		return (c - '0') & 0x0F;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2Bits(byte b) {
		int z = b;
		z |= 256;
		String str = Integer.toBinaryString(z);
		int len = str.length();
		return str.substring(len - 8, len);
	}

	/**
	 * 将二进制字符串转换回字节
	 * 
	 * @param bString
	 * @return byte
	 */
	public static byte bit2Byte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
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
	 * 整型转换成2字节数组
	 * 
	 * @param value
	 *            要转换的整型值
	 * @return
	 */
	public static byte[] short2Bytes(int value) {
		byte[] write = new byte[2];
		write[0] = (byte) ((value >>> 8) & 0xFF);
		write[1] = (byte) ((value >>> 0) & 0xFF);
		return write;
	}

	/**
	 * 整型转换成4字节数组
	 * 
	 * @param value
	 *            要转换的整型值
	 * @return
	 */
	public static byte[] int2Bytes(int value) {
		byte[] write = new byte[4];
		write[0] = (byte) ((value >>> 24) & 0xFF);
		write[1] = (byte) ((value >>> 16) & 0xFF);
		write[2] = (byte) ((value >>> 8) & 0xFF);
		write[3] = (byte) ((value >>> 0) & 0xFF);
		return write;
	}

	/**
	 * 整型转换成8字节数组
	 * 
	 * @param value
	 *            要转换的整型值
	 * @return
	 */
	public static byte[] long2Bytes(long value) {
		byte[] write = new byte[8];
		write[0] = (byte) ((value >>> 56) & 0xFF);
		write[1] = (byte) ((value >>> 48) & 0xFF);
		write[2] = (byte) ((value >>> 40) & 0xFF);
		write[3] = (byte) ((value >>> 32) & 0xFF);
		write[4] = (byte) ((value >>> 24) & 0xFF);
		write[5] = (byte) ((value >>> 16) & 0xFF);
		write[6] = (byte) ((value >>> 8) & 0xFF);
		write[7] = (byte) ((value >>> 0) & 0xFF);
		return write;
	}

	/**
	 * 字节数组转换成短整型
	 * 
	 * @param value
	 * @return
	 */
	public static short bytes2Short(byte[] value) {
		int s1 = (value[0] & 0xFF) << 8;
		int s2 = (value[1] & 0xFF) << 0;
		return (short) (s1 | s2);
	}

	/**
	 * 字节数组转换成整型
	 * 
	 * @param value
	 * @return
	 */
	public static int bytes2Int(byte[] value) {
		int result = 0;
		int len = Math.min(4, value.length);
		for (int i = 0; i < len; i++) {
			int shift = (len - 1 - i) * 8;
			result += (value[i] & 0xFF) << shift;
		}
		return result;
	}

	/**
	 * 字节数组转换成长整型
	 * 
	 * @param value
	 * @return
	 */
	public static long bytes2Long(byte[] value) {
		long L1 = (value[0] & 0xFF) << 56;
		long L2 = (value[1] & 0xFF) << 48;
		long L3 = (value[2] & 0xFF) << 40;
		long L4 = (value[3] & 0xFF) << 32;
		long L5 = (value[4] & 0xFF) << 24;
		long L6 = (value[5] & 0xFF) << 16;
		long L7 = (value[6] & 0xFF) << 8;
		long L8 = (value[7] & 0xFF) << 0;
		return (L1 | L2 | L3 | L4 | L5 | L6 | L7 | L8);
	}

	/**
	 * 从指定字节数组中拷贝部分数据
	 * 
	 * @param origin
	 * @param from
	 * @param to
	 * @return
	 */
	public static byte[] copyBytes(byte[] origin, int from, int to) {
		int len = to - from;
		if (len < 0 || origin.length - from <= 0) {
			throw new IllegalArgumentException("copyBytes->error arguments:to="
					+ to + ",from=" + from);
		}
		byte[] ret = new byte[len];
		if (len == 0)
			return ret;
		System.arraycopy(origin, from, ret, 0,
				Math.min(len, origin.length - from));
		return ret;
	}

	/**
	 * 重置字节数组的大小，然后把原内容复制到新的字节数组中
	 * 
	 * @param origin
	 * @param newSize
	 * @return
	 */
	public static byte[] resizeBytes(byte[] origin, int newSize) {
		if (newSize < 0) {
			throw new IllegalArgumentException("resizeBytes->newSize must >= 0");
		}
		byte[] ret = new byte[newSize];
		if (newSize == 0)
			return ret;
		System.arraycopy(origin, 0, ret, 0, Math.min(origin.length, newSize));
		return ret;
	}

	/**
	 * 读取输入流中字节,并保存到指定的字节数组中
	 * 
	 * @param is
	 * @param data
	 * @param off
	 * @param len
	 */
	public static void readData(InputStream is, byte data[], int off, int len) {
		int hasRead = 0;
		final int BUFFER = 1024;
		while (hasRead < len) {
			try {
				int remain = len - hasRead;
				int count = is.read(data, off + hasRead,
						remain > BUFFER ? BUFFER : remain);
				if (count < 0)
					throw new IOException("readData->read data error");
				hasRead += count;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：字节转换为十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		byte[] buff = new byte[2];
		buff[0] = hex[(b >> 4) & 0x0f];
		buff[1] = hex[b & 0x0f];
		return new String(buff);
	}

	/**
	 * byte数组转换为16进制字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
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
	 * @param bytes
	 * @param offset
	 *            偏移量
	 * @param size
	 *            要转换的字节量
	 * @return
	 */
	public static String bytes2Hex(byte bytes[], int offset, int size) {
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
	 * 
	 * 16进制转换为byte数组
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hex2Bytes(String hexStr) {
		byte[] bts = new byte[hexStr.length() / 2];
		for (int i = 0, j = 0; j < bts.length; j++) {
			bts[j] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
			i += 2;
		}
		return bts;
	}

	/**
	 * 功能： 从十六进制字符串到字节数组转换
	 * 
	 * @param hexstr
	 * @param offset
	 *            偏移量
	 * @param size
	 *            要转换的字符量(2的整倍数)
	 * @return
	 */
	public static byte[] hex2Bytes(String hexstr, int offset, int size) {
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
	
	public static byte[] toBytes(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			// 从数组最后开始
			bLocalArr[iArrayLen - 1 - i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * 合并字节数组
	 * <p>
	 * <br>
	 * 
	 * @param <T>
	 * @param array1
	 *            第一个数组
	 * @param array2
	 *            第二个数组
	 * @return 合并后数组
	 */
	public static <T> T append(T array1, T array2) {
		int len1 = Array.getLength(array1);
		int len2 = Array.getLength(array2);
		Class<?> classz = array1.getClass().getComponentType();
		Object dest = Array.newInstance(classz, len1 + len2);
		System.arraycopy(array1, 0, dest, 0, len1);
		System.arraycopy(array2, 0, dest, len1, len2);
		return (T) dest;
	}

	public static void main(String[] args) {
		byte[] bytes = short2Bytes(23258);

		String temp = byte2Bits((byte) 0x12);
		System.out.println("二进制：" + temp);
		System.out.println("二进制：" + bit2Byte(temp.substring(2)));

		System.out.println("进制：" + bytes2Int(bytes));

		System.out.println("将woaini转为不同进制的字符串：");
		System.out.println("可以转换的进制范围：" + Character.MIN_RADIX + "-"
				+ Character.MAX_RADIX);
		System.out.println("2进制：" + binary(bytes, 2));
		System.out.println("5进制：" + binary(bytes, 5));
		System.out.println("8进制：" + binary(bytes, 8));
		System.out.println("16进制：" + binary(bytes, 16));
		System.out.println("32进制：" + binary(bytes, 32));
		System.out.println("64进制：" + binary(bytes, 64));// 这个已经超出范围，超出范围后变为10进制显示

	}
}

package com.tww.utils.test;

import com.tww.utils.BytesUtil;

public class Test {
	public static void main(String[] args) {
		Integer i=36;
		System.out.println(Integer.toHexString(i));
		System.out.println(BytesUtil.byteToHex(i.byteValue()));
	}
}

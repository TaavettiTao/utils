package com.tww.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Bytes {

	public static Logger logger=Logger.getLogger(Bytes.class);
	
    public static byte[] getBCD4(int x) {
        if (x > 99999999 || x < 0) {
            throw new IllegalArgumentException("x must between 0 and 99999999,now x=" + x);
        }
        String s = "" + x;
        int loop = 8 - s.length();
        for (int i = 0; i < loop; i++) {
            s = "0" + s;
        }
        return StringUtil.hexStringToByteArray(s);
    }

    public static byte[] getBCD2(int x) {
        if (x > 999 || x < 0) {
            throw new IllegalArgumentException("x must between 0 and 999,now x=" + x);
        }
        byte[] result = new byte[2];
        result[0] = (byte) (x / 100);
        result[1] = (byte) (((x % 100) / 10 << 4) | (x % 10));
        return result;
    }

    public static byte getBCD1(int x) {
        if (x > 99 || x < 0) {
            throw new IllegalArgumentException("x must between 0 and 99,now x=" + x);
        }
        return (byte) (((x % 100) / 10 << 4) | (x % 10));
    }

    public static byte xorCheck(byte[] data) {
        byte result = 0;
        for (int i = 0; i < data.length; i++) {
            result ^= data[i];
        }
        return result;
    }

    public static int bcd2Int(byte[] data) {
        String numStr = StringUtil.byteArrayToHexString(data);
        return Integer.parseInt(numStr);
    }

    public static byte[] getLong(long x) {
        byte[] result = new byte[8];
        result[0] = (byte) ((x & 0xFF00000000000000L) >> 56);
        result[1] = (byte) ((x & 0xFF000000000000L) >> 48);
        result[2] = (byte) ((x & 0xFF0000000000L) >> 40);
        result[3] = (byte) ((x & 0xFF00000000L) >> 32);
        result[4] = (byte) ((x & 0xFF000000) >> 24);
        result[5] = (byte) ((x & 0xFF0000) >> 16);
        result[6] = (byte) ((x & 0xFF00) >> 8);
        result[7] = (byte) (x & 0xFF);
        return result;
    }

    public static byte[] getInt(int x) {
        byte[] result = new byte[4];
        result[0] = (byte) ((x & 0xFF000000) >> 24);
        result[1] = (byte) ((x & 0xFF0000) >> 16);
        result[2] = (byte) ((x & 0xFF00) >> 8);
        result[3] = (byte) (x & 0xFF);
        return result;
    }

    
    public static byte[] getShort(short x) {
        byte[] result = new byte[2];
        result[0] = (byte) ((x & 0xFF00) >> 8);
        result[1] = (byte) (x & 0xFF);
        return result;
    }

    public static int parseInt(byte[] spaceData) {
        String s = StringUtil.byteArrayToHexString(spaceData);
        int i = Integer.parseInt(s, 16);
        return i;
    }

    public static short parseShort(byte[] spaceData) throws IOException {
    	if(spaceData.length!=2)throw new IOException("长度必须为2");
        String s = StringUtil.byteArrayToHexString(spaceData);
        short i = Short.parseShort(s, 16);
        return i;
    }
    
    public static byte[] append(byte[] byteArray,byte[] data){
        byte[] dest = new byte[byteArray.length+data.length];
        System.arraycopy(byteArray, 0, dest, 0, byteArray.length);
    	System.arraycopy(data, 0, dest, byteArray.length, data.length); 
    	return dest;   			
    }
    
    public static int parseInt(byte[] spaceData, int index, int offset) {
        byte[] result = new byte[offset];
        System.arraycopy(spaceData, index, result, 0, offset);
        return parseInt(result);
    }

    public static long parseLong(byte[] spaceData) {
        String s = StringUtil.byteArrayToHexString(spaceData);
        long L = Long.parseLong(s, 16);
       
        return L;
    }

    public static byte[] getShortNaturalInteger(int x) {
        byte[] result = new byte[2];
        result[0] = (byte) ((x & 0xFF00) >> 8);
        result[1] = (byte) (x & 0xFF);
        return result;
    }

    public static byte[] getLongCounter(long x) {
        byte[] result = new byte[5];
        result[0] = (byte) ((x & 0xFF00000000L) >> 32);
        result[1] = (byte) ((x & 0xFF000000) >> 24);
        result[2] = (byte) ((x & 0xFF0000) >> 16);
        result[3] = (byte) ((x & 0xFF00) >> 8);
        result[4] = (byte) (x & 0xFF);
        return result;
    }

    public static byte[] getAID3Byte(int aid) {
        byte[] data = new byte[3];
        data[0] = (byte) ((aid & 0xFF0000) >> 16);
        data[1] = (byte) ((aid & 0xFF00) >> 8);
        data[2] = (byte) (aid & 0xFF);
        return data;
    }

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                            '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte to hex digit
     */
    public static String byte2hex(byte b){
    	StringBuffer buf=new StringBuffer();
    	byte2hex(b,  buf);
    	return buf.toString();
    }
    
    /*
     * Converts a byte array to hex string
     */
    public static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
             byte2hex(block[i], buf);
           /*  if (i < len-1) {
                 buf.append(":");
             }*/
        }
        return buf.toString();
    }
    
    /**
     * 将8字节数据转换为64位二进制
     * @param hexByteArray 8字节
     * @return 二进制
     */
    public static String toBinaryString(byte[] hexByteArray){	
    	logger.info("parser 位图:"+StringUtil.byteArrayToHexString(hexByteArray));
		BigInteger bi=new BigInteger(hexByteArray);
		logger.info("parser 位图:"+bi.longValue());
		String bitString=Long.toBinaryString(bi.longValue());
		String bits="0000000000000000000000000000000000000000000000000000000000000000";
		if(bitString.length()<64){	
			int fillLen =bits.length()-bitString.length();
			bitString=bits.substring(0, fillLen)+bitString;				
		}
		logger.info("parser 主二进制编码:"+bitString);
		return bitString;
    	
    }
    
    
    public static String binaryString2hexString(String bString)  
    {  
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)  
            return null;  
        StringBuffer tmp = new StringBuffer();  
        int iTmp = 0;  
        for (int i = 0; i < bString.length(); i += 4)  
        {  
            iTmp = 0;  
            for (int j = 0; j < 4; j++)  
            {  
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);  
            }  
            tmp.append(Integer.toHexString(iTmp));  
        }  
        return tmp.toString();  
    }  
    
    /**
     * 主位图（2-64）
     * 将二进制位图转换为16进制数据
     * @param bitmap
     * @return
     */
    public static byte[] bitMapToByteArray(String secondaryFlag,List<Integer> bitMap){
    	StringBuffer bits=new StringBuffer(secondaryFlag+"000000000000000000000000000000000000000000000000000000000000000");
    	
    	for(int i=0;i<bits.length();i++){
    		if(bitMap.contains(i+1)){
    			bits.setCharAt(i, '1');
    		}		
    	}
    	logger.info("packet 主位图二进制编码="+bits.toString());
    	String s=binaryString2hexString(bits.toString());
    	return StringUtil.hexStringToByteArray(s);
    }
    
    /**
     * 副位图（66-128），65域不存在
     * 将二进制位图转换为16进制数据
     * @param bitmap
     * @return
     */
    public static byte[] secondaryBitmapToByteArray(String secondaryFlag,List<Integer> bitMap){
    	StringBuffer bits=new StringBuffer(secondaryFlag+"000000000000000000000000000000000000000000000000000000000000000");
    	
    	for(int i=0;i<=bits.length();i++){
    		if(bitMap.contains(65+i)){
    			bits.setCharAt(i, '1');
    		}		
    	}
    	logger.info("packet 副位图二进制编码="+bits.toString());
    	String s=binaryString2hexString(bits.toString());
    	return StringUtil.hexStringToByteArray(s);
    }
    
    
    public static boolean equals(byte[] one, byte[] two) throws NoSuchAlgorithmException {
        String s = StringUtil.byteArrayToHexString(MessageDigest.getInstance("MD5").digest(one));
        String s2 = StringUtil.byteArrayToHexString(MessageDigest.getInstance("MD5").digest(two));
        return s.equals(s2);
    }
    
    public static String intHexStringTobinaryString(String str) throws Exception{
    	if(str.length()!=8)throw new Exception("长度必须为8");
    	long i=parseLong(StringUtil.hexStringToByteArray(str));
    	String binary=Integer.toBinaryString((int)i);
    	String bits="00000000000000000000000000000000";
		if(binary.length()<32){	
			int fillLen =bits.length()-binary.length();
			binary=bits.substring(0, fillLen)+binary;				
		}
    	return binary;
    }
    
    public static List<Integer> getIntegerBitMap(String binary) throws Exception{
    	if(binary.length()!=32)throw new Exception("长度必须为32");
    	List<Integer> maps=new ArrayList<Integer>();
    	for(int i=0;i<binary.length();i++){
    		if(binary.charAt(i)=='1'){
    			maps.add(i+1);
    		}	
    	}
    	return maps;
    }
    
    /**
	 * 元转分
	 * @param amount
	 * @return
	 */
	public static String yuan2Fen(Integer amount){    
        String s= BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
        StringBuffer buffer=new StringBuffer("000000000000");
        buffer.replace(buffer.length()-s.length(), buffer.length(), s);
        return buffer.toString();
    }    
	
	/**
	 * 补足6位字节分
	 * @param fen
	 * @return
	 */
	public static String fillFen(Integer fen){
		String s = String.valueOf(fen);
		StringBuffer buffer=new StringBuffer("000000000000");
	    buffer.replace(buffer.length()-s.length(), buffer.length(), s);
	    return buffer.toString();
	}
	
	/**
	 * 分转元
	 * @param transAmount
	 * @return
	 */
	public static String fen2Yuan(String transAmount){
		String amount=new BigDecimal(transAmount).divide(new BigDecimal(100)).setScale(2).toString();
		return amount;
	}
	
	/**
	 * 分转元
	 * @param transAmount
	 * @return
	 */
	public static double fen2DoubleYuan(String transAmount){
		double amount=new BigDecimal(transAmount).divide(new BigDecimal(100)).setScale(2).doubleValue();
		return amount;
	}
	
	/**
	 * 如果分不足12位，则不足12位。
	 * 如1,补足之后为000000000001
	 * @param value
	 * @return
	 */
	public static String fillFen(String value){
		String init="000000000000";
		int fillLen=init.length()-value.length();
		return init.substring(0, fillLen)+value;
	}
	
	/**
	 * 整型转字节数组
	 * @param iSource
	 * @param iArrayLen
	 * @return
	 */
	public static byte[] toByteArr(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			// 从数组最后开始
			bLocalArr[iArrayLen - 1 - i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}
	
	/**
	 * 金额转大写
	 * @param n
	 * @return
	 */
	public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"},
                     {"", "拾", "佰", "仟"}};
 
        String head = n < 0? "负": "";
        n = Math.abs(n);
         
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";    
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    } 
	
    public static void main(String[] args) throws Exception{
    	System.out.println(StringUtil.byteArrayToHexString(getInt(0x0A)));
    	System.out.println(StringUtil.byteArrayToHexString(getShort((short)0x0A)));
    	System.out.println(StringUtil.byteArrayToHexString(getShortNaturalInteger(0x0A)));
    	System.out.println(StringUtil.byteArrayToHexString(getBCD2(0x0A)));
    	System.out.println(StringUtil.byteArrayToHexString(getBCD4(0x0A)));
    	System.out.println(StringUtil.byteArrayToHexString(getAID3Byte(0x0A)));
    	
    	
    }
    
    public static void getPrimary(String str){
    	int count=1;
    	String st = "";
    	for(int bitIndex=1;bitIndex<str.length();bitIndex++){  
    		count++;
	    	if(str.charAt(bitIndex)=='1'){    	
	    		st+=count+",";
	    	}
	    }
    	System.out.println("st "+st);
    }
  
}

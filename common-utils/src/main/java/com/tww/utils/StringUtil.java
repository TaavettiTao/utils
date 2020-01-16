/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tww.utils;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Vector;
/**
 * 
* @ClassName: StringUtil 
* @Description: 字符串工具类 
* @author taoww
* @date 2017年3月3日 下午4:25:19 
*
 */
public class StringUtil {

    public static final char[] LOGIC_CHARS = new char[]{'!', '^', '|', '&'};
    public static final char[] BRACE_CHARS = new char[]{'{', '}', '(', ')', ']', '['};
    public static final char[] COMPARE_CHARS = new char[]{'=', '<', '>'};
    public static final char[] MATH_CHARS = new char[]{'+', '-', '*', '/', '%'};
    public static final char[] SEPARATOR_CHARS = new char[]{',', ';', ':', '.', '\\', '/'};
    public static final char[] SPACE_CHARS = new char[]{' ', '\t', '\n', '\r', '\f'};
    public static final char[] INDEX_CHARS = new char[]{'@', '#'};
    public static final char[] QUOTE_CHARS = new char[]{'"', '\''};
    public static final char[] CONTROL_CHARS = new char[]{'\r', '\f'};
    public static final char[] INVALID_CHARS = new char[]{
        '{', '}', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
        '-', '+', '=', '\\', '\'', '|', ']', '[', '"', ':', ';',
        '?', '/', ',', '>', '<', '~', '`', ' ', '\t', '\n', '\r', '\f'
    };
    public static final char[] GSM_CHARS = new char[]{
        '\u0040', '\u00A3', '\u0024', '\u00A5', '\u00E8', '\u00E9', '\u00F9', '\u00EC',
        '\u00F2', '\u00E7', '\n', '\u00D8', '\u00F8', '\r', '\u00C5', '\u00E5',
        '\u0394', '\u005F', '\u03A6', '\u0393', '\u039B', '\u03A9', '\u03A0', '\u03A8',
        '\u03A3', '\u0398', '\u039E', '\u00A0', '\u00C6', '\u00E6', '\u00DF', '\u00C9',
        '\u0020', '\u0021', '\u0022', '\u0023', '\u00A4', '\u0025', '\u0026', '\'',
        '\u0028', '\u0029', '\u002A', '\u002B', '\u002C', '\u002D', '\u002E', '\u002F',
        '\u0030', '\u0031', '\u0032', '\u0033', '\u0034', '\u0035', '\u0036', '\u0037',
        '\u0038', '\u0039', '\u003A', '\u003B', '\u003C', '\u003D', '\u003E', '\u003F',
        '\u00A1', '\u0041', '\u0042', '\u0043', '\u0044', '\u0045', '\u0046', '\u0047',
        '\u0048', '\u0049', '\u004A', '\u004B', '\u004C', '\u004D', '\u004E', '\u004F',
        '\u0050', '\u0051', '\u0052', '\u0053', '\u0054', '\u0055', '\u0056', '\u0057',
        '\u0058', '\u0059', '\u005A', '\u00C4', '\u00D6', '\u00D1', '\u00DC', '\u00A7',
        '\u00BF', '\u0061', '\u0062', '\u0063', '\u0064', '\u0065', '\u0066', '\u0067',
        '\u0068', '\u0069', '\u006A', '\u006B', '\u006C', '\u006D', '\u006E', '\u006F',
        '\u0070', '\u0071', '\u0072', '\u0073', '\u0074', '\u0075', '\u0076', '\u0077',
        '\u0078', '\u0079', '\u007A', '\u00E4', '\u00F6', '\u00F1', '\u00FC', '\u00E0'
    };

    /**
     * 字符串数组按自然规则排序，获取排序后的字符串
     * @param strArr
     *        字符串数组
     * @param delimiter
     *        分隔符
     * @param asc
     *        是否升序
     * @return
     */
    public static String sortStrArrToStr(String[] strArr, String delimiter, final boolean asc) {
        Arrays.sort(strArr, new Comparator() {

            public int compare(Object o1, Object o2) {
                int result = ((String) o1).compareTo((String) o2);
                result = (asc) ? result : ((-1) * result);
                return result;
            }
        });
        StringBuffer sb = new StringBuffer();
        for (String s : strArr) {
            sb.append(s + delimiter);
        }
        String result = sb.substring(0, sb.length() - 1);
        return result;
    }

    /**
     * 获取Unicode编码的字节数组
     * @param str
     * @return
     */
    public static byte[] getUnicode(String str) {
        char[] chars = str.toCharArray();
        byte[] result = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; ++i) {
            result[2 * i] = (byte) (chars[i] / 256);
            result[2 * i + 1] = (byte) (chars[i] % 256);
        }
        return result;
    }
    public static final String HexCode[] = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F"
    };

    /**
     * 获取ASCII字符串
     * @param data
     *        原始字节数组
     * @param offset
     *        偏移量
     * @param size
     *        长度
     * @return
     */
    public static String getCASCIIString(byte[] data, int offset, int size) {
        CharArrayWriter out = new CharArrayWriter();
        for (int i = offset; i < size + offset; ++i) {
            if (data[i] == 0) {
                break;
            }
            out.write((char) data[i]);
        }
        return out.toString();
    }

    public static String getCUCS2String(byte[] data, int offset, int size) {
        try {
            int i;
            size = (size / 2) * 2;
            for (i = 0; i < size; i += 2) {
                if (data[offset + i] == 0 && data[offset + i + 1] == 0) {
                    break;
                }
            }
            String str = new String(data, offset, i, "UTF-16BE");
            return new String(data, offset, i, "UTF-16BE");
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取Hex字符串
     * @param b
     *        原始字节数组
     * @param offset
     *        偏移量
     * @param size
     *        长度
     * @return
     */
    public static String byteArrayToHexString(byte b[], int offset, int size) {
        if (b == null) {
            return null;
        }
        String result = "";
        for (int i = offset; i < offset + size; i++) {
            result = result + byteToHexString(b[i]);
        }
        return result;
    }

    public static String byteArrayToHexString(byte b[]) {
        if (b == null) {
            return null;
        }
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result = result + byteToHexString(b[i]);
        }
        return result;
    }

    public static byte[] hexStringToByteArray(String text) {
        if (text == null) {
            return null;
        }
        byte[] result = new byte[text.length() / 2];
        for (int i = 0; i < result.length; ++i) {
            int x = Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16);
            result[i] = x <= 127 ? (byte) x : (byte) (x - 256);
        }
        return result;
    }

    public static String hexStringToString(String hexString, String charSet) {
        if (hexString == null) {
            return null;
        }
        String result = "";
        try {
            result = new String(hexStringToByteArray(hexString), charSet);
        } catch (Exception ex) {
        }
        return result;
    }

    public static String hexStringToAsciiString(String hexString) {
        return hexStringToString(hexString, "ASCII");
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    public static boolean isASCII(String s) {
        char[] data = s.toCharArray();
        for (int i = 0; i < data.length; ++i) {
            if (data[i] < 0 || data[i] > 127) {
                return false;
            }
        }
        return true;
    }

    public static String getQuotedSubstring(String text, String beginToken, String endToken) {
        if (text == null) {
            return "";
        }
        int pos1 = text.indexOf(beginToken);
        if (pos1 == -1) {
            return "";
        }
        int pos2 = text.indexOf(endToken, pos1 + 1);
        if (pos2 == -1) {
            return "";
        }
        if (pos2 <= pos1) {
            return "";
        }
        return text.substring(pos1 + beginToken.length(), pos2);
    }

    public static String getTruncatedString(String src, String encoding, int length) {
        if (src == null) {
            return null;
        }
        String result = "";
        for (int i = 0; i < src.length(); ++i) {
            char c = src.charAt(i);
            try {
                if ((result + c).getBytes(encoding).length >= length) {
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return result;
            }
            result += c;
        }
        return result;
    }

    public static String getTruncatedString(String src, int length) {
        if (src == null) {
            return null;
        }
        if (src.length() < length) {
            return src;
        }
        return src.substring(0, length);
    }

    public static String getFixedLengthString(String src, int length) {
        if (src.length() < length) {
            char[] tail = new char[length - src.length()];
            Arrays.fill(tail, ' ');
            return src + new String(tail);
        } else {
            return src.substring(0, length);
        }
    }
    static Calendar cal = null;

    public static String getTimestamp() {
        return getTimestamp(System.currentTimeMillis(), "// ::.");
    }

    public static String getTimestamp(String separators) {
        return getTimestamp(System.currentTimeMillis(), separators);
    }

    public static String getTimestamp(long time) {
        return getTimestamp(time, "// ::.");
    }

    public static String getTimestamp(long time, String separators) {
        if (cal == null) {
            cal = Calendar.getInstance();
        }
        cal.setTimeInMillis(time);
        StringBuffer result = new StringBuffer(cal.get(Calendar.YEAR) + String.valueOf(separators.charAt(0)));
        result.append((cal.get(Calendar.MONTH) + 1) + String.valueOf(separators.charAt(1)));
        result.append(cal.get(Calendar.DAY_OF_MONTH) + String.valueOf(separators.charAt(2)));
        result.append(cal.get(Calendar.HOUR_OF_DAY) + String.valueOf(separators.charAt(3)));
        result.append(cal.get(Calendar.MINUTE) + String.valueOf(separators.charAt(4)));
        result.append(cal.get(Calendar.SECOND) + String.valueOf(separators.charAt(5)));
        result.append(cal.get(Calendar.MILLISECOND));
        return result.toString();
    }

    public static int countSimilarChars(String s1, String s2) {
        int l1 = s1.length(), l2 = s2.length(), i;
        int size = l1 < l2 ? l1 : l2;
        for (i = 0; i < size; ++i) {
            if (s1.charAt(i) != s2.charAt(i)) {
                break;
            }
        }
        return i;
    }

    public static String[] splitWords(String words, String regex) {
        String[] terms = words.split(regex);
        Vector<String> result = new Vector<String>();
        for (int i = 0; i < terms.length; ++i) {
            if (terms[i].length() > 0) {
                result.add(terms[i]);
            }
        }
        String[] data = new String[result.size()];
        return (String[]) result.toArray(data);
    }

    public static String[] splitWords(String words) {
        return splitWords(words, "[\\p{Blank}\\p{Punct}]");
    }

    public static int head(int start, byte[] data, byte[] separator) {
        int state = 0;
        for (int i = start; i < data.length; ++i) {
            if (data[i] == separator[state]) {
                state++;
            }
            if (state == separator.length) {
                return i + 1 - separator.length;
            }
        }
        return -1;
    }

    public static Vector<byte[]> split(byte[] data, byte[] separator) {
        Vector<byte[]> result = new Vector<byte[]>();
        int start = 0;
        while (start < data.length) {
            int pos = head(start, data, separator);
            if (pos != -1) {
                byte[] term = new byte[pos - start];
                System.arraycopy(data, start, term, 0, term.length);
                result.add(term);
                start += term.length + separator.length;
            } else {
                byte[] term = new byte[data.length - start];
                System.arraycopy(data, start, term, 0, term.length);
                result.add(term);
                start += term.length + separator.length;
            }
        }
        return result;
    }

    public static void split(Vector<String> tem, String str, String flag, int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length();) {
            if (compare(str, flag, i)) {
                i = i + flag.length();
                String sc = builder.toString();
                tem.add(sc);
                builder = new StringBuilder();
            } else {
                builder.append(str.substring(i, i + 2));
                i = i + 2;
            }
        }
        if (!builder.toString().equals("")) {
            tem.add(builder.toString());
        }
    }

    public static boolean compare(String str, String flag, int start) {
        if ((start + flag.length()) > str.length()) {
            return false;
        } else {
            String tem = str.substring(start, start + flag.length());
            if (tem.equals(flag)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String GBKToISO(String content) {
        if (content == null) {
            return "";
        }
        try {
            return new String(content.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException ex) {
            return content;
        }
    }

    public static String[] parseArray(String data, String separators) {
        return parseArray(data, separators.charAt(0), separators.charAt(1), separators.charAt(2), separators.charAt(3));
    }

    public static String[] parseArray(String data, char open, char comma, char close, char quote) {
        Vector<String> result = new Vector<String>();
        int state = 0;
        StringBuilder buff = new StringBuilder();
        char[] all = data.trim().toCharArray();
        boolean inQuote = false;
        for (int i = 0; i < all.length; ++i) {
            char c = all[i];
            if (inQuote) {
                if (c == quote) {
                    inQuote = false;
                }
                buff.append(c);
            } else {
                if (c == quote) {
                    inQuote = true;
                    buff.append(c);
                } else if (c == open) {
                    if (state > 0) {
                        buff.append(c);
                    }
                    state++;
                } else if (c == close) {
                    state--;
                    if (all.length - 1 == i && buff.length() > 0) {
                        result.add(buff.toString());
                    } else {
                        buff.append(c);
                    }
                } else if (c == comma) {
                    if (state == 1 && buff.length() > 0) {
                        result.add(buff.toString());
                        buff = new StringBuilder();
                    } else if (state > 0) {
                        buff.append(c);
                    }
                } else {
                    buff.append(c);
                }
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
    
    /**
     * ��00
     * @param con
     * @return
     */
    public static byte[] appendZero(String con){
    	byte[] content = con.getBytes();
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {
			bos.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int i = 8 - content.length % 8;
    	while(i>0){
    		bos.write(0x00);
    		i--;
    	}
    	return bos.toByteArray();
    }
    /**
     * �Ȳ�80���ٲ�00
     * @param con
     * @return
     */
    public static byte[] append80(String con){
    	byte[] content = con.getBytes();
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {
			bos.write(content);
			bos.write(0x80);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int i = 8 - (content.length + 1)% 8;  //1�ǲ���80
    	while(i>0){
    		bos.write(0x00);
    		i--;
    	}
    	return bos.toByteArray();
    }
    
    public static byte[] trim80(byte[] con){
    	String content = new String(con);
    	int index = content.lastIndexOf("80");
    	if(index != -1){
    		return content.substring(0,index).getBytes();
    	}
    	return con;
    }
    
    
	public static String getLengthOneByte(String value){
		int len = value.length() / 2;
		if(len < 10)
			return "0"+len;
		return len+"";
	}
	
	public static byte[] trimZero(byte[] data){
		byte b = 0x00;
		int n = 0;
		for(int i=0;i<data.length;i++){
			if(data[i] == b){
				n = i;
				break;
			}
		}
		if(n == 0)
			return new byte[0];
		else{
			byte[] retData =new byte[n];
			System.arraycopy(data, 0, retData, 0, n);
			return retData;
		}
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}


    public static void main(String[] args) {
//        String x = "{{\"t11\",\"t12\"},{\"t21\",\"t22\"}}";
//        String[] result = parseArray(x, "{,}\"");
//        for (String r : result) {
//            System.out.println(r);
//        }
        
        String s = "1234567890";
        byte[] con = appendZero(s);
        System.out.println(StringUtil.byteArrayToHexString(con));
    }
}

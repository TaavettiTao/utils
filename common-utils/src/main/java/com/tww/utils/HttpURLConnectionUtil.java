package com.tww.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
/**
 * HttpURLConnection方式发起接口访问
 * @author taoweiwei
 *
 */
public class HttpURLConnectionUtil {
	private Logger logger = LoggerFactory.getLogger(HttpURLConnection.class);
	public String getMapValue(Map<String, Object> req, String key) {
		if( req.get(key) == null ) {
			return "";
		}
		if( req.get(key).toString() == null ) {
			return "";
		}
		if( req.get(key).toString().length() == 0 )
			return "";
		else 
			return req.get(key).toString();
	}
	
	public String getMapStringValue(Map<String, String> req, String key) {
		if( req.get(key) == null ) {
			return "";
		}
		if( req.get(key).toString() == null ) {
			return "";
		}
		if( req.get(key).toString().length() == 0 )
			return "";
		else 
			return req.get(key).toString();
	}
	
	public String getSubString( String s, int begin, int end ) {
		if( s == null ) 
			return null;
		if( s.length() > begin ) {
			if( s.length() > end )
				return s.substring(begin, s.length());
			else 
				return s.substring(begin, end);
		} else {
			return null;
		}
	}
	
	public String changeY2F(String amount){  
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString();  
    } 
    public void printStringMap(Map<String, String> m) {
        for (String key : m.keySet()) {
        	logger.info("key:{} value:{}", key, m.get(key) );
        }
	}
    public void printObjectMap(Map<String, Object> m) {
        for (String key : m.keySet()) {
        	logger.info("key:{} value:{}", key, m.get(key) );
        }
	}
    public String time2timestamp(String t) {
    	if( t.length() != 14 ) {
    		return t;
    	}
    	return t.substring(0, 4) + "-" + t.substring(4, 6) + "-" + t.substring(6, 8) + " " + t.substring(8, 10) + ":" + t.substring(10, 12) + ":" + t.substring(12, 14);
    }
    
    public String httpRequestGet(String url,Map<String,String> params) {  
        String result = null;  
        try {  
            InputStream is = httpRequestToStream(url,  params);  
            BufferedReader in = new BufferedReader(new InputStreamReader(is,  
                    "UTF-8"));  
            StringBuffer buffer = new StringBuffer();  
            String line = "";  
            while ((line = in.readLine()) != null) {  
                buffer.append(line);  
            }  
            result = buffer.toString();  
        } catch (Exception e) {  
            return null;  
        }  
        return result;  
    }  
  
    /**
     * 获取请求响应输入流
     * @param url 请求url
     * @param params 参数
     * @return
     */
    private InputStream httpRequestToStream(String url,  
            Map<String, String> params) {  
         InputStream is = null;  
            try {  
                String parameters = "";  
                boolean hasParams = false;  
                for(String key : params.keySet()){  
                    String value = URLEncoder.encode(params.get(key), "UTF-8");  
                    parameters += key +"="+ value +"&";  
                    hasParams = true;  
                }  
                if(hasParams){  
                    parameters = parameters.substring(0, parameters.length()-1);  
                }  
  
  
                url += "?"+ parameters;  
                logger.info("url={}", url);
                URL u = new URL(url);  
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();  
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
                conn.setRequestProperty("Accept-Charset", "UTF-8");  
                conn.setRequestProperty("contentType", "utf-8");  
                conn.setConnectTimeout(50000);    
                conn.setReadTimeout(50000);  
                conn.setDoInput(true);  
                //设置请求方式，默认为GET  
                conn.setRequestMethod("GET");  
  
  
                is = conn.getInputStream();  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return is;  
    } 
    
    public String httpRequestGetSplit(String url,   
            Map<String,String> params) {  
        String result = null;  
        try {  
            InputStream is = httpRequestToStreamSplit(url,  params);  
            BufferedReader in = new BufferedReader(new InputStreamReader(is,  
                    "UTF-8"));  
            StringBuffer buffer = new StringBuffer();  
            String line = "";  
            while ((line = in.readLine()) != null) {  
                buffer.append(line);  
            }  
            result = buffer.toString();  
        } catch (Exception e) {  
            return null;  
        }  
        return result;  
    }  
  
    private InputStream httpRequestToStreamSplit(String url,  
            Map<String, String> params) {  
         InputStream is = null;  
            try {  
                String parameters = "";  
                boolean hasParams = false;  
                for(String key : params.keySet()){  
                    String value = URLEncoder.encode(params.get(key), "UTF-8");  
                    parameters += key +"="+ value +"&";  
                    hasParams = true;  
                }  
                if(hasParams){  
                    parameters = parameters.substring(0, parameters.length()-1);  
                }  
  
  
                url += "&"+ parameters;  
                logger.info("url={}", url);
                URL u = new URL(url);  
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();  
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
                conn.setRequestProperty("Accept-Charset", "UTF-8");  
                conn.setRequestProperty("contentType", "utf-8");  
                conn.setConnectTimeout(50000);    
                conn.setReadTimeout(50000);  
                conn.setDoInput(true);  
                //设置请求方式，默认为GET  
                conn.setRequestMethod("GET");  
  
  
                is = conn.getInputStream();  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return is;  
    } 
    
    public String httpRequestPost(String add_url, String method, JSONObject obj ) {
        try {
            URL url = new URL(add_url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");  

            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            logger.info("httpRequest:{}:{}", add_url, obj.toString());
            String encoderJson = URLEncoder.encode(obj.toString(), "UTF-8");
            out.write(obj.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
             
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sbf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbf.append(lines);
            }
            System.out.println(sbf);
            reader.close();
            // 断开连接
            connection.disconnect();
            return sbf.toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
    }
    
    
    /**
     * 获取支付通重定向地址
     * @param url
     * @param params
     * @return
     */
    public String httpRequestForRedirectLocation(String url,  Map<String, String> params) {  
            try {  
                String parameters = "";  
                boolean hasParams = false;  
                for(String key : params.keySet()){  
                    String value = URLEncoder.encode(params.get(key), "UTF-8");
                    parameters += key +"="+ value +"&";  
                    hasParams = true;  
                }  
                if(hasParams){  
                    parameters = parameters.substring(0, parameters.length()-1);  
                }  
                url+="?"+parameters;
                logger.info("请求url={}", url);
                URL u = new URL(url);  
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();  
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");  
                conn.setRequestProperty("Accept-Charset", "UTF-8"); 
                conn.setConnectTimeout(50000);    
                conn.setReadTimeout(50000);  
                conn.setDoInput(true);  
                conn.setRequestMethod("POST");
                //是否自动执行 http 重定向，默认为true,此处关闭自动重定向，获取header内location
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                
                int statusCode = conn.getResponseCode();
                logger.info("重定向请求响应状态码：{}",statusCode);
                
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String lines;
	            StringBuffer sbf = new StringBuffer();
	            while ((lines = reader.readLine()) != null) {
	                lines = new String(lines.getBytes(), "utf-8");
	                sbf.append(lines);
	            }
	            logger.info("接口请求响应数据：{}",sbf);
	            reader.close();
	            conn.disconnect();
                if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                		||statusCode==HttpStatus.SC_MOVED_TEMPORARILY){
                	//获取重定向header中location
                	String location = conn.getHeaderField("Location");//获取 重定向地址
                	logger.info("接口请求重定向地址：{}",location);
                	return location;
                }else{
                	return "";
                }
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return "";  
    } 
    
    public static void main(String[] args) {
    	
    	HttpURLConnectionUtil utilCollection=new HttpURLConnectionUtil();
    	
    	Map<String, String> reqData = new HashMap<String, String>();
    	SimpleDateFormat trantime = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date=new Date();
		System.out.println(trantime.format(date)); 
		String orderid = String.format("%s%s", trantime.format(date), RandomStringUtils.randomNumeric(4));
		//订单号:每个商户唯一
		reqData.put("txn_no", orderid);
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
		
		String key="c7946cd3389dfac88b2ea50c2f9aab06";
		String sign="";
		try{
			//sign=MD5Signature.genSign(reqData, "MD5", key, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		reqData.put("sign",sign);
		System.out.println("sign:"+sign);
		
		String reqUrl="http://uchannel.payexpress.biz/payfront/mobile/pay/codePublic";
		
		String locationUrl=utilCollection.httpRequestForRedirectLocation(reqUrl, reqData);
		System.out.println(locationUrl);
	}
    
    /**
     * 获取url域名
     * @param url ：http://uchannel.payexpress.biz/payfront/mobile/pay/codePublic
     * @return http://uchannel.payexpress.biz
     * @throws MalformedURLException 
     */
    public String getPayExpressDemain(String url) throws Exception{
    	URL u = new URL(url);
    	String protocol=u.getProtocol();
    	String host=u.getHost();
    	return protocol+"://"+host;
    }
}

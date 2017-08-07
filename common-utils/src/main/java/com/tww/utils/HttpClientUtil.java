/**
 * Copyright Shanghai COS Software Co., Ltd.
 * All right reserved
 * Created on 2015
 */

package com.tww.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @FileName: HttpClientHelper.java
 * @Description:
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2015
 * @author yangb
 * @version 1.0
 * @Create: 2015-8-20 上午10:53:20
 * 
 * @Modification History
 * @Date Author Version Description
 * @----------------------------------------------------
 * @2015-8-20 yangb 1.0 create
 */
public class HttpClientUtil {

	public static int connectionTimeout = 10 * 1000;

	public static int soTimeout = 30 * 1000;

	private static HttpClient httpClient;

	private HttpClientUtil() {
	}

	/**
	 * get请求
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public static String getRequest(String uri, Map<String, String> params) {
		try {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(uri);
			if (null != params) {
				urlBuilder.append("?");
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					urlBuilder
							.append(URLEncoder.encode(param.getKey(), "UTF-8"))
							.append('=')
							.append(URLEncoder.encode(param.getValue(), "UTF-8"));
					if (iterator.hasNext()) {
						urlBuilder.append('&');
					}
				}
			}
			// 创建HttpClient对象
			HttpClient client = getHttpClient();
			// 发送get请求创建HttpGet对象
			HttpGet getMethod = new HttpGet(urlBuilder.toString());
			HttpResponse response = client.execute(getMethod);
			// 获取状态码
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				StringBuilder builder = new StringBuilder();
				// 获取响应内容
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					builder.append(s);
				}
				return builder.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public static String postRequest(String uri, Map<String, ?> params) {
		try {
			// 创建HttpClient对象
			HttpClient client = getHttpClient();
			// 发get请求创建HttpGet对象
			HttpPost httpPost = makeHttpPost(uri, params);
			HttpResponse response = client.execute(httpPost);
			int statueCode = response.getStatusLine().getStatusCode();
			if (statueCode == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8"); // 获取响应内容
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String postRequest(String uri, String params)
			throws Exception {
		try {
			// 创建HttpClient对象
			HttpClient client = getHttpClient();
			// 发get请求创建HttpGet对象
			HttpPost httpPost = makeHttpPost(uri, null);
			// 添加http头信息 application/json,application/xml
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
			// 接受数据类型
			httpPost.addHeader("Accept", "application/json");

			// 创建传递参数封装 实体对象
			StringEntity se = new StringEntity(params, "UTF-8");
			// 创建传递参数封装 实体对象

			// 把实体对象存入到httpPost对象中
			httpPost.setEntity(se);

			HttpResponse response = client.execute(httpPost);
			int statueCode = response.getStatusLine().getStatusCode();
			if (statueCode == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) { // 初始化工作
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				// 允许所有主机的验证
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpParams params = new BasicHttpParams();

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(params, true);

				// 设置连接管理器的超时
				ConnManagerParams.setTimeout(params, 5000);
				// 设置连接超时
				HttpConnectionParams.setConnectionTimeout(params,
						connectionTimeout);
				// 设置socket超时
				HttpConnectionParams.setSoTimeout(params, soTimeout);

				// 设置http https支持
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sf, 443));

				ClientConnectionManager conManager = new ThreadSafeClientConnManager(
						params, schReg);
				DefaultHttpClient defHttpClient = new DefaultHttpClient(
						conManager, params);

				// 定义重试策略
				DefaultHttpRequestRetryHandler retryhandler = new DefaultHttpRequestRetryHandler(
						0, false);
				defHttpClient.setHttpRequestRetryHandler(retryhandler);
				httpClient = defHttpClient;
			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient();
			}
		}
		return httpClient;
	}

	private static HttpPost makeHttpPost(String url, Map<String, ?> params)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		if (params != null && params.size() > 0) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (String name : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(name, params
						.get(name).toString()));
			}
			// 设置字符集
			HttpEntity httpentity = new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8);
			// 请求httpRequest
			httpPost.setEntity(httpentity);
		}
		HttpParams timeParams = new BasicHttpParams();
		/* 连接超时 */
		HttpConnectionParams
				.setConnectionTimeout(timeParams, connectionTimeout);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(timeParams, soTimeout);
		httpPost.setParams(timeParams);
		httpPost.addHeader("Connection", "close");
		return httpPost;
	}
}

@SuppressWarnings("deprecation")
class SSLSocketFactoryEx extends SSLSocketFactory {

	SSLContext sslContext = SSLContext.getInstance("TLS");

	public SSLSocketFactoryEx(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);

		TrustManager tm = new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

			}
		};
		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}

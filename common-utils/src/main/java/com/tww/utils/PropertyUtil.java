/**
 * 
 */
package com.tww.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

public class PropertyUtil {
	private static Properties properties = new Properties();

	static {
		try {
			//读取classpath下资源文件
			InputStream inputStream=PropertyUtil.class.getResourceAsStream("/application.properties");
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定问配置文件中key对应值
	 *        properties配置文件名
	 * @param key
	 *        关键字
	 * @return
	 */
	public static String getProperty(String key) {
		String value = null;
		try {
			value = properties.getProperty(key);
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

	public static BigDecimal getBigDecimalProperty(String key) {
		BigDecimal value = null;
		try {
			value = ConvUtils.convToDecimal(properties.getProperty(key));
		} catch (Exception e) {
			value = BigDecimal.ZERO;
		}
		return value;
	}



}

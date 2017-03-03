/**
 * 
 */
package com.tww.utils;

import java.util.ResourceBundle;
/**
 * 
* @ClassName: PropertyUtil 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author taoww
* @date 2017年3月3日 下午4:18:09 
*
 */
public class PropertyUtil {

	/**
	 * 获取指定问配置文件中key对应值
	 * @param propertiesName
	 *        properties配置文件名
	 * @param key
	 *        关键字
	 * @return
	 */
	public static String getProperty(String propertiesName,String key) {
		String value = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("");
			value = rb.getString(key);
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

}

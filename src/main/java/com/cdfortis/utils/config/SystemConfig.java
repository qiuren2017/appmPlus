package com.cdfortis.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取config.properties文件
 * @author 乔新宇
 *
 */
public class SystemConfig {
	
	private static final String PROPERTIES_FILE_NAME="config.properties";
	private static final String FDFS_CLIENT_PROPERTIEs="fdfs_client.properties";
	
	public static String getResource(String key) throws IOException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties =  new Properties();
		InputStream in = loader.getResourceAsStream(PROPERTIES_FILE_NAME);
		properties.load(in);
		String value = properties.getProperty(key);
		in.close();
		return value;
	}
	public static String getFDFSConfigResource(String key) throws IOException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream in = loader.getResourceAsStream(FDFS_CLIENT_PROPERTIEs);
		properties.load(in);
		String value = properties.getProperty(key);
		// 编码转换，从ISO-8859-1转向指定编码
		value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		in.close();
		return value;
	}
	public static void modifyResource(String key ,String value) throws IOException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties =  new Properties();
		InputStream in = loader.getResourceAsStream(PROPERTIES_FILE_NAME);
		properties.load(in);
		properties.setProperty(key, value);
		in.close();
	}
}

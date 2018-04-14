package com.cdfortis.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cdfortis.utils.log.Log4jUtils;
/**
 * <p>Title: 大家不要随便使用这个类。建议大家使用SystemConfig。
 * 			如果你的项目是在开机启动时加载一次配置的话，可以使用此类</p>
 * <p>Description: 此类与SystemConfig的不同之处在于，
 * 				这个类在加载一次配置的时候会打印info日志</p> 
 * <p>createDate: 2017年7月21日上午11:23:19</p> 
 * @author zhangNan
 */
public class SystemConfigUtil {
	// 静态工具方法，私有化构造器，防止被创建对象
	private SystemConfigUtil() {
		throw new AssertionError("[Tool utils can't be instantiation]");
	}

	private static final Logger logger = Logger.getLogger(SystemConfig.class);

	private static final String PROPERTIES_FILE_NAME = "config.properties";

	public static String getResource(String key) {
		try {
			logger.info("[Load configure] [" + key + "] ...");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();
			InputStream in = loader.getResourceAsStream(PROPERTIES_FILE_NAME);
			properties.load(in);
			in.close();
			String value = properties.getProperty(key);
			return value;
		} catch (Exception e) {
			logger.error("[Load configure error] [" + key + "]"
					+ Log4jUtils.getTrace(e));
		}
		return null;
	}

	public static void modifyResource(String key, String value)
			throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream in = loader.getResourceAsStream(PROPERTIES_FILE_NAME);
		properties.load(in);
		in.close();
		properties.setProperty(key, value);
	}
}

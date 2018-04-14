package com.cdfortis.utils.config;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.cdfortis.utils.log.Log4jUtils;

/**
 * <p>Title: 获取配置文件的工具类</p> 
 * <p>Description: 加载配置信息，返回Properties</p> 
 * <p>createDate: 2017年8月2日上午10:17:33</p> 
 * @author zhangNan
 */
public class PropertiesUtils {
	
	private static final Logger logger = Logger.getLogger(PropertiesUtils.class);
	/**
	 * Default load config properties file
	 */
	private static final String DEFAULT_CONFIG_NAME = "config.properties";
	
	/**
	 * @Title: getDefaultProperties 
	 * @Description: 获取 config.properties 的配置信息
	 * @CreateDate: 2017年8月2日上午10:50:19
	 * @author: zhangn
	 */
	public static Properties getDefaultProperties(){
		return getProperties(DEFAULT_CONFIG_NAME);
	}
	
	/**
	 * @Title: getDefaultProperties 
	 * @Description: 获取指定文件相对路径名称的配置信息,使用Utf-8编码
	 * @CreateDate: 2017年8月2日上午10:50:48
	 * @author: zhangn
	 */
	public static Properties getProperties(String realPath){
		return geProperties(realPath, "UTF-8");
	}
	
	/**
	 * @Title: geProperties 
	 * @Description: 取指定文件相对路径名称的配置信息,需指定编码
	 * @CreateDate: 2017年8月2日上午11:16:25
	 * @author: zhangn
	 */
	public static Properties geProperties(String realPath, String encoded){
		try {
			logger.info("[Load configure] [" + realPath + "] ...");
			
			return PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(realPath), encoded));
			
		} catch (Exception e) {
			logger.error("[Load configure error] [" + realPath + "]"
					+ Log4jUtils.getTrace(e));
		}
		return null;
	}
}

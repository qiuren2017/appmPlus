package com.cdfortis.utils.file;

import java.util.Properties;

import com.cdfortis.utils.config.PropertiesUtils;

/**
 * Title: 获取与上传阿里云OSS的相关配置
 * Description:
 * createDate: 2017年10月27日上午10:26:08
 * 
 * @author zhangNan
 */
public class OSSConfigFactory {
	
	private static final Properties CONFIG_PROPERTIES = PropertiesUtils.getDefaultProperties();
	
	private static final String END_POINT = CONFIG_PROPERTIES.getProperty("endpoint"); // 连接区域地址
	private static final String ACCESS_KEY_ID = CONFIG_PROPERTIES.getProperty("accessKeyId"); // 连接keyId
	private static final String ACCESS_KEY_SECRET = CONFIG_PROPERTIES.getProperty("accessKeySecret");// 连接秘钥
	private static final String BUCKET_NAME = CONFIG_PROPERTIES.getProperty("bucketName"); // 需要存储的bucketName
	private static final String FILE_LOCATION = CONFIG_PROPERTIES.getProperty("fileLocation"); // 图片保存路径
	
	public static Properties getConfigProperties() {
		return CONFIG_PROPERTIES;
	}

	public static String getEndPoint() {
		return END_POINT;
	}

	public static String getAccessKeyId() {
		return ACCESS_KEY_ID;
	}

	public static String getAccessKeySecret() {
		return ACCESS_KEY_SECRET;
	}

	public static String getBucketName() {
		return BUCKET_NAME;
	}

	public static String getFileLocation() {
		return FILE_LOCATION;
	}

}

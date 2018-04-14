package com.cdfortis.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.cdfortis.utils.log.Log4jUtils;

public class AliyunOSSUploadUtils {

	private static final Logger LOGGER = Logger.getLogger(AliyunOSSUploadUtils.class);

	/**
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传InputStream返回半路径且关闭输入流
	 * @param InputStream
	 * @param fileName
	 *            文件名不含文件后缀
	 * @param fileType
	 *            文件后缀不含“.”
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFile(InputStream inputStream, String fileName, String fileType) {
		return putObject(inputStream, fileType, fileName, false, true);
	}

	/**
	 * @MethodName: uploadFileNOCloseInput
	 * @Description: OSS单文件上传InputStream不关闭输入流
	 * @param InputStream
	 * @param fileName
	 *            文件名不含文件后缀
	 * @param fileType
	 *            文件后缀不含“.”
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFileNOCloseInput(InputStream inputStream, String fileName, String fileType) {
		return putObject(inputStream, fileType, fileName, false, false);
	}

	/**
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传
	 * @param file
	 * @param fileName
	 *            文件名不含文件后缀
	 * @param fileType
	 *            文件后缀不含“.”
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFile(File file, String fileName, String fileType) {
		try (InputStream inputStream = new FileInputStream(file)) {
			return uploadFileNOCloseInput(inputStream, fileType, fileName);
		} catch (IOException e) {
			LOGGER.error("Upload file to OSS:" + Log4jUtils.getTrace(e));
		}
		return null;
	}

	/**
	 * @Title: putObject
	 * @Description: 上传文件到阿里云OSS方法,注意isCloseInput为true时这个方法将会在执行完成之后关闭输入流，
	 *               isFullUrl为true返回全路径，为false返回半路径
	 * @CreateDate: 2017年10月27日上午10:43:52
	 * @author: zhangn
	 */
	public static String putObject(InputStream input, String fileType, String fileName, boolean isFullUrl,
			boolean isCloseInput) {
		String url = null; // 默认null
		OSSClient ossClient = null;
		StringBuffer fileNameBuffer = new StringBuffer();
		try {
			Calendar cal = Calendar.getInstance();
			fileNameBuffer.append(OSSConfigFactory.getFileLocation()).append(cal.get(Calendar.YEAR)).append('/')
					.append((cal.get(Calendar.MONTH) + 1)).append('/').append(fileName).append('.').append(fileType);
			if (isFullUrl) {
				url = OSSConfigFactory.getEndPoint().replaceFirst("http://",
						"http://" + OSSConfigFactory.getBucketName() + ".") + "/" + fileNameBuffer.toString(); // 上传成功再返回的文件路径
			} else {
				url = "/" + fileNameBuffer.toString();
			}
			ossClient = new OSSClient(OSSConfigFactory.getEndPoint(), OSSConfigFactory.getAccessKeyId(),
					OSSConfigFactory.getAccessKeySecret());
			ObjectMetadata meta = new ObjectMetadata(); // 创建上传Object的Metadata
			meta.setContentType(AliyunOSSUploadUtils.contentType(fileType)); // 设置上传内容类型
			meta.setCacheControl("no-cache"); // 被下载时网页的缓存行为
			PutObjectRequest request = new PutObjectRequest(OSSConfigFactory.getBucketName(), fileNameBuffer.toString(),
					input, meta); // 创建上传请求
			ossClient.putObject(request);
		} catch (Exception e) {
			LOGGER.error("Upload file to OSS:" + Log4jUtils.getTrace(e));
		} finally {
			ossClient.shutdown();
			if (isCloseInput) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("input closs fail:" + Log4jUtils.getTrace(e));
				}
			}
		}
		return url;
	}

	/**
	 * 
	 * @MethodName: contentType
	 * @Description: 获取文件类型
	 * @param FileType
	 * @return String
	 */
	private static String contentType(String fileType) {
		fileType = fileType.toLowerCase();
		switch (fileType) {
		case "bmp":
			return "image/bmp";
		case "gif":
			return "image/gif";
		case "png":
		case "jpeg":
		case "jpg":
			return "image/jpeg";
		case "html":
			return "text/html";
		case "txt":
			return "text/plain";
		case "vsd":
			return "application/vnd.visio";
		case "ppt":
		case "pptx":
			return "application/vnd.ms-powerpoint";
		case "doc":
		case "docx":
			return "application/msword";
		case "xml":
			return "text/xml";
		case "mp4":
			return "video/mp4";
		default:
			break;
		}
		return "application/octet-stream";
	}

}

package com.cdfortis.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.cdfortis.utils.config.SystemConfig;

/**
 * 
 * @ClassName: OSSUploadUtil
 * @Description: 阿里云OSS文件上传工具类
 * @author 陈辉翔
 * @date 2016年11月3日 下午12:03:24
 * @version 0.0.4
 * 
 * v0.0.4-20171130: 
 * 	1.新增deleteFile(String[] files)传递数组方法
 * 
 * 
 */
public class OSSUploadUtil {
	
	private static OSSConfig config = null;

	/**
	 * 
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传file
	 * @param file	
	 * @param fileType 文件后缀
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFile(File file,String fileType){
		config = config==null?new OSSConfig():config;
		String fileName = UUID.randomUUID().toString().toUpperCase().replace("-", "")+"."+fileType;	//文件名，根据UUID来
		return putObject(file,fileType,fileName,false);
	}
	/**
	 * 
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传(自定义文件名)
	 * 文件名重复会自动追加序号，保证不重复不覆盖	
	 * @param file	
	 * @param fileType 文件后缀
	 * @param newName 文件名
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFile(File file,String fileType,String newName){
		config = config==null?new OSSConfig():config;
		int dex = newName.lastIndexOf("."+fileType);
		newName = newName.substring(0,dex==-1?newName.length():dex);
		OSSClient ossClient = null;
		String name = null;
		try {
			ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			String fileName = config.getFileLocation() + year + "/" + month + "/";
			boolean isExit = false;		//是否存在
			int index = 1;
			do {
				name = isExit?newName+"("+(index++)+")."+fileType:newName+"."+fileType;
				isExit = ossClient.doesObjectExist(config.getBucketName(), fileName+name);		//判断文件名是否存在
			} while (isExit);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ossClient.shutdown();
		}
		return putObject(file,fileType,name,false);
	}
	
	/**
	 * 
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传InputStream
	 * @param file	
	 * @param fileType 文件后缀
	 * @return String 文件地址(相对路径)
	 */
	public static String uploadFile(InputStream inputStream,String fileType){
		config = config==null?new OSSConfig():config;
		String fileName = UUID.randomUUID().toString().toUpperCase().replace("-", "")+"."+fileType;	//文件名，根据UUID来
		return putObject(inputStream,fileType,fileName,false);
	}
	
	/**
	 * 
	 * @MethodName: uploadFileFullUrl
	 * @Description: OSS单文件上传
	 * @param file	
	 * @param fileType 文件后缀
	 * @return String 文件地址(全路径)
	 */
	public static String uploadFileFullUrl(File file,String fileType){
		config = config==null?new OSSConfig():config;
		String fileName = UUID.randomUUID().toString().toUpperCase().replace("-", "")+"."+fileType;	//文件名，根据UUID来
		return putObject(file,fileType,fileName,true);
	}
	
	/**
	 * 
	 * @MethodName: updateFile
	 * @Description: 更新文件:只更新内容，不更新文件名和文件地址。
	 * 				(因为地址没变，可能存在浏览器原数据缓存，不能及时加载新数据，例如图片更新，请注意)
	 * @param file
	 * @param fileType
	 * @param oldUrl
	 * @return String
	 */
	public static String updateFile(File file,String fileType,String oldUrl){
		String fileName = getFileName(oldUrl);
		if(fileName==null) return null;
		int index = oldUrl.indexOf("http://");
		int index2 = oldUrl.indexOf("https://");
		if((index+index2)>=-1){
			//表示全路径
			return putObject(file, fileType,fileName,true);
		}
		return putObject(file,fileType,fileName,false);
	}
	
	/**
	 * 
	 * @MethodName: replaceFile
	 * @Description: 替换文件:删除原文件并上传新文件，文件名和地址同时替换
	 * 				 (解决原数据缓存问题，只要更新了地址，就能重新加载数据)
	 * @param file
	 * @param fileType 文件后缀
	 * @param oldUrl 需要删除的文件地址
	 * @return String 文件地址
	 */
	public static String replaceFile(File file,String fileType,String oldUrl){
		deleteFile(oldUrl);		//先删除原文件
		int index = oldUrl.indexOf("http://");
		int index2 = oldUrl.indexOf("https://");
		if((index+index2)>=-1){
			//表示全路径
			return uploadFileFullUrl(file, fileType);
		}
		return uploadFile(file, fileType);
	}
	
	/**
	 * 
	 * @MethodName: deleteFile
	 * @Description: 单文件删除
	 * @param fileUrl 需要删除的文件url
	 * @return boolean 是否删除成功
	 */
	public static boolean deleteFile(String fileUrl){
		if(fileUrl==null||"".equals(fileUrl)) return true;
		config = config==null?new OSSConfig():config;
		int index = fileUrl.indexOf("http://");
		int index2 = fileUrl.indexOf("https://");
		String bucketName = "";
		String fileName = "";
		if((index+index2)>=-1){
			//表示全路径
			bucketName = OSSUploadUtil.getBucketName(fileUrl);		//根据url获取bucketName
			fileName = OSSUploadUtil.getFileName(fileUrl);			//根据url获取fileName
		}else{
			//表示相对路径
			bucketName = config.getBucketName();
			int in = fileUrl.indexOf("/");
			if(in==0){
				fileName = fileUrl.replaceFirst("/","");
			}else{
				fileName = fileUrl;
			}
		}
		if(bucketName==null||fileName==null) return false;
		OSSClient ossClient = null; 
		try {
			ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret()); 
			GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(fileName);
			ossClient.deleteObject(request);
		} catch (Exception oe) {
            oe.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
		return true;
	}
	
	/**
	 * 
	 * @MethodName: deleteFile
	 * @Description: 批量文件删除(较快)：适用于全路径相同endPoint和BucketName
	 * @param fileUrls 需要删除的文件url集合
	 * @return int 成功删除的个数
	 */
	public static int deleteFile(List<String> fileUrls){
		if(fileUrls==null||fileUrls.size()<1){
			return -1;
		}
		config = config==null?new OSSConfig():config;
		int deleteCount = 0;	//成功删除的个数
		String bucketName = OSSUploadUtil.getBucketName(fileUrls.get(0));		//根据url获取bucketName
		List<String> fileNames = OSSUploadUtil.getFileName(fileUrls);			//根据url获取fileName
		if(bucketName==null||fileNames.size()<=0) return 0;
		OSSClient ossClient = null; 
		try {
			ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret()); 
			DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(fileNames);
			DeleteObjectsResult result = ossClient.deleteObjects(request);
			deleteCount = result.getDeletedObjects().size();
		} catch (OSSException oe) {
            oe.printStackTrace();
            throw new RuntimeException("OSS服务异常:", oe);
        } catch (ClientException ce) {
        	ce.printStackTrace();
			throw new RuntimeException("OSS客户端异常:", ce);
        } finally {
            ossClient.shutdown();
        }
		return deleteCount;
	}
	/**
	 * 
	 * @MethodName: deleteFile
	 * @Description: 批量文件删除(较快)：适用于全路径相同endPoint和BucketName
	 * @param fileUrls 需要删除的文件url集合
	 * @return int 成功删除的个数
	 */
	public static int deleteFile(String[] fileUrls){
		if(fileUrls.length<0){
			return -1;
		}
		List<String> list = new ArrayList<>(); 
		for (int i = 0; i < fileUrls.length; i++) {
			list.add(fileUrls[i]);
		}
		return deleteFile(list);
	}
	
	/**
	 * 
	 * @MethodName: batchDeleteFiles
	 * @Description: 批量文件删除-(较慢)：推荐使用
	 * @param fileUrls 需要删除的文件url集合
	 * @return int 成功删除的个数
	 */
	public static int deleteFiles(List<String> fileUrls){
		int count = 0;
		for (String url : fileUrls) {
			if(deleteFile(url)){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @MethodName: putObject
	 * @Description: 上传文件
	 * @param file
	 * @param fileType
	 * @param fileName
	 * @return String
	 */
	private static String putObject(File file,String fileType,String fileName,boolean isFullUrl){
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			 return null;
		}finally{
			file.delete();
		}  
		return putObject(input, fileType, fileName, isFullUrl);
	}
	
	/**
	 * 
	 * @MethodName: putObject
	 * @Description: 上传文件
	 * @param file
	 * @param fileType
	 * @param fileName
	 * @return String
	 */
	private static String putObject(InputStream input,String fileType,String fileName,boolean isFullUrl){
		config = config==null?new OSSConfig():config;
		String url = null;		//默认null
		OSSClient ossClient = null;  
		try {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			fileName = config.getFileLocation() + year + "/" + month + "/" + fileName;
			if(isFullUrl){
				url = config.getEndpoint().replaceFirst("http://","http://"+config.getBucketName()+".") + "/" + fileName;			//上传成功再返回的文件路径
			}else{
				url = "/" + fileName;
			}
			ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret()); 
			ObjectMetadata meta = new ObjectMetadata();						// 创建上传Object的Metadata
			meta.setContentType(OSSUploadUtil.contentType(fileType));		// 设置上传内容类型
			meta.setCacheControl("no-cache");								// 被下载时网页的缓存行为  
			PutObjectRequest request = new PutObjectRequest(config.getBucketName(), fileName,input,meta);			//创建上传请求
			ossClient.putObject(request); 
		} catch (OSSException oe) {
            oe.printStackTrace();
            return null;
        } catch (ClientException ce) {
        	ce.printStackTrace();
        	return null;
        } finally {
        	try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				input = null;
				ossClient.shutdown();
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
	private static String contentType(String fileType){
		fileType = fileType.toLowerCase();
		String contentType = "";
		switch (fileType) {
		case "bmp":	contentType = "image/bmp";
					break;
		case "gif":	contentType = "image/gif";
					break;
		case "png":	
		case "jpeg":	
		case "jpg":	contentType = "image/jpeg";
					break;
		case "html":contentType = "text/html";
					break;
		case "txt":	contentType = "text/plain";
					break;
		case "vsd":	contentType = "application/vnd.visio";
					break;
		case "ppt":	
		case "pptx":contentType = "application/vnd.ms-powerpoint";
					break;
		case "doc":	
		case "docx":contentType = "application/msword";
					break;
		case "xml":contentType = "text/xml";
					break;
		case "mp4":contentType = "video/mp4";
					break;
		default: contentType = "application/octet-stream";
					break;
		}
		return contentType;
     }  
	
	/**
	 * 
	 * @MethodName: getBucketName
	 * @Description: 根据url获取bucketName
	 * @param fileUrl 文件url
	 * @return String bucketName
	 */
	private static String getBucketName(String fileUrl){
		if (fileUrl==null) return null;
		String http = "http://";
		String https = "https://";
		int httpIndex = fileUrl.indexOf(http);
		int httpsIndex = fileUrl.indexOf(https);
		int startIndex  = 0;
		if(httpIndex==-1){
			if(httpsIndex==-1){
				return null;
			}else{
				startIndex = httpsIndex+https.length();
			}
		}else{
			startIndex = httpIndex+http.length();
		}
		int endIndex = fileUrl.indexOf(".oss-"); 
		return fileUrl.substring(startIndex, endIndex);
	}
	
	/**
	 * 
	 * @MethodName: getFileName
	 * @Description: 根据url获取fileName
	 * @param fileUrl 文件url
	 * @return String fileName
	 */
	private static String getFileName(String fileUrl){
		String str = "aliyuncs.com/";
		int beginIndex = fileUrl.indexOf(str);
		if(beginIndex==-1) return null;
		return fileUrl.substring(beginIndex+str.length());
	}
	
	/**
	 * 
	 * @MethodName: getFileName
	 * @Description: 根据url获取fileNames集合
	 * @param fileUrl 文件url
	 * @return List<String>  fileName集合
	 */
	private static List<String> getFileName(List<String> fileUrls){
		List<String> names = new ArrayList<>();
		for (String url : fileUrls) {
			names.add(getFileName(url));
		}
		return names;
	}
	
	//测试方法
	public static void main(String[] args) {
		boolean a = OSSUploadUtil.deleteFile("http://databucket.oss-cn-hangzhou.aliyuncs.com/circlebuyMS/9C071DDC5E154A18B3CDE40790F3F064.png");
		System.out.println(a);
	}

}

/**
 * 
 * @ClassName: OSSConfig
 * @Description: OSS配置类
 * @author 陈辉翔
 * @date 2016年11月4日 下午3:58:36
 */
class OSSConfig{
	private  String endpoint;  		//连接区域地址
    private  String accessKeyId;  	//连接keyId
    private  String accessKeySecret;//连接秘钥
    private  String bucketName;  	//需要存储的bucketName
    private  String fileLocation;  	//图片保存路径
	
    public OSSConfig() {
    	try {
			this.endpoint = SystemConfig.getResource("endpoint");
			this.bucketName = SystemConfig.getResource("bucketName");
			this.fileLocation = SystemConfig.getResource("fileLocation");
			this.accessKeyId = SystemConfig.getResource("accessKeyId");
			this.accessKeySecret = SystemConfig.getResource("accessKeySecret");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
}
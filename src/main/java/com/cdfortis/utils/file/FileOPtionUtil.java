package com.cdfortis.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * 
 * @ClassName: FileOPtionUtil   
 * @Description: TODO(服务器文件上传工具)   
 * @author 邱仁
 * @date 2017-3-31 下午12:34:47   
 *
 */
public class FileOPtionUtil {
	private  static String configFile= Thread.currentThread().getContextClassLoader().getResource("fdfs_client.properties").getPath();
	
	/**
	 * 单文件上传到文件服务器
	 * @param upFile上传的文件
	 * @param fileSuffixName上传文件的后缀名
	 * @return 该文件在文件服务器的地址
	 * @throws Exception
	 */
	public static String uploadFile(File upFile,String fileSuffixName) throws Exception {
		FileInputStream fis = null;
		configFile = URLDecoder.decode(configFile, "utf-8");
		ClientGlobal.init(configFile);
		TrackerClient2 trackerClient  = new TrackerClient2();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, null);
	//	StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		fis = new FileInputStream(upFile);
		byte[] file_buff = null;  
		String[] results;
		if(fis != null){  
			int len = fis.available();  
			file_buff = new byte[len];
			fis.read(file_buff);  
		}
		results = storageClient.upload_file(null, file_buff,fileSuffixName, null);
		fis.close();
		trackerServer.close();
		return results[0].toString()+"/"+results[1];
	}
	
	/**
	 * 单文件上传到文件服务器
	 * @param upFile上传的文件
	 * @param fileSuffixName上传文件的后缀名
	 * @return 该文件在文件服务器的地址
	 * @throws Exception
	 */
	public static String uploadFile(InputStream fis,String fileSuffixName) throws Exception {
		configFile = URLDecoder.decode(configFile, "utf-8");
		ClientGlobal.init(configFile);
		TrackerClient2 trackerClient  = new TrackerClient2();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, null);
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		byte[] file_buff = null;  
		String[] results;
		if(fis != null){  
			int len = fis.available();  
			file_buff = new byte[len];
			fis.read(file_buff);  
		}
		results = storageClient.upload_file(null, file_buff,fileSuffixName, null);
		fis.close();
		trackerServer.close();
		return results[0].toString()+"/"+results[1];
	}
	
	/**
	 * 多文件上传
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,String> uploadFiles(List<File> upFiles) throws Exception{
		HashMap<String, String> map = new HashMap<String,String>();
		for(File file:upFiles){
			String fileName = file.getName();
			String uploadAdress = uploadFile(file,"");
			map.put(fileName, uploadAdress);
		}
		return map;
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件地址
	 * @return 0为成功，2为此文件不存在，其他的为错误
	 * @throws Exception
	 */
	public static String deleteFile(String filePath) throws Exception{
		configFile = URLDecoder.decode(configFile, "utf-8");
		ClientGlobal.init(configFile);
		TrackerClient trackerClient  = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);
		String result;
		try {
			result =String.valueOf(storageClient.delete_file1(filePath));
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}finally{
			trackerServer.close();
		}
		return result;
	}
	
	/**
	 * 修改文件（文件类型改变）
	 * @param newFile
	 * @param newFileExtName
	 * @param oldFilePath
	 * @param oldFileExtName
	 * @return 新文件地址
	 */
	public static String modifyFile(File newFile,String newFileExtName,String oldFilePath){
		try {
			String result = deleteFile(oldFilePath);
			if(result.equals("0") || result.equals("2")){
				return uploadFile(newFile,newFileExtName);
			}else{
				return "modify file error!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	/**
	 * 修改文件内容文件后缀名不变
	 * @param newFile
	 * @param oldFilePath
	 * @return 0表示成功，其他的表示错误
	 * @throws Exception
	 */
	public static String modifyFile1(File newFile,String oldFilePath)throws Exception{
		configFile = URLDecoder.decode(configFile, "utf-8");
		ClientGlobal.init(configFile);
		FileInputStream fis = new FileInputStream(newFile);
		byte[] file_buff = null;  
		String results;
		if(fis != null){  
			int len = fis.available();  
			file_buff = new byte[len];
			fis.read(file_buff);  
		}
		
		TrackerClient trackerClient  = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
		results = String.valueOf(storageClient1.modify_file1(oldFilePath, 0, file_buff));
		fis.close();
		trackerServer.close();
		return results;
	}

}

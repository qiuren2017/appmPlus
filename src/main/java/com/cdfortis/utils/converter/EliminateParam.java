package com.cdfortis.utils.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cdfortis.utils.log.Log4jUtils;


public class EliminateParam {
	private static Logger logger = Logger.getLogger(EliminateParam.class);
	
	public static String jsonFollowHandle(String json, String resultName){
		String resultJson = null;
		try {
			resultJson = eliminate(json, resultName);
		} catch (Exception e) {
			logger.error("剔除set方法异常,将使用包含set的json" + Log4jUtils.getTrace(e));
			return json;
		}
		return resultJson;
	}
	
	
	
	public static String eliminate(String json, String resultName) throws Exception{
		if(StringUtils.isBlank(json) || StringUtils.isBlank(resultName)){
			return json;
		}
		JSONObject jsonObject = new org.json.JSONObject(json);
		boolean hasResultName = jsonObject.has(resultName);
		if(!hasResultName){//若发生错误，json中并没有resultName
			return json;
		}
		Object obj = jsonObject.get(resultName);
		if(obj instanceof JSONObject){//如果resultName中是一个对象
			JSONObject jObj = jsonObject.getJSONObject(resultName);
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jObj.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			while(iterator.hasNext()){
				String keyName = iterator.next();
				if(keyName.startsWith("set")){
					iterator.remove();
				}else if(jObj.get(keyName) instanceof JSONObject){//对象嵌套另一个对象
					rejectInnerObject(jObj, keyName, map);
				}else if (jObj.get(keyName) instanceof JSONArray){//对象中嵌套一个数组
					rejectInnerArray(jObj, keyName, map);
				}
			}
			if(map.size() > 0){
				for(String key : map.keySet()){
					jObj.remove(key);
					jObj.put(key, map.get(key));
				}
			}
			jsonObject.remove(resultName);
			jsonObject.put(resultName, jObj);
		}else if(obj instanceof JSONArray){//如果resultName 中是一个对象的数组
			JSONArray array = jsonObject.getJSONArray(resultName);
			JSONArray array2 = new JSONArray();
			if(array != null && array.length() > 0){
				for(int i = 0 ; i < array.length(); i++ ){
					if(array.get(i) instanceof JSONObject){
						JSONObject jsonObj = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						@SuppressWarnings("unchecked")
						Iterator<String> iterator = jsonObj.keys();
						while(iterator.hasNext()){
							String keyName = iterator.next();
							if(keyName.startsWith("set")){
								iterator.remove();
							}else if(jsonObj.get(keyName) instanceof JSONObject){//对象嵌套另一个对象
								rejectInnerObject(jsonObj, keyName, map);
							}else if(jsonObj.get(keyName) instanceof JSONArray){//对象中嵌套一个数组
								rejectInnerArray(jsonObj, keyName, map);
							}
						}
						if(map.size() > 0){
							for(String key : map.keySet()){
								jsonObj.remove(key);
								jsonObj.put(key, map.get(key));
							}
						}
						array2.put(jsonObj);
					}else{
						//若结果集中数组中，并没有全放对象，放置了一个非jsonObject，直接返回。
						return jsonObject.toString();
					}
				}
				jsonObject.remove(resultName);
				jsonObject.put(resultName, array2);
			}
		}
		return jsonObject.toString();
	}
	//剔除JSONObject中的以set开头的变量
	private static void rejectInnerObject(JSONObject jsonObj, String keyName, Map<String, Object> map) throws Exception{
		JSONObject jobj = (JSONObject) jsonObj.get(keyName);
		@SuppressWarnings("unchecked")
		Iterator<String> it =  jobj.keys();
		while(it.hasNext()){
			String name = it.next();
			if(name.startsWith("set")){
				it.remove();
			}
		}
		map.put(keyName, jobj);
	}
	
   //剔除jsonArray中的以set开头的变量
   private static void rejectInnerArray(JSONObject jsonObj, String keyName, Map<String, Object> map) throws Exception{
	   JSONArray array = jsonObj.getJSONArray(keyName);
	   JSONArray array2 = new JSONArray();
		if(array != null && array.length() > 0){
			for(int i = 0 ; i < array.length(); i++ ){
				if(array.get(0) instanceof JSONObject){
					JSONObject jobj = array.getJSONObject(i);
					@SuppressWarnings("unchecked")
					Iterator<String> it = jobj.keys();
					while(it.hasNext()){
						String key = it.next();
						if(key.startsWith("set")){
							it.remove();
						}
					}
					array2.put(jobj);
				}else{
					//若jsonArray中放置有一个的不是jsonObject
				   map.put(keyName, array);
				   return;
				}
			}
		}
		map.put(keyName, array2);
   }
}

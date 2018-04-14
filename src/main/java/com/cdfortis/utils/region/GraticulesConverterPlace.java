package com.cdfortis.utils.region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;
import com.cdfortis.utils.config.SystemConfig;

public class GraticulesConverterPlace {
	
	public String [] getCityAndProvince(Double longitude, Double latitude){
		//根据经纬度获取结果集jsonObject
		JSONObject json = getJson(longitude, latitude);
		if(json == null){
			System.out.println("格式转换错误");
			return null;
		}
		if(json.getInteger("status") != 0){
			System.out.println("baiduAPI错误，获取地理位置失败");
			return null;
		}
		String city = json.getJSONObject("result").getJSONObject("addressComponent").getString("city");
		String province = json.getJSONObject("result").getJSONObject("addressComponent").getString("province");
		if(city ==null || province == null){
			System.out.println("该经纬度不合法");
			return null;
		}
		return new String[]{province, city};
	}
	
	//纬度30.65031789063351 经度：104.073486328125
	public JSONObject  getJson(Double longitude, Double latitude){
		//拼写url,设置 pois=0 不返回周边建筑地理位置，设置 output=json 返回json 格式文件
		String ak = null;
		try {
			ak = SystemConfig.getResource("ak");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = "http://api.map.baidu.com/geocoder/v2/?ak="+ak
				+"&callback=renderReverse&location="+latitude
				+","+ longitude +"&output=json&pois=0";
		//获取结果集
		String info = getInfo(url);
		String str  = info.substring(29, info.length() - 1);
		//将结果集转换成json
		JSONObject json = JSONObject.parseObject(str);
		return json;
	}
	
	public String getInfo(String url){
		StringBuffer buffer = new StringBuffer();
		try {
			URL oracl = new URL(url);
			URLConnection connection = oracl.openConnection();
			//设置UTF-8编码，以防格式转换出错
			InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String str;
			while((str = br.readLine()) != null){
				buffer.append(str);
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			System.out.println("网络异常 ,获取数据失败 ... ");
			e.printStackTrace();
		}
		return buffer.toString();
	}
}

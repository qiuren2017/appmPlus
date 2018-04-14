package com.cdfortis.utils.map;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	private static final double EARTH_RADIUS = 6378137; 
	/**
	 * 获取偏移坐标
	 * @param longitude 已知点的经度
	 * @param latitude	已知点的纬度
	 * @param s			范围偏移值（千米）
	 * @return
	 */
	public static Map<String, Double> getOffsetPoints(double longitude, double latitude, double s){
		Map<String, Double> map = new HashMap<String, Double>();
		// 地球半径（单位：千米）
		double R = 6371;
		double dlon;// 经度偏移度数
		double dlat;// 纬度偏移度数

		// 计算偏移度数
		dlon = rad2deg(2 * Math.asin(Math.sin(s / (2 * R)) / Math.cos(deg2rad(latitude))));
		dlat = rad2deg(s / R);

		// 组装返回数据
		map.put("latLeftTop", latitude + dlat);// 外切正方形的左上点的纬度
		map.put("lonLeftTop", longitude - dlon);// 外切正方形的左上点的经度
		
//		map.put("latRightTop", latitude + dlat);// 外切正方形的右上点的纬度
//		map.put("lonRightTop", longitude + dlon);// 外切正方形的右上点的经度
		
//		map.put("latLeftBottom", latitude - dlat);// 外切正方形的左下点的纬度
//		map.put("lonLeftBottom", longitude - dlon);// 外切正方形的左下点的经度
		
		map.put("latRightBottom", latitude - dlat);// 外切正方形的右下点的纬度
		map.put("lonRightBottom", longitude + dlon);// 外切正方形的右下点的经度
		return map;
	}
	
	 /** *//** 
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
     * @param lng1 
     * @param lat1 
     * @param lng2 
     * @param lat2 
     * @return 
     */  
    public static double getDistance(double lng1, double lat1, double lng2, double lat2)  
  {  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lng1) - rad(lng2);  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +  Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    }  
	
    
	/**
	 * 将角度转换为弧度
	 * @param degree
	 * @return
	 */
	public static double deg2rad(double degree) {
		return degree / 180 * Math.PI;
	}

	/**
	 * 将弧度转换为角度
	 * @param radian
	 * @return
	 */
	public static double rad2deg(double radian) {
		return radian * 180 / Math.PI;
	}
	
  private static double rad(double d) {  
        return d * Math.PI / 180.0;  
     } 
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		MapUtils mapUtils = new MapUtils();
		
		double num = mapUtils.getDistance(105.777281, 26.068257, 105.777569, 26.068316);
		System.out.println(num);
	}
	
}

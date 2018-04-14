package com.cdfortis.utils.math;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: Arith 精确计算类
 * @Description: 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 * @author chenhx
 * @version 1.0.0
 * @date 2017年10月24日 上午11:53:41
 */
public class Arith {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private Arith() {}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1   	 被加数
	 * @param ...v2   多参数被加数
	 * @return 多参数的和
	 */
	public static double add(double v1, double ...v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for(double v:v2){  
			BigDecimal b2 = new BigDecimal(Double.toString(v));
			b1 = b1.add(b2);
	    }  
		return b1.doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1	被减数
	 * @param ...v2	多参数减数
	 * @return 多参数的差
	 */
	public static double sub(double v1, double ...v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for(double v:v2){  
			BigDecimal b2 = new BigDecimal(Double.toString(v));
			b1 = b1.subtract(b2);
	    }  
		return b1.doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1	被乘数
	 * @param ...v2	多参数乘数
	 * @return 多参数的积
	 */
	public static double mul(double v1, double ...v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for(double v:v2){  
			BigDecimal b2 = new BigDecimal(Double.toString(v));
			b1 = b1.multiply(b2);
	    }  
		return b1.doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1	被除数
	 * @param v2	除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1	被除数
	 * @param v2	除数
	 * @param scale	表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"精确位必须为正整数或0!");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v		需要四舍五入的数字
	 * @param scale	小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	/**
	 * 将数字转换为千分位表示字符串
	 * 123456789.12 => 123,456,789.12
	 * @param namber 需要转换的数字
	 * @return 转换后的字符串
	 */
	public static String instance(String number) {
		if(StringUtils.isBlank(number)) number = "0";
		Double d = Double.parseDouble(number);
		return instance(d);
	}
	
	/**
	 * 将数字转换为千分位表示字符串
	 * 123456789.12 => 123,456,789.12
	 * @param namber 需要转换的数字
	 * @return 转换后的字符串
	 */
	public static String instance(double number) {
		return DecimalFormat.getNumberInstance().format(number);
	}
	
	/**
	 * 将数字转换为千分位表示字符串
	 * 123456789 => 123,456,789
	 * @param namber 需要转换的数字
	 * @return 转换后的字符串
	 */
	public static String instance(long number) {
		return DecimalFormat.getNumberInstance().format(number);
	}
	
	/**
	 * 将数字转换为千分位（货币）表示字符串
	 * 123456789.12 => ￥123,456,789.12
	 * @param namber 需要转换的数字
	 * @return 转换后的字符串
	 */
	public static String currencyInstance(double number) {
		return NumberFormat.getCurrencyInstance().format(number);
	}
	
	/**
	 * 将数字转换为千分位（货币）表示字符串
	 * 123456789 => ￥123,456,789
	 * @param namber 需要转换的数字
	 * @return 转换后的字符串
	 */
	public static String currencyInstance(long number) {
		return NumberFormat.getCurrencyInstance().format(number);
	}
	
	public static void main(String[] args) {
		System.out.println(Arith.instance(null));
	}
}

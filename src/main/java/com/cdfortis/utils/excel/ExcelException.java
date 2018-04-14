package com.cdfortis.utils.excel;
/**
 * 
 * @ClassName: ExcelException   
 * @Description: TODO(excel操作异常定义)   
 * @author 邱仁
 * @date 2017-3-24 上午11:30:23   
 *
 */
public class ExcelException extends Exception{
	private static final long serialVersionUID = -8462471811050000644L;

	public ExcelException() {
		// TODO Auto-generated constructor stub
	}

	public ExcelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExcelException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ExcelException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}

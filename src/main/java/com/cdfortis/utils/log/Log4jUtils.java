package com.cdfortis.utils.log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * log4j工具类
 * @author Jackie
 *
 */
public class Log4jUtils {
	
	
	/**
	 * log4j日志记录
	 * @param t
	 * @return
	 */
	public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}

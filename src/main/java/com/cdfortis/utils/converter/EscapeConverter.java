package com.cdfortis.utils.converter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.web.util.HtmlUtils;

/**
 * 用于防止XSS攻击
 * 
 * 
 */
public class EscapeConverter extends StrutsTypeConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o != null) {
			if (o instanceof String[]) {
				String[] str = (String[]) o;
				if (str != null && str[0] != null) {
					return HtmlUtils.htmlEscape((String) ((String[]) o)[0]);
				}
			} else if (o instanceof String) {
				String str = (String) o;
				if (str != null) {
					return HtmlUtils.htmlEscape(str);
				}
			}
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		return values;
	}

}

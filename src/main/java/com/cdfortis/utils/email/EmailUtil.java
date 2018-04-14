package com.cdfortis.utils.email;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cdfortis.utils.config.SystemConfig;
import com.sun.mail.util.MailConnectException;

import freemarker.template.Template;

/**
 * 
 * @ClassName: EmailUtil
 * @Description: 邮件操作工具类
 * @author chenhx
 * @date 2017年7月31日 上午10:16:15
 * @version 0.0.1
 */
public class EmailUtil{

	private static final int SUCC = 0;		//成功
	private static final int FAIL = -1;		//失败
	private static final int AUTHENTICATION  = -2;			//发送者身份验证异常
	private static final int CONNECT_ERR  = -3;				//网络异常
	private static final int NULL_MAIL  = -4;				//收件人空指针异常
	

	/**
	 * 
	 * @MethodName: sendMailByFreeMarker
	 * @Description: 通过FreeMarker模板发送邮件
	 * @param config void 邮件配置文件
	 */
	public static Result sendMailByFreeMarker(EmailConfig config) {
		Result result = new Result(SUCC, "邮件发送成功!");
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(config.getHost());
			mailSender.setPort(config.getPort());
			mailSender.setUsername(config.getFrom());
			mailSender.setPassword(config.getFromPwd());
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
			helper.setFrom(config.getFrom());
			String[] to = config.getToArr();
			String[] Cc = config.getCcArr();
			if((to==null&&Cc==null)||(to.length<=0&&Cc.length<=0)){
				result.setResultCode(NULL_MAIL);
				result.setMsg("收件人和抄送人不能同时为空！");
				return result;
			}
			
			if(to!=null&&to.length>0) helper.setTo(to);		//接收
			if(Cc!=null&&Cc.length>0) helper.setCc(Cc);		//抄送
			helper.setSubject(config.getSubject());
			helper.setText(config.getText(), true);
			//图片嵌入到html文件中
			Map<String,File> map = config.getInLineMap();
			if(map!=null){
				for (String key : map.keySet()) {
					helper.addInline(key, map.get(key));
				}
			}
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			result.setMsg("邮件发送失败!");
			result.setResultCode(FAIL);
			return result;
		} catch (MailSendException e) {				
			e.printStackTrace();
			Exception ex = e.getMessageExceptions()[0];	
			if(ex instanceof MailConnectException){			//网络连接异常
				result.setResultCode(CONNECT_ERR);
				result.setMsg("网络连接异常!");
				return result;
			}else if(ex instanceof SendFailedException){	//邮箱地址错误异常
				Address[] address = ((SendFailedException) ex).getInvalidAddresses();	//获取错误邮箱数组
				return removeErrMailResend(address, config);		//调用移除错误邮箱并重新发送邮件
			}else if(ex instanceof NullPointerException){
				result.setResultCode(NULL_MAIL);
				result.setMsg("收件人为空!");
				return result;
			}
		} catch (MailAuthenticationException e) {	//身份认证异常
			e.printStackTrace();
			result.setResultCode(AUTHENTICATION);
			result.setMsg("发件箱配置或密码错误!");
			return result;
		} catch (Exception e) {						//其他异常
			e.printStackTrace();
			result.setMsg("邮件发送失败!");
			result.setResultCode(FAIL);
			return result;
		}
		return result;
	}

    /**
     * 
     * @MethodName: getMailText
     * @Description: 获取邮件文本（通过加载FreeMarker模板HTML）
     * @param freemarkerConfig 配置文件
     * @param freemarkerFileName 模板名 eg: "classNotice.html"
     * @param map 模板动态数据值
     * @return
     * @throws Exception String
     */
    public static String getMailText(FreeMarkerConfigurer freemarkerConfig,
    		String freemarkerFileName,Map<String,String> map) throws Exception {  
        // 通过指定模板名获取FreeMarker模板实例  
        Template template = freemarkerConfig.getConfiguration().getTemplate(freemarkerFileName);   
        // 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。  
        String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);  
        return htmlText;  
    } 

    /**
     * 
     * @MethodName: removeErrMailResend
     * @Description: 移除错误邮箱并重新调用发送邮件
     * @param address 错误邮箱数组
     * @param config  邮件配置
     * @return Result 返回结果(result.result:错误邮箱数组)
     */
    private static Result removeErrMailResend(Address[] address,EmailConfig config){
    	Result result = new Result();
    	if(address==null||address.length<=0) return result; 
    	String[] toArr = config.getToArr();
    	String[] CcArr = config.getCcArr();
    	for (int i=0,len=address.length;i<len;i++) {
			String a = address[i].toString();
			toArr = ArrayUtils.removeElement(toArr, a);
			CcArr = ArrayUtils.removeElement(CcArr, a);
		}
    	config.setToArr(toArr);
    	config.setCcArr(CcArr);
    	Result newResult =  sendMailByFreeMarker(config);		//递归调用重新发送邮件
    	result.setResultCode(newResult.getResultCode());
    	result.setMsg(newResult.getMsg());
    	result.setResult(address); 		//返回错误邮箱数组对象，提示前台用
    	return result;
    }
    
    
    /**
     * 
     * @ClassName: EmailConfig
     * @Description: 邮件工具配置类
     * @author chenhx
     * @date 2017年8月2日 下午3:13:21
     */
    public static class EmailConfig{
    	private String host;		//发送邮件的host 
    	private int port;			//发送端口
    	private String from;		//邮件发送者
    	private String fromPwd;		//邮件发送者密码
    	private String[] toArr;		//邮件接收者数组
    	private String[] CcArr;		//邮件抄送数组
    	private String subject;		//邮件主题
    	private String text;		//邮件文本
    	private Map<String,File> inLineMap;		//嵌入到模板中的图片
    	
    	public EmailConfig() {
    		super();
    		this.inLineMap = new HashMap<>();
		}
		public EmailConfig(String host, int port, String from, String fromPwd,
				String[] toArr,String[] CcArr, String subject, String text) {
			this();
			this.host = host;
			this.port = port;
			this.from = from;
			this.fromPwd = fromPwd;
			this.toArr = toArr;
			this.CcArr = CcArr;
			this.subject = subject;
			this.text = text;
			
		}
		/**
		 * 
		 * @Description: 通过依赖配置文件获取host和port
		 * @param from
		 * @param fromPwd
		 * @param to
		 * @param toArr
		 * @param CcArr
		 * @param subject
		 * @param text
		 */
		public EmailConfig(String from, String fromPwd,
				String[] toArr,String[] CcArr, String subject, String text) {
			this(null,-1,from,fromPwd,toArr,CcArr,subject,text);
			try {
				this.host = SystemConfig.getResource("emailHost");
				this.port = Integer.parseInt(SystemConfig.getResource("emailPort"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 
		 * @Description: 通过依赖配置文件获取host和port,from,pwd
		 * @param toArr   邮件接收者数组 eg: {"aa@qq.com","bb@qq.com"}
		 * @param CcArr   邮件抄送者数组 eg: {"cc@qq.com","dd@qq.com"}
		 * @param subject 邮件主题
		 * @param text    邮件正文
		 */
		public EmailConfig(String[] toArr,String[] CcArr, String subject, String text) {
			this(null,-1,null,null,toArr,CcArr,subject,text);
			try {
				this.host = SystemConfig.getResource("emailHost");
				this.port = Integer.parseInt(SystemConfig.getResource("emailPort"));
				this.from = SystemConfig.getResource("emailFrom");
				this.fromPwd = SystemConfig.getResource("emailPwd");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 
		 * @Description: 通过依赖配置文件获取host和port,from,pwd
		 * @param toArr   邮件接收者数组 eg: {"aa@qq.com","bb@qq.com"}
		 * @param subject 邮件主题
		 * @param text    邮件正文
		 */
		public EmailConfig(String[] toArr, String subject, String text) {
			this(null,-1,null,null,toArr,null,subject,text);
			try {
				this.host = SystemConfig.getResource("emailHost");
				this.port = Integer.parseInt(SystemConfig.getResource("emailPort"));
				this.from = SystemConfig.getResource("emailFrom");
				this.fromPwd = SystemConfig.getResource("emailPwd");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 
		 * @MethodName: putInLineVal
		 * @Description: 向配置中追加模板参数
		 * @param key
		 * @param file void
		 */
		public void putInLineVal(String key,File file){
			this.inLineMap.put(key, file);
		}
		
		public String getFrom() {
    		return from;
    	}
    	public void setFrom(String from) {
    		this.from = from;
    	}
    	public String getFromPwd() {
    		return fromPwd;
    	}
    	public void setFromPwd(String fromPwd) {
    		this.fromPwd = fromPwd;
    	}
    	public String[] getToArr() {
    		return toArr;
    	}
    	public void setToArr(String[] toArr) {
    		this.toArr = toArr;
    	}
    	public String getSubject() {
    		return subject;
    	}
    	public void setSubject(String subject) {
    		this.subject = subject;
    	}
    	public String getText() {
    		return text;
    	}
    	public void setText(String text) {
    		this.text = text;
    	}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public Map<String, File> getInLineMap() {
			return inLineMap;
		}
		public void setInLineMap(Map<String, File> inLineMap) {
			this.inLineMap = inLineMap;
		}
		public String[] getCcArr() {
			return CcArr;
		}
		public void setCcArr(String[] ccArr) {
			CcArr = ccArr;
		}
    	
    }
}


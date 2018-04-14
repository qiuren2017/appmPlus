package com.cdfortis.utils.email;

public class Result {
    /**
     * 成功
     */
    public static final int SUCC = 0;
    
    /**
     * 失败
     */
    public static final int FAIL = -1;
    
    /**
     * 系统错误
     */
    public static final int ERROR = -999;
    
    /**
     * 没有注册
     */
    public static final int NO_REG = -2;
    
    /**
     * 缺少参数
     */
    public static final int NO_PARAM = -3;
    
    /**
     * 没有手机验证码;
     */
    public static final int NO_CODE = -4;
    
    /**
     * 验证码时间超时;
     */
    public static final int LONG_TIME = -5;
    
    /**
     * 密码错误
     */
    public static final int WRONG_PASS = -6;
    
    /**
     * 没有登陆
     */
    public static final int NO_LOGIN = -7;
    
    /**
     * 没有查到信息
     */
    public static final int IS_NULL = -8;
    
    /**
     * 手机号已经存在
     */
    public static final int PHONE_NUM_EXIST = -9;
    
    /**
     * IMEI号已经存在
     */
    public static final int IMEI_NUM_EXIST = -10;
    
    /**
     * 验证码发送太频繁
     */
    public static final int TOO_OFTEN = -11;
    
    /**
     * 60秒后才可以发送短信
     */
    public static final int WAIT_60_SECONDS = -12;
    
    /**
     * 不允许取消订单
     */
    public static final int ORDER_BACK_FAIL = -13;
    
    /**
     * 订单确认完成失败（不是在已发货或已送达的订单状态下完成）
     */
    public static final int ORDER_AFFIRM_FAIL = -14;
    
    /**
     * 验证码发送太频繁，一小时内只能发送3次
     */
    public static final int TOO_OFTEN2 = -16;
    
    /**
     * 用户已被绑定
     */
    public static final int USER_BIND = -18;
    
    /**
     * 用户未注册
     */
    public static final int PHONE_NOT_REG = -19;
    
    /**
     * 订单状态不匹配
     */
    public static final int ORDER_MISMATCHING = -20;
    
    /**
     * 账号被禁用
     */
    public static final int USER_DISABLE = -21;
    
    /**
     * 没有访问权限 （手机设备登录 访问必须 用户登录访问 的接口时，返回的错误代码）
     */
    public static final int NO_AUTH = -22;
    
    /**
     * 活动结束
     */
    public static final int ACTIVITY_END = -23;
    
    /**
     * 药店已被禁用
     */
    public static final int STORE_END = -24;
    
    /**
     * 订单金额不能为小于等于 0
     */
    public static final int AMOUNT_FAIL = -25;

    /**
     * 某件商品下架
     */
    public static final int SOLD_OUT = -26;
    
    /**
     * 不能添加用户自己为好友
     */
    public static final int SAME_PHONE_NUM = -27;
    
    /**
     * 好友已被添加
     */
    public static final int FRIEND_REPEAT = -28;
    
    /**
     * 设备已注册
     */
    public static final int UNIT_REG = -29;
    
    /**
     * 合约失效
     */
    public static final int CONTRACT_OVER = -30;
    
    /**
     * 用户有该医生合约
     */
    public static final int DOCTOR_CONTRACT = -31;
    
    /**
     * 未付款
     */
    public static final int NON_PAYMENT = -32;
    
    /**
     * 未找到该医生
     */
    public static final int DOC_NOT_FOUND = -33;
    
    /**
     * 合约已更换过医生
     */
    public static final int CONTRACT_CHANGE = -34;
    
    /**
     * 将错误代码转化为信息
     * 
     * @param errCode
     * @return
     */
    public static String toString(int errCode) {
        switch (errCode) {
            case SUCC:
                return "成功";
            case FAIL:
                return "失败";
            case ERROR:
                return "系统错误";
            case NO_REG:
                return "没有注册";
            case NO_PARAM:
                return "缺少参数";
            case NO_CODE:
                return "验证码错误";
            case LONG_TIME:
                return "验证码时间超时";
            case WRONG_PASS:
                return "用户名或密码错误";
            case NO_LOGIN:
                return "没有登陆";
            case IS_NULL:
                return "没有查到信息";
            case PHONE_NUM_EXIST:
                return "手机号已经注册";
            case IMEI_NUM_EXIST:
                return "IMEI号已经存在";
            case TOO_OFTEN:
                return "验证码发送太频繁";
            case WAIT_60_SECONDS:
                return "验证码发送太频繁，60秒后再尝试发送";
            case ORDER_BACK_FAIL:
                return "当前订单不能取消";
            case ORDER_AFFIRM_FAIL:
                return "确认订单失败";
            case TOO_OFTEN2:
                return "验证码一小时内重发次数超过3次";
            case ORDER_MISMATCHING:
                return "订单状态已改变，请刷新";
            case USER_BIND:
                return "用户已被绑定";
            case PHONE_NOT_REG:
                return "该用户未注册";
            case USER_DISABLE:
                return "该账户已被禁用";
            case NO_AUTH:
                return "您还没有登录，请登录使用...";
                // case NO_AUTH:
                // return "没有访问权限";
            case ACTIVITY_END:
                return "活动结束";
            case STORE_END:
                return "药店已被禁用";
            case AMOUNT_FAIL:
                return "订单金额不能小于等于0";
            case SOLD_OUT:
                return "订单中一件或多件商品已下架";
            case SAME_PHONE_NUM:
                return "不能添加用户自己为好友";
            case FRIEND_REPEAT:
                return "好友已被添加";
            case UNIT_REG:
                return "设备已注册";
            case CONTRACT_OVER:
                return "合约失效";
            case DOCTOR_CONTRACT:
                return "用户有该医生合约";
            case NON_PAYMENT:
                return "未付款";
            case DOC_NOT_FOUND:
                return "未找到该医生";
            case CONTRACT_CHANGE:
                return "合约已换过医生";
            default:
                return "其他错误";
        }
    }

	/**
	 *方法执行是否成功
	 */
	private int resultCode =FAIL;
	
	/**
	 * 提示信息
	 */
	
	private String msg = "";
	/**
	 * 其他信息
	 */
	private Object result= null;
	
	
	public Result(int resultCode, String msg, Object result) {
		super();
		this.resultCode = resultCode;
		this.msg = msg;
		this.result = result;
	}
	
	public Result(int resultCode, String msg) {
		super();
		this.resultCode = resultCode;
		this.msg = msg;
	}

	public Result(int resultCode) {
		super();
		this.resultCode = resultCode;
	}

	public Result(int resultCode, Object result) {
		super();
		this.resultCode = resultCode;
		this.result = result;
	}

	public Result() {
		super();
	}

	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}

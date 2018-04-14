package com.cdfortis.utils.table;
/**
 * <p>Title: 根据药店或连锁ID返回药品表的表名</p> 
 * <p>Description: 工具方法</p> 
 * <p>createDate: 2017年6月5日上午10:28:46</p> 
 * @author zhangNan
 */
public class DrugTableMapped {
	
	private final static String CHAIN_PREFIX = "b_chain_drug_";
	
	private final static String STORE_PREFIX = "b_store_drug_";
	
	private DrugTableMapped(){
		throw new AssertionError("[Tool utils can't be instantiation]");
	}
	/**
	 * @Title: getChainTableName 
	 * @Description: 根据连锁Id返回表名，取连锁ID最后一位拼接b_chain_drug_
	 * 				若连锁ID为空，则返回null
	 * @CreateDate: 2017年6月6日下午2:50:54
	 * @author: zhangn
	 */
	public static String getChainTableName(Long chainId){
		if(chainId == null){
			return null;
		}
		String chainValue = String.valueOf(chainId);
		int length = chainValue.length();
		return CHAIN_PREFIX.concat(chainValue.substring(length - 1, length));
	}
	/**
	 * @Title: getStoreTableName 
	 * @Description: 根据药店ID返回药店的表名，取药店ID最后一位拼接b_store_drug_
	 * 				其中若药店ID为空，则返回null
	 * @CreateDate: 2017年6月6日下午2:51:34
	 * @author: zhangn
	 */
	public static String getStoreTableName(Long storeId){
		if(storeId == null){
			return null;
		}
		String storeValue = String.valueOf(storeId);
		int length = storeValue.length();
		return STORE_PREFIX.concat(storeValue.substring(length - 1, length));
	}
}

package com.bccv.threedimensionalworld.tool;

/**
 * 常量集合。（请为每个常量添加详细的注释）
 * @author WuYelin
 *
 */
public interface ConstantValue {

	/**
	 * 字符集
	 */
	String CHARSET = "UTF-8";
	/**
	 * 分页的数据条数
	 */
	int PAGE_SIZE = 20;
	
	/**
	 * API接口
	 */
	String GetAdsList = "http://127.0.0.1/rr/1/index.php/Api/Index/iPhone_v1/opt/getShareslist";
	
	String GetMyAdsList = "http://127.0.0.1/rr/1/index.php/Api/Index/iPhone_v1/opt/shareList";
}

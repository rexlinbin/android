package com.bccv.ebook.network;

/**
 * 回调接口
 */
public interface HttpCallback {
	/**
	 * 回调方法
	 * 
	 * @param object
	 *            返回的数据对象
	 */
	public void onResult(NetResBean response);

	public void onCancel();

	public void onError(String errorMsg);
}
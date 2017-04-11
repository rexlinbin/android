package com.bccv.ebook.network;

import android.content.Context;

public class HttpReqImpl extends HttpReq {

	private NetResBean dataBean;

	public HttpReqImpl(Context context, String url, ReqestMethod reqMethod,
			HttpCallback callback) {
		this.mContext = context;
		this.mUrl = url;
		this.reqMethod = reqMethod;
		this.mCallBack = callback;
	}

	/**
	 * 设置需要转换的Bean
	 * 
	 * @param dataBean
	 */
	public void setReportBean(NetResBean dataBean) {
		this.dataBean = dataBean;
	}

	public NetResBean getReportBean() {
		return this.dataBean;
	}

	@Override
	protected NetResBean processResponse(String response) {
		// TODO Auto-generated method stub
		if (dataBean == null) {
			dataBean = new NetResBean() {
				@Override
				public void parseData(String data) {
				}
			};

		}
		dataBean.setData(response);
		return dataBean;
	}
}

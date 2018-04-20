package com.mm.tnxrs.app.controller;

import com.jfinal.core.Controller;
import com.mm.tnxrs.common.bean.BaseResponse;
import com.mm.tnxrs.common.bean.Code;

public class BaseController extends Controller{

	/**
	 * 响应接口不存在
	 */
	public void render404() {
		renderJson(new BaseResponse(Code.NOT_FOUND));
	}
	
	/**
	 * 响应参数错误
	 * @param message
	 */
	public void renderArgumentError(String message){
		renderJson(new BaseResponse(Code.ARGUMENT_ERROR, message));
	}
	
	/**
	 * 响应操作成功
	 * @param message
	 */
	public void renderSuccess(String message){
		renderJson(new BaseResponse(Code.SUCCESS,message));
	}
	
	/**
	 * 响应操作失败
	 * @param message
	 */
	public void renderFailed(String message){
		renderJson(new BaseResponse(Code.FAILED, message));
	}
	
	/**
	 * 判断请求类型是否相同
	 * @param method
	 * @return
	 */
	protected boolean methodType(String method) {
		return getRequest().getMethod().equalsIgnoreCase(method);
	}
	
}

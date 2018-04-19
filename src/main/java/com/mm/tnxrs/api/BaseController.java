package com.mm.tnxrs.api;

import java.lang.reflect.Array;
import java.util.List;

import com.jfinal.core.Controller;
import com.mm.tnxrs.common.Require;
import com.mm.tnxrs.common.bean.BaseResponse;
import com.mm.tnxrs.common.bean.Code;
import com.mm.tnxrs.common.utils.StringUtils;

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
	
	/**
	 * 判断参数值是否为空
	 * @param rules
	 * @return
	 */
	public boolean notNull(Require rules){
		if(rules == null || rules.getLength() < 1)
			return true;
		
		for(int i=0,total=rules.getLength();i<total;i++){
			Object key = rules.get(i);
			String message = rules.getMessage(i);
			BaseResponse response = new BaseResponse(Code.ARGUMENT_ERROR);
			
			if(key == null){
				renderJson(response.setMessage(message));
				return false;
			}
			
			if(key instanceof String && StringUtils.isEmpty((String)key)){
				renderJson(response.setMessage(message));
				return false;
			}
			
			if(key instanceof Array){
				Object[] arr = (Object[]) key;
				
				if(arr.length < 1){
					renderJson(response.setMessage(message));
					return false;
				}
			}
		}
		
		return true;
	}
}

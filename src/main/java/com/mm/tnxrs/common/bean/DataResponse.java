package com.mm.tnxrs.common.bean;

import com.alibaba.fastjson.JSONObject;

public class DataResponse extends BaseResponse{
	private Object data;
	
	public DataResponse(){
		super();
	}
	
	public DataResponse(Object data){
		this.data = data;
	}
	
	public DataResponse(Integer code){
		super(code);
	}
	
	public DataResponse(Integer code,String message){
		super(code, message);
	}
	
	public DataResponse setData(Object data){
		this.data = data;
		return this;
	}
	
	public Object getData(){
		return data;
	}
	
	public DataResponse put(String key,Object value) {
		if (data == null) {
			data = new JSONObject();
		}
		((JSONObject)data).put(key, value);
		return this;
	}
}

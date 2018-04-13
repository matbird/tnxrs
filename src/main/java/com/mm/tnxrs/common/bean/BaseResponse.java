package com.mm.tnxrs.common.bean;

public class BaseResponse {

	private Integer code = Code.SUCCESS;
	private String message;
	
	public BaseResponse(){
		
	}
	
	public BaseResponse(String message) {
		this.message = message;
	}
	
	public BaseResponse(Integer code) {
		this.code = code;
	}
	
	public BaseResponse(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public BaseResponse setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public BaseResponse setMessage(String message) {
		this.message = message;
		return this;
	}
}

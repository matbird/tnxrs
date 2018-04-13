package com.mm.tnxrs.common.bean;

public interface Code {

int SUCCESS = 1;
	
	int FAILED = 0;
	
	/**
	 * 参数错误
	 */
	int ARGUMENT_ERROR = 2;
	
	/**
	 * 服务器错误
	 */
	int ERROR = 500;
	
	/**
	 * 接口不存在
	 */
	int NOT_FOUND = 404;
	
	/**
	 *  token 无效
	 */
	int TOKEN_INVALID = 422;
	
	/**
	 * 账号已存在
	 */
	int ACCOUNT_EXISTS = 3;
	
	/**
	 * 验证码错误
	 */
	int CODE_ERROR = 4;
}

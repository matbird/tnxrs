package com.mm.tnxrs.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.mm.tnxrs.common.bean.BaseResponse;
import com.mm.tnxrs.common.bean.Code;

public class ErrorInterceptor implements Interceptor{
private static final Logger log = LoggerFactory.getLogger(ErrorInterceptor.class);
	
	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			inv.getController().renderJson(new BaseResponse(Code.ERROR,"server error"));
		}
	}
}

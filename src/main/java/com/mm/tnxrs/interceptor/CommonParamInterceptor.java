package com.mm.tnxrs.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.mm.tnxrs.common.bean.BaseResponse;
import com.mm.tnxrs.common.bean.Code;
import com.mm.tnxrs.common.bean.DataResponse;

/**
 * 通用参数校验
 * deviceId
 * deviceType
 * version
 * channelType
 * @author Administrator
 *
 */
public class CommonParamInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		// 所有请求都走post
		if(!"post".equalsIgnoreCase(c.getRequest().getMethod())){
			c.renderJson(new BaseResponse(Code.FAILED,"请使用post请求,当前"+c.getRequest().getMethod()));
			return ;
		}
		
		
	}
}

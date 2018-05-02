package com.mm.tnxrs.web.reg;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.mm.tnxrs.common.kit.IpKit;

public class RegController extends Controller{
	
	private static final RegService srv = RegService.me;
	
	public void index() {
		
	}

	/**
	 * 注册操作
	 */
	@Before(RegValidator.class)
	public void save() {
		String ip = IpKit.getRealIp(getRequest());
		Ret ret = srv.reg(getPara("userName"), getPara("password"), getPara("nickName"), ip);
		if(ret.isOk()) {
			ret.set("regEmail", getPara("userName"));
		}
		renderJson(ret);
	}
	
	public void reSendActivateEmail() {
		Ret ret = srv.reSendActivateEmail(getPara("email"));
		renderJson(ret);
	}
	
	/**
	 * 激活
	 */
	public void activate() {
		Ret ret = srv.activate(getPara("authCode"));
		setAttr("ret", ret);
		render("activate.html");
	}
	
	/**
	 * 显示还未激活页面
	 */
	public void notActivated() {
		render("not_activated.html");
	}
	
	public void captcha() {
		renderCaptcha();
	}
}

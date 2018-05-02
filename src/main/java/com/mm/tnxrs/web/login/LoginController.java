package com.mm.tnxrs.web.login;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.mm.tnxrs.common.kit.IpKit;

public class LoginController extends Controller{
	
	static final LoginService srv = LoginService.me;
	
	public void index() {
		keepPara("returnUrl");
		render("index.html");
	}

	/**
	 * 登录
	 */
	@Before(LoginValidator.class)
	public void doLogin() {
		boolean keepLogin = getParaToBoolean("keepLogin",false);
		String loginIp = IpKit.getRealIp(getRequest());
		Ret ret = srv.login(getPara("userName"), getPara("password"), keepLogin, loginIp);
		if(ret.isOk()) {
			String sessionId = ret.getStr(LoginService.sessionIdName);
			int maxAgeInSeconds = ret.getAs("maxAgeInSeconds");
			setCookie(LoginService.sessionIdName, sessionId, maxAgeInSeconds,true);
			setAttr(LoginService.loginAccountCacheName, ret.get(LoginService.loginAccountCacheName));
			
			ret.set("returnUrl", getPara("returnUrl", "/"));
		}
		renderJson(ret);
	}
	
	@Clear
	@ActionKey("/logout")
	public void logout() {
		srv.logout(getCookie(LoginService.sessionIdName));
		removeCookie(LoginService.sessionIdName);
		redirect("/");
	}
	
	/**
	 * 显示忘记密码页面
	 */
	public void forgetPassword() {
		render("forget_password.html");
	}
	
	public void sendRetrievePasswordEmail() {
		Ret ret = srv.sendRetrievePasswordAuthEmail(getPara("email"));
		renderJson(ret);
	}
	
	/**
	 * 1：keepPara("authCode") 将密码找回链接中问号挂参的 authCode 传递到页面
	 * 2：在密码找回页面中与用户输入的新密码一起传回给 doPassReturn 进行密码修改
	 */
	public void retrievePassword() {
		keepPara("authCode");
		render("retrieve_password.html");
	}
	
	/**
	 * ajax 密码找回
	 */
	public void doRetrievePassword() {
		Ret ret = srv.retrievePassword(getPara("authCode"), getPara("newPassword"));
		renderJson(ret);
	}
	
	public void captcha() {
		renderCaptcha();
	}
}

package com.mm.tnxrs.web.login;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mm.tnxrs.common.authcode.AuthCodeService;
import com.mm.tnxrs.common.kit.EmailKit;
import com.mm.tnxrs.common.model.Account;
import com.mm.tnxrs.common.model.AuthCode;
import com.mm.tnxrs.common.model.Session;

public class LoginService {

	public static final LoginService me = new LoginService();
	private final Account dao = new Account().dao();
	
	// 存放登录用户的cacheName
	public static final String loginAccountCacheName = "loginAccount";
	
	// "tnxrsId" 仅用于cookie名称,其他地方如cache中全部用sessionId作为key
	public static final String sessionIdName = "tnxrsId";
	
	public Ret login(String userName,String password,boolean keepLogin,String loginIp) {
		userName = userName.toLowerCase().trim();
		password = password.trim();
		Account loginAccount = dao.findFirst("select * from account where userName = ? limit 1",userName);
		if(loginAccount == null) {
			return Ret.fail("msg", "用户名或密码不正确");
		}
		if(loginAccount.isStatusLocked()) {
			return Ret.fail("msg", "账号已被锁定");
		}
		if(loginAccount.isStatusReg()) {
			return Ret.fail("msg", "账号未激活,请先激活账号");
		}
		
		String salt = loginAccount.getSalt();
		String hashedPass = HashKit.sha256(salt+password);
		// 密码未通过验证
		if(loginAccount.getPassword().equals(hashedPass) == false) {
			return Ret.fail("msg", "密码不正确");
		}
		
		// 如果用户勾选保持登录,暂定过期时间为 3 年,否则为120分钟,单位 秒
		long liveSeconds = keepLogin ? 3 * 365 * 24 * 60 * 60 : 120 * 60;
		// 传递给控制层的cookie
		int maxAgeInSeconds = (int) (keepLogin ? liveSeconds : -1);
		// 用于设置session的过期时间,需要转换成毫秒
		long expireAt = System.currentTimeMillis() + (liveSeconds * 1000);
		// 保存登录session到数据库
		Session session = new Session();
		String sessionId = StrKit.getRandomUUID();
		session.setId(sessionId);
		session.setAccountId(loginAccount.getId());
		session.setExpireAt(expireAt);
		if(!session.save()) {
			return Ret.fail("msg","session保存失败,请联系管理员");
		}
		
		loginAccount.removeSensitiveInfo();
		loginAccount.put("sessionId", sessionId);
		CacheKit.put(loginAccountCacheName, sessionId, loginAccount);
		
//		TODO 保存登录日志
//		createLoginLog(loginAccount.getId(), loginIp);
		
		return Ret.ok(sessionIdName, sessionId)
				.set(loginAccountCacheName, loginAccount)
				.set("maxAgeInSeconds", maxAgeInSeconds);
	}
	
	/**
	 * 通过sessionId从缓存中获取账户account
	 * @param sessionId
	 * @return
	 */
	public Account getLoginAccountWithSessionId(String sessionId) {
		return CacheKit.get(loginAccountCacheName, sessionId);
	}
	
	/**
	 * 通过sessionId获取登录用户信息
	 * 
	 * 1.先从缓存里去,如果取到则返回,没取到从数据库中获取
	 * 2.在数据库中取,如果有,则检测会话是否过期,过期则清除记录
	 * 	 如果没过期,则先放一份在缓存,然后再返回
	 * @param sessionId
	 * @param loginIp
	 * @return
	 */
	public Account loginWithSessionId(String sessionId,String loginIp) {
		Session session = Session.dao.findById(sessionId);
		if(session == null) { // 会话不存在
			return null;
		}
		if(session.isExpired()) { // 会话已过期,清除会话
			session.delete();
			return null;
		}
		Account loginAccount = dao.findById(session.getAccountId());
		if(loginAccount != null && loginAccount.isStatusOk()) {
			loginAccount.removeSensitiveInfo();
			loginAccount.put("sessionId", sessionId);
			CacheKit.put(loginAccountCacheName, sessionId, loginAccount);
			
			// TODO 创建登录日志
//			createLoginLog(loginAccount.getId(), loginIp);
			return loginAccount;
		}
		return null;
	}
	
	public void logout(String sessionId) {
		if(sessionId != null) {
			CacheKit.remove(loginAccountCacheName, sessionId);
			Session.dao.deleteById(sessionId);
		}
	}
	
	/**
	 * 发送密码找回邮件
	 * @param userName
	 * @return
	 */
	public Ret sendRetrievePasswordAuthEmail(String userName) {
		if(StrKit.isBlank(userName) || userName.indexOf('@') == -1) {
			return Ret.fail("msg", "email格式不正确");
		}
		userName = userName.toLowerCase().trim();
		Account account = dao.findFirst("select * from account where userName=? limit 1",userName);
		if(account == null) {
			return Ret.fail("msg","您输入的email还没有注册");
		}
		
		String authCode = AuthCodeService.me.createRetrievePasswordAuthCode(account.getId());
		String title = "TNXRS 密码找回邮件";
		String content = "在浏览器地址栏里输入并访问下面链接即可重置密码：\n\n"
				+ " http://localhost/login/retrievePassword?authCode="
				+ authCode;
		
		String emailServer = PropKit.get("emailServer");
		String fromEmail = PropKit.get("fromEmail");
		String emailPass = PropKit.get("emailPass");
		String toEmail = account.getStr("userName");
		
		try {
			EmailKit.sendEmail(emailServer, fromEmail, emailPass, toEmail, title, content);
			return Ret.ok("msg", "密码找回邮件已发送至邮箱");
		} catch (Exception e) {
			return Ret.fail("msg", "密码找回邮件发送失败,请联系管理员");
		}
	}
	
	/**
	 * 找回密码
	 * @param authCodeId
	 * @param newPassword
	 * @return
	 */
	public Ret retrievePassword(String authCodeId,String newPassword) {
		if(StrKit.isBlank(newPassword)) {
			return Ret.fail("msg", "新密码不能为空");
		}
		if(newPassword.length() < 6) {
			return Ret.fail("msg", "新密码长度不能小于6位");
		}
		
		AuthCode authCode = AuthCodeService.me.getAuthCode(authCodeId);
		if(authCode != null && authCode.isValidRetrievePasswordAuthCode()) {
			String salt = HashKit.generateSaltForSha256();
			newPassword = HashKit.sha256(salt+newPassword);
			int accountId = authCode.getAccountId();
			int result = Db.update("update account set password=?,salt=? where id = ? limit 1", newPassword,salt,accountId);
			if(result > 0) {
				return Ret.ok("msg", "密码更新成功,现在即可登录");
			}else {
				return Ret.fail("msg", "未找到账号,请联系管理员");
			}
		}else {
			return Ret.fail("msg", "authCode不存在或已经失效,可以尝试重新发送找回密码邮件");
		}
	}
}

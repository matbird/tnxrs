package com.mm.tnxrs.web.reg;

import java.util.Date;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.mm.tnxrs.common.authcode.AuthCodeService;
import com.mm.tnxrs.common.kit.EmailKit;
import com.mm.tnxrs.common.model.Account;
import com.mm.tnxrs.common.model.AuthCode;

public class RegService {

	public static final RegService me = new RegService();
	private static Account dao = new Account().dao();
	
	/**
	 * 用户名是否被注册
	 * @param userName
	 * @return
	 */
	public boolean isUserNameExists(String userName) {
		userName = userName.toLowerCase().trim();
		return Db.queryInt("select id from account where userName=? limit 1",userName) != null;
	}
	
	/**
	 * 昵称不区分大小写,避免多个昵称看上去差不多的情况
	 * @param nickName
	 * @return
	 */
	public boolean isNickNameExists(String nickName) {
		nickName = nickName.toLowerCase().trim();
		return Db.queryInt("select id from account where lower(nickName)=? limit 1",nickName) != null;
	}
	
	/**
	 * 用户注册操作
	 * @param userName
	 * @param password
	 * @param nickName
	 * @param ip
	 * @return
	 */
	public Ret reg(String userName,String password,String nickName,String ip) {
		if(StrKit.isBlank(userName) || StrKit.isBlank(password) || StrKit.isBlank(nickName)) {
			return Ret.fail("msg", "邮箱,昵称或密码不能为空");
		}
		
		userName = userName.toLowerCase().trim(); // 邮箱存小写
		nickName = nickName.trim();
		password = password.trim();
		
		if (nickName.contains("@") || nickName.contains("＠")) { // 全角半角都要判断
			return Ret.fail("msg", "昵称不能包含 \"@\" 字符");
		}
		if (nickName.contains(" ") || nickName.contains("　")) { // 检测是否包含半角或全角空格
			return Ret.fail("msg", "昵称不能包含空格");
		}
		if(isNickNameExists(nickName)) {
			return Ret.fail("msg","该昵称已被注册,请换一个");
		}
		if(isUserNameExists(userName)) {
			return Ret.fail("msg", "该邮箱已被注册,请换一个");
		}
		
		// 密码加盐
		String salt = HashKit.generateSaltForSha256();
		password = HashKit.sha256(salt+password);
		
		// 创建账户
		Account account = new Account();
		account.setNickName(nickName);
		account.setUserName(userName);
		account.setIp(ip);
		account.setSalt(salt);
		account.setAvatar(Account.DEFAULT_AVATAR);
		account.setPassword(password);
		account.setCreatedAt(new Date());
		account.setStatus(Account.STATUS_REG);
		
		if(account.save()) {
			String authCode = AuthCodeService.me.createRegActivateAuthCode(account.getInt("id"));
			if(sendRegActivateAuthEmail(authCode, account)) {
				return Ret.ok("msg", "注册成功，激活邮件已发送，请查收并激活账号：" + userName);
			} else {
				return Ret.fail("msg", "注册成功，但是激活邮件发送失败，可能是邮件服务器出现故障！");
			}
		}else {
			return Ret.fail("msg","账户保存失败,请联系管理员");
		}
	}
	
	/**
	 * 发送账号激活授权邮件
	 */
	private boolean sendRegActivateAuthEmail(String authCode, Account reg) {
		String title = "TNXRS激活邮件";
		String content = "在浏览器地址栏里输入并访问下面激活链接即可完成账户激活：\n\n"
				+ " http://localhost/reg/activate?authCode="
				+ authCode;

		String emailServer = PropKit.get("emailServer");
		String fromEmail = PropKit.get("fromEmail");
		String emailPass = PropKit.get("emailPass");
		String toEmail = reg.getStr("userName");
		try {
			EmailKit.sendEmail(emailServer, fromEmail, emailPass, toEmail, title, content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 激活账号，返回 false 表示激活码已过期或者不存在
	 * 激活账号不要去自动登录，激活邮件如果发错到了别人的邮箱，会有别人冒用的可能
	 * 并且登录功能还有额外有选择过期时间的功能
	 * @param authCodeId
	 * @return
	 */
	public Ret activate(String authCodeId) {
		AuthCode authCode = AuthCodeService.me.getAuthCode(authCodeId);
		if(authCode != null && authCode.isValidRegActivateAuthCode()) {
			// 更新账户状态为激活,激活的是注册的用户,锁定的用户不激活
			int n = Db.update("update account set status=? where id = ? and status = ?",Account.STATUS_OK,authCode.get("accountId"),Account.STATUS_REG);
			if(n > 0) {
				return Ret.ok("msg", "账号激活成功，欢迎加入 ！");
			} else {
				return Ret.fail("msg", "未找到需要激活的账号，可能是账号已经激活或已经被锁定，请联系管理员");
			}
		}else {
			return Ret.fail("msg", "authCode 不存在或已经失效，可以尝试在登录页再次发送激活邮件");
		}
	}
	
	/**
	 * 重新发送激活邮件
	 * @param userName
	 * @return
	 */
	public Ret reSendActivateEmail(String userName) {
		if (StrKit.isBlank(userName) || userName.indexOf('@') == -1) {
			return Ret.fail("msg", "email 格式不正确，请重新输入");
		}
		userName = userName.toLowerCase().trim();   // email 转成小写
		if ( ! isUserNameExists(userName)) {
			return Ret.fail("msg", "email 没有被注册，无法收取激活邮件，请先去注册");
		}
		
		Account account = dao.findFirst("select * from account where userName=? and status=? limit 1",userName,Account.STATUS_REG);
		if (account == null) {
			return Ret.fail("msg", "该账户已经激活，可以直接登录");
		}
		String authCode = AuthCodeService.me.createRegActivateAuthCode(account.getId());
		if (sendRegActivateAuthEmail(authCode, account)) {
			return Ret.ok("msg", "激活码已发送至邮箱，请收取激活邮件并进行激活");
		} else {
			return Ret.fail("msg", "激活邮件发送失败，可能是邮件服务器出现故障！");
		}
	}
}

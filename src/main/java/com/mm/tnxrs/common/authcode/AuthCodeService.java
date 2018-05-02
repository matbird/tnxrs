package com.mm.tnxrs.common.authcode;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.mm.tnxrs.common.model.AuthCode;

/**
 * 授权码业务
 * 1:邮件激活
 * 2:密码找回
 * 3:可扩展其他业务
 * @author Administrator
 *
 */
public class AuthCodeService {
	public static final AuthCodeService me = new AuthCodeService();
	private static final AuthCode dao = new AuthCode().dao();
	
	/**
	 * 创建注册激活授权码,有效时长为3600秒
	 * @param accountId
	 * @return
	 */
	public String createRegActivateAuthCode(int accountId) {
		return createAuthCode(accountId, AuthCode.TYPE_REG_ACTIVATE, 3600);
	}
	
	/**
	 * 创建密码找回授权码，一个小时后过期，3600 秒
	 */
	public String createRetrievePasswordAuthCode(int accountId) {
		return createAuthCode(accountId, AuthCode.TYPE_RETRIEVE_PASSWORD, 3600);
	}
	
	/**
	 * 获取授权码,授权码只能使用一次,被获取后立即删除
	 * @param authCodeId
	 * @return
	 */
	public AuthCode getAuthCode(String authCodeId) {
		if (StrKit.notBlank(authCodeId)) {
			AuthCode authCode = dao.findById(authCodeId.trim());
			if(authCode != null) {
				authCode.delete(); // 查询到之后立即删除
				return authCode;
			}
		}
		return null;
	}
	
	/**
	 * 创建授权码,并自动保存到数据库
	 * @param accountId 账户id
	 * @param authType 授权类型
	 * @param expireTime 授权码过期时长,单位为秒
	 * @return
	 */
	private String createAuthCode(int accountId,int authType,int expireTime) {
		long et = expireTime;
		long expireAt = System.currentTimeMillis() + (et * 1000);
		
		AuthCode ac = new AuthCode();
		ac.setId(StrKit.getRandomUUID());
		ac.setAccountId(accountId);
		ac.setType(authType);
		ac.setExpireAt(expireAt);
		
		if(ac.save()) {
			return ac.getId();
		}else {
			throw new RuntimeException("保存auth_code失败,请联系管理员");
		}
	}
	
	/**
	 * 看一眼授权码，未过期时则不删除
	 */
	public Ret peekAuthCode(String id) {
		AuthCode authCode = dao.findById(id);	
		if (authCode != null) {
			if (authCode.notExpired()) {
				return Ret.ok("authCode", authCode);
			} else {
				authCode.delete();
				return Ret.fail("msg", "授权码已过期");
			}
		} else {
			return Ret.fail("msg", "授权码不存在");
		}
	}
	
	/**
	 * 主动清除未使用过的过期授权码
	 * 不用经常调用，因为授权码在第一次使用时会自动删除，过期的未删除的授权码仅是未使用过的
	 */
	public int clearExpiredAuthCode() {
		return Db.update("delete from auth_code where expireAt < ?", System.currentTimeMillis());
	}
}

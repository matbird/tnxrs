package com.mm.tnxrs.common.model;

import com.mm.tnxrs.common.model.base.BaseAccount;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Account extends BaseAccount<Account> {
	
	public static final String DEFAULT_AVATAR = "x.jpg";    // 刚注册时使用默认头像

	public static final int STATUS_LOCKED = -1;	// 锁定账号，无法做任何事情
	public static final int STATUS_REG = 0;			// 注册、未激活
	public static final int STATUS_OK = 1;			// 正常、已激活
	
	public boolean isStatusOk() {
		return getStatus() == STATUS_OK;
	}

	public boolean isStatusReg() {
		return getStatus() == STATUS_REG;
	}

	public boolean isStatusLocked() {
		return getStatus() == STATUS_LOCKED;
	}
	
	/**
	 * 移除敏感信息
	 * @return
	 */
	public Account removeSensitiveInfo() {
		remove("password", "salt");
		return this;
	}
}
package com.mm.tnxrs.common.model;

import com.mm.tnxrs.common.model.base.BaseSession;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Session extends BaseSession<Session> {
	
	public static final Session dao = new Session().dao();
	
	/**
	 * 判断会话是否已过期
	 * @return
	 */
	public boolean isExpired() {
		return getExpireAt() < System.currentTimeMillis();
	}
	
	/**
	 * 判断是否未过期
	 * @return
	 */
	public boolean isNotExpired() {
		return !isExpired();
	}
}
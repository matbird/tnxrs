package com.mm.tnxrs.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSerial<M extends BaseSerial<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setDesc(java.lang.String desc) {
		set("desc", desc);
	}

	public java.lang.String getDesc() {
		return get("desc");
	}

	public void setCover(java.lang.String cover) {
		set("cover", cover);
	}

	public java.lang.String getCover() {
		return get("cover");
	}

	public void setCreatedAt(java.lang.String createdAt) {
		set("createdAt", createdAt);
	}

	public java.lang.String getCreatedAt() {
		return get("createdAt");
	}

	public void setCateId(java.lang.Integer cateId) {
		set("cateId", cateId);
	}

	public java.lang.Integer getCateId() {
		return get("cateId");
	}

}

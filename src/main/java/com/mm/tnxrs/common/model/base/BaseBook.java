package com.mm.tnxrs.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBook<M extends BaseBook<M>> extends Model<M> implements IBean {

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

	public void setImages(java.lang.String images) {
		set("images", images);
	}

	public java.lang.String getImages() {
		return get("images");
	}

	public void setFrom(java.lang.String from) {
		set("from", from);
	}

	public java.lang.String getFrom() {
		return get("from");
	}

	public void setDownload(java.lang.String download) {
		set("download", download);
	}

	public java.lang.String getDownload() {
		return get("download");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setCreatedAt(java.lang.String createdAt) {
		set("createdAt", createdAt);
	}

	public java.lang.String getCreatedAt() {
		return get("createdAt");
	}

	public void setSerialId(java.lang.Integer serialId) {
		set("serialId", serialId);
	}

	public java.lang.Integer getSerialId() {
		return get("serialId");
	}

	public void setSize(java.lang.Integer size) {
		set("size", size);
	}

	public java.lang.Integer getSize() {
		return get("size");
	}

}
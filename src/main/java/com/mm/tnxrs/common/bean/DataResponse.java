package com.mm.tnxrs.common.bean;

import java.util.List;

public class DataResponse extends BaseResponse{
	private List<?> data;

	public DataResponse() {
		super();
	}

	public DataResponse(Integer code, String message) {
		super(code, message);
	}

	public DataResponse(Integer code) {
		super(code);
	}

	public DataResponse(String message) {
		super(message);
	}

	public DataResponse(List<?> data) {
		this.data = data;
	}

	public List<?> getData() {
		return data;
	}

	public DataResponse setData(List<?> data) {
		this.data = data;
		return this;
	}

}

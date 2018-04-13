package com.mm.tnxrs.api;

import java.util.ArrayList;
import java.util.List;

public class CommonApiController extends BaseController{

	public void test() {
		renderText("Hello World!");
	}
	
	public void list() {
		List<String> list = new ArrayList<>();
		list.add("经典");
		list.add("推荐");
		list.add("历史");
		list.add("战争");
		list.add("童话");
		renderDataResponse(list);
	}
	
	public void category() {
		List<String> list = new ArrayList<>();
		list.add("经典");
		list.add("推荐");
		list.add("历史");
		list.add("战争");
		list.add("童话");
		list.add("外国");
		list.add("革命");
		list.add("武侠");
		list.add("评书");
		
		renderDataResponse(list);
	}
}

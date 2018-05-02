package com.mm.tnxrs.web.index;

import java.util.List;

import com.jfinal.core.Controller;
import com.mm.tnxrs.common.model.Category;
import com.mm.tnxrs.common.model.Serial;

public class IndexController extends Controller{

	static IndexService isrv = IndexService.me;
	
	public void index() {
		List<Serial> serialList = isrv.getSerialList();
		List<Category> cateList = isrv.getCateList();
		
		setAttr("serialList", serialList);
		setAttr("cateList", cateList);
		
		render("index.html");
	}
}

package com.mm.tnxrs.web.index;

import java.util.List;

import com.mm.tnxrs.common.model.Category;
import com.mm.tnxrs.common.model.Serial;

public class IndexService {

	public static final IndexService me = new IndexService();
	private static Serial sdao = new Serial().dao();
	private static Category cdao = new Category().dao();
	
	public List<Serial> getSerialList(){
		return sdao.find("select s.id,s.name,s.cover,s.desc from serial s order by s.createdAt desc limit 10");
	}
	
	public List<Category> getCateList(){
		return cdao.find("select c.id,c.name from category c");
	}
}

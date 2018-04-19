package com.mm.tnxrs.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mm.tnxrs.model.Book;
import com.mm.tnxrs.model.Serial;

public class SerialService {

	public final static SerialService me = new SerialService();
	private final Serial dao = new Serial().dao();
	private final static int PAGE_SIZE = 10;
	
	/**
	 * 通过类别id获取套书列表
	 * @param pageNumber
	 * @param cateId
	 * @return
	 */
	public Page<Serial> paginate(int pageNumber,int cateId){
		Page<Serial> serialPage = dao.paginate(pageNumber, PAGE_SIZE, "select s.id,s.name,s.cover,s.desc", 
				"from serial s where s.cateId = ? order by s.createdAt desc", cateId);
		return serialPage;
	}
	
	/**
	 * 通过id获取详情
	 * @param serialId
	 * @return
	 */
	public Serial findById(int serialId) {
		return dao.findFirst("select s.id,s.name,s.cover,s.desc from serial s where id = ?",serialId);
	}
	
	/**
	 * 模糊查询
	 * @param keyword
	 * @return
	 */
	public List<Serial> queryByKeyword(String keyword){
		return dao.find("select s.id,s.name,s.cover,s.desc from serial s where locate (?,s.name) > 0 "
				+ "or locate (?,s.desc) > 0 order by createdAt desc limit 20", keyword,keyword);
	}
	
	/**
	 * 获取套书详情
	 * @param serialId
	 * @return
	 */
	public Serial getDetail(int serialId) {
		Serial serial = CacheKit.get("serialsCache", serialId);
		if(serial == null) {
			serial = findById(serialId);
			List<Book> books = BookService.me.findAllBySerialId(serialId);
			serial.put("books", books);
			CacheKit.put("serialsCache", serialId, serial);
		}
		return serial;
	}
}

package com.mm.tnxrs.app.service;

import java.util.List;

import com.mm.tnxrs.common.model.Book;

public class BookService {

	public final static BookService me = new BookService();
	private final Book dao = new Book().dao();
	
	/**
	 * 通过套书下的所有画册信息
	 * @param serialId
	 * @return
	 */
	public List<Book> findAllBySerialId(int serialId) {
		return dao.find("select b.id,b.name,b.cover from book b where b.serialId = ? and b.status = 1", serialId);
	}

	public Book findById(int book_id) {
		return dao.findFirst("select b.id,b.name,b.cover,b.from,b.images from book b where b.id = ? and b.status = 1", book_id);
	}
}

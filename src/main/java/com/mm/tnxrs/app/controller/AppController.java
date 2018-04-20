package com.mm.tnxrs.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jetty.util.ajax.JSONObjectConvertor;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mm.tnxrs.app.interceptor.CommonParamInterceptor;
import com.mm.tnxrs.app.service.BookService;
import com.mm.tnxrs.app.service.SerialService;
import com.mm.tnxrs.common.bean.Code;
import com.mm.tnxrs.common.bean.DataResponse;
import com.mm.tnxrs.common.kit.StringUtils;
import com.mm.tnxrs.common.model.Book;
import com.mm.tnxrs.common.model.Category;
import com.mm.tnxrs.common.model.Serial;

//@Before(CommonParamInterceptor.class)
public class AppController extends BaseController{

	private static final String BASE_URL = "http://localhost/";
	private final Category dao = new Category().dao();
	
	static SerialService ssrv = SerialService.me;
	static BookService bsrv = BookService.me;
	
	public void test() {
		renderText("Hello World!");
	}
	
	/**
	 * 列出所有的api接口
	 */
	@ActionKey("/api")
	public void showApi() {
		List<API> list = new ArrayList<>();
		list.add(new API("api/v1/init",            "初始化在线参数",          "-"));
		list.add(new API("api/v1/category",        "获取分类列表",           "-"));
		list.add(new API("api/v1/serials",         "通过分类id和索引获取套书",  "cate_id:分类id  index:索引,默认为0"));
		list.add(new API("api/v1/serials/detail",  "获取套书详情",            "serial_id:套书id"));
		list.add(new API("api/v1/search",          "搜索,返回结果是套书",      "search_key:搜索关键字"));
		list.add(new API("api/v1/book/detail",     "获取书册详情",            "book_id:"));
		list.add(new API("api/v1/book/download",   "获取书册下载地址",         "device_id:设备id,用于校验是否获取下载地址(invalid_id,valid_id,other) book_id:"));
		list.add(new API("api/v1/feedback",        "反馈",                  "device_id:设备id feedback:反馈内容"));
		
		JSONObject api_list_object = new JSONObject();
		api_list_object.put("api_list", list);
		renderJson(new DataResponse(api_list_object));
	}
	
	/**
	 * 在线参数初始化
	 */
	public void init() {
		JSONObject init_object = new JSONObject();
		init_object.put("show_ad", false);
		init_object.put("show_extend", false);
		
		renderJson(new DataResponse(init_object));
	}
	
	/**
	 * 获取分类列表
	 */
	public void category() {
		//1.处理请求方法
		//2.判断检查请求参数
		//3.进行数据库请求操作
		//4.相应的业务逻辑处理
		//5.返回数据
		
		List<Category> list = CacheKit.get("categoryCache", "cateList", ()->{
			String sql = "select id,name from category";
			return dao.find(sql);
		});
		
		renderJson(new DataResponse().put("cate_list", list));
	}
	
	/**
	 * 通过分类id获取套书
	 */
	public void serials() {
		Page<Serial> paginate = ssrv.paginate(getParaToInt("index", 1), getParaToInt("cate_id", 1));
		renderJson(new DataResponse().put("list", paginate.getList())
				.put("total", paginate.getTotalRow()));
	}
	
	/**
	 * 获取套书详情
	 */
	@ActionKey("/api/v1/serials/detail")
	public void serialDetail() {
		Integer serialId = getParaToInt("serial_id");
		Serial serial = ssrv.getDetail(serialId);
		renderJson(new DataResponse().setData(serial));
	}
	
	/**
	 * 搜索
	 */
	public void search() {
		String keyWord = StringUtils.filter(getPara("search_key"));
		if(!StringUtils.isEmpty(keyWord)) {
			List<Serial> serials = ssrv.queryByKeyword(keyWord);
			renderJson(new DataResponse().put("list", serials));
		}
	}
	
	/**
	 * 获取书册信息
	 */
	@ActionKey("/api/v1/book/detail")
	public void bookDetail() {
		Book book = bsrv.findById(getParaToInt("book_id"));
		String images = book.getImages();
		book.put("imageUrls", images.split(","));
		book.remove("images");
		renderJson(new DataResponse(book));
	}
	
	/**
	 * 校验并返回下载地址
	 */
	@ActionKey("/api/v1/book/download")
	public void bookDownload() {
		/*String device_id = getPara("device_id");
		int book_id = getParaToInt("book_id");
		if("invalid_id".equals(device_id)) {
			renderJson(new DataResponse(Code.TOKEN_INVALID, "校验失败"));
		}else if("valid_id".equals(device_id)){
			Book book = new Book(1, "西游记上美版01", "http://f10.baidu.com/it/u=3826138337,4253245402&fm=72");
			book.setDownload("http://of2squwm4.bkt.clouddn.com/%E7%BB%9F%E8%AE%A1%E5%AD%A6%E4%B9%A0%E6%96%B9%E6%B3%95.pdf");
			
			renderJson(new DataResponse(book));
		}else {
			renderJson(new DataResponse(Code.FAILED, "failed"));
		}*/
		
		
	}
	
	/**
	 * 反馈
	 */
	public void feedback() {
		String content = getPara("feedback");
		String device_id = getPara("device_id");
		
		renderJson(new DataResponse(Code.SUCCESS, "success"));
	}
	
	// -------------------------------------
	
	public class API{
		private String api;
		private String desc;
		private String params;
		
		public API(String api, String desc, String params) {
			super();
			this.api = api;
			this.desc = desc;
			this.params = params;
		}
		public String getApi() {
			return BASE_URL+api;
		}
		public void setApi(String api) {
			this.api = api;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getParams() {
			return params;
		}
		public void setParams(String params) {
			this.params = params;
		}
		
	}
}

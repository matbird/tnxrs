package com.mm.tnxrs.common.route;

import com.jfinal.config.Routes;
import com.mm.tnxrs.web.index.IndexController;
import com.mm.tnxrs.web.login.LoginController;
import com.mm.tnxrs.web.reg.RegController;

public class WebRoutes extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/_view");
		
		add("/",IndexController.class,"/index");
		add("/reg",RegController.class);
		add("/login",LoginController.class);
	}

}

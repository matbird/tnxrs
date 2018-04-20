package com.mm.tnxrs.common.route;

import com.jfinal.config.Routes;
import com.mm.tnxrs.app.controller.AppController;

public class ApiRoutes extends Routes{

	@Override
	public void config() {
		add("/api/v1", AppController.class);
	}

}

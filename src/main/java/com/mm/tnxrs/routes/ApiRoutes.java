package com.mm.tnxrs.routes;

import com.jfinal.config.Routes;
import com.mm.tnxrs.api.CommonApiController;

public class ApiRoutes extends Routes{

	@Override
	public void config() {
		add("/api/v1", CommonApiController.class);
	}

}

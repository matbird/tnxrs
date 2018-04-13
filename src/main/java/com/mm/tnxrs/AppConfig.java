package com.mm.tnxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.mm.tnxrs.routes.ApiRoutes;

public class AppConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setEncoding("UTF-8");
		me.setViewType(ViewType.JSP);
	}

	@Override
	public void configRoute(Routes me) {
		me.add(new ApiRoutes());
	}

	@Override
	public void configEngine(Engine me) {
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

	private static Logger log = LoggerFactory.getLogger(AppConfig.class);
	public static void main(String[] args) {
		log.info("test slf4j");
		JFinal.start("src/main/webapp", 80, "/");
	}
}

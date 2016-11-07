package io.github.japinl.woyun.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

@Order(1)
public class CommonInitializer extends CommonWebAppAttributes implements WebApplicationInitializer{
	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(CommonInitializer.class);

	@Override
	public void onStartup(ServletContext context) throws ServletException {
		super.setServletContext(context);
		
		WebFilter sessionFilter = new WebFilter("sessionFilter", new SessionFilter(), true, "/list/*");
		
	//	initFilters(sessionFilter);
	}
}

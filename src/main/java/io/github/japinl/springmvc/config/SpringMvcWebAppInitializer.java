package io.github.japinl.springmvc.config;

import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/*
 * Automatically configure DispatcherServlet and Spring application context in the application's servlet context.
 */
public class SpringMvcWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	private final static Logger LOG = LoggerFactory.getLogger(SpringMvcWebAppInitializer.class);
	
	/* Map DispathcerServlet to /
	 */
	@Override
	protected String[] getServletMappings() {
		LOG.info("servlet mappings");
		return new String[] { "/" };
	}
	
	/*
	 * Configure the application context created by ContextLoaderListener
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class };
	}
	
	/* Define beans for DispathcerServlet's application context.
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { SpringMvcConfig.class };
	}
	
	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}
}

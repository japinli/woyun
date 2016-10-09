package io.github.japinl.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc                                  // Enable Spring MVC
@ComponentScan("io.github.japinl.springmvc")   // Enable component-scanning
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
	private final static String welcome = "/static/html/index.html";
	/*
	 * Configure a view resolver
	 */
	/*@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/static/html/");
		resolver.setSuffix(".html");
		resolver.setExposeContextBeansAsAttributes(true);
		return resolver;
	}
	*/
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName(welcome);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		super.configureViewResolvers(registry);
	}
	
	/*
	 * Configure static content handling
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
}

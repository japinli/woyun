package cn.signit.server.conf;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import cn.signit.conf.ConfigProps;
import cn.signit.cons.UrlPath;
import cn.signit.untils.encode.CommonAjaxJsonInterceptor;

/**
 *基于注解的web mvc上下文Controller（Spring MVC Context）配置
 *
 * @ClassName WebMvcConfig
 * @author ZhangHongdong
 * @date 2015年8月21日-下午7:15:09
 * @version 1.1.0
 */
@Configuration
@ComponentScan(basePackages={"cn.signit.controller"},excludeFilters={ @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)})
//启用Aspect自动代理，即：<aop:aspectj-autoproxy/>
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class WebMvcConfig extends WebMvcConfigurationSupport{
		private final static Logger LOG = LoggerFactory.getLogger(WebMvcConfig.class);
		private final static String WELCOME_PAGE="wesign/login.html";
		
	    /**
	    *添加静态资源映射
	    */
	    @Override
	    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
	    	   LOG.info("======================>>  添加静态资源映射( {} ) ",registry.getClass().getName());
	           super.addResourceHandlers(registry);
	           registry.addResourceHandler("/favicon.ico").addResourceLocations("/WEB-INF/favicon.ico");
	           registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/classes/static/");
	    }
	    
	    
	   /* *//**
		   *配置默认欢迎视图
		   *//*
	    @Override
	    protected void addViewControllers(ViewControllerRegistry registry) {
	    		super.addViewControllers(registry);
	    		registry.addViewController("/").setViewName(WELCOME_PAGE);
	    }
*/
	    /**
		   *配置默认Servlet处理静态资源
		   */
	    @Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	    	 LOG.info("======================>>  配置默认Servlet处理静态资源( {} ) ",configurer.getClass().getName());
	        configurer.enable();
	    }
	    
	    /**
	    *配置转换器
	    */
	    @Override
	    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	    		LOG.info("======================>>  配置XML, JSON,消息转换器( {} ) ",HttpMessageConverter.class.getName());
	            super.configureMessageConverters(converters);
	            //xml转换器
	            converters.add(new Jaxb2RootElementHttpMessageConverter());
	            //json转换器
	            converters.add(mappingJackson2HttpMessageConverter());
	            //消息转换器
	            converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
	    }
	    
	    //json转换器
	    @Bean
	    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
	    	MappingJackson2HttpMessageConverter jsonConverter =  new MappingJackson2HttpMessageConverter();
	    	//空值不进行序列化
	    	jsonConverter.getObjectMapper().setSerializationInclusion(Include.NON_NULL)
																				.setSerializationInclusion(Include.NON_EMPTY);
            return jsonConverter;
	    }
	    
	    /**
		  *配置内容类型（ContentType）转发规则
		  */
	    @Override
	    protected void configureContentNegotiation(ContentNegotiationConfigurer contentnegotiationconfigurer) {
	    	LOG.info("======================>>  配置内容类型（ContentType）转发规则( {} ) ",contentnegotiationconfigurer.getClass().getName());
	    	super.configureContentNegotiation(contentnegotiationconfigurer);
	    	contentnegotiationconfigurer
	    			.favorPathExtension(true)
	    			.useJaf(false)  
	    			.favorParameter(true)
	    			.parameterName("mediaType")  
	    			.ignoreAcceptHeader(true)
	    			.defaultContentType(MediaType.APPLICATION_JSON)
	    			.mediaType("xml", MediaType.APPLICATION_XML)
	    			.mediaType("json", MediaType.APPLICATION_JSON);
	    }
	    
	    /**
		  *配置异常解析器
		  */
		@Override
	    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
	    	LOG.info("======================>> 配置异常解析器( {} ) ",HandlerExceptionResolver.class.getName());
	    	SimpleMappingExceptionResolver b = new SimpleMappingExceptionResolver();
	    	b.setDefaultErrorView("error/pages/404");
	    	b.setDefaultStatusCode(404);
	    	b.addStatusCode("error/pages/500", 500);
	    	b.addStatusCode("error/pages/503", 503);
	        Properties mappings = new Properties();
	       /* mappings.put("org.springframework.dao.DataAccessException", "error/genericError");
	        mappings.put("java.lang.RuntimeException", "error/genericError");*/
	        b.setExceptionMappings(mappings);
	        list.add(b);
	    }
	    
		/**
		  *对文件上传的支持
		  */
	    @Bean
	    public MultipartResolver  multipartResolver() {
	    	LOG.info("======================>> 配置对文件上传的支持( {} ) ",MultipartResolver.class.getName());
	    	//针对不同操作系统来适配文件上传
	    	String os = System.getProperty("os.name").toLowerCase();
	    	if(os.indexOf("win") >= 0){//windows
	    		return new CommonsMultipartResolver();
	    	}else{//linux or mac or others
	    		return new StandardServletMultipartResolver();
	    	}
	    }
	    
	    /**
		  *对所有ajax请求，并且数据为Json的消息进行拦截处理，避免出现类似404,500等的错误返回
		  */
	    @Bean
		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping() {
			LOG.info("======================>>  添加Ajax的Json相关拦截器( {} ) ",CommonAjaxJsonInterceptor.class.getName());
			RequestMappingHandlerMapping reqMapping = super.requestMappingHandlerMapping();
			//支持路径参数包含小数
			reqMapping.setUseSuffixPatternMatch(false);
			reqMapping.setInterceptors(new Object[]{new CommonAjaxJsonInterceptor()});
			return reqMapping;
		}
}

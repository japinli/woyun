package cn.signit.server;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import cn.signit.server.conf.BaseConfig;
import cn.signit.server.conf.DataConfig;
import cn.signit.server.conf.MongoConfig;
import cn.signit.server.conf.SecurityConfig;
import cn.signit.server.conf.ServiceConfig;
import cn.signit.server.conf.StandardTimesConfig;
import cn.signit.server.conf.SystemConfig;
import cn.signit.server.conf.ThymeleafConfig;
import cn.signit.server.conf.V2CAServiceConfig;
import cn.signit.server.conf.WebMvcConfig;

/**
*初始化基于注解的Spring MVC WEB容器（相当于web.xml中的Spring DispatcherServlet配置）
*
* @ClassName WebAppSecurityInitializer
* @author ZhangHongdong
* @date 2015年8月26日-下午6:19:51
* @version 1.1.0
*/
@Order(2)
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	private final static Logger LOG = LoggerFactory.getLogger(WebInitializer.class);

	/**
	*用@Configuration标注的应用上下文，除web应用上下文
	*
	*/
	@Override
	protected Class<?>[] getRootConfigClasses() {
			LOG.info("======================>>  配置@Configuration标注的应用上下文，除web应用上下文");
			return new Class[]{BaseConfig.class, 
					SecurityConfig.class,
					   StandardTimesConfig.class,
					   /*SystemConfig.class,*/
					   DataConfig.class,
					   MongoConfig.class
					   };
	}
	
	/**
	*用@Configuration标注的web应用上下文
	*/
	@Override
	protected Class<?>[] getServletConfigClasses() {
		 LOG.info("======================>>  配置@Configuration标注的web应用上下文");
		 return new Class[]{					
				 									SystemConfig.class,
				 						            ServiceConfig.class,
				 						            V2CAServiceConfig.class,
				 						            ThymeleafConfig.class,
				 									WebMvcConfig.class
				 									};
	}
	
@Override
protected FrameworkServlet createDispatcherServlet(
		WebApplicationContext servletAppContext) {
	  DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
      dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
      return dispatcherServlet;
}
	
	/**
	*DispatcherServlet映射路径
	*/
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
	
	/**
	*定义Sevlet名字
	*/
	@Override
	protected String getServletName(){
		return "Forensic Plant";
	}
	
	/**
	*自定义Spring DispatcherServlet相关参数
	*/
	 @Override
	    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
			registration.setAsyncSupported(true);
	        registration.setMultipartConfig(getMultipartConfigElement());
	    }
	 
	    private MultipartConfigElement getMultipartConfigElement() {
	        MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
	        return multipartConfigElement;
	    }
	 
	    // 临时文件存储目录
	    private static final String LOCATION = System.getProperty("java.io.tmpdir"); 
	    
	    // 100MB : 文件最尺寸
	    private static final long MAX_FILE_SIZE = 104857600; 
	    
	  //500MB : 包含Multipart总共请求数据的尺寸
	  //超过此尺寸Spring将抛出异常
	    private static final long MAX_REQUEST_SIZE = 524288000; 
	    
	    //10MB : 接收的文件达到指定尺寸时将会写到磁盘上
	    private static final int FILE_SIZE_THRESHOLD = 10485760; 
}

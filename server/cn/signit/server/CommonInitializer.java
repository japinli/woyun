/**
* @author ZhangHongdong
* @date 2015年8月26日-下午2:31:51
* @see (参阅)
*/
package cn.signit.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

/**
*初始化通用WEB容器（相当于web.xml）
*
* @ClassName CommonInitializer
* @author ZhangHongdong
* @date 2015年8月26日-下午2:31:51
* @version 1.1.0
*/
@Order(1)
public class CommonInitializer extends CommonWebAppAttributes implements WebApplicationInitializer{
	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(CommonInitializer.class);
	/**
	* Servlet3.0容器（如Tomcat/Jboss/Jetty等）启动时自动进行初始化
	* 
	*@param context web应用上下文
	*@throws ServletException web应用启动异常
	*/
	@Override
	public void onStartup(ServletContext context) throws ServletException {
		super.setServletContext(context);
		
		/**
		 * +--------------------------------------------------------+
		 * |																		|
		 * |初始化过滤器（Filter）  							    |
		 * |																		|
		 * +--------------------------------------------------------+
		 */
		
		//会话过滤器
		//WebFilter sessionFilter = new WebFilter("sessionFilter", new SessionFilter(), true, "/api/*");
		
		//initFilters(sessionFilter);
	}
}

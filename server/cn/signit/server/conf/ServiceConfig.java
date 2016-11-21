/**
* @author ZhangHongdong
* @date 2015年8月26日-下午10:35:06
* @see (参阅)
*/
package cn.signit.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

/**
*基于注解的web mvc上下文Service（Spring MVC Context）配置
*
* @ClassName ServiceConfig
* @author ZhangHongdong
* @date 2015年8月26日-下午10:35:06
* @version 1.1.0
*/
@Configuration
//启用定时任务，自动识别@Scheduled
@EnableScheduling
@ComponentScan(basePackages={BaseConfig.BASE_PKG_NAME+"service"},excludeFilters={ @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
public class ServiceConfig {
	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(ServiceConfig.class);
}

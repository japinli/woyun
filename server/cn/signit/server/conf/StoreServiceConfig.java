/**
* @author:ZhangHongdong
* @date:2016年2月22日-上午9:59:00
* @see: (参阅)
*/
package cn.signit.server.conf;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
/**
 *存储文件服务配置
 * @ClassName: StoreServiceConfig
 * @author:ZhangHongdong
 * @date:2016年2月22日-上午9:59:00
 * @version:1.2.0
 */
@Configuration
@ComponentScan(basePackages={BaseConfig.BASE_PKG_NAME+"**.service.files"},excludeFilters={ @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
@Import(BaseConfig.class)
public class StoreServiceConfig {
	private final static Logger LOG = LoggerFactory.getLogger(StoreServiceConfig.class);
}

package cn.signit.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.signit.conf.ConfigProps;
import cn.signit.utils.time.StandardTimes;
import cn.signit.utils.time.StandardTimesFactory;
import cn.signit.utils.timestamp.SignitTSAClient;
import cn.signit.utils.timestamp.TSAClient;

/**
* @ClassName StandardTimesConfig
* @author Liwen
* @date 2016年5月12日-下午4:06:29
* @version (版本号)
* @see (参阅)
*/
@Configuration
public class StandardTimesConfig {
	private final static Logger LOG = LoggerFactory.getLogger(StandardTimesConfig.class);
	@Bean
	public StandardTimes standardTimes(){
		LOG.info("======================>>  配置标准时间源( {} )",StandardTimes.class.getName());
		return StandardTimesFactory.getDefault();
	}
	
	@Bean
	public TSAClient tsaClient(){
		String url=ConfigProps.get("timestamp.url");
		LOG.info("======================>>  配置时间戳服务器( {} )",url);
		return new SignitTSAClient(url);
	}
}

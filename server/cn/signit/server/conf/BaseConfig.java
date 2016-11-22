package cn.signit.server.conf;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.signit.conf.ConfigProps;


/**
*基础配置文件
* @ClassName BaseConfig
* @author Liwen
* @date 2016年5月12日-下午4:53:38
* @version (版本号)
* @see (参阅)
*/
@Configuration
public class BaseConfig {
	private final static Logger LOG = LoggerFactory.getLogger(BaseConfig.class);
	 /**
	    *   基础包名
	    */
	public final static String BASE_PKG_NAME = "cn.signit.";
			//全局安全提供者
			static{
				LOG.info("======================>>  配置全局安全提供者( {} ) ",BouncyCastleProvider.class.getName());
				Security.addProvider(new BouncyCastleProvider());
			}
			
			private ConfigProps props;
			
			@Bean
			public ConfigProps props(){
				if(props!=null){
					return props;
				}else{
					this.props=new ConfigProps();
				}
				return props;
			}
}

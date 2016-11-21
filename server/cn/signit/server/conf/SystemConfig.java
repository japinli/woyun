package cn.signit.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.signit.beans.PeersInfo;
import cn.signit.conf.ConfigProps;
import cn.signit.pkcs.cert.CertReadFactory;
import cn.signit.tools.utils.PathTools;

/**
*系统配置初始化
* @ClassName SystemConfig
* @author Liwen
* @date 2016年5月12日-下午4:36:32
* @version (版本号)
* @see (参阅)
*/
@Configuration
public class SystemConfig {
	private final static Logger LOG = LoggerFactory.getLogger(SystemConfig.class);
	
	
	@Bean
	public PeersInfo peerInfo() throws Exception{
		LOG.info("======================>>  配置系统证书相关信息","");
		LOG.info("======================>>  由受保护的KeyStore获取节点证书( {} ) ",ConfigProps.get("cacert.path.cert"));
		CertReadFactory fac=new CertReadFactory(
					PathTools.getPath(ConfigProps.get("cacert.path.keystore")).toString(),
					PathTools.getPath(ConfigProps.get("cacert.path.cert")).toString(),
					ConfigProps.get("cacert.alias"),
					ConfigProps.get("cacert.password"));
		
			try {
				
				PeersInfo info=new PeersInfo(fac.getCertificate(),fac.getPrivateKey(),fac.getPublicKey());
				//初始化节点类型
			
				return info;
			} catch (Exception e) { 
				LOG.error("根证书初始化失败"+e.getLocalizedMessage());
				throw e;
		}
	}
}

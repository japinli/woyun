package cn.signit.server.conf;

import javax.annotation.Resource;

import org.bouncycastle.math.ec.ECCurve.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.signit.conf.ConfigProps;
import cn.signit.sdk.BaseApiException;
import cn.signit.sdk.BaseRequest;
import cn.signit.sdk.BaseResponse;
import cn.signit.sdk.DefaultRequestClient;

/**
*第二个版本的CA服务配置
* @ClassName V2CAServiceConfig
* @author Liwen
* @date 2016年10月10日-上午9:29:37
* @version (版本号)
* @see (参阅)
*/
@Configuration
@Import({BaseConfig.class})
@ComponentScan(basePackages={BaseConfig.BASE_PKG_NAME+"**.service.signca"})
public class V2CAServiceConfig {
	private final static Logger LOG = LoggerFactory.getLogger(V2CAServiceConfig.class);
	
	@Bean
	public V2CertRequestClient v2CertRequestClient(){
		
		String gateWay=ConfigProps.get("cert.request.server.url");
		String appId=ConfigProps.get("cert.request.auth.appid");
		String key=ConfigProps.get("cert.request.auth.key");
		LOG.info("-----------配置CA2.0平台-------");
		LOG.info("-----------网关:"+gateWay);
		return new V2CertRequestClient(gateWay,appId,key,"json","UTF-8");
	}
	
	/**
	*CA新版本请求客户端,避免注入冲突，对默认请求客户端进行包装
	* @ClassName V2CertRequestClient
	* @author Liwen
	* @date 2016年10月11日-上午11:11:17
	* @version (版本号)
	* @see (参阅)
	*/
	public class V2CertRequestClient{
		private DefaultRequestClient client;
		/**
		*
		* @param serverUrl
		* @param appId
		* @param key
		*@see (参阅)
		*/
		public V2CertRequestClient(String serverUrl, String appId, String key) {
			client=new DefaultRequestClient(serverUrl, appId, key);
		}
		
		public V2CertRequestClient(String serverUrl, String appId, String key, String format) {
			client=new DefaultRequestClient(serverUrl, appId, key,format);
		}

		public V2CertRequestClient(String serverUrl, String appId, String key, String format, String charset) {
			client=new DefaultRequestClient(serverUrl, appId, key,format,charset);
		}
		
		public <T extends BaseResponse> T execute(BaseRequest<T> request) throws BaseApiException {
			return client.execute(request, null);
		}
		
	}
	
}

package cn.signit.server.conf;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.signit.conf.ConfigProps;
import cn.signit.cons.PeerMode;
import cn.signit.forensic.block.BlockCache;
import cn.signit.forensic.block.cache.SimpleConsensuseCache;
import cn.signit.network.Peer;
import cn.signit.network.PeersTemp;
import cn.signit.pkcs.cert.X509CertSigner;
import cn.signit.pkcs.x509.keystore.KeyStoreUtil;
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
	public PeersTemp PeersTemp() throws NoSuchAlgorithmException, CertificateException, IOException, Exception{
		LOG.info("======================>>  初始化本地节点相关信息:");
		PeersTemp peersTemp=new PeersTemp();
		Peer peer=new Peer();
		peer.setIp(ConfigProps.get("peer.ip"));
		peer.setName(ConfigProps.get("peer.name"));
		peer.setUrl(ConfigProps.get("peer.url"));
		
		X509CertSigner signer=new X509CertSigner(ConfigProps.get("peers.signAlgorithm"));
		signer.load(KeyStoreUtil.getKeyStorefrompath(PathTools.getPath(ConfigProps.get("peers.path.keystore")).toString(), 
				ConfigProps.get("peers.keystore.password")), ConfigProps.get("peers.keystore.password"));
		peersTemp.setSinger(signer);
		peersTemp.setLocalPeer(peer);
		peersTemp.setModel(PeerMode.valueOf(ConfigProps.get("peer.model")));
		LOG.info("NAME= {} , IP= {} , URL= {} , MODEL= {};"
				,peer.getName(),peer.getIp(),peer.getUrl(),peersTemp.getModel());
		return peersTemp;
	}
	
	@Bean
	public BlockCache blockTemp(){
		LOG.info("======================>>  初始化区块缓存:");
		return new SimpleConsensuseCache().reset();
	}
	
/*	@Bean
	public PeersInfo peerInfo() throws Exception{
		LOG.info("======================>>  配置系统证书相关信息","");
		LOG.info("======================>>  由受保护的KeyStore获取节点证书( {} ) ",ConfigProps.get("cacert.path.cert"));
		CertReadFactory fac=new CertReadFactory(
					PathTools.getPath(ConfigProps.get("peers.path.keystore")).toString(),
					PathTools.getPath(ConfigProps.get("peers.path.cert")).toString(),
					ConfigProps.get("peers.keystore.alias"),
					ConfigProps.get("peers.keystore.password"));
		
			try {
				
				PeersInfo info=new PeersInfo(fac.getCertificate(),fac.getPrivateKey(),fac.getPublicKey());
				//初始化节点类型
			
				return info;
			} catch (Exception e) { 
				LOG.error("根证书初始化失败"+e.getLocalizedMessage());
				throw e;
		}
	}*/
}

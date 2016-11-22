package cn.signit.service.signca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.signit.ca.api.request.CACertNormalRequest;
import cn.signit.ca.api.request.CACertOneTimeRequest;
import cn.signit.ca.api.response.CACertNormalResponse;
import cn.signit.ca.api.response.CACertOneTimeResponse;
import cn.signit.pkcs.p10.req.PKCS10Request;
import cn.signit.pkcs.p12.PKCS12Maker;
import cn.signit.pkcs.x509.tools.RSAKeyFactory;
import cn.signit.sdk.internal.util.codec.Base64;
import cn.signit.server.conf.V2CAServiceConfig.V2CertRequestClient;
import cn.signit.service.signca.wapper.DNRequestWrapper;
import cn.signit.service.signca.wapper.P12CertRequestWrapper;
import cn.signit.service.signca.wapper.P12CertResp;


/**
*使用新版CA实现证书请求
* @ClassName CertRequestVersion2Service
* @author Liwen
* @date 2016年10月10日-下午4:39:43
* @version (版本号)
* @see (参阅)
*/
@Service("v2CertRequestServiceImpl")
public class V2CertRequestServiceImpl implements CertRequestService{
	@Autowired
	private V2CertRequestClient v2CertRequestClient;

	/**
	*执行证书请求
	*@param p12CertRequestWrapper
	*@return
	*@see (参阅)
	*/
	@Override
	public P12CertResp doP12CertRequest(P12CertRequestWrapper p12CertRequestWrapper) {
		try {
			/**CSR证书申请**/
			//产生密钥对
			KeyPair kp=RSAKeyFactory.getKeyPair(2048);
			//使用dn和公钥 实例化一个请求对象
			DNRequestWrapper dnRequestWrapper = p12CertRequestWrapper.getDnRequestWrapper();
			PKCS10Request req=new PKCS10Request(dnRequestWrapper.toX500DN(),kp.getPublic());
			//序列化信息(激活组装)
			req.built();
			//使用私钥对生成的信息签名
			req.sign(kp.getPrivate());
			//获取该请求的BASE64编码
			String csr=req.getBase64String();
			CACertNormalResponse resp=v2CertRequestClient.execute(
					new CACertNormalRequest().setCsr(csr).setKeepTime(365));
			 if(resp==null){
				 throw new IOException("连接CA失败,请联系管理员");
			 }
			 
			 if(resp.getSignCert()!=null){
				 CertPath certPath=CertificateFactory.getInstance("X.509").generateCertPath(new ByteArrayInputStream(Base64.decodeBase64(resp.getSignCert())));
				 Certificate[] certs=new Certificate[certPath.getCertificates().size()];
				 certPath.getCertificates().toArray(certs);
				 /**拼装P12证书**/
				 //此处替换了ZhangHongdong的cn.signit.crypto.cert.P12CertMaker
				 //cn.signit.pkcs.p12.PKCS12Maker针对原来的类进行了证书路径检查优化
				 PKCS12Maker p12CertMaker  = new PKCS12Maker(kp.getPrivate(), dnRequestWrapper.getRealName(), p12CertRequestWrapper.getKeyPassword(),certs);
				return new P12CertResp(resp.getAuthorizationCode(),resp.getReferencNumber(),p12CertMaker);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	* P12一次性证书申请请求
	* 
	*@param p12CertRequestWrapper p12证书请求的封装
	*@return 申请P12证书的响应的封装。请求失败，则返回null
	*/
	@Override
	public P12CertResp doP12OneTimeCertRequest(P12CertRequestWrapper p12CertRequestWrapper) {
		try {
			/**CSR证书申请**/
			//产生密钥对
			KeyPair kp=RSAKeyFactory.getKeyPair(2048);
			//使用dn和公钥 实例化一个请求对象
			DNRequestWrapper dnRequestWrapper = p12CertRequestWrapper.getDnRequestWrapper();
			PKCS10Request req=new PKCS10Request(dnRequestWrapper.toX500DN(),kp.getPublic());
			//序列化信息(激活组装)
			req.built();
			//使用私钥对生成的信息签名
			req.sign(kp.getPrivate());
			//获取该请求的BASE64编码
			String csr=req.getBase64String();
			CACertOneTimeResponse resp=v2CertRequestClient.execute(
					new CACertOneTimeRequest().setCsr(csr));
			 if(resp==null){
				 throw new IOException("连接CA失败,请联系管理员");
			 }
			 
			 if(resp.getSignCert()!=null){
				 CertPath certPath=CertificateFactory.getInstance("X.509").generateCertPath(new ByteArrayInputStream(Base64.decodeBase64(resp.getSignCert())));
				 Certificate[] certs=new Certificate[certPath.getCertificates().size()];
				 certPath.getCertificates().toArray(certs);
				 /**拼装P12证书**/
				 //此处替换了ZhangHongdong的cn.signit.crypto.cert.P12CertMaker
				 //cn.signit.pkcs.p12.PKCS12Maker针对原来的类进行了证书路径检查优化
				 PKCS12Maker p12CertMaker  = new PKCS12Maker(kp.getPrivate(), dnRequestWrapper.getRealName(), p12CertRequestWrapper.getKeyPassword(),certs);
				return new P12CertResp(p12CertMaker);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}

package cn.signit.service.db;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.bouncycastle.operator.ContentSigner;

import cn.signit.domain.mysql.UserCertificate;
import cn.signit.service.signca.wapper.P12CertRequestWrapper;

/**
*数字证书服务
* @ClassName CertificateSerivce
* @author Liwen
* @date 2016年11月7日-下午3:01:41
* @version (版本号)
* @see (参阅)
*/
public interface CertificateSerivce {
	public Long requestCert(Long userId,P12CertRequestWrapper p12);
	/**
	 * 获取用户证书信息
	 * 是否附加证书文件流
	 */
	public UserCertificate getUserCert(Long userId,boolean withData);
	
	/**
	 * 根据用户id，获取用户证书，生成用户签名服务块
	 * 
	 */
	public ContentSigner getUserSigner(Long userId,String password);
	
	
	public KeyStore getUserKeyStore(Long userId,String password);
	/**
	 * 获取服务器本身的签名
	 *//*
	public ContentSigner getServerSigner();*/
}

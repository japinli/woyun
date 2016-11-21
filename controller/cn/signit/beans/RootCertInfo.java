package cn.signit.beans;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
*系统基本配置
* @ClassName SystemBaseInfo
* @author Liwen
* @date 2016年5月12日-下午4:30:02
* @version (版本号)
* @see (参阅)
*/
public class RootCertInfo implements Serializable{
	private static final long serialVersionUID = 2339705635801044373L;
	private final  X509Certificate rootCert;
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private  int keepTime;
	
	public RootCertInfo(X509Certificate rootCert,PrivateKey privateKey,PublicKey publicKey){
		this.rootCert=rootCert;
		this.privateKey=privateKey;
		this.publicKey=publicKey;
	}

	public X509Certificate getRootCert() {
		return rootCert;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public int getKeepTime() {
		return keepTime;
	}

	public void setKeepTime(int keepTime) {
		this.keepTime = keepTime;
	}
	
	
}

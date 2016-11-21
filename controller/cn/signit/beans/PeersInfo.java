package cn.signit.beans;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import cn.signit.cons.PeerMode;

/**
*系统基本配置
* @ClassName SystemBaseInfo
* @author Liwen
* @date 2016年5月12日-下午4:30:02
* @version (版本号)
* @see (参阅)
*/
public class PeersInfo implements Serializable{
	private static final long serialVersionUID = -2475988470332236548L;
	private final  X509Certificate rootCert;
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private  int keepTime;
	
	private PeerMode mode;
	
	public PeersInfo(X509Certificate rootCert,PrivateKey privateKey,PublicKey publicKey){
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

	public PeerMode getMode() {
		return mode;
	}

	public void setMode(PeerMode mode) {
		this.mode = mode;
	}
	
	
}

package cn.signit.service.signca.wapper;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import cn.signit.pkcs.p12.PKCS12Maker;

/**
*申请P12证书的响应
*
* @ClassName P12CertResp
* @author ZhangHongdong
* @date 2015年9月16日-下午5:48:26
* @version 1.1.0
*/
public class P12CertResp {
	//打包的私钥
	private PrivateKey privateKey;
	//打包的公钥证书链
	private Certificate[] certificateChain;
	//pfx访问密码
	private String pfxPassword;
	//pfx数据字节
	private byte[] pfxOutBytes;
	//证书授权码
	private String authorizationCode;
	//证书参考码
	private String referenceNumber;
	//证书序列号
	private String serialCode;
	//证书有效开始日期
	private Date startTime;
	//证书有效截止日期
	private Date endTime;
	//证书使用者
	private String subject;
	//证书发行者
	private String issuer;
	
	
	public P12CertResp(PKCS12Maker p12CertMaker){
		this(null, null, p12CertMaker);
	}
	
	public P12CertResp(String authorizationCode,String referenceNumber,PKCS12Maker p12CertMaker) {
		this.privateKey = p12CertMaker.getPrivateKey();
		this.certificateChain = p12CertMaker.getCertificateChain();
		this.pfxPassword = p12CertMaker.getPfxPassword();
		this.pfxOutBytes = p12CertMaker.getPfxOutBytes();
		X509Certificate cert = (X509Certificate) this.certificateChain[0];
		initCertInfo(cert);
	}
	
	private void initCertInfo(X509Certificate cert){
		this.serialCode = cert.getSerialNumber().toString();
		this.startTime = cert.getNotBefore();
		this.endTime = cert.getNotAfter();
		this.subject = cert.getSubjectDN().getName().trim().replace("OID.1.2.840.113549.1.9.1","EMAIL").replace("OID.2.5.4.4", "SN");
		this.issuer = cert.getIssuerDN().getName().trim().replace("OID.1.2.840.113549.1.9.1","EMAIL").replace("OID.2.5.4.4", "SN");
	}
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public Certificate[] getCertificateChain() {
		return certificateChain;
	}
	public void setCertificateChain(Certificate[] certificateChain) {
		this.certificateChain = certificateChain;
	}
	public String getPfxPassword() {
		return pfxPassword;
	}
	public void setPfxPassword(String pfxPassword) {
		this.pfxPassword = pfxPassword;
	}
	public byte[] getPfxOutBytes() {
		return pfxOutBytes;
	}
	public void setPfxOutBytes(byte[] pfxOutBytes) {
		this.pfxOutBytes = pfxOutBytes;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getSerialCode() {
		return serialCode;
	}
	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	

}

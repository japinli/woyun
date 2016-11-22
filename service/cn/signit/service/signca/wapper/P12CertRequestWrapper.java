/**
* @author ZhangHongdong
* @date 2015年9月16日-下午1:56:04
* @see (参阅)
*/
package cn.signit.service.signca.wapper;


/**
 *p12证书请求的封装
 *
 * @ClassName P12CertRequestWrapper
 * @author ZhangHongdong
 * @date 2015年9月16日-下午1:56:04
 * @version 1.1.0
 */
public class P12CertRequestWrapper {
	private String keyPassword;
	private DNRequestWrapper dnRequestWrapper;
	
	public P12CertRequestWrapper(){
			this.keyPassword = "";
			this.dnRequestWrapper = new DNRequestWrapper();
	}
	
	public P12CertRequestWrapper(String keyPassword,
			DNRequestWrapper dnRequestWrapper) {
		this();
		if(keyPassword != null){
			this.keyPassword = keyPassword;
		}
		if(dnRequestWrapper != null){
			this.dnRequestWrapper = dnRequestWrapper;
		}
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	public DNRequestWrapper getDnRequestWrapper() {
		return dnRequestWrapper;
	}

	public void setDnRequestWrapper(DNRequestWrapper dnRequestWrapper) {
		this.dnRequestWrapper = dnRequestWrapper;
	}
}

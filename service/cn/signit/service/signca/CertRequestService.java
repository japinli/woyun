/**
* @author:ZhangHongdong
* @date:2016年2月26日-上午10:52:17
* @see: (参阅)
*/
package cn.signit.service.signca;

import cn.signit.service.signca.wapper.P12CertRequestWrapper;
import cn.signit.service.signca.wapper.P12CertResp;

/**
 *证书申请服务接口
 * @ClassName CertRequestService
 * @author ZhangHongdong
 * @date 2015年9月16日-下午1:45:04
 * @version 1.1.0
 */
public interface CertRequestService {
	/**
	* P12证书申请请求
	* 
	*@param p12CertRequestWrapper p12证书请求的封装
	*@return 申请P12证书的响应的封装。请求失败，则返回null
	*/
	public P12CertResp doP12CertRequest(P12CertRequestWrapper p12CertRequestWrapper);
	
	/**
	* P12一次性证书申请请求
	* 
	*@param p12CertRequestWrapper p12证书请求的封装
	*@return 申请P12证书的响应的封装。请求失败，则返回null
	*/
	public P12CertResp doP12OneTimeCertRequest(P12CertRequestWrapper p12CertRequestWrapper);
}

package cn.signit.service;

import cn.signit.domain.mysql.EvidenceInfo;

/**
*执行区块链相关操作
* @ClassName BlockService
* @author Liwen
* @date 2016年11月14日-上午10:38:43
* @version (版本号)
* @see (参阅)
*/
public interface BlockService {
	/**
	 * 向当前区块中添加新的证据信息
	 */
	public boolean addEvidences(EvidenceInfo info);
	
	public boolean createBlock();
}

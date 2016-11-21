package cn.signit.service;

import cn.signit.network.message.ReqMessage;
import cn.signit.network.message.RespMessage;

/**
*执行模拟P2P网络通信相关操作
* @ClassName P2PNetService
* @author Liwen
* @date 2016年10月10日-下午3:27:02
* @version (版本号)
* @see (参阅)
*/
public interface P2PNetPeersService {
	/**
	 * 执行查找，包括
	 * <br>查找peer节点
	 * <br>查找资源resource
	 * <br>查找相关信息info
	 */
	public RespMessage find(ReqMessage msg);
	
	/**
	 * 执行广播，包括
	 * <br>广播节点加入
	 * <br>广播资源添加
	 */
	public RespMessage advertise(ReqMessage msg);
	
}

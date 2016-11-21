package cn.signit.service.db;

import java.util.List;

import cn.signit.network.Peer;

/**
*节点信息服务
* @ClassName PeersService
* @author Liwen
* @date 2016年10月10日-下午3:28:24
* @version (版本号)
* @see (参阅)
*/
public interface PeersService {
	/**
	 * 获取通信节点列表
	 */
	public List<Peer> peerList();
	/**
	 * 新增通信节点
	 */
	public void addPeer(Peer peer);
	
	/**
	 * 移除列表
	 */
	public void removePeer(Peer peer);
	/**
	 * 节点是否在线
	 */
	public boolean isOnline(Peer peer);
}

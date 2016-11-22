package cn.signit.network;

import java.util.List;

import cn.signit.beans.PeersInfo;
import cn.signit.cons.PeerMode;
import cn.signit.pkcs.cert.X509CertSigner;

/**
*节点基础信息类
* @ClassName PeersTemp
* @author Liwen
* @date 2016年11月20日-上午8:10:33
* @version (版本号)
* @see (参阅)
*/
public  class PeersTemp {
	/**
	 * 本地节点签名者相关
	 */
	private X509CertSigner singer;
	/**
	 * 节点初始化模式
	 */
	private PeerMode model;
	
	/**
	 * 本地节点信息
	 */
	private Peer localPeer;
	/**
	 * 可信根节点
	 */
	private Peer root;
	/**
	 * 议长节点
	 */
	private Peer chairPeer;
	
	/**
	 * 在线节点列表
	 */
	private List<Peer> online;

	public Peer getRoot() {
		return root;
	}

	public void setRoot(Peer root) {
		this.root = root;
	}

	public Peer getChairPeer() {
		return chairPeer;
	}

	public void setChairPeer(Peer chairPeer) {
		this.chairPeer = chairPeer;
	}

	public List<Peer> getOnline() {
		return online;
	}

	public void setOnline(List<Peer> online) {
		this.online = online;
	}

	public PeerMode getModel() {
		return model;
	}

	public void setModel(PeerMode model) {
		this.model = model;
	}

	public Peer getLocalPeer() {
		return localPeer;
	}

	public void setLocalPeer(Peer localPeer) {
		this.localPeer = localPeer;
	}

	public X509CertSigner getSinger() {
		return singer;
	}

	public void setSinger(X509CertSigner singer) {
		this.singer = singer;
	}
	
	
}

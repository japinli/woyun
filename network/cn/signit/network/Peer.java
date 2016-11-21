package cn.signit.network;

import java.security.PublicKey;

/**
*定义节点信息
* @ClassName Peer
* @author Liwen
* @date 2016年10月10日-下午3:36:52
* @version (版本号)
* @see (参阅)
*/
public class Peer {
	private String url;//外网访问地址，如果有
	private String ip;//直接IP访问地址，用于内网通信
	private String name;//节点备注名称
	private String peerid;//定义节点在P2P网络中的
	private String peerKey;//节点的信任秘钥的散列值
	private int level;//节点存活等级
	private PublicKey publicKey;//节点公钥
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}

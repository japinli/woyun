/**
 * @author:Liwen
 * @date:2016年3月24日-下午1:19:18
 * @see: (参阅)
 */
package cn.signit.untils.mail.impl;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: ServerConfig
 * @author: Liwen   
 * @date:2016年3月24日-下午1:19:18 
 * @version:(版本号)
 * @see: (参阅)
 */
public class ServerInfo {
	private String host;
	private String sendAccount;
	private String sendPass;
	private String nickName;
	/**
	 * @param serverAddress;
	 * @param sendAccount;
	 * @param sendPass;
	 */
	public ServerInfo(String host,String sendAccount,String sendPass,String nickName){
		this.host=host;
		this.sendAccount=sendAccount;
		this.sendPass=sendPass;
		this.nickName=nickName;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSendAccount() {
		return sendAccount;
	}
	public void setSendAccount(String sendAccount) {
		this.sendAccount = sendAccount;
	}
	public String getSendPass() {
		return sendPass;
	}
	public void setSendPass(String sendPass) {
		this.sendPass = sendPass;
	}
	
}

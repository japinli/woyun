package cn.signit.controller.beans;

/**
*附加的上传信息
*可包含指定上传到的节点信息，附加信息等除上传文件本身以外的其他所有信息
* @ClassName ExtendUploadData
* @author Liwen
* @date 2016年11月11日-下午4:38:44
* @version (版本号)
* @see (参阅)
*/
public class ExtendUploadData {
	//是否加密
	private boolean isEncrypted;
	//加密秘钥
	private String password;
	
	private String signPassword;
	
	
	public boolean isEncrypted() {
		return isEncrypted;
	}
	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSignPassword() {
		return signPassword;
	}
	public void setSignPassword(String signPassword) {
		this.signPassword = signPassword;
	}
	
	
	
}

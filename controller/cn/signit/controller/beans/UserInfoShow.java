package cn.signit.controller.beans;

import cn.signit.cons.desc.IdentifyStateDesc;

/**
*个人中心信息展示封装
* @ClassName UserInfoShow
* @author Liwen
* @date 2016年6月1日-下午7:53:03
* @version 1.2.0
*/
public class UserInfoShow{
	private String identify=IdentifyStateDesc.UNTREATED.getDescription();;//实名认证
	private String emailIdentify=IdentifyStateDesc.UNTREATED.getDescription();//邮箱认证
	private String phoneIdentify=IdentifyStateDesc.UNTREATED.getDescription();;//手机认证
	private String account;//账户
	private String accountType;//账户类型
	private String email;//邮件地址
	private String phone;//手机
	private String realName;//真实姓名
	private String identifyLevel;//认证等级
	private String idCardCode;//身份证号
	
	public String getIdCardCode() {
		return idCardCode;
	}
	public void setIdCardCode(String idCardCode) {
		this.idCardCode = idCardCode;
	}
	public String getIdentifyLevel() {
		return identifyLevel;
	}
	public void setIdentifyLevel(String identifyLevel) {
		this.identifyLevel = identifyLevel;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public String getEmailIdentify() {
			if(getPhone()!=null){
				this.emailIdentify=IdentifyStateDesc.PASS.getDescription();
			}
		return emailIdentify;
	}
	public void setEmailIdentify(String emailIdentify) {
		this.emailIdentify = emailIdentify;
	}
	public String getPhoneIdentify() {
			if(getPhone()!=null){
				this.phoneIdentify=IdentifyStateDesc.PASS.getDescription();
			}
		return phoneIdentify;
	}
	public void setPhoneIdentify(String phoneIdentify) {
		this.phoneIdentify = phoneIdentify;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}

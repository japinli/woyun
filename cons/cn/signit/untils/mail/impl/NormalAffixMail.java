/**
 * @author:Liwen
 * @date:2016年3月24日-下午1:36:34
 * @see: (参阅)
 */
package cn.signit.untils.mail.impl;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: NormalAffixMail
 * @author: Liwen   
 * @date:2016年3月24日-下午1:36:34 
 * @version:(版本号)
 * @see: (参阅)
 */
public class NormalAffixMail extends NormalMail{
	
	public NormalAffixMail(MailAffix affix,ServerInfo serverInfo,SendInfo sendInfo){
		setServerInfo(serverInfo);
		setSendInfo(sendInfo);
		setAffix(affix);
	}
	public NormalAffixMail(ServerInfo serverInfo,SendInfo sendInfo){
		setServerInfo(serverInfo);
		setSendInfo(sendInfo);
	}
	public boolean send(){
		try{
			super.send(host, user, pwd);
			return true;
		}catch(Exception e){
			System.err.println("邮件发送失败!"+e.getLocalizedMessage());
			return false;
		}
	}
	public void setAffix(MailAffix affix){
		super.affix=affix.getaffixpath();
		super.affixbyte=affix.getAffixbyte();
		super.affixbyteType=affix.getAffixbyteType();
		super.affixName=affix.getAffixName();
	}
	public void setServerInfo(ServerInfo serverInfo){
		super.user=serverInfo.getSendAccount();
		super.from=serverInfo.getSendAccount();
		super.pwd=serverInfo.getSendPass();
		super.host=serverInfo.getHost();
	}
	public void setSendInfo(SendInfo sendInfo){
		super.to=sendInfo.getSendto();
		super.subject=sendInfo.getSubject();
		super.conText=sendInfo.getText();
	}
}

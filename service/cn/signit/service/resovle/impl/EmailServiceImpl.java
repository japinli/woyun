/**
 * @author:Liwen
 * @date:2016年3月24日-上午11:21:54
 * @see: (参阅)
 */
package cn.signit.service.resovle.impl;

import org.springframework.stereotype.Service;

import cn.signit.conf.ConfigProps;
import cn.signit.service.EmailService;
import cn.signit.untils.mail.impl.NormalAffixMail;
import cn.signit.untils.mail.impl.SendInfo;
import cn.signit.untils.mail.impl.ServerInfo;

/**
 * @ClassName: EmailServiceImpl
 * @author: Liwen   
 * @date:2016年3月24日-上午11:21:54 
 * @version:(版本号)
 * @see: (参阅)
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService{

	/**
	*@param subject
	*@param nickname
	*@param message
	*@return
	*@see: (参阅)
	*/
	@Override
	public boolean send(String subject, String sendto, String message) {
		ServerInfo serverInfo=getMailServerInfo();
		SendInfo sendInfo=new SendInfo(sendto,subject,message);
		NormalAffixMail mail=new NormalAffixMail(serverInfo,sendInfo);
		return mail.send();
	}

	/**
	*@param subject
	*@param nickname
	*@param message
	*@param affixbyte
	*@param affixbyteType
	*@param affixName
	*@return
	*@see: (参阅)
	*/
	@Override
	public boolean send(String subject, String sendto, String message, byte[] affixbyte, String affixbyteType,
			String affixName) {
		return false;
	}
	
	private ServerInfo getMailServerInfo(){
		String mailServer=ConfigProps.get("email.host");
		String mailAccount=ConfigProps.get("email.account");
		String mailPass=ConfigProps.get("email.pass");
		String nickName=ConfigProps.get("common_nickname");
		ServerInfo serverInfo=new ServerInfo(mailServer,mailAccount,mailPass,nickName);
		return serverInfo;
	}

}

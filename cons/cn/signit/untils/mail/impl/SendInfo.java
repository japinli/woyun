/**
 * @author:Liwen
 * @date:2016年3月24日-下午1:21:42
 * @see: (参阅)
 */
package cn.signit.untils.mail.impl;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: MailConfig
 * @author: Liwen   
 * @date:2016年3月24日-下午1:21:42 
 * @version:(版本号)
 * @see: (参阅)
 */
public class SendInfo {
	private String sendto;
	private String subject;
	private String text;
	public SendInfo(String sendto,String subject,String text){
		this.sendto=sendto;
		this.subject=subject;
		this.text=text;
	}
	public String getSendto() {
		return sendto;
	}
	public void setSendto(String sendto) {
		this.sendto = sendto;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}

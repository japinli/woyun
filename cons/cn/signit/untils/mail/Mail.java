package cn.signit.untils.mail;

/**
 * 邮件发送接口
 */
public interface Mail {
	void setAddress(String from,String to,String subject);
	void setConText(String conText);
	void send(String host, String user, String pwd) throws Exception;
}

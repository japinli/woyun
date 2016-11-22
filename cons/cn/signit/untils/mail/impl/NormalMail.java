package cn.signit.untils.mail.impl;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.signit.untils.mail.Mail;

/**
 *发送带普通邮件
 * @ClassName SessionListener
 * @author:liwen
 * @date 2014年12月29日-上午10:43:53
 * @version 1.0.0
 */
public class NormalMail implements Mail{
	protected String host = "";  //smtp服务器
	protected String from = "";  //发件人地址
	protected String to = "";    //收件人地址
	protected String affix =null; //附件地址
	protected byte[] affixbyte;//附件字节流
	protected String affixbyteType;//附件字节流类型
	protected String affixName = ""; //附件名称
	protected String user = "";  //用户名
	protected String pwd = "";   //密码
	protected String subject = ""; //邮件标题
	protected String conText="";//邮件正文
	 /**
	  * 邮件的发件人地址、收件人地址及邮件标题设置
	  */
	 public void setAddress(String from,String to,String subject){
	  this.from = from;
	  this.to   = to;
	  this.subject = subject;
	 }
	 /**
	  * 邮件正文
	  */
	 public void setConText(String conText){
		  this.conText=conText;
		 }
	 /**
	  * 邮件发送
	  */
	    public void send(String host,String user,String pwd) {
	     this.host = host;
	     this.user = user;
	     this.pwd  = pwd;
	      
	        Properties props = new Properties();
	      
	        //设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
	        props.put("mail.smtp.host", host);
	        //需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
	        props.put("mail.smtp.auth", "true");
	      
	        //用刚刚设置好的props对象构建一个session
	        Session session = Session.getDefaultInstance(props);
	      
	        //有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
	        //用（你可以在控制台（console)上看到发送邮件的过程）
	        session.setDebug(true);
	      
	        //用session为参数定义消息对象
	        MimeMessage message = new MimeMessage(session);
	        try{
	         //加载发件人地址
	            message.setFrom(new InternetAddress(from));
	           //加载收件人地址
	            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
	           //加载标题
	            message.setSubject(subject);
	          
	            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
	            Multipart multipart = new MimeMultipart();         
	          
	          
	            //   设置邮件的文本内容
	            BodyPart contentPart = new MimeBodyPart();
	           /* contentPart.setText("邮件的具体内容在此");*/
	           // contentPart.setText(conText);
	            contentPart.setContent(conText, "text/html;charset=utf-8");
	            multipart.addBodyPart(contentPart);
	            //将multipart对象放到message中
	            message.setContent(multipart);
	            //保存邮件
	            message.saveChanges();
	            //   发送邮件
	            Transport transport = session.getTransport("smtp");
	            
	            //连接服务器的邮箱
	            transport.connect(host, user, pwd);
	            //把邮件发送出去
	            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			        CommandMap.setDefaultCommandMap(mc);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }catch(Exception e){
	            e.printStackTrace();
	            System.err.println("邮件:"+to+"发送失败!");
	        }
	    }  
}

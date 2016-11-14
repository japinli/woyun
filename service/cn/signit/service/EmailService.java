/**
 * @author:Liwen
 * @date:2016年3月24日-上午11:19:06
 * @see: (参阅)
 */
package cn.signit.service;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: EmailService
 * @author: Liwen   
 * @date:2016年3月24日-上午11:19:06 
 * @version:(版本号)
 * @see: (参阅)
 */
public interface EmailService {
	public boolean send(String subject,String sendto,String message);
	public boolean send(String subject,String sendto,String message,byte[] affixbyte,String affixbyteType,String affixName);
}

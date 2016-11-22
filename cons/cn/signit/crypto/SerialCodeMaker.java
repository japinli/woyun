/**
* @author ZhangHongdong
* @date 2015年4月9日-上午11:33:57
* @see (参阅)
*/
package cn.signit.crypto;

import java.nio.ByteBuffer;
import java.util.UUID;

import cn.signit.tools.utils.Base64Utils;



/**
 *序列码制作工具
 *
 * @ClassName SerialCodeMaker
 * @author ZhangHongdong
 * @date 2015年4月9日-上午11:33:57
 * @version 1.1.0
 */
public final class SerialCodeMaker {
		private final static char KEY = 0x06;
		
		private static String make(){
			return UUID.randomUUID().toString();
		}
		
		/**
		* 产生加密的原始序列码，如果需要获得解密的原始序列码，则只能通过<br/>
		* {@link cn.signit.utils.OriginalSerialCodeMakerUtil.getSecretMake(String fromSecretMakeStr)}
		* <br/>
		* 获得
		* 
		*@return 加密的原始序列码字符串(base64编码)
		*/
		public static String getSecret(){
			return getSecret(make());
		}
		
		/**
		* 产生加密的原始序列码，如果需要获得解密的原始序列码，则只能通过<br/>
		* {@link cn.signit.utils.OriginalSerialCodeMakerUtil.getSecretMake(String fromSecretMakeStr)}
		* <br/>
		* 获得
		* @param uuid 唯一标识符
		* 
		*@return 加密的原始序列码字符串(base64编码)
		*/
		public static String getSecret(String uuid){
			if(uuid == null){
				uuid = make();
			}
			int uuidLen = uuid.length();
			byte[] toStrBytes = new byte[uuidLen];
	        for (int i = 0; i < uuidLen; i++){  
	        	char uuidSecretChar = (char) (uuid.charAt(i) ^ KEY);
	        	toStrBytes[i] = (byte)(uuidSecretChar & 0xFF);
	        }  
			return Base64Utils.encode(toStrBytes);
		}
	
		/**
		* 获得解密的原始序列码
		* 
		*@param fromSecret 加密的原始序列码，只能是通过<br/>
		*{@link cn.signit.utils.OriginalSerialCodeMakerUtil.secretMake()}
		*<br/>
		*处理后的字符串
		*
		*@return 解密的原始序列码字符串
		*/
		public static String getPlain(String fromSecret){
			if(fromSecret == null){
				return null;
			}
			byte[] fromStrBytes = Base64Utils.decode(fromSecret);
			ByteBuffer bb = ByteBuffer.wrap(fromStrBytes);
			char[] chars = new char[bb.remaining()];
			for(int i=0; i < chars.length; i++){
				chars[i] = (char)((fromStrBytes[i+bb.position()] & 0xFF) ^ KEY);
			}
			return String.valueOf(chars);
		}
		
	/*	public static void main(String[] args) {
			System.out.println(getSecretMake("ZWVkMDQxZTYrNjE+MysyYjVlKz9kZGUrNDE1PzJiNTdkYmJj"));
		}*/
		
		public static void main(String[] args) {
			String toSecretStr = null;
			long s1 = System.currentTimeMillis();
			//toSecretStr = secretMake();
			for(int i=0; i < 1000000; i++){
				toSecretStr = getSecret();
				getPlain(toSecretStr);
			}
			//toSecretStr = secretMake();
			long e1 = System.currentTimeMillis();
			System.out.println(e1-s1);
			System.out.println("存入数据库======>"+toSecretStr);
			System.out.println("数据库取出======>"+toSecretStr);
			System.out.println("解码取出的数据======>"+getPlain(toSecretStr));
		}
}

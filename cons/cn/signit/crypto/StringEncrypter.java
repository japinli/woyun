/**
61.147.103.6 * @author:zhanghongdong
 * @date:2014-4-6-下午2:08:32
 * @see: (参阅)
 */
package cn.signit.crypto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cn.signit.tools.utils.Base64Utils;



/**
 * 字符串消息加密器
 * 
 * @author: zhanghongdong
 * @date:2014-4-6-下午2:08:32
 * @version:0.1.0
 */
public class StringEncrypter {
	private final static String ENCODING = "UTF-8"; 
	public final static String DEFAULT_AGLORITHM = "AES"; 
	
	/**
	 * 对称加密字符串
	 * 
	 * @param content 待加密内容
	 * @param password 加密密钥
	 * @param aglorithm 加密算法
	 * @return 加密后的消息字符串（base64编码）
	 */
	public static String encrypt(String content,String password,String aglorithm)  {
		return encrypt(false, content, password,aglorithm);
	}
	
	/**
	 * AES加密字符串
	 * 
	 * @param content 待加密内容
	 * @param password 加密密钥
	 * @return 加密后的消息字符串（base64编码）
	 */
	public static String aesEncrypt(String content,String password) {
		return encrypt(false, content, password,DEFAULT_AGLORITHM);
	}
	
	/**
	* 针对URL编码的AES加密字符串
	* 
	*@param content 待加密内容
	*@param password 加密密钥
	*@param aglorithm 加密算法
	*@return 加密后的针对URL的消息字符串（base64编码，且"+"和"/"分别替换成了"-"和"_"）
	*/
	public static String aesEncrypt4URL(String content,String password) {
		return encrypt(true, content, password,DEFAULT_AGLORITHM);
	}
	
	/**
	* 针对URL编码的对称加密字符串
	* 
	*@param content 待加密内容
	*@param password 加密密钥
	*@param aglorithm 加密算法
	*@return 加密后的针对URL的消息字符串（base64编码，且"+"和"/"分别替换成了"-"和"_"）
	*/
	public static String encrypt4URL(String content,String password,String aglorithm) {
		return encrypt(true, content, password,aglorithm);
	}
	
	private static String encrypt(boolean isForURL,String content,String password,String aglorithm) {
		byte[] encryptResult = null;
		String encryptResultStr = null;
		encryptResult = encrypt(content.getBytes(Charset.forName(ENCODING)), password,aglorithm);
		if(!isForURL){
			encryptResultStr = Base64Utils.encode(encryptResult);
		}else{
			encryptResultStr = Base64Utils.encode4URL(encryptResult);
		}
		return encryptResultStr;
	}

	/**
	 * AES解密字符串
	 * 
	 * @param encryptResultStr 待解密内容（base64编码）
	 * @param password 待解密密钥
	 * @return 解密后的消息字符串
	 */
	public static String aesDecrypt(String encryptResultStr,String password){
			return decrypt(false, encryptResultStr, password,DEFAULT_AGLORITHM);
	}
	/**
	 * 对称解密字符串
	 * 
	 * @param encryptResultStr 待解密内容（base64编码）
	 * @param password 待解密密钥
	 * @param aglorithm 解密算法
	 * @return 解密后的消息字符串
	 */
	public static String decrypt(String encryptResultStr,String password,String aglorithm) {
			return decrypt(false, encryptResultStr, password,aglorithm);
	}
	
	/**
	 * 针对URL编码的对称解密字符串
	 * 
	 * @param encryptResultStr 待解密内容（base64编码，且"+"和"/"分别已经被替换成了"-"和"_"）
	 * @param password 待解密密钥
	 * @param aglorithm 解密算法
	 * @return 解密后的消息字符串
	 */
	public static String decrypt4URL(String encryptResultStr,String password,String aglorithm) {
			return decrypt(true, encryptResultStr, password,aglorithm);
	}
	
	/**
	 * 针对URL编码的AES解密字符串
	 * 
	 * @param encryptResultStr 待解密内容（base64编码，且"+"和"/"分别已经被替换成了"-"和"_"）
	 * @param password 待解密密钥
	 * @param aglorithm 解密算法
	 * @return 解密后的消息字符串
	 */
	public static String aesDecrypt4URL(String encryptResultStr,String password) {
			return decrypt(true, encryptResultStr, password,DEFAULT_AGLORITHM);
	}
	
	private  static String decrypt(boolean isForURL,String encryptResultStr,String password,String aglorithm) {
		byte[] base64DecrptBytes = null;
		if(!isForURL){
			base64DecrptBytes = Base64Utils.decode(encryptResultStr);
		}else{
			base64DecrptBytes = Base64Utils.decode4URL(encryptResultStr);
		}
		//BASE64位解密
		byte[] plainStrBytes = decrypt(base64DecrptBytes, password,aglorithm);
		
		// BASE64位解密
	/*	String decrpt = ebotongDecrypto(encryptResultStr);
		byte[] decryptFrom = parseHexStr2Byte(decrpt);
		byte[] decryptResult = decrypt(decryptFrom, password);*/
		return new String(plainStrBytes,Charset.forName(ENCODING));
	}

	/**
	 * BASE64编码字符串
	 */
	public static String ebotongEncrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = str.getBytes(ENCODING);
				result = Base64Utils.encode(encodeByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//base64加密超过一定长度会自动换行 需要去除换行符
		return result.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
	}

	/**
	 * BASE64解码字符串
	 */
	public static String ebotongDecrypto(String str) {
		try {
			String Decstr = new String(Base64Utils.decode(str),"utf-8");
			return Decstr;
		} catch (IOException e) {
			e.printStackTrace();
			return str;
		}
	}
	
	/**  
	 * 加密  
	 *   
	 * @param content 需要加密的内容  
	 * @param password  加密密钥
	 * @param alogrithm 加密算法
	 * @return  加密后的字节数组,如果失败,则返回null
	 */  
	private static byte[] encrypt(byte[] content, String password,String alogrithm)  {   
	        try {              
	                KeyGenerator kgen = KeyGenerator.getInstance(alogrithm); 
	                //防止linux下 随机生成key
	                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
	                secureRandom.setSeed(password.getBytes());   
	                kgen.init(128, secureRandom);
	                //kgen.init(128, new SecureRandom(password.getBytes()));   
	                SecretKey secretKey = kgen.generateKey();   
	                byte[] enCodeFormat = secretKey.getEncoded();   
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, alogrithm);   
	                Cipher cipher = Cipher.getInstance(alogrithm);//创建密码器AES默认使用"AES/ECB/PKCS5Padding"  
	                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
	                byte[] result = cipher.doFinal(content);   
	                return result; // 加密   
	        } catch (Exception e) {   
	                e.printStackTrace();   
	        } 
	        return null;   
	}


	/**解密  
	 * @param content  待解密内容  
	 * @param password 解密密钥  
	 * @param alogrithm 解密算法
	 * @return   解密后的字节数组,如果失败,则返回null
	 */  
	private static byte[] decrypt(byte[] content, String password,String alogrithm)  {   
	        try {   
	                 KeyGenerator kgen = KeyGenerator.getInstance(alogrithm); 
	               //防止linux下 随机生成key
		             SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
		             secureRandom.setSeed(password.getBytes());   
		             kgen.init(128, secureRandom);
	                 //kgen.init(128, new SecureRandom(password.getBytes()));   
	                 SecretKey secretKey = kgen.generateKey();   
	                 byte[] enCodeFormat = secretKey.getEncoded();   
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, alogrithm);               
	                 Cipher cipher = Cipher.getInstance(alogrithm);//创建密码器AES默认使用"AES/ECB/PKCS5Padding"
	                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
	                byte[] result = cipher.doFinal(content);   
	                return result; // 加密   
	        } catch (Exception e) {   
	                e.printStackTrace();   
	        }
	        return null;   
	}  


	
	/**
	 * 将字符串字节数组进行十六进制编码
	 * @param results 用于进行十六进制的字节数组
     * @return 返回十六进制的字符串
     */  
	public static String bytesToHexString(byte[] results){
		 if(results == null)
             return null;
     StringBuilder hexString = new StringBuilder();
     for(int i=0;i<results.length;i++){
             int hi = (results[i]>>4) & 0x0f;
             int lo = results[i] & 0x0f;
             hexString.append(Character.forDigit(hi, 16)).append(Character.forDigit(lo, 16));
     }
     return hexString.toString();
	}
	
	/**
	* 将十六进制的字符串转换成原始字符串的字节数组
	* 
	*@param hexString 十六进制的字符串
	*@return 原始字符串的字节数组
	*/
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}
	
	 private static byte charToByte(char c) {  
		    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	
	
/*private static String getOperatedStr(ProcessBuilder pb) {
		Process p = null;
		String getStr = null;
		try {
			p = pb.start();
			if (p.waitFor() == 0) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);+
				}
				line = sb.toString();
				if (!Pattern.compile("Exception").matcher(line).find())
					getStr = line;
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			p.destroy();
		}
		return getStr;
	}*/

	public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException {
		System.out.println(aesEncrypt("123456", "zhd"));
		//System.out.println(decrypt("mXSYBvhcaFf/vlgUVHgpJw==", "zhd"));
		//System.out.println(decrypt4URL("WaHzBPyq7cBHWnO2X_K6yh3b6WjOl1aqXQSxtfEVbEQ=", "742825668w4Zp6VE2k0Aj783n475B793", "AES"));
		//System.out.println(new Date(1438574152855l).toString());
		//System.out.println(encrypt4URL(String.valueOf(System.currentTimeMillis())+"&"+AuthTokenMaker.getStringRandom(5), "zhd1234567"));
		/*for (int i = 0; i < 100000; i++) {
			System.out.println(StringEncrypter.encrypt("zhd", "123456"));
			System.out.println(StringEncrypter.decrypt("GHelf2wp6eHoLCx6wmUkug==", "123456"));
		}*/
		//System.out.println(TextEncrypter.encrypt("SUN"));
		//System.out.println(TextEncrypter.decrypt("OEE4RjZGRjUyRjE1MUYzNDI4MURFNTk4N0U3MEZFRkNBNTMwOEQyQzdEMzA1NzNBRTY2NzkxNEU0MkU1QTVBQTAyMEM0NUI4ODUxRDAwMUIwMTVFRjdFN0I1OUE0REZD"));
	}
}

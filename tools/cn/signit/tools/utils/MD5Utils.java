package cn.signit.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Utils
 * @Description: TODO(MD5加密工具)
 * @author zhanghongdong   
 * @date 2014-3-22 下午12:28:15 
 * 
 */
public class MD5Utils {
	
	/**
	 * MD5编码生成32位MD5码 
	 * @param inStr 将要进行MD5编码的字符串
     * @return 返回十六进制的MD5字符串
     */  
	public static String toMD5(String inStr){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for(int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte)charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		return bytesToHexString(md5Bytes);
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
	
	/**
	 * 将MD5编码的字符串进行加密
	 * @param inStr 将要进行MD5编码加密的字符串
     * @return 将MD5编码的字符串进行加密后的字符串
     */  
	 public static String toEncryptedMD5(String inStr){
	    	return convertMD5(toMD5(inStr));
	    }
	 
	 /**
	  	 * 将MD5编码的字符串进行解密
		 * @param inEncrypted MD5编码的字符串进行加密后的字符串
	     * @return MD5编码的字符串
	     */  
	    public static String toDecryptedMD5(String inEncrypted){
	    	return convertMD5(inEncrypted);
	    }
	 /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    private static String convertMD5(String inStr){  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ '^');  
        }  
        String s = new String(a);  
        return s;  
    } 
    public static void main(String[] args) {
    	String str = MD5Utils.toMD5("esignserver.ks.pwd@signit.cn");
    	//山东CA
    	System.out.println(MD5Utils.toEncryptedMD5("002590c68086"));
    	System.out.println(MD5Utils.toEncryptedMD5(str));
    	System.out.println(MD5Utils.toMD5("v1&2184M3-G5-01M2-r6-U3p-619-R5S1h0&1435126369510&MD5"));
    	System.out.println(MD5Utils.toMD5("v1&1435126369510&6Qp9d69Sfx&MD5"));
    
	}
}

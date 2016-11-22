/**
* @author ZhangHongdong
* @date 2015年8月31日-下午11:10:35
* @see (参阅)
*/
package cn.signit.untils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *Unicode编码工具
 * @ClassName UnicodeUtil
 * @author ZhangHongdong
 * @date 2015年8月31日-下午11:10:35
 * @version 1.1.0
 */
public class UnicodeUtil {
	private final static Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
	
	 /**
	* 中文转Unicode
	* 
	*@param str 中文字符串
	*@return Unicode编码形式的字符串
	*/
	public static String chineseToUnicode(String str){  
	        StringBuilder result = new StringBuilder("");  
	        for (int i = 0; i < str.length(); i++){  
	            int chr1 = (char) str.charAt(i);  
	            //汉字范围 \u4e00-\u9fa5 (中文)  
	            if(chr1>=19968&&chr1<=171941){
	                result.append("\\u" + Integer.toHexString(chr1));
	            }else{  
	                result.append(str.charAt(i));
	            }  
	        }  
	        return result.toString();  
	    }  
	 

	 /**
	* Unicode转普通字符串
	* 
	*@param str Unicode编码形式的字符串
	*@return 普通字符串
	*/
	public static String unicodeToString(String str) { 
		 Matcher matcher = PATTERN.matcher(str);
		 char ch;
		 while (matcher.find()) {
		     ch = (char) Integer.parseInt(matcher.group(2), 16);
		     str = str.replace(matcher.group(1), ch + "");    
		 }
		 return str;
	}
	
	public static String utf8ToString(String str){
		try {
			return java.net.URLDecoder.decode(str,   "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}
}

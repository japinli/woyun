/**
* @author ZhangHongdong
* @date 2014年12月12日-上午9:26:19
* @see  (参阅)
*/
package cn.signit.cons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *正则表达式常量类
 * @ClassName RegexType
 * @author ZhangHongdong
 * @date 2014年12月12日-上午9:26:19
 * @version 1.0.0
 */
public final class RegexType {
		public final static String PROTECTED_URI = "";
	
		public final static String QUERY_METHOD = "^[0-9]{1,2}$";/*1到2位的任意整型数字*/
		public final static String DATA_GET_METHOD = "^(?:data|path)$";/*数据获取方式，data：通过数据（Data）自身（base64编码的字符串）获得数据; path：通过路径（Path）获得数据*/
		public final static String SIGN_STATE = "^(?:unsigned|signed|no_cert_signed|before_signed|all_own_signed|all_signed)$";
		public final static String DATA_POST_METHOD = DATA_GET_METHOD;
		public final static String ANY_NUMBER = "^[0-9]{0,19}$";/*任意整型1~19位长度的数字*/
		public final static String ANY_NORMAL_TEXT = "^[a-zA-Z0-9_%#-\u4e00-\u9fa5]+$";/*任意正常字符*/
		public final static String MD5_NUMBER = "^[a-fA-F0-9]{32,32}$";/*MD5字符串*/
		public final static String STRONG_PASSWORD = "^(?![0-9]*$)(?![a-zA-Z]*$)(?![!\\[\\]\"#$%&'()*+,\\-./:;<=>?@^_`{|}~]*$).{6,50}$"; /* 至少6位（6~50位）且由字母、数字、特殊字符，不含空格的任两种的组合*/
		public final static String BOOLEAN = "^(?:true|false)$";/*true/false*/
		public final static String EMAIL="^([a-zA-Z0-9]+[?:_|\\_|\\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[?:_|\\_|\\.|-]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";/*电子邮箱有效格式*/
		public final static String USERNAME="^[a-zA-Z]\\w{2,19}$";/*用户名有效格式（用户名只能包含大小字母、数字、下划线，且只能以字母开头,长度3~20位）*/
		public final static String PHONE_NUMBER="^(?:13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$";/*有效手机号码*/
		public final static String  IP = "^(?:([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]))$";/*有效IP地址*/
		public final static String SIGN_FILE_TYPE="^(?:(?i)pdf|doc|docx|xls|xlsx|ppt|pptx|wps|et|dps)$";/*支持的签名文件类型*/
		public final static String VERIFY_FILE_TYPE="^(?:pdf)$";/*支持的验证签名文件类型*/
		public final static String SEAL_TYPE = "^(?:(?i)png|jpeg|jpg|jpe|gif|bmp|tiff)$";/*支持的印章文件类型*/
		public final static String OFFICE_TYPE="^(?:(?i)doc|docx|xls|xlsx|ppt|pptx|wps|et|dps)$";/*支持的office文件类型*/
		public final static String REG_TYPE_CODE="^\\d{5}$";/*用户注册角色类型编码*/
		public final static String ROLE_TYPE_NAME="^(?:person|enterprise)$";/*用户注册角色类型名编码*/
		
		public static boolean find(String regex,String matchingStr){
			return Pattern.compile(regex).matcher(matchingStr).find();
		}
		public static void main(String[] args) {
			//[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?
			Pattern p = Pattern.compile(EMAIL);
			Matcher m = p.matcher("zhd-zz@signit.cn");
			System.out.println(m.find());
			//String documentLocation = "/signit/.cloudsign/RES-USER/doc/xxx/rewr33-dffsd-dfdf-klsdf9.xxx";
			//String toPdfLoc = new StringBuffer(documentLocation).replace(documentLocation.lastIndexOf("xxx"), documentLocation.length(), "pdf").toString();
			//System.out.println(toPdfLoc);
			System.out.println(System.currentTimeMillis());
		}
}

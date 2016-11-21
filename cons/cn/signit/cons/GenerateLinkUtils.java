/**
* @author ZhangHongdong
* @date 2014年12月17日-下午3:01:14
* @see  (参阅)
*/
package cn.signit.cons;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import cn.signit.tools.utils.MD5Utils;

/**
 * 生成帐户激活、重新设置密码的链接 
 * @ClassName GenerateLinkUtils
 * @author ZhangHongdong
 * @date 2014年12月17日-下午3:01:14
 * @version 1.0.0
 */
public class GenerateLinkUtils {  
      
    private static final String CHECK_CODE = "checkCode";  
      
    /** 
     * 生成帐户激活链接 
     */  
    public static String generateActivateLink(ServletRequest request,String servletPath) {  
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	return httpRequest.getRequestURL().toString().replace(httpRequest.getServletPath(), servletPath);
    }  
      
    /** 
     * 生成重设密码的链接 
     */  
    public static String generateResetPwdLink(ServletRequest request,String servletPath) {  
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	return httpRequest.getRequestURL().toString().replace(httpRequest.getServletPath(), servletPath);
    }  
    
    /** 
     * 生成支付成功后的支付宝调用通知返回的链接 
     */  
    public static String generatePaymentReturnLink(ServletRequest request,String servletPath){
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	return httpRequest.getRequestURL().toString().replace(httpRequest.getServletPath(), servletPath);
    }
      
    /** 
     * 生成验证帐户的MD5校验码 
     * @param userOriginalSerialCode 用户原始序列码
     * @param userRandomCode 用户随机码
     * @return 将用户邮箱和随机码组合后，通过md5加密后的16进制格式的字符串 
     */  
    public static String generateCheckcode(String userOriginalSerialCode,String userRandomCode) {  
        String userOrigSerialCode = OriginalSerialCodeMaker.getSecretMake(userOriginalSerialCode);
        String randomCode = userRandomCode;  
        return MD5Utils.toMD5(userOrigSerialCode + "$" + randomCode);  
    }  
      
    /** 
     * 验证校验码是否和注册时发送的验证码一致 
     * @param userOriginalSerialCode 将要激活的用户原始序列码
     * @param userRandomCode 将要激活的用户随机码
     * @param checkcode 注册时发送的校验码 
     * @return 如果一致返回true，否则返回false 
     */  
    public static boolean verifyCheckcode(String userOriginalSerialCode,String userRandomCode,ServletRequest request) {  
        String checkCode = request.getParameter(CHECK_CODE);  
        return generateCheckcode(userOriginalSerialCode,userRandomCode).equals(checkCode);  
    }  
    
    /** 
     * 验证校验码是否和注册时发送的验证码一致 
     * @param userOriginalSerialCode 将要激活的用户原始序列码
     * @param userRandomCode 将要激活的用户随机码
     * @param checkcode 注册时发送的校验码 
     * @return 如果一致返回true，否则返回false 
     */  
    public static boolean verifyCheckcode(String userOriginalSerialCode,String userRandomCode,String checkCode) {  
    	return generateCheckcode(userOriginalSerialCode,userRandomCode).equals(checkCode);  
    }  
}  
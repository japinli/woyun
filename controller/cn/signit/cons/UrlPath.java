package cn.signit.cons;

/**
*请求路径
* @ClassName UrlPath
* @author Liwen
* @date 2016年4月19日-上午11:49:37
* @version (版本号)
* @see (参阅)
*/
public class UrlPath {

			
			//====================系统根路径=======================
			public final static String HOME_ROOT = "/";//WEB根目录名
			public final static String SYSTEM_ROOT = "/wesign";//系统页面根目录名
		
			
			public final static String API_ERROR=SYSTEM_ROOT+"/error";//API错误通知类
			//*********API相关***************//
			public final static String API_ERROR_SIGN=API_ERROR+"/sign";//API签名验证错误
			public final static String API_ERROR_APPID=API_ERROR+"/app";//API权限错误通知类
			public final static String API_ERROR_PATH=API_ERROR+"/path";//API路径错误通知类
			
			//====================页面根路径=======================
			public final static String COMMON_ROOT = SYSTEM_ROOT+"/common";//公共资源目
			public final static String USER_ROOT = SYSTEM_ROOT+"/user";
			public final static String USER_ANY_ROOT = SYSTEM_ROOT+"/user";
			public final static String DOC_ROOT =USER_ROOT+"/documents";//用户文档资源
				//用户文档资源(包含匿名角色)
			public final static String DOC_ANY_ROOT =USER_ANY_ROOT+"/documents";
			public final static String CERT_ROOT =USER_ROOT+"/certificates";//用户证书资源

			//====================错误页面路径=======================
			public final static String PAGE_403 = "/**/403.html"; //403-禁止访问错误页面
			public final static String PAGE_404 = "/**/404.html"; //404-找不到资源错误页面
			public final static String PAGE_500 = "/**/500.html"; //500-服务器内部错误页面-服务器正在升级或维护
			public final static String PAGE_503 = "/**/503.html"; //503-服务器不可达错误页面-服务器正在升级或维护
			
			
			//====================主页页面路径=======================
			public final static String PAGE_HOME = "/index.html";//官网主页面
			public final static String PAGE_APP_DOWNLOAD = "/app-download.html";//官网扫码下载APP页面
			public final static String PAGE_STATE = "/state.html";//官网服务条款声明页面
			
			//====================用户页面路径=======================
			public final static String USER_ROOT_RES = SYSTEM_ROOT+"/**";
			//登录前
			public final static String PAGE_USER_LOGIN = SYSTEM_ROOT+"/login.html";//登录页面
			public final static String PAGE_USER_REGISTER = SYSTEM_ROOT+"/register.html";//注册页面
			public final static String PAGE_USER_FIND_PWD= SYSTEM_ROOT+"/find-password.html";//找回密码页面
			public final static String PAGE_USER_RESET_PWD= SYSTEM_ROOT+"/reset-password.html";//重设密码页面
			public final static String PAGE_USER_ACTIVATE_TIP = SYSTEM_ROOT+"/activate-tip.html";//激活账号提示页面
			public final static String PAGE_USER_REACTIVATE_TIP = SYSTEM_ROOT+"/reactivate-tip.html";//获取重新激活账号提示页面
			public final static String PAGE_USER_ACTIVATE_STATE_TIP = SYSTEM_ROOT+"/activate-state-tip.html";//激活状态提示页面
			public final static String PAGE_USER_TRANSIT_STATE_TIP = SYSTEM_ROOT+"/transit-state-tip.html";//过渡状态提示页面
			
			

			//登录后
			public final static String PAGE_USER_HOME = USER_ROOT+PAGE_HOME;//用户个人中心页面
			public final static String PAGE_USER_MY_INFO = USER_ROOT+"/my-info.html";//用户个人信息页面
			public final static String PAGE_USER_INFO_RESET_PWD = USER_ROOT+"/my-info/reset-password.html";//用户个人重设登录密码页面
			public final static String PAGE_USER_INFO_RESET_PHONE  = USER_ROOT+"/my-info/reset-phone.html";//用户个人绑定或重设手机页面
			public final static String PAGE_USER_INFO_RESET_EMAIL = USER_ROOT+"/my-info/reset-email.html";//用户个人绑定或重设邮箱页面
			public final static String PAGE_USER_PERSON_AUTH = USER_ROOT+"/person-auth.html";//用户个人实名认证页面
			public final static String PAGE_USER_PERSON_AUTH_RESULT_BY_ALIPAY = USER_ROOT+"/person-auth/auto-mode/alipaycertifi-result.html";//用户个人采用支付宝自动实名认证回调结果页面
			public final static String PAGE_USER_ENTERPRISE_AUTH = USER_ROOT+"/enterprise-auth.html";//用户企业实名认证页面
			public final static String PAGE_USER_LOGOUT = USER_ROOT+"/logout.html";//用户个人信息页面
			public final static String PAGE_USER_DOC_HOME = DOC_ROOT+"/index.html";//用户个人信息页面

			
			//====================证书页面路径（登录后）=======================
			public final static String PAGE_CERT_HOME = CERT_ROOT+PAGE_HOME;//用户证书中心页面
			public final static String PAGE_CERT_DETAILS = CERT_ROOT+"/details.html";//用户证书详情页面
			public final static String PAGE_CERT_RENEWAL = CERT_ROOT+"/renewal.html";//用户证书更新页面
			//====================证书JSON接口路径（登录后）=======================
			public final static String JSON_CERT_SRV = CERT_ROOT+"/server-side/request";//用户服务器端证书申请接口地址
			public final static String JSON_CERT_RES = CERT_ROOT+"/{cert-id}";//用户指定证书资源接口地址
			public final static String JSON_CERTS_RES = CERT_ROOT;//用户证书列表资源接口地址

			
			public final static String HANDLE_LOGIN=USER_ROOT+"/login";//用户登录逻辑处理
			/**
			* 解析含有占位符的指定URL路径
			*@param urlPath 含有占位符的指定URL路径
			*@param replaceStr 待替换的字符串
			*@return 真正可用的URL路径
			*@since 1.2.0
			*@author Zhanghongdong
			*/
			public static String getRealUrlPath(String urlPath,Object... replaceStr){
				if(replaceStr == null){
					return urlPath;
				}
				for (Object str : replaceStr) {
					urlPath = urlPath.replaceFirst(REGEX_PLACE_HOLDER, str.toString());
				}
				 return urlPath;
			}
			
			/**
			* 解析并重定向含有占位符的指定URL路径
			*@param urlPath 含有占位符的指定URL路径
			*@param replaceStr 待替换的字符串
			*@return 重定向到真正可用的URL路径
			*@since 1.2.0
			*@author Zhanghongdong
			*/
			public static String redirectUrlPath(String urlPath,Object... replaceStr){
				return "redirect:".concat(getRealUrlPath(urlPath, replaceStr));
			}
			
			/**
			* 解析并转发含有占位符的指定URL路径
			*@param urlPath 含有占位符的指定URL路径
			*@param replaceStr 待替换的字符串
			*@return 重定向到真正可用的URL路径
			*@since 1.2.0
			*@author Zhanghongdong
			*/
			public static String forwardUrlPath(String urlPath,Object... replaceStr){
				return "forward:".concat(getRealUrlPath(urlPath, replaceStr));
			}
			
			/**
			* 解析含有占位符的指定URL路径模板
			*@param urlPath 含有占位符的指定URL路径
			*@return URL路径模板
			*@since 1.2.0
			*@author Zhanghongdong
			*/
			public static String getUrlPathTemplate(String urlPath){
				return urlPath.replaceAll(REGEX_PLACE_HOLDER, "%s");
			}

			private final static String REGEX_PLACE_HOLDER = "\\{[^}]*\\}";
}

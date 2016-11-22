package cn.signit.cons.rest;

import javax.servlet.http.HttpServletRequest;

import cn.signit.conf.ConfigProps;

/**
* Rest页面数据（html）资源的URI路径常量类
* @ClassName RestPagePath
* @author ZhangHongdong
* @date 2016年10月19日-下午3:25:03
* @version 0.0.2
*/
public final class RestPagePath extends RestPath{
//-----------------------------------------------------------------------------------------------------------
// 官网页面路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 主页请求页面路径
	*/
	public final static String HOME = "/index.html";
	/**
	* 主页逻辑页面路径
	*/
	public final static String HOME_LOC = PageLogicGroup.HOME.prefix+HOME;
	/**
	* APP下载请求页面路径
	*/
	public final static String APP_DOWNLOAD = "/app-download.html";
	/**
	* APP下载逻辑页面路径
	*/
	public final static String APP_DOWNLOAD_LOC =PageLogicGroup.HOME.prefix+APP_DOWNLOAD;
	/**
	* 注册声明条款请求页面路径
	*/
	public final static String STATE = "/state.html";
	/**
	* 注册声明条款请求页面路径
	*/
	public final static String STATE_LOC = PageLogicGroup.HOME.prefix+STATE;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 错误页面路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 403-禁止访问请求错误页面
	*/
	public final static String FORBIDDEN_403 = "/**/403.html";
	/**
	* 403-禁止访问逻辑错误页面
	*/
	public final static String FORBIDDEN_403_LOC = PageLogicGroup.ERROR.prefix+"/403.html";
	/**
	* 404-找不到资源请求错误页面
	*/
	public final static String NOT_FOUND_404 = "/**/404.html"; 
	/**
	* 404-找不到资源逻辑错误页面
	*/
	public final static String NOT_FOUND_404_LOC = PageLogicGroup.ERROR.prefix+"/404.html"; 
	/**
	* 500-服务器内部请求错误页面-服务器正在升级或维护
	*/
	public final static String INTERNAL_SERVER_ERROR_500 = "/**/500.html"; 
	/**
	* 500-服务器内部请求错误页面-服务器正在升级或维护
	*/
	public final static String INTERNAL_SERVER_ERROR_500_LOC =  PageLogicGroup.ERROR.prefix+"/500.html"; 
	/**
	* 503-服务器不可达请求错误页面-服务器正在升级或维护
	*/
	public final static String SERVICE_UNAVAILABLE_503 = "/**/503.html"; 
	/**
	* 503-服务器不可达逻辑错误页面-服务器正在升级或维护
	*/
	public final static String SERVICE_UNAVAILABLE_503_LOC = PageLogicGroup.ERROR.prefix+"/503.html"; 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 系统页面路径
//-----------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	/**
	 * 声明系统根路径
	 */
	public final static String SYS_WESIGN = "/wesign";
	public final static String SYS_WF = "/wf";
	public final static String SYS_WESIGN_OPEN = SYS_WESIGN+"/common/open";
	
	
	
	//------------------------------------------------------------------------------
	/**
	 * 登录请求页面
	 */
	public final static String LOGIN = SYS_WESIGN+"/login.html";
	/**
	 * 登录逻辑页面
	 */
	public final static String LOGIN_LOC = PageLogicGroup.SYSTEM.prefix+LOGIN;
	/**
	 * 注册请求页面
	 */
	public final static String REGISTER = SYS_WESIGN+"/register.html";
	/**
	 * 注册逻辑页面
	 */
	public final static String REGISTER_LOC = PageLogicGroup.SYSTEM.prefix+REGISTER;
	/**
	 * 找回密码请求页面
	 */
	public final static String FIND_PWD = SYS_WESIGN+"/find-password.html";
	/**
	 * 找回密码逻辑页面
	 */
	public final static String FIND_PWD_LOC = PageLogicGroup.SYSTEM.prefix+FIND_PWD;
	/**
	 * 重设密码请求页面
	 */
	public final static String RESET_PWD = SYS_WESIGN+"/reset-password.html";
	/**
	 * 重设密码逻辑页面
	 */
	public final static String RESET_PWD_LOC = PageLogicGroup.SYSTEM.prefix+RESET_PWD;
	/**
	 * 重设密码提示请求页面
	 */
	public final static String ACTIVATE_TIP = SYS_WESIGN+"/activate-tip.html";
	/**
	 * 重设密码提示逻辑页面
	 */
	public final static String ACTIVATE_TIP_LOC = PageLogicGroup.SYSTEM.prefix+ACTIVATE_TIP;
	/**
	 * 获取重新激活账号提示请求页面
	 */
	public final static String REACTIVATE_TIP = SYS_WESIGN+"/reactivate-tip.html";
	/**
	 * 获取重新激活账号提示逻辑页面
	 */
	public final static String REACTIVATE_TIP_LOC = PageLogicGroup.SYSTEM.prefix+REACTIVATE_TIP;
	/**
	 * 激活状态提示请求页面
	 */
	public final static String ACTIVATE_STATE_TIP = SYS_WESIGN+"/activate-state-tip.html";
	/**
	 * 激活状态提示逻辑页面
	 */
	public final static String ACTIVATE_STATE_TIP_LOC = PageLogicGroup.SYSTEM.prefix+ACTIVATE_STATE_TIP;
	/**
	 * 过渡状态提示请求页面
	 */
	public final static String TRANSIT_STATE_TIP = SYS_WESIGN+"/transit-state-tip.html";
	/**
	 * 过渡状态提示逻辑页面
	 */
	public final static String TRANSIT_STATE_TIP_LOC = PageLogicGroup.SYSTEM.prefix+TRANSIT_STATE_TIP;
	/**
	 * 登录前信封过渡状态提示请求页面
	 */
	public final static String ENVELOPE_OUT_TRANSIT_TIP = SYS_WESIGN+"/env-transit-tip.html";
	/**
	 * 登录前信封过渡状态提示逻辑页面
	 */
	public final static String ENVELOPE_OUT_TRANSIT_TIP_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_OUT_TRANSIT_TIP;
	
	
	
	//------------------------------------------------------------------------------
	private final static String USER_BASE = SYS_WESIGN+"/users";
	/**
	 * 用户个人中心请求页面
	 */
	public final static String USER_HOME = USER_BASE+"/index.html";
	/**
	 * 用户个人中心逻辑页面
	 */
	public final static String USER_HOME_LOC = PageLogicGroup.SYSTEM.prefix+USER_HOME;
	/**
	 * 用户个人信息请求页面
	 */
	public final static String USER_MY_INFO = USER_BASE+"/my-info.html";
	/**
	 * 用户个人信息逻辑页面
	 */
	public final static String USER_MY_INFO_LOC = PageLogicGroup.SYSTEM.prefix+USER_MY_INFO;
	/**
	 * 用户个人重设登录密码请求页面
	 */
	public final static String USER_INFO_RESET_PWD = USER_BASE+"/my-info/reset-password.html";
	/**
	 * 用户个人重设登录密码逻辑页面
	 */
	public final static String USER_INFO_RESET_PWD_LOC = PageLogicGroup.SYSTEM.prefix+USER_INFO_RESET_PWD;
	/**
	 * 用户个人绑定或重设手机请求页面
	 */
	public final static String USER_INFO_RESET_PHONE = USER_BASE+"/my-info/reset-phone.html";
	/**
	 * 用户个人绑定或重设手机逻辑页面
	 */
	public final static String USER_INFO_RESET_PHONE_LOC = PageLogicGroup.SYSTEM.prefix+USER_INFO_RESET_PHONE;
	/**
	 * 用户个人绑定或重设邮箱请求页面
	 */
	public final static String USER_INFO_RESET_EMAIL = USER_BASE+"/my-info/reset-email.html";
	/**
	 * 用户个人绑定或重设邮箱逻辑页面
	 */
	public final static String USER_INFO_RESET_EMAIL_LOC = PageLogicGroup.SYSTEM.prefix+USER_INFO_RESET_EMAIL;
	/**
	 * 用户个人实名认证请求页面
	 */
	public final static String USER_PERSON_AUTH = USER_BASE+"/auth/person-auth.html";
	/**
	 * 用户个人实名认证逻辑页面
	 */
	public final static String USER_PERSON_AUTH_LOC = PageLogicGroup.SYSTEM.prefix+USER_PERSON_AUTH;
	/**
	 * 用户个人采用支付宝自动实名认证回调结果请求页面
	 */
	public final static String USER_PERSON_AUTH_RESULT_BY_ALIPAY = USER_BASE+"/person-auth/auto-mode/alipaycertifi-result.html";
	/**
	 * 用户个人采用支付宝自动实名认证回调结果逻辑页面
	 */
	public final static String USER_PERSON_AUTH_RESULT_BY_ALIPAY_LOC = PageLogicGroup.SYSTEM.prefix+USER_BASE+"/auth/alipaycertifi-result.html";
	/**
	 * 用户企业实名认证请求页面
	 */
	public final static String USER_ENTERPRISE_AUTH = USER_BASE+"/auth/enterprise-auth.html";
	/**
	 * 用户企业实名认证逻辑页面
	 */
	public final static String USER_ENTERPRISE_AUTH_LOC = PageLogicGroup.SYSTEM.prefix+USER_ENTERPRISE_AUTH;
	/**
	 * 用户退出请求页面
	 */
	public final static String USER_LOGOUT = SYS_WESIGN+"/logout.html";
	/**
	 * 用户退出逻辑页面
	 */
	public final static String USER_LOGOUT_LOC = PageLogicGroup.SYSTEM.prefix+USER_LOGOUT;
	
	
	
	
	//------------------------------------------------------------------------------
	private final static String DOC_BASE = SYS_WESIGN+"/documents";
	/**
	 * 用户文档中心请求页面
	 */
	public final static String DOC_HOME = DOC_BASE+"/index.html";
	/**
	 * 用户文档中心逻辑页面
	 */
	public final static String DOC_HOME_LOC = PageLogicGroup.SYSTEM.prefix+DOC_HOME;
	/**
	 * 用户文档预览请求页面
	 */
	public final static String DOC_PREVIEW = DOC_BASE+"/preview.html";
	/**
	 * 用户文档预览逻辑页面
	 */
	public final static String DOC_PREVIEW_LOC = PageLogicGroup.SYSTEM.prefix+DOC_PREVIEW;
	/**
	 * 用户文档归档请求页面
	 */
	public final static String DOC_ARCHIVE = DOC_BASE+"/archive.html";
	/**
	 * 用户文档归档逻辑页面
	 */
	public final static String DOC_ARCHIVE_LOC = PageLogicGroup.SYSTEM.prefix+DOC_ARCHIVE;
	
	
	
	//------------------------------------------------------------------------------
	private final static String ENV_BASE = SYS_WESIGN+"/envelopes";
	/**
	 * 自己签信封请求页面
	 */
	public final static String ENVELOPE_ME = ENV_BASE+"/me.html";
	/**
	 * 自己签信封逻辑页面
	 */
	public final static String ENVELOPE_ME_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_ME;
	/**
	 * 我与他人签信封请求页面
	 */
	public final static String ENVELOPE_ME_AND_OTHERS = ENV_BASE+"/me-and-others.html";
	/**
	 * 我与他人签信封逻辑页面
	 */
	public final static String ENVELOPE_ME_AND_OTHERS_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_ME_AND_OTHERS;
	/**
	 * 他人签信封请求页面
	 */
	public final static String ENVELOPE_OTHERS = ENV_BASE+"/others.html";
	/**
	 * 他人签信封逻辑页面
	 */
	public final static String ENVELOPE_OTHERS_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_OTHERS;
	/**
	 * 信封签名操作请求页面
	 */
	public final static String ENVELOPE_SIGNATURE = ENV_BASE+"/signature.html";
	/**
	 * 信封签名操作逻辑页面
	 */
	public final static String ENVELOPE_SIGNATURE_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_SIGNATURE;
	/**
	 * 信封自己签操作请求页面
	 */
	public final static String ENVELOPE_ME_SIGNATURE = ENV_BASE+"/me-signature.html";
	/**
	 * 信封自己签操作逻辑页面
	 */
	public final static String ENVELOPE_ME_SIGNATURE_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_ME_SIGNATURE;
	/**
	 * 信封分配表单请求页面
	 */
	public final static String ENVELOPE_FORMS_ASSIGN = ENV_BASE+"/forms-assign.html";
	/**
	 * 信封分配表单逻辑页面
	 */
	public final static String ENVELOPE_FORMS_ASSIGN_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_FORMS_ASSIGN;
	/**
	 * 信封文档预览请求页面
	 */
	public final static String ENVELOPE_PREVIEW = ENV_BASE+"/preview.html";
	/**
	 * 信封文档预览逻辑页面
	 */
	public final static String ENVELOPE_PREVIEW_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_PREVIEW;
	/**
	 * 登录后信封相关过渡请求页面
	 */
	public final static String ENVELOPE_IN_TRANSIT_TIP = ENV_BASE+"/transit-tip.html";
	/**
	 * 登录后信封相关过渡逻辑页面
	 */
	public final static String ENVELOPE_IN_TRANSIT_TIP_LOC = PageLogicGroup.SYSTEM.prefix+ENVELOPE_IN_TRANSIT_TIP;
	
	
	
	//------------------------------------------------------------------------------
	private final static String SEAL_BASE = SYS_WESIGN+"/seals";
	/**
	 * 登录后信封相关过渡请求页面
	 */
	public final static String SEAL_HOME = SEAL_BASE+"/index.html";
	/**
	 * 登录后信封相关过渡逻辑页面
	 */
	public final static String SEAL_HOME_LOC = PageLogicGroup.SYSTEM.prefix+SEAL_HOME;
	
	/**
	 * 手机等移动终端签名面板的请求页面
	 */
	public final static String PHONE_SIGN_PAD = SYS_WESIGN_OPEN+"/sign-pads/phone";
	/**
	 * 手机等移动终端签名面板的逻辑页面
	 */
	public final static String PHONE_SIGN_PAD_LOC = PageLogicGroup.SYSTEM.prefix+SEAL_BASE+"/sign-pad-board.html";
	/**
	 * 页面签名面板请求页面
	 */
	public final static String WEB_SIGN_PAD = SYS_WESIGN_OPEN+"/sign-pads/web";
	/**
	 * 页面签名面板逻辑页面
	 */
	public final static String WEB_SIGN_PAD_LOC = PageLogicGroup.SYSTEM.prefix+SEAL_BASE+"/sign-pad.html";
	/**
	 * 访问二维码的链接地址请求页面
	 */
	public final static String WEB_SIGN_PAD_QRCODE_ACCESS = WEB_SIGN_PAD+"/qrcode/{sign-pad-room}";
	
	
	//------------------------------------------------------------------------------
	private final static String CERT_BASE = SYS_WESIGN+"/certificates";
	/**
	 * 用户证书中心请求页面
	 */
	public final static String CERT_HOME =  CERT_BASE+"/index.html";
	/**
	 * 用户证书中心逻辑页面
	 */
	public final static String CERT_HOME_LOC = PageLogicGroup.SYSTEM.prefix+CERT_HOME;
	/**
	 * 用户证书详情请求页面
	 */
	public final static String CERT_DETAILS =  CERT_BASE+"/details.html";
	/**
	 * 用户证书详情逻辑页面
	 */
	public final static String CERT_DETAILS_LOC = PageLogicGroup.SYSTEM.prefix+CERT_DETAILS;
	/**
	 * 用户证书更新请求页面
	 */
	public final static String CERT_RENEWAL =  CERT_BASE+"/renewal.html";
	/**
	 * 用户证书更新逻辑页面
	 */
	public final static String CERT_RENEWAL_LOC = PageLogicGroup.SYSTEM.prefix+CERT_RENEWAL;
	
	
	//------------------------------------------------------------------------------
	private final static String PAY_BASE = SYS_WESIGN+"/payments";
	/**
	 * 用户资费中心请求页面
	 */
	public final static String PAY_HOME =  PAY_BASE+"/index.html";
	/**
	 * 用户资费中心逻辑页面
	 */
	public final static String PAY_HOME_LOC = PageLogicGroup.SYSTEM.prefix+PAY_HOME;
	/**
	 * 用户可用套餐请求页面
	 */
	public final static String PAY_COMBO =  PAY_BASE+"/combo.html";
	/**
	 * 用户可用套餐逻辑页面
	 */
	public final static String PAY_COMBO_LOC = PageLogicGroup.SYSTEM.prefix+PAY_COMBO;
	/**
	 * 用户购买套餐请求页面
	 */
	public final static String PAY_BUY_COMBO =  PAY_BASE+"/pay-combo.html";
	/**
	 * 用户购买套餐逻辑页面
	 */
	public final static String PAY_BUY_COMBO_LOC = PageLogicGroup.SYSTEM.prefix+PAY_BUY_COMBO;
	/**
	 * 用户支付结果请求页面
	 */
	public final static String PAY_BUY_RESULT = PAY_BASE+"/pay-result.html";
	/**
	 * 用户支付结果逻辑页面
	 */
	public final static String PAY_BUY_RESULT_LOC = PageLogicGroup.SYSTEM.prefix+PAY_BUY_RESULT;
	/**
	 * 用户检查支付结果请求页面
	 */
	public final static String PAY_COMBO_BUY_RESULT_CHECK =  PAY_BASE+"/check";
	/**
	 * 用户检查支付结果逻辑页面
	 */
	public final static String PAY_COMBO_BUY_RESULT_CHECK_LOC = PAY_BUY_RESULT_LOC;
	/**
	 * 消费账单请求页面
	 */
	public final static String PAY_BILL_RECORDS = PAY_BASE+"/bill-records.html";
	/**
	 * 消费账单逻辑页面
	 */
	public final static String PAY_BILL_RECORDS_LOC = PageLogicGroup.SYSTEM.prefix+PAY_BILL_RECORDS;
	/**
	 * 购买记录请求页面
	 */
	public final static String PAY_PURCHASE_RECORDS = PAY_BASE+"/purchase-records.html";
	/**
	 * 购买记录逻辑页面
	 */
	public final static String PAY_PURCHASE_RECORDS_LOC = PageLogicGroup.SYSTEM.prefix+PAY_PURCHASE_RECORDS;
	/**
	 * 索取发票请求页面
	 */
	public final static String PAY_INVOICE_CLAIM = PAY_BASE+"/invoice-claim.html";
	/**
	 * 索取发票逻辑页面
	 */
	public final static String PAY_INVOICE_CLAIM_LOC = PageLogicGroup.SYSTEM.prefix+PAY_INVOICE_CLAIM;
	/**
	 * 支付结果回调请求页面
	 */
	public final static String PAY_BUY_RESULT_RETURN_URL = SYS_WESIGN_OPEN+"/payments/callback";
	
	
	
	
	//------------------------------------------------------------------------------
	/**
	* 公共需要检查或验证的资源调度分发处理器
	*/
	public final static String COMMON_OPEN_CHECK_ITEM = SYS_WESIGN_OPEN+"/check-items/{item}";
	
	private final static String COMMON_PERSON_AUTH_BY_ALIPAY_BASE = SYS_WESIGN_OPEN+"/person-auth/auto-mode/alipay";
	private final static String COMMON_ENTERPRISE_AUTH_BASE = SYS_WESIGN_OPEN+"/enterprise-auth";
	
	/**
	* 用户实名认证-支付宝快捷登录
	*/
	public final static String COMMON_PERSION_AUTH_ALIPAY_FAST_LOGIN=COMMON_PERSON_AUTH_BY_ALIPAY_BASE+"/fast";
	/**
	* 用户实名认证-支付宝OAUTH2.0授权，即快捷登录数据回调地址
	*/
	public final static String COMMON_PERSION_AUTH_ALIPAY_OAUTH=COMMON_PERSON_AUTH_BY_ALIPAY_BASE+"/oauth";
	/**
	* 用户实名认证-支付宝OAUTH2.0授权,数据回调地址
	*/
	public final static String COMMON_PERSION_AUTH_ALIPAY_OAUTH_RESULT=COMMON_PERSION_AUTH_ALIPAY_OAUTH+"/result";
	/**
	* 企业实名认证企业委托书模板地址
	*/
	public final static String COMMON_ENTERPRISE_AUTH_ATTORNEY_LETTER=COMMON_ENTERPRISE_AUTH_BASE+"/templates/attorney-letter";
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 公共页面路径
//-----------------------------------------------------------------------------------------------------------
	/**
	 * 声明公共根路径
	 */
	public final static String COMMON = "/common";	
	
	

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//	华丽的分割线
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static enum PageLogicGroup{
		HOME("home/pages"),
		ERROR("error/pages"),
		COMMON("common/pages"),
		SYSTEM("system/pages"),
		;
		private static String LOGIC_RES_ROOT_PATH = null;
		static{
			try {
				
				LOGIC_RES_ROOT_PATH = ConfigProps.get("page.template.thymeleaf.prefix");
			} catch (Exception e) {
				System.out.println("WARN:load Props error![ "+e.getMessage()+" ], use default");
			}
			if(LOGIC_RES_ROOT_PATH == null){
				LOGIC_RES_ROOT_PATH = "/WEB-INF/classes/static/";
			}
		}
		private String prefix;
		PageLogicGroup(String prefix){
			this.prefix = prefix;
		}
		
		public String prefix(){
			return this.prefix;
		}
		
		public String absolutePrefix(){
			return LOGIC_RES_ROOT_PATH+this.prefix;
		}
		
	}
	
	
	public static String convertServerPath(String requestUri){
		if(requestUri == null) return null;
		
		//system页面的请求
		/*if(requestUri.startsWith(ROOT_CORE) || requestUri.startsWith(ROOT_WF)){
			return "system".concat("/pages").concat(requestUri);
		}*/
		
		//home页面的请求
		
		//error页面的请求
		
		return null;
	}
	
	public static String convertServerPath(HttpServletRequest request){
		if(request == null) 
			return null;
		return convertServerPath(request.getRequestURI());
	}
	
}

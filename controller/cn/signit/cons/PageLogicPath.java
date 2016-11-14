package cn.signit.cons;


/**
*服务器页面资源逻辑地址常量类
* @ClassName PageLogicPath
* @author ZhangHongdong
* @date 2016年4月12日-下午1:05:54
* @version 1.2.0
*/
public  enum PageLogicPath {
	//==============页面根路径================
	HOME_NAME("home"),
	SYSTEM_NAME("system"),
	ERROR_NAME("error"),

	ROOT_HOME(HOME_NAME.path()+"/pages"),
	ROOT_SYSTEM(SYSTEM_NAME.path()+"/pages"),
	ROOT_ERROR(ERROR_NAME.path()+"/pages"),
	
	//==============错误页面路径================
	ERROR_403(ROOT_ERROR.path()+"/403.html"),
	ERROR_404(ROOT_ERROR.path()+"/404.html"),
	ERROR_500(ROOT_ERROR.path()+"/500.html"),
	ERROR_503(ROOT_ERROR.path()+"/503.html"),
	
	//==============官网主页页面路径================
	HOME(ROOT_HOME.path()+"/index.html"),
	APP_DOWNLOAD(ROOT_HOME.path()+"/app-download.html"),
	STATE(ROOT_HOME.path()+"/state.html"),
	
	//==============用户相关页面路径================
	//登录前
	LOGIN(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/login.html"),
	REGISTER(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/register.html"),
	FIND_PWD(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/find-password.html"),
	RESET_PWD(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/reset-password.html"),
	ACTIVATE_TIP(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/activate-tip.html"),
	ACTIVATE_STATE_TIP(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/activate-state-tip.html"),
	TRANSIT_STATE_TIP(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/transit-state-tip.html"),
	PHONE_SIGN_PAD(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/seals/sign-pad-board.html"),
	WEB_SIGN_PAD(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/seals/sign-pad.html"),
	//登录后
	USER_HOME(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/index.html"),
	USER_MY_INFO(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/my-info.html"),
	USER_INFO_RESET_PWD(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/my-info/reset-password.html"),
	USER_INFO_RESET_PHONE(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/my-info/reset-phone.html"),
	USER_INFO_RESET_EMAIL(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/my-info/reset-email.html"),
	USER_PERSON_AUTH(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/auth/person-auth.html"),
	USER_PERSON_AUTH_RESULT_BY_ALIPAY(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/auth/alipaycertifi-result.html"),
	USER_ENTERPRISE_AUTH(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/users/auth/enterprise-auth.html"),
	
	
	//==============用户文档页面路径================
	DOC_HOME(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/index.html"),
	DOC_PREVIEW(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/preview.html"),
	DOC_ARCHIVE(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/archive.html"),
	DOC_SIGN(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/signature.html"),
	DOC_SIGN_SELF_SIGN(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/sign-self-signature.html"),
	DOC_SIGN_SELF(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/sign-self.html"),
	DOC_WORKFLOW(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/sign-workflow.html"),
	DOC_SIGN_OTHERS(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/sign-others.html"),
	DOC_SIGN_FIELDS_ASSIGN(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/sign-fields-assign.html"),
	DOC_TRANSIT_STATE_TIP(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/transit-state-tip.html"),
	DOC_VERIFY(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/documents/verification.html"),
	
	
	//==============用户签章页面路径================
	SEAL_HOME(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/seals/index.html"),

	
	//==============用户证书页面路径================
	CERT_HOME(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/certificates/index.html"),
	CERT_DETAILS(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/certificates/details.html"),
	CERT_RENEWAL(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/certificates/renewal.html"),

	
	//==============用户资费页面路径================
	PAY_HOME(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/index.html"),
	PAY_COMBO(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/combo.html"),
	PAY_BUY_COMBO(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/pay-combo.html"),
	PAY_BUY_RESULT(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/pay-result.html"),
	PAY_BILL_RECORDS(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/bill-records.html"),
	PAY_PURCHASE_RECORDS(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/purchase-records.html"),
	PAY_INVOICE_CLAIM(ROOT_SYSTEM.path()+UrlPath.SYSTEM_ROOT+"/payments/invoice-claim.html"),
	;
	
	
	
	
	//private static String  PREFIX_PATH=PropsFactory.create(ProfileProps.class, true).get("page.template.thymeleaf.prefix").toString();
	private String path;
	
	PageLogicPath(String path){
		this.path = path;
	}
		
	public  String path(){
		return this.path;
	}
	
	public  String redirectPath(){
		return "redirect:"+this.path;
	}
	
	public String redirectFromSystemPath(){
		return "redirect:"+this.path.replaceFirst(ROOT_SYSTEM.path(), "");
	}
	
	public String redirectFromHomePath(){
		return "redirect:"+this.path.replaceFirst(ROOT_HOME.path(), "");
	}
	
	public String redirectFromErrorPath(){
		return "redirect:"+this.path.replaceFirst(ROOT_ERROR.path(), "");
	}
	
	public  String forwardPath(){
		return "forward:"+this.path;
	}
	
	public  String forwardFromSystemPath(){
		return "forward:"+this.path.replaceFirst(ROOT_SYSTEM.path(), "");
	}
	
	public  String forwardFromHomePath(){
		return "forward:"+this.path.replaceFirst(ROOT_HOME.path(), "");
	}
	
	public  String forwardFromErrorPath(){
		return "forward:"+this.path.replaceFirst(ROOT_ERROR.path(), "");
	}

}
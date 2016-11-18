package cn.signit.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.signit.cons.PageLogicPath;
import cn.signit.cons.UrlPath;
import cn.signit.domain.mysql.User;
import cn.signit.untils.UnicodeUtil;
import cn.signit.untils.message.SessionKeys;


/**
*用户页面获取相关控制
* @ClassName UserPageController
* @author Liwen
* @date 2016年11月11日-下午2:41:44
* @version (版本号)
* @see (参阅)
*/
@Controller
@SessionAttributes(SessionKeys.LOGIN_USER)
public class UserPageController implements InUserPageController{
	
	
	@RequestMapping(value=UrlPath.HOME_ROOT,method=RequestMethod.GET)
	public String rootPage(@RequestParam(required = false) String error,@RequestParam(required = false) String logout,Model model){
		//初始化绑定登录表单数据模型
		model.addAttribute(new LoginForms());
		//是否启用验证码
		model.addAttribute(SessionKeys.ENABLE_CAPTCHA,false);
		
		return PageLogicPath.LOGIN.path();
	}

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_HOME,method={RequestMethod.GET,RequestMethod.POST})
	@Override
	public String getHomePage(@ModelAttribute(SessionKeys.LOGIN_USER) User user, Model model) {
		model.addAttribute("userInfo", user);
		return PageLogicPath.USER_HOME.path();
	}

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_LOGIN,method=RequestMethod.GET)
	@Override
	public String getLoginPage(@RequestParam(required = false) String error,@RequestParam(required = false) String logout,Model model) {
		//初始化绑定登录表单数据模型
		model.addAttribute(new LoginForms());
		//是否启用验证码
		model.addAttribute(SessionKeys.ENABLE_CAPTCHA,false);
		
		return PageLogicPath.LOGIN.path();
	}
	
	/**
	 * 用户注册页面
	 * @param error
	 * @param model
	 * @return
	 */
	@RequestMapping(value=UrlPath.PAGE_USER_REGISTER,method=RequestMethod.GET)
	@Override
	public String getRegistPage(@RequestParam(required = false) String error, Model model) {
		model.addAttribute(new RegisterForms());
		return PageLogicPath.REGISTER.path();
	}
	
	@RequestMapping(value=UrlPath.PAGE_USER_DOC_SIGN_SELF, method=RequestMethod.GET)
	@Override
	public String getUploadPage(@RequestParam(required = false) String error, Model model) {
		return PageLogicPath.DOC_SIGN_SELF.path();
	}
	

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_MY_INFO,method=RequestMethod.GET)
	@Override
	public String getMyInfoPage(@ModelAttribute(SessionKeys.LOGIN_USER) User user, Model model) {
		model.addAttribute("userInfo", user);
		return PageLogicPath.USER_MY_INFO.path();
	}

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_DOC_HOME,method=RequestMethod.GET)
	@Override
	public String getDocumentPage(@ModelAttribute(SessionKeys.LOGIN_USER) User user, Model model) {
		model.addAttribute("userInfo", user);
		return PageLogicPath.DOC_HOME.path();
	}

	/**
	* 登录表单数据
	* @ClassName LoginForms
	* @author ZhangHongdong
	* @date 2016年4月27日-上午10:43:31
	* @version 1.2.0
	*/
	public static class LoginForms{
		private String username;
		private String password;
		private Boolean rememberMe;
		private String captcha;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Boolean getRememberMe() {
			return rememberMe;
		}
		public void setRememberMe(Boolean rememberMe) {
			this.rememberMe = rememberMe;
		}
		public String getCaptcha() {
			return captcha;
		}
		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}
	}
	
	/**
	* 注册表单数据
	* @ClassName LoginForms
	* @author ZhangHongdong
	* @date 2016年4月27日-上午10:43:31
	* @version 1.2.0
	*/
	public static class RegisterForms{
		protected String username;  // Email or Phone number
		protected String realName;
		protected String password;
		protected Long type;
		protected String captcha;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getRealName() {
			return realName;
		}
		public void setRealName(String realName) {
			this.realName = UnicodeUtil.unicodeToString(realName);
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getCaptcha() {
			return captcha;
		}
		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}
		public Long getType() {
			if(type == null && username != null){
			/*	if(username.indexOf("@") > 0){//邮箱注册
					type = CommonType.RegistType.EMAIL.getId();
				}else if(RegexType.find(RegexType.ANY_NUMBER, username)){//手机注册
					type = CommonType.RegistType.PHONE.getId();
				}else{//用户名注册
					type = CommonType.RegistType.USERNAME.getId();
				}*/
			}
			return type;
		}
		public void setType(Long type) {
			this.type = type;
		}
		public boolean requiredHaveNull(){
			return username == null || password == null || getType() == null;
		}
	}
	
}

package cn.signit.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.signit.cons.desc.IdentifyStateDesc;
import cn.signit.cons.desc.UserLevelType;
import cn.signit.cons.rest.RestPagePath;
import cn.signit.cons.PageLogicPath;
import cn.signit.cons.UrlPath;
import cn.signit.controller.beans.UserInfoShow;
import cn.signit.domain.mysql.User;

import cn.signit.service.db.UserService;
import cn.signit.untils.UnicodeUtil;
import cn.signit.untils.message.SessionKeys;
import jodd.madvoc.RootPackages;


/**
*用户页面获取相关控制
* @ClassName UserPageController
* @author Liwen
* @date 2016年11月11日-下午2:41:44
* @version (版本号)
* @see (参阅)
*/
@Controller
@SessionAttributes({SessionKeys.LOGIN_USER})
public class UserPageController implements InUserPageController{
	@Resource
	private UserService userService;
	
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
	public String getHomePage(@ModelAttribute(SessionKeys.LOGIN_USER)User user, Model model) {
		User nowUser=userService.getUser(user.getId());
		model.addAttribute("userInfo", getDisplayMyInfo(nowUser));
		model.addAttribute(SessionKeys.LOGIN_USER, nowUser);
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
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_MY_INFO,method=RequestMethod.GET)
	@Override
	public String getMyInfoPage(@ModelAttribute(SessionKeys.LOGIN_USER)User user, Model model) {
		User nowUser=userService.getUser(user.getId());
		model.addAttribute("userInfo", getDisplayMyInfo(nowUser));
		model.addAttribute(SessionKeys.LOGIN_USER, nowUser);
		//初始化用户信息
		return PageLogicPath.USER_MY_INFO.path();
	}

	/**
	* 获取用户注册页面
	*@param model 需要动态填充的数据模型
	*@return 用户注册页面逻辑地址
	*@since 0.0.2
	*@author Zhanghongdong
	*/
	@RequestMapping(value=RestPagePath.REGISTER,method=RequestMethod.GET)
	@Override
	public String getRegisterPage(Model model) {
		//初始化绑定注册表单数据模型
		model.addAttribute(new RegisterForms());
		
		return RestPagePath.REGISTER_LOC;
	}
	
	
	/**
	 * 个人信息中心数据封装
	 */
	private UserInfoShow getDisplayMyInfo(User user){
		UserInfoShow show=new UserInfoShow();
		show.setAccount(availableAccount(user));
		show.setAccountType("普通账户");
		show.setEmail(user.getEmail());
		show.setPhone(user.getPhone());
		show.setRealName(user.getRealName());
		show.setEmailIdentify(show.getEmailIdentify());
		show.setPhoneIdentify(show.getPhoneIdentify());
		show.setIdentifyLevel(UserLevelType.getDescription(user.getLevel()));
		return identifyInfoHandle(show,user);
	}
	
	/**
	 * 处理身份认证信息
	 * @param info
	 * @param user
	 * @return UserInfoShow 处理后的信息
	 */
	private UserInfoShow identifyInfoHandle(UserInfoShow info,User user){
		
		int state=IdentifyStateDesc.UNTREATED.getState();//初始化状态为未认证
		info.setIdentify(IdentifyStateDesc.getDescription(state));
		return info;
	}
	
	/**
	 * 根据企业认证信息初始化账户的相关信息
	 */
	/*private UserInfoShow initByEnterpriseProveInfo(UserInfoShow info,ProveInfo ep){
		info.setRealName(ep.getName());//展示的信息，账户真实姓名为认证的企业名称
		if(ep.getPhone()!=null){//根据06.16晚讨论的结果，如果账户已经绑定的手机登录账号，那么判断为已认证。如果账户的认证资料中包含手机号，那么也认为是已认证。
			info.setPhoneIdentify(IdentifyStateDesc.PASS.getDescription());
		}
		info.setIdentify(IdentifyStateDesc.getDescription(ep.getState()));
		return info;
	}*/
	/**
	 * 获取用户账户名
	 * @param user
	 * @return 账户
	 */
	public String availableAccount(User user){
		if(user.getEmail() != null && !user.getEmail().trim().equals("")){
			return user.getEmail();
		}
		if(user.getPhone() != null && !user.getPhone().trim().equals("")){
			return user.getPhone();
		}
		return "unknown";
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
		protected String username;
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

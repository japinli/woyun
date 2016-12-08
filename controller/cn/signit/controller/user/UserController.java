/**
 * @author:文
 * @date:2015年7月21日-上午9:36:58
 * @see: (参阅)
 */
package cn.signit.controller.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.signit.conf.ConfigProps;
import cn.signit.cons.GenerateLinkUtils;
import cn.signit.cons.OriginalSerialCodeMaker;
import cn.signit.cons.PageLogicPath;
import cn.signit.cons.RegexType;
import cn.signit.cons.UrlPath;
import cn.signit.cons.desc.VerifyResult;
import cn.signit.cons.rest.RestPagePath;

import cn.signit.controller.user.UserPageController.LoginForms;

import cn.signit.domain.mysql.User;
import cn.signit.service.EmailService;
import cn.signit.service.db.RepoService;
import cn.signit.service.db.UserLastLoginService;
import cn.signit.service.db.UserService;
import cn.signit.tools.utils.MD5Utils;
import cn.signit.untils.UnicodeUtil;
import cn.signit.untils.http.HttpUtil;
import cn.signit.untils.message.SessionKeys;
import cn.signit.untils.message.SessionResults;
import cn.signit.utils.repo.RepoPath;
/**
 *平台入口，登陆
 * @ClassName: MgrUserController
 * @author: 文   
 * @date:2015年7月21日-上午9:36:58 
 * @version:(版本号)
 * @see: (参阅)
 */
@Controller
public class UserController {
	public final static Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userService;
	@Resource
	private EmailService emailService;
	@Resource
	private UserLastLoginService lastLoginService;
	@Resource
	private RepoService repoService;
	
	protected final static String LOGIN_JSP= "/" + ConfigProps.get("welcome.page_path");
	protected final static String LOGIN_PARAMTER = "username";
	/* 重定向 */
	protected final static String REDIRECT_LOGIN_URI = "redirect:"+LOGIN_JSP;
	protected final static String REDIRECT_LOGIN_PASS_URI = "redirect:/html/infoUp.html";
	protected final static String REDIRECT_LOGIN_NO_PASS_URI = LOGIN_JSP;
	
	protected final static String FORWARD_TO_ACTIVATE_URI = "";
	/* 转发 */
	protected final static String FORWARD_LOGIN_URI = LOGIN_JSP;
	protected final static String EMAIL_VERIFY_SUCCESS = "redirect:/html/emailCheck.html";
	
	/**
	* 提交用户登录表单
	*@param model 需要动态填充的数据模型
	*@return 提交用户登录表单处理后的forward或redirect的控制器地址
	*@since 1.2.0
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_LOGIN,method=RequestMethod.POST)
	public String submitLoginForm(@ModelAttribute LoginForms forms,Model model,HttpServletRequest request) {
		String username=forms.getUsername();
		User user=userService.getUser(username);
		if(user==null){
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.USER_NOT_EXIST_ERROR);
			return PageLogicPath.LOGIN.path();
		}
		String password=MD5Utils.toMD5(forms.getPassword());
		if (password.equals(user.getPassword())) {
			//设置每个Session的最大有效时间（单位：秒）
			request.getSession().setAttribute(SessionKeys.LOGIN_USER, user);
			lastLoginService.updateLastLoginTime(user.getId(), username);
			LOG.info("用户: {} 登录", forms.getUsername());
			return "redirect:"+UrlPath.PAGE_USER_HOME;
		} else {
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.USER_NAME_OR_PWD_ERROR);
		}
		
		return PageLogicPath.LOGIN.path();
	}
	
	/**
	 * 用户退出登录
	 */
	@RequestMapping(value=RestPagePath.USER_LOGOUT,method=RequestMethod.GET)
	public String userLogout(@ModelAttribute LoginForms forms, HttpServletRequest request) {
		request.getSession().removeAttribute(SessionKeys.LOGIN_USER);
		LOG.info("用户: {} 退出", forms.getUsername());
		return PageLogicPath.LOGIN.path();
	}
	
	/**
	 * 检测用户注册信息及用户是否已注册
	 * @param forms 用户注册表单
	 * @param model 需要动态填充的数据模型
	 * @return true - 用户注册信息完整，false - 用户注册信息不完整或已经注册
	 */
	private boolean registCheckError(RegisterForms forms, Model model) {
		// 必填项为空
		if (forms.requiredHaveNull()) {
			model.addAttribute(SessionKeys.RESULT_ERROR, SessionResults.INPUT_ERROR);
			model.addAttribute(forms);
			return false;
		}

		User chekcUser = userService.getUser(forms.getUsername());
		if (chekcUser != null) {
			model.addAttribute(SessionKeys.RESULT_ERROR, SessionResults.USER_EXISTED_ERROR);
			model.addAttribute(forms);
			return false;
		}
		
		return true;
	}
	
	/**
	* 提交用户注册表单
	*@param forms 提交注册数据表单
	*@param model 需要动态填充的数据模型
	*@return 提交用户注册表单处理后的forward或redirect的控制器地址
	*/
	@RequestMapping(value=RestPagePath.REGISTER, method=RequestMethod.POST)
	public String submitRegisterForm(@ModelAttribute RegisterForms forms,Model model,HttpServletRequest request) {
		boolean ok = registCheckError(forms, model);
		if(ok == false){
			return RestPagePath.REGISTER_LOC;
		}
		return registByEmail(forms, model,request);
	}
	
	/**
	 * 从注册页面获取用户注册信息
	 * @param forms 提交的用户注册表单
	 * @param request 注册请求
	 * @return 用户对象
	 */
	private User getUserFromRegisterForms(RegisterForms forms, HttpServletRequest request) {
		User user = new User();
		
		Date now = Calendar.getInstance().getTime();
		user.setRegistDate(now);
		user.setActivated(false);
		user.setEmail(forms.getUsername());
		user.setRealName(forms.getRealName());
		user.setPassword(MD5Utils.toMD5(forms.getPassword()));
		user.setIp(HttpUtil.getRealRemoteIpAddr(request));
		
		// RandomCode & OriginalSerialCode 用户验证用户注册信息
		user.setRandomCode(UUID.randomUUID().toString());
		user.setOriginalSerialCode(OriginalSerialCodeMaker.secretMake());
		
		return user;
	}
	
	/**
	 * 发送用户验证邮件
	 * @param request 用户注册请求
	 * @param user 用户信息
	 * @return true - 成功，false - 失败
	 */
	private boolean sendVerifyEmail(HttpServletRequest request, User user) {
		String path = UrlPath.USER_REGIEST_CHECK_ROOT + "/" + user.getId() + "/";
		String code = GenerateLinkUtils.generateCheckcode(user.getNormalOrigiSerialCode(), user.getRandomCode());
		String link = GenerateLinkUtils.generateActivateLink(request, path + code + "/activate");
		String subject_format = ConfigProps.get("mail.msg.activate_subject");
		String content_format = ConfigProps.get("mail.msg.activate_contents");
		String subject = String.format(subject_format, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		String message = String.format(content_format, user.availableUserName(), link, link);
		return emailService.send(subject,	user.getEmail(), message);
	}
	
	/**
	 * 利用邮件注册新用户
	 * @param forms 提交的用户注册表单
	 * @param model 需要动态填充的数据模型
	 * @param request
	 * @return
	 */
	private String registByEmail(RegisterForms forms, Model model, HttpServletRequest request){
		User user = getUserFromRegisterForms(forms, request);
		userService.addUserAndGetId(user);
		Long id = user.getId();
		if (id != null) {
			// 创建默认仓库
			repoService.createRepository(user, RepoPath.default_repo);
			boolean isSend = sendVerifyEmail(request, user);
			if (isSend) {
				LOG.info("注册邮件:({})发送成功!",user.getEmail());
				return RestPagePath.REDIRECT.concat(RestPagePath.ACTIVATE_TIP).concat("?").concat(SessionKeys.RESULT_TYPE).concat("=").concat("2");
			} else {
				LOG.info("注册邮件:({})发送失败!",user.getEmail());
				model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.PROCESS_FAILURE);
				model.addAttribute(forms);
				return RestPagePath.REGISTER_LOC;
			}
		} else {
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.PROCESS_FAILURE);
			model.addAttribute(forms);
			return RestPagePath.REGISTER_LOC;
		}
		
	}

	
	//重设密码错误检测
	@SuppressWarnings("unused")
	private String resetPwdCheckError(ResetPasswordForms forms,Model model){
		//必填项为空
		if(forms.requiredHaveNull()){
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.INPUT_ERROR);
			return RestPagePath.REDIRECT.concat(RestPagePath.FIND_PWD);
		}
			
		return null;
	}
	
	/**
	 * 用户邮件激活处理
	 */
	@RequestMapping(value =UrlPath.USER_REGIEST_ACTIVATE, method = RequestMethod.GET)
	public String activate(@PathVariable("id") String id,
			@PathVariable("checkcode") String checkCode,
			HttpServletRequest request,Model model){
		User user=userService.getUser(Long.parseLong(id));
		if(user.getActivated()){
			return RestPagePath.REDIRECT.concat(RestPagePath.LOGIN);
		}
		//增加稳定性，去掉超时检查
		/*int between=DateUtil.getHoursBetween(new Date(), user.getLastLoginDate());
		if(between>24){
			user.setActivateState(DataSet.USER_WAIT_AVTIVATED);
			//超时处理
			model.addObject(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
			LOG.info("用户:({})激活链接超时!",user.getEmail());
			model.addObject(SessionKeys.RESULT_DESC, "激活链接超时");
			model.setViewName(REDIRECT_LOGIN_URI);
			return model;
		}*/
		boolean isVerifyed=GenerateLinkUtils.verifyCheckcode(user.getNormalOrigiSerialCode(), user.getRandomCode(), checkCode);
		if(isVerifyed){
			user.setActivated(true);
			user.setRandomCode(UUID.randomUUID().toString());
		}else{
			//链接无效
			user.setActivated(false);
			user.setRandomCode(UUID.randomUUID().toString());
			model.addAttribute(SessionKeys.RESULT_ERROR, VerifyResult.FAILURE_URL_INVALID.getDescription());
			model.addAttribute(SessionKeys.RESULT_CODE, VerifyResult.FAILURE_URL_INVALID.getCode());
		}
		userService.updateUser(user);
		//填充页面动态模型
		model.addAttribute(SessionKeys.RESULT_ERROR, SessionResults.USER_REGIST_SUCCESS);
		model.addAttribute(SessionKeys.RESULT_CODE, VerifyResult.SUCCESS.getCode());
		model.addAttribute(SessionKeys.RESULT_TYPE, 2);
		return RestPagePath.ACTIVATE_STATE_TIP_LOC;
	}
	
	/**
	* 获取用户激活账号提示页面
	*@param type 注册类型
	*@param model 需要动态填充的数据模型
	*@return 用户激活账号提示页面逻辑地址
	*@since 0.0.2
	*@author Zhanghongdong
	*/
	@RequestMapping(value=RestPagePath.ACTIVATE_TIP,method=RequestMethod.GET)
	public String getActivateTipPage(Model model){
		model.addAttribute(SessionKeys.RESULT_TYPE, 2);
		return RestPagePath.ACTIVATE_TIP_LOC;
	}
	

	/**
	* 注册表单数据
	* @ClassName LoginForms
	* @author ZhangHongdong
	* @date 2016年4月27日-上午10:43:31
	* @version 0.0.2
	*/
	public static class RegisterForms{
		protected String username;
		protected String realName;
		protected String password;
		protected Long type;
		protected String captcha;
		protected Long from;
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
				if(username.indexOf("@") > 0){//邮箱注册
					type = 2L;
				}else if(RegexType.find(RegexType.ANY_NUMBER, username)){//手机注册
					type = 3L;
				}else{//用户名注册
					type = 1L;
				}
			}
			return type;
		}
		public void setType(Long type) {
			this.type = type;
		}
		public boolean requiredHaveNull(){
			return ObjectUtils.isEmpty(username) ||  ObjectUtils.isEmpty(password) || ObjectUtils.isEmpty(getType());
		}
		public Long getFrom() {
			return from;
		}
		public void setFrom(Long from) {
			this.from = from;
		}
	}
	
	/**
	* 重设密码表单数据
	* @ClassName ResetPasswordForms
	* @author ZhangHongdong
	* @date 2016年4月27日-上午10:43:31
	* @version 0.0.2
	*/
	public static class ResetPasswordForms{
		private String token;
		private String account;
		private String password;
	
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean requiredHaveNull(){
			return account == null || token == null || password == null;
		}
	}
}

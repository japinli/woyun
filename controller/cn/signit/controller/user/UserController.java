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
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.Hdfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
import cn.signit.service.db.UserService;
import cn.signit.service.files.HdfsService;
import cn.signit.tools.utils.MD5Utils;
import cn.signit.untils.UnicodeUtil;
import cn.signit.untils.http.HttpUtil;
import cn.signit.untils.message.SessionKeys;
import cn.signit.untils.message.SessionResults;
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
	private HdfsService hdfsService;

	
	protected final static String LOGIN_JSP="/"+ConfigProps.get("welcome.page_path");
	protected final static String LOGIN_PARAMTER="username";
	/* 重定向 */
	protected final static String REDIRECT_LOGIN_URI = "redirect:"+LOGIN_JSP;
	protected final static String REDIRECT_LOGIN_PASS_URI = "redirect:/html/infoUp.html";
	protected final static String REDIRECT_LOGIN_NO_PASS_URI = LOGIN_JSP;
	
	protected final static String FORWARD_TO_ACTIVATE_URI="";
	/* 转发 */
	protected final static String FORWARD_LOGIN_URI = LOGIN_JSP;
	protected final static String EMAIL_VERIFY_SUCCESS="redirect:/html/emailCheck.html";
	
	/**
	* 提交用户登录表单
	*@param model 需要动态填充的数据模型
	*@return 提交用户登录表单处理后的forward或redirect的控制器地址
	*@since 1.2.0
	*/
	@RequestMapping(value=UrlPath.PAGE_USER_LOGIN,method=RequestMethod.POST)
	public String submitLoginForm(@ModelAttribute LoginForms forms,Model model,HttpServletRequest request) {
		//交给Spring Security完成,当配置loginProcessingUrl("/j_spring_security_check")时，此项可用
		//return PageLogicPath.LOGIN.redirectFromSystemPath();
		String username=forms.getUsername();
		User user=userService.getUser(username);
		if(user==null){
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.USER_NOT_EXIST_ERROR);
			return PageLogicPath.LOGIN.path();
		}
		String password=MD5Utils.toMD5(forms.getPassword());
		if(password.equals(user.getPassword())){
			LOG.info("用户:"+forms.getUsername()+"登录成功!");
			//设置每个Session的最大有效时间（单位：秒）
			request.getSession().setAttribute(SessionKeys.LOGIN_USER, user);
			return "redirect:"+UrlPath.PAGE_USER_HOME;
		}else{
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
		return PageLogicPath.LOGIN.path();
	}
	
	/**
	* 提交用户注册表单
	*@param forms 提交注册数据表单
	*@param model 需要动态填充的数据模型
	*@return 提交用户注册表单处理后的forward或redirect的控制器地址
	*@since 0.0.2
	*@author Zhanghongdong
	*/
	@RequestMapping(value=RestPagePath.REGISTER,method=RequestMethod.POST)
	public String submitRegisterForm(@ModelAttribute RegisterForms forms,Model model,HttpServletRequest request) {
		String errorLink = registCheckError(forms, model);
		if(errorLink != null){
			return errorLink;
		}
		return registByEmail(forms, model,request);
		
	}
	
	private String registByEmail(RegisterForms registerForms,Model model,HttpServletRequest request){
		User user=new User();
		Date date = Calendar.getInstance().getTime();
		user.setEmail(registerForms.getUsername());
		user.setRegistDate(date);
		user.setIp(HttpUtil.getRealRemoteIpAddr(request));
		user.setRandomCode(UUID.randomUUID().toString());
		user.setOriginalSerialCode(OriginalSerialCodeMaker.secretMake());
		user.setActivated(false);
		user.setPassword(MD5Utils.toMD5(registerForms.getPassword()));
		user.setRealName(registerForms.getRealName());
		user.setRootDirName(registerForms.getUsername());  // 使用邮件名创建目录
		Long id = null;
		userService.addUserAndGetId(user);
		id=user.getId();
		if(id!=null){
			// 用户注册成功，创建目录
			String path = hdfsService.createDirectory(registerForms.getUsername());
			if (path == null) {
				LOG.info("创建用户目录失败！");
			} else {
				LOG.info("创建用户目录成功！{}", path);
				user.setRootDir(path);
				userService.updateUser(user);
			}
			System.out.println(System.getProperty("user.dir"));
			// 发送邮件验证
			String activateLink=GenerateLinkUtils.generateActivateLink(request, UrlPath.USER_REGIEST_CHECK_ROOT+"/"+id+"/"
					+GenerateLinkUtils.generateCheckcode(user.getNormalOrigiSerialCode(), user.getRandomCode())
					+"/activate");
			String subject=String.format(ConfigProps.get("mail.msg.activate_subject"), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			String nickname=ConfigProps.get("mail.msg.common_nickname");
			String message=String.format(ConfigProps.get("mail.msg.activate_contents"), user.availableUserName(),activateLink, activateLink);
			boolean isSend=emailService.send(subject, user.getEmail(), message);
			if(isSend){
				LOG.info("注册邮件:({})发送成功!",user.getEmail());
				return RestPagePath.REDIRECT.concat(RestPagePath.ACTIVATE_TIP).concat("?").concat(SessionKeys.RESULT_TYPE).concat("=").concat("2");
			}else{
				LOG.info("注册邮件:({})发送失败!",user.getEmail());
				model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.PROCESS_FAILURE);
				model.addAttribute(registerForms);
				return RestPagePath.REGISTER_LOC;
			}
		}else{
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.PROCESS_FAILURE);
			model.addAttribute(registerForms);
			return RestPagePath.REGISTER_LOC;
		}
		
	}
	
	//注册错误检测
	private String registCheckError(RegisterForms registerForms,Model model){
		//必填项为空
		if(registerForms.requiredHaveNull()){
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.INPUT_ERROR);
			model.addAttribute(registerForms);
			return RestPagePath.REGISTER_LOC;
		}
		//检查用户存在
		User chekcUser=userService.getUser(registerForms.getUsername());
		if(chekcUser != null){//如果该账户已经存在
			model.addAttribute(SessionKeys.RESULT_ERROR,SessionResults.USER_EXISTED_ERROR);
			model.addAttribute(registerForms);
			return RestPagePath.REGISTER_LOC;
		}
		return null;
	}
	
	//重设密码错误检测
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

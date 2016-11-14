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
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import cn.signit.conf.ConfigProps;
import cn.signit.cons.GenerateLinkUtils;
import cn.signit.cons.OriginalSerialCodeMaker;
import cn.signit.cons.PageLogicPath;
import cn.signit.cons.RegexType;
import cn.signit.cons.UrlPath;
import cn.signit.controller.user.UserPageController.LoginForms;
import cn.signit.domain.mysql.User;
import cn.signit.service.EmailService;
import cn.signit.service.UserService;
import cn.signit.untils.http.HttpUtil;
import cn.signit.untils.message.CommonResp;
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
	public String submitLoginForm(@ModelAttribute LoginForms forms, Model model, HttpServletRequest request) {
		//交给Spring Security完成,当配置loginProcessingUrl("/j_spring_security_check")时，此项可用
		//return PageLogicPath.LOGIN.redirectFromSystemPath();
		String username = forms.getUsername();
		User user = userService.getUser(username);
		if (user == null) {
			LOG.info("用户:" + forms.getUsername() + "用户不存在!");
			model.addAttribute(SessionKeys.RESULT_ERROR, SessionResults.USER_NOT_EXIST_ERROR);
			return PageLogicPath.LOGIN.path();
		}
		if (forms.getPassword().equals(user.getPassword())) {
			LOG.info("用户:" + forms.getUsername() + "登录成功!");
			// 设置用户 Session
			request.getSession().setAttribute(SessionKeys.LOGIN_USER, user);
			return "redirect:" + UrlPath.PAGE_USER_HOME;
		} else {
			LOG.info("用户:" + forms.getUsername() + "用户名或密码错误!");
			model.addAttribute(SessionKeys.RESULT_ERROR, SessionResults.USER_NAME_OR_PWD_ERROR);
		}
		
		return PageLogicPath.LOGIN.path();
	}
	
	/**
	 * 提交用户登录
	 *
	 * @see: (参阅)
	 * @since eSignServer v2.0
	 */

	@RequestMapping(value = UrlPath.HANDLE_LOGIN, method = RequestMethod.POST)
	public ModelAndView login(User user,
			HttpServletRequest request, HttpServletResponse response,
			ModelAndView modelAndView, Model model) {
		User temp=userService.getUser(user.availableUserName());
		if(temp==null){
			modelAndView.addObject(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
			modelAndView.addObject(SessionKeys.RESULT_DESC, SessionResults.USER_NOT_EXIST_ERROR);
			modelAndView.setViewName(UrlPath.PAGE_USER_MY_INFO);
			return modelAndView;
		}
		if(!temp.getActivated()){
			modelAndView.addObject(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
			modelAndView.addObject(SessionKeys.RESULT_DESC, SessionResults.USER_NOT_ACTIVATED_ERROR);
			modelAndView.setViewName(REDIRECT_LOGIN_URI);
			return modelAndView;
		}
		if(temp.getPassword().equals(user.getPassword())){
			/** 用户认证通过，加入用户到session **/
			model.addAttribute(SessionKeys.LOGIN_USER, temp);
			//设置每个Session的最大有效时间（单位：秒）
			request.getSession().setAttribute(SessionKeys.LOGIN_USER, temp);
			request.getSession().setMaxInactiveInterval(ConfigProps.getInt("server.session_max_alive_seconds"));
			LOG.info("用户:"+temp.availableUserName()+"登录成功!");
			modelAndView.setViewName(REDIRECT_LOGIN_PASS_URI);
		}else{
			modelAndView.addObject(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
			modelAndView.addObject(SessionKeys.RESULT_DESC, SessionResults.USER_NAME_OR_PWD_ERROR);
			
			modelAndView.setViewName(REDIRECT_LOGIN_URI);
		}
		
		return modelAndView;
		
	}
	
	/**
	 * 用户退出
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 *
	 * @see: (参阅)
	 * @since eSignServer v1.0
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			HttpServletRequest request,HttpServletResponse response, SessionStatus status,RedirectAttributesModelMap redirectModelMap) throws IllegalBlockSizeException, BadPaddingException {
		status.setComplete();
		request.getSession().removeAttribute(SessionKeys.LOGIN_USER);
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LOGIN_URI);
		if(status.isComplete() && request.getSession().getAttribute(SessionKeys.LOGIN_USER) == null){
			modelAndView.addObject(SessionKeys.RESULT_CODE, 0);
			modelAndView.addObject(SessionKeys.RESULT_DESC, "正常退出系统");
		}else{
			modelAndView.addObject(SessionKeys.RESULT_CODE, 1);
			modelAndView.addObject(SessionKeys.RESULT_DESC, "尚未退出系统");
		}
		modelAndView.addObject(SessionKeys.RESULT_CLEAR_MODEL, true);
		return modelAndView;
	}
	
	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	@ResponseBody()
	public CommonResp regist(User user,HttpServletRequest request,Model model){
		CommonResp res=new CommonResp();
		User temp=userService.getUser(user.availableUserName());
		if(temp!=null){//账户已经存在
			LOG.info("用户已经注册:"+user.availableUserName());
			res.basicFailureMsg("用户已经注册！");
			return res;
		}
		if(user!=null){
			Date date = Calendar.getInstance().getTime();
			user.setRegistDate(date);
			user.setIp(HttpUtil.getRealRemoteIpAddr(request));
			user.setRandomCode(UUID.randomUUID().toString());
			user.setOriginalSerialCode(OriginalSerialCodeMaker.secretMake());
			user.setActivated(false);;
			Long id = null;
			id=userService.addUserAndGetId(user);
			if(id!=null){
				String activateLink=GenerateLinkUtils.generateActivateLink(request, "/user/"+id+"/"
						+GenerateLinkUtils.generateCheckcode(user.getNormalOrigiSerialCode(), user.getRandomCode())
						+"/activate");
				String subject=String.format(ConfigProps.get("mail.msg.activate_subject"), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				String nickname=ConfigProps.get("mail.msg.common_nickname");
				String message=String.format(ConfigProps.get("mail.msg.activate_contents"), user.availableUserName(),activateLink, activateLink);
				boolean isSend=emailService.send(subject, user.getEmail(), message);
				if(isSend){
					LOG.info("注册邮件:({})发送成功!",user.getEmail());
					return res.basicSuccessMsg("发送成功!");
				}else{
					LOG.info("注册邮件:({})发送失败!",user.getEmail());
					return res.basicFailureMsg("激活失败!");
				}
				
			}
		}
		res.basicFailureMsg("关键信息不足!");
		return res;

	}
	/**
	 * 用户邮件激活处理
	 */
	@RequestMapping(value = "/{id:"+RegexType.ANY_NUMBER+"}/{checkcode:"+RegexType.MD5_NUMBER+"}/activate", method = RequestMethod.GET)
	public ModelAndView activate(@PathVariable("id") String id,
			@PathVariable("checkcode") String checkCode,
			HttpServletRequest request,ModelAndView model){
		User user=userService.getUser(Long.parseLong(id));
		if(user.getActivated()){
			model.addObject(SessionKeys.RESULT_DESC, "用户重复激活");
			model.setViewName(REDIRECT_LOGIN_URI);
			return model;
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
			user.setActivated(true);;
			user.setRandomCode(UUID.randomUUID().toString());
		}else{
			//链接无效
			user.setActivated(false);;
			user.setRandomCode(UUID.randomUUID().toString());
			model.addObject(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
			model.addObject(SessionKeys.RESULT_DESC, "激活链接无效");
			model.setViewName(REDIRECT_LOGIN_URI);
			return model;
		}
		userService.updateUser(user);
		return model;
	}
	
	/**
	 * 获取登录用户信息
	 * @see: (参阅)
	 */
	@RequestMapping(value = "/nowuser", method = RequestMethod.GET)
	@ResponseBody
	public CommonResp getLoinUserInfo(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			HttpServletRequest request,HttpServletResponse response){
		CommonResp resp=new CommonResp();
		user.setPassword("***");
		resp.basicSuccessMsg("获取成功")
		.setResultData(user);
		return resp;
	}
}

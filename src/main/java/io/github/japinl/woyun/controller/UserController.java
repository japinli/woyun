package io.github.japinl.woyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.DirsService;
import io.github.japinl.service.UserService;
import io.github.japinl.woyun.common.WoStatus;
import io.github.japinl.woyun.domain.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private DirsService dirsService;
	
	/*
	 * @brief 检测用户是否已注册
	 * @param name [in] 待检测的用户名
	 */
	@RequestMapping(value = "/checkuser")
	@ResponseBody
	public WoStatus checkUser(@RequestParam("name") String name) {
		boolean flag = userService.isUsernameRegister(name);
		return new WoStatus(flag ? 1 : 0);
	}
	
	/*
	 * @brief 检测用户邮箱是否已注册
	 * @param email [in] 待检测的用户邮箱地址
	 */
	@RequestMapping(value = "/checkemail")
	@ResponseBody
	public WoStatus checkEmail(@RequestParam("email") String email) {
		boolean flag = userService.isEamilRegister(email);
		return new WoStatus(flag ? 1 : 0);
	}
	
	/*
	 * @brief 检测电话号码是否已注册
	 * @param email [in] 待检测的电话号码
	 */
	@RequestMapping(value = "/checkphone")
	@ResponseBody
	public WoStatus checkPhone(@RequestParam("phone") String phone) {
		boolean flag = userService.isPhoneRegister(phone);
		return new WoStatus(flag ? 1 : 0);
	}
	
	/*
	 * @brief 用户登录
	 * @param user [in] 包含用户名及密码的User对象
	 * @return {"status": 0} 登录成功， {"status": 1} 登录失败
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus login(@RequestBody User user) {
		System.out.println(user.getName());
		boolean flag = userService.login(user.getName(), user.getPassword());
		return new WoStatus(flag ? 0 : 1);
	}
	
	/*
	 * @breif 用户注册并创建相应的用户目录
	 * @param user [in] 包含用户注册信息的User对象
	 * @return {"status": 0} 注册成功， {"status": 1} 注册失败 
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus register(@RequestBody User user) {
		WoStatus status = new WoStatus(0);
		boolean success = dirsService.createDirectory(user.getName());
		if (success) {
			success = userService.register(user);
			if (success) {
				return status;
			} else {
				dirsService.deleteDirectory(user.getName());
			}
		}
		
		status.setStatus(1);
		return status;
	}
}

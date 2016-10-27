package io.github.japinl.woyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.UserService;
import io.github.japinl.woyun.common.WoStatus;
import io.github.japinl.woyun.domain.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
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
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus login(@RequestBody User user) {
		System.out.println(user.getName());
		boolean flag = userService.login(user.getName(), user.getPassword());
		return new WoStatus(flag ? 0 : 1);
	}
}

package io.github.japinl.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.springmvc.dao.UserMapper;
import io.github.japinl.springmvc.model.User;

@Controller
public class LoginController {
	
	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class); 

	@Resource
	private UserMapper userMapper;
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST) 
	@ResponseBody
	public Map<String, Object> login(@RequestBody User user) {
		Map<String, Object> result = new HashMap<String, Object>();

		User temp = userMapper.login(user);
		if (temp == null) {
			result.put("status", 1);
		} else {
			result.put("status", 0);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/signup/checkuser", method = RequestMethod.GET, params = "username")
	@ResponseBody
	public Map<String, Object> checkUser(@RequestParam String username) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		User tmp = userMapper.isUsernameRegisted(username);
		
		if (tmp == null) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		
		LOG.info(result.toString());
		
		return result;
	}
	
	@RequestMapping(value = "/signup/checkemail", method = RequestMethod.GET, params = "email")
	@ResponseBody
	public Map<String, Object> checkEmail(@RequestParam String email) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		User tmp = userMapper.isEmailRegisted(email);
		
		if (tmp == null) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		
		LOG.info(result.toString());
		
		return result;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestBody User user) {
		Map<String, Object> result = new HashMap<String, Object>();

		int ret = userMapper.insertSelective(user);
		LOG.info("register: insertSelective: " + ret);
		if (ret != 0) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		
		return result;
	}
}

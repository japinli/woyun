package io.github.japinl.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.springmvc.dao.UserMapper;
import io.github.japinl.springmvc.model.User;

@Controller
public class LoginController {

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
	
	@RequestMapping(value = "/signup/checkuser", method = RequestMethod.GET, params = "name")
	@ResponseBody
	public Map<String, Object> checkuser(@RequestParam String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		System.out.println(name);
		result.put("status", 0);
		return result;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestBody User user) {
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("status", 0);
		return result;
	}
}

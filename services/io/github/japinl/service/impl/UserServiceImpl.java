package io.github.japinl.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import io.github.japinl.service.UserService;
import io.github.japinl.woyun.dao.UserMapper;
import io.github.japinl.woyun.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	public boolean isUsernameRegister(String name) {
		User user = userMapper.selectByName(name);
		if (user == null) {
			return false;
		}
		return true;
	}
	
	public boolean isEamilRegister(String email) {
		
		return true;
	}
	
	public boolean isPhoneRegister(String phone) {
		
		return false;
	}
}

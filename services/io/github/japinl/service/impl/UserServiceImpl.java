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
		return user == null ? false : true;
	}
	
	public boolean isEamilRegister(String email) {
		User user = userMapper.selectByEmail(email);
		return user == null ? false : true;
	}
	
	public boolean isPhoneRegister(String phone) {
		User user = userMapper.selectByPhone(phone);
		return user == null ? false : true;
	}
	
	public boolean login(String name, String password) {
		User user = userMapper.selectByNameAndPassword(name, password);
		return user == null ? false : true;
	}
}

package io.github.japinl.woyun.dao;

import io.github.japinl.woyun.model.User;

public interface UserMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);
	
	User login(User user);
	
	User isUsernameRegisted(String username);
	
	User isEmailRegisted(String email);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
}
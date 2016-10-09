package io.github.japinl.springmvc.dao;

import io.github.japinl.springmvc.model.User;

public interface UserMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);
	
	User login(User user);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
}
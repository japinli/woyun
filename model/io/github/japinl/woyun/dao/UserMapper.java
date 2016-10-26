package io.github.japinl.woyun.dao;

import org.apache.ibatis.annotations.Param;

import io.github.japinl.woyun.domain.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectByName(String name);
    
    User selectByEmail(String email);
    
    User selectByPhone(String phone);
    
    User selectByNameAndPassword(@Param("name") String name, @Param("password") String password);
}
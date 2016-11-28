package cn.signit.dao.mysql;

import cn.signit.domain.mysql.UserLastLogin;

public interface UserLastLoginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLastLogin record);

    int insertSelective(UserLastLogin record);

    UserLastLogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLastLogin record);

    int updateByPrimaryKey(UserLastLogin record);
    
    UserLastLogin selectByUserId(Long uid);
}
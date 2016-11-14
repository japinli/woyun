package cn.signit.dao.mysql;

import cn.signit.domain.mysql.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    // 根据用户名获取用户对象
    User selectByAvaiableName(String name);
}
package cn.signit.dao.mysql;

import cn.signit.domain.mysql.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    //自行添加的查询
    User selectByAvailableName(String name);
    
    Long insertAndGetId(User user);
    
    //List<User> selectAllUsers();
    
    
}
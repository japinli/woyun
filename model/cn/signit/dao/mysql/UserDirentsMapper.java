package cn.signit.dao.mysql;

import org.apache.ibatis.annotations.Param;

import cn.signit.domain.mysql.UserDirents;

public interface UserDirentsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDirents record);

    int insertSelective(UserDirents record);

    UserDirents selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDirents record);

    int updateByPrimaryKey(UserDirents record);
    
    UserDirents selectByUidAndPath(@Param("uid") Long uid, @Param("path") String path);
}
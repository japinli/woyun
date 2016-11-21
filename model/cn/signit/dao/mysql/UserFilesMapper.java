package cn.signit.dao.mysql;

import cn.signit.domain.mysql.UserFiles;

public interface UserFilesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFiles record);

    int insertSelective(UserFiles record);

    UserFiles selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFiles record);

    int updateByPrimaryKey(UserFiles record);
}
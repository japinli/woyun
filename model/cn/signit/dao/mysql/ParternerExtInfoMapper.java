package cn.signit.dao.mysql;

import cn.signit.domain.mysql.ParternerExtInfo;

public interface ParternerExtInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ParternerExtInfo record);

    int insertSelective(ParternerExtInfo record);

    ParternerExtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ParternerExtInfo record);

    int updateByPrimaryKey(ParternerExtInfo record);
}
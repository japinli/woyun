package cn.signit.dao.mysql;

import cn.signit.domain.mysql.ProveInfo;

public interface ProveInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProveInfo record);

    int insertSelective(ProveInfo record);

    ProveInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProveInfo record);

    int updateByPrimaryKey(ProveInfo record);
    
    ProveInfo selectByUserId(Long userId);
}
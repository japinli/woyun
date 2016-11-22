package cn.signit.dao.mysql;

import java.util.List;

import cn.signit.domain.mysql.SystemLog;
import cn.signit.domain.web.SqlSystemLog;

public interface SystemLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemLog record);

    int insertSelective(SystemLog record);

    SystemLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemLog record);

    int updateByPrimaryKey(SystemLog record);
    
    Long total();
    List<SystemLog> selectSelective(SqlSystemLog info);
    Long selectSelectiveTotal(SqlSystemLog info);
}
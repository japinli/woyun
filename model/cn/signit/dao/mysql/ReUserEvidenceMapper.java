package cn.signit.dao.mysql;

import cn.signit.domain.mysql.ReUserEvidence;

public interface ReUserEvidenceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReUserEvidence record);

    int insertSelective(ReUserEvidence record);

    ReUserEvidence selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReUserEvidence record);

    int updateByPrimaryKey(ReUserEvidence record);
}
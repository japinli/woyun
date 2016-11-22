package cn.signit.dao.mysql;

import cn.signit.domain.mysql.ReBlockEvidence;

public interface ReBlockEvidenceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReBlockEvidence record);

    int insertSelective(ReBlockEvidence record);

    ReBlockEvidence selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReBlockEvidence record);

    int updateByPrimaryKey(ReBlockEvidence record);
}
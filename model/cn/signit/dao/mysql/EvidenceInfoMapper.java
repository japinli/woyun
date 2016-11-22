package cn.signit.dao.mysql;

import java.util.List;

import cn.signit.domain.mysql.EvidenceInfo;

public interface EvidenceInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EvidenceInfo record);

    int insertSelective(EvidenceInfo record);

    EvidenceInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EvidenceInfo record);

    int updateByPrimaryKeyWithBLOBs(EvidenceInfo record);

    int updateByPrimaryKey(EvidenceInfo record);
    
    Long insertAndGetId(EvidenceInfo record);
    
    List<EvidenceInfo> selectSelective(EvidenceInfo record);
}
package cn.signit.dao.mysql;

import java.util.List;

import cn.signit.domain.mysql.UserCertificate;

public interface UserCertificateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserCertificate record);

    int insertSelective(UserCertificate record);

    UserCertificate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCertificate record);

    int updateByPrimaryKeyWithBLOBs(UserCertificate record);

    int updateByPrimaryKey(UserCertificate record);
    
    Long insertSelectiveAndGetId(UserCertificate record);
    
    List<UserCertificate> selectSelective(UserCertificate record);
}
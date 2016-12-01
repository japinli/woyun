package cn.signit.dao.mysql;

import org.apache.ibatis.annotations.Param;

import cn.signit.domain.mysql.Repo;

public interface RepoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Repo record);

    int insertSelective(Repo record);

    Repo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Repo record);

    int updateByPrimaryKey(Repo record);
    
    int insertAndGetId(Repo record);
    
    Repo selectByRepoNameAndUserEmail(@Param("repoName") String repoName, @Param("userEmail") String userEmail);
}
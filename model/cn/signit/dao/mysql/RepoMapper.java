package cn.signit.dao.mysql;

import cn.signit.domain.mysql.Repo;

public interface RepoMapper {
    int deleteByPrimaryKey(String repoId);

    int insert(Repo record);

    int insertSelective(Repo record);
}
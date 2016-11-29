package cn.signit.service.db.impl;

import javax.annotation.Resource;

import cn.signit.service.db.RepoService;
import cn.signit.service.git.GitService;
import cn.signit.untils.PathUtils;

public class RepoServiceImpl implements RepoService {
	
	@Resource
	private GitService gitService;

	public boolean createRepository(String repo) {
		String path = PathUtils.repo + repo;
		return gitService.createRepository(path);
	}
}

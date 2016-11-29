package cn.signit.service.git.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.signit.service.git.GitService;
import cn.signit.untils.PathUtils;

@Service("gitService")
public class GitServiceImpl implements GitService {

	public final static Logger LOG = LoggerFactory.getLogger(GitServiceImpl.class);
	
	public boolean createRepository(String dir) {
		LOG.info(PathUtils.repo);
		return false;
	}
}

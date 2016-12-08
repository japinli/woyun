package cn.signit.service.git.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.signit.service.git.GitService;
import cn.signit.utils.repo.RepoPath;

@Service("gitService")
public class GitServiceImpl implements GitService {

	public final static Logger LOG = LoggerFactory.getLogger(GitServiceImpl.class);
	
	public boolean createRepository(String repo) {
		LOG.info(RepoPath.repo);
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			builder.setGitDir(new File(repo, ".git"));
			builder.setWorkTree(new File(repo, "xxxxx"));
			Repository repository = builder.build();
			repository.create();
			LOG.info("Created a new repository at " + repository.getDirectory());
			return true;
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

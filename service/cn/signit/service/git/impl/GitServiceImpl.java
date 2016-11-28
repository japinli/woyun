package cn.signit.service.git.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.signit.service.git.GitService;

public class GitServiceImpl implements GitService {

	public final static Logger LOG = LoggerFactory.getLogger(GitServiceImpl.class);
	public final static String ROOT = System.getProperty("user.home");
	
	public boolean createRepository(String dir) {
		LOG.info(ROOT);
		try {
			Repository repository = FileRepositoryBuilder.create(new File("/home/japin/testgit/.git"));
			repository.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

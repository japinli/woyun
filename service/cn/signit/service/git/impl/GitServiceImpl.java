package cn.signit.service.git.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import cn.signit.service.git.GitService;

public class GitServiceImpl implements GitService {

	public boolean createRepository(String dir) {
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

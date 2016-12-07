package cn.signit.utils.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import cn.signit.untils.RepoPath;

public class RepoUtils {

	/**
	 * 获取仓库对象
	 * @param repoName 仓库名（格式: 用户邮件/仓库编号）
	 * @return 仓库对象
	 * @throws IOException
	 * @note 该函数会构造出仓库的绝对路径
	 */
	public static Repository getRepository(String repoName) throws IOException {
		String path = RepoPath.getRepositoryPath(repoName);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(path, RepoPath.git)).readEnvironment().build();
	}
	
	/**
	 * 获取仓库对象
	 * @param user 用户邮件
	 * @param repo 仓库编号
	 * @return 仓库对象
	 * @throws IOException
	 */
	public static Repository getRepository(String user, String repo) throws IOException {
		String fullpath = RepoPath.getRepositoryPath(user, repo);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(fullpath, RepoPath.git)).readEnvironment().build();
	}
	
	/**
	 * 获取指定提交的信息
	 * @param repository 仓库对象
	 * @param commit 提交记录，若 commit 为空，读取最新提交记录
	 * @return
	 * @throws IOException
	 */
	public static RevCommit getRevCommit(Repository repository, String commit) throws IOException {
		if (commit.isEmpty()) {
			final ObjectId head = repository.resolve(RepoPath.HEAD);
			commit = head.getName();
		}
		
		try (RevWalk revWalk = new RevWalk(repository)) {
			return revWalk.parseCommit(ObjectId.fromString(commit));
		}
	}
	
	public static TreeWalk getTreeWalk(Repository repository, RevTree tree, String path) throws IOException {
		TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);
		if (treeWalk == null) {
			throw new FileNotFoundException(path + " 未找到");
		}
		return treeWalk;
	}
}

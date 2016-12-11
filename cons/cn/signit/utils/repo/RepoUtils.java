package cn.signit.utils.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import cn.signit.entry.FileInfo;

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
	
	/**
	 * 删除文件获取目录
	 * @param parent 父目录路径名
	 * @param name 待删除的文件或目录名
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	public static boolean deleteFile(String filename) throws IOException {
		File file = new File(filename);
		if (file.isFile()) {
			return file.delete();
		} 
		
		for (File f : file.listFiles()) {
			deleteFile(f.getAbsolutePath());
		}
		
		return file.delete();
	}
	
	/**
	 * 获取指定路径的文件(夹)信息
	 * @param path 路径名
	 * @return
	 */
	public static FileInfo getFileInfo(String path) {
		File file = new File(path);
		return getFileInfo(file);
	}
	
	public static FileInfo getFileInfo(File file) {
		// 忽略 git 管理目录
		if (file.getName().equals(".git")) {
			return null;
		}
		
		String type = file.isDirectory() ? "dir" : "file";
		String filename = file.getName();
		long size = filename.length();
		long mtime = file.lastModified(); 
		return new FileInfo(type, filename, size, mtime);
	}
	
	public static List<FileInfo> getDirectoryInfo(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			return null;
		}
		List<FileInfo> infos = new ArrayList<FileInfo>();
		
		for (File f : file.listFiles()) {
			FileInfo fileInfo = getFileInfo(f);
			if (fileInfo != null) {
				infos.add(fileInfo);
			}
		}
		
		return infos;
	}
	
	public static boolean gitCommit(String repoName, String pattern, String message) throws IOException {
		Repository repository = getRepository(repoName);
		return gitCommit(repository, pattern, message);
	}
	
	private static boolean gitCommit(Repository repository, String filePattern, String message) {
		try {
			Git git = new Git(repository);
			git.add().addFilepattern(filePattern).call();
			git.commit().setMessage(message).call();
			git.close();
			return true;
		} catch (NoFilepatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}

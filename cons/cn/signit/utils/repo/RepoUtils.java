package cn.signit.utils.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.signit.entry.FileInfo;

public class RepoUtils {

	private final static Logger LOG = LoggerFactory.getLogger(RepoUtils.class);
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
	 * @return 文件信息
	 */
	public static FileInfo getFileInfo(String path) {
		File file = new File(path);
		return getFileInfo(file);
	}
	
	/**
	 * 获取文件(夹)信息
	 * @param file 文件对象
	 * @return 文件信息
	 */
	public static FileInfo getFileInfo(File file) {
		// 忽略 git 管理目录
		if (file.getName().equals(".git")) {
			return null;
		}
		
		String type = file.isDirectory() ? "dir" : "file";
		String filename = file.getName();
		long size = file.length();
		long mtime = file.lastModified(); 
		return new FileInfo(type, filename, size, mtime);
	}
	
	/**
	 * 获取目录文件信息
	 * @param path 目录路径
	 * @return 文件信息列表
	 */
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
	
	public void viewHistory(String repoName, String path) throws IOException {
		Repository repository = getRepository(repoName);
		RevCommit revCommit = RepoUtils.getRevCommit(repository, "");
		RevTree tree = revCommit.getTree();
		
		if (path.isEmpty()) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {
					
					LOG.info("{}", treeWalk.getPathString());
				}
			}
		} else {
			try (TreeWalk treeWalk = getTreeWalk(repository, tree, path)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {
					LOG.info("{}", treeWalk.getPathString());
				}
			}
		}
	}
	
	/**
	 * 根据用户邮件仓库ID获取文件、文件夹或仓库的更改记录
	 * @param user 用户邮件
	 * @param repoId 仓库ID
	 * @param path 路径
	 * @return 更改记录列表
	 * @throws NoHeadException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static List<CommitRecord> getFileHistory(String user, String repoId, String path) throws NoHeadException, GitAPIException, IOException {
		String repoName = RepoPath.contact(user, repoId);
		return getFileHistory(repoName, path);
	}
	
	/**
	 ** 根据仓库名（用户邮件+仓库ID）获取文件、文件夹或仓库的更改记录
	 * @param repoName 仓库名
	 * @param path 路径名，为空时表示获取仓库更改记录
	 * @return 更改记录列表
	 * @throws NoHeadException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static List<CommitRecord> getFileHistory(String repoName, String path) throws NoHeadException, GitAPIException, IOException {
		Repository repository = getRepository(repoName);
		try (Git git = new Git(repository)) {
			Iterable<RevCommit> commits = null;
			if (path.isEmpty()) {
				commits = git.log().all().call();
			} else {
				commits = git.log().addPath(path).all().call();
			}
			List<CommitRecord> records = new ArrayList<CommitRecord>();
			for (RevCommit commit : commits) {
				String author = commit.getCommitterIdent().getName();
				String email = commit.getCommitterIdent().getEmailAddress();
				String message = commit.getFullMessage();
				String sha1 = commit.name();
				int mtime = commit.getCommitTime();
				records.add(new CommitRecord(author, email, message, sha1, (long) mtime));
			}
			return records;
		}
	}
	
	/**
	 * 还原文件到指定的提交记录状态
	 * @param user 用户邮件
	 * @param repoid 仓库ID
	 * @param commit 提交记录标志
	 * @param pathname 仓库下的文件相对路径
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	public static boolean restoreFileByCommit(String user, String repoid, String commit, String pathname) throws IOException {
		String repoName = RepoPath.contact(user, repoid);
		return restoreFileByCommit(repoName, commit, pathname);
	}
	
	/**
	 * 还原文件到指定的提交记录状态
	 * @param repoName 仓库名 (用户邮件/仓库ID)
	 * @param commit 提交记录标志
	 * @param pathname 仓库下的文件相对路径
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	public static boolean restoreFileByCommit(String repoName, String commit, String pathname) throws IOException {
		Repository repository = getRepository(repoName);
		try (Git git = new Git(repository)) {
			try {
				git.checkout().addPath(pathname).setStartPoint(commit).call();
				String message = String.format(RepoPath.restore_msg, pathname);
				gitCommit(repository, pathname, message);
				LOG.info("提交成功: {} 仓库{}", repoName, message);
				return true;
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 提交记录
	 */
	public static class CommitRecord {
		private String message; /** 提交信息 */
		private String user;    /** 提交者 */
		private String email;   /** 提交者的邮件 */
		private String commit;  /** 提交记录标志 */
		Long mtime;             /** 提交时间*/
		
		public CommitRecord(String user, String email, String message, String commit, Long mtime) {
			this.user = user;
			this.email = email;
			this.message = message;
			this.commit = commit;
			this.mtime = mtime;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getCommit() {
			return commit;
		}
		public void setCommit(String commit) {
			this.commit = commit;
		}
		public Long getMtime() {
			return mtime;
		}
		public void setMtime(Long mtime) {
			this.mtime = mtime;
		}
	}
}

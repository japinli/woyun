package cn.signit.service.db.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import cn.signit.dao.mysql.RepoMapper;
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
import cn.signit.entry.FileInfo;
import cn.signit.entry.RepoInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.Convert;
import cn.signit.untils.RepoPath;

@Service("repoService")
public class RepoServiceImpl implements RepoService {

	private final static Logger LOG = LoggerFactory.getLogger(RepoServiceImpl.class);
	
	@Resource
	private RepoMapper repoDao;
	
	public boolean isRepositoryExists(String userEmail, String repoName) {
		Repo repo = repoDao.selectByRepoNameAndUserEmail(repoName, userEmail);
		if (repo == null) {
			return false;
		}
		
		return true;
	}
	
	public RepoInfo createRepository(User user, String repo) {
		String repoId = UUID.randomUUID().toString();
		String path = RepoPath.getRepositoryPath(user.getEmail(), repoId);
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			builder.setGitDir(new File(path, RepoPath.git));
			Repository repository = builder.build();
			repository.create();
			
			setRepositoryConfig(repository, user.getRealName(), user.getEmail());
			
			try (Git git = new Git(repository)) {
				String commit_msg = String.format(RepoPath.create_repo_msg, repo);
				git.commit().setMessage(commit_msg).call();
			}

			LOG.info("用户 {} 创建仓库 {} 成功", user.getEmail(), repo);
			
			// 更新数据库
			Repo record = new Repo(repoId, repo, user.getEmail());
			if (Convert.toBoolean(repoDao.insert(record))) {
				LOG.info("用户 {} 更新仓库({})信息成功", user.getEmail(), repo);
				RepoInfo info = new RepoInfo(record);
				File library = new File(repository.getWorkTree().toString(), record.getRepoName());
				info.setModifyTime(FS.DETECTED.lastModified(library));
				info.setRepoSize(FS.DETECTED.length(library));
				return info;
			}
			
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public List<RepoInfo> getRepositoriesInfo(User user) throws IOException {
		List<Repo> repositories = repoDao.selectByUserEmail(user.getEmail());
		
		List<RepoInfo> repoInfos = new ArrayList<RepoInfo>();
		for (Repo repo : repositories) {
			RepoInfo item = getRepositoryInfo(repo);
			if (item == null) {
				continue;
			}
			repoInfos.add(item);
		}
		
		return repoInfos;
	}

	public boolean listRepository(String parent, String repo) {
		try {
			Repository repository = getRepository(parent, repo);
			LOG.info("Repository Directory: {}", repository.getDirectory());
			RevTree tree = getTree(repository);
			getFile(repository, tree);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public boolean addFile(String parent, String repo, String filename) throws IOException {
		Repository repository = getRepository(parent, repo);
		/*
		 * TODO: 文件实际存储
		 */
		File file = new File(repository.getWorkTree().getPath(), filename);
		if (!file.createNewFile()) {
			throw new IOException("创建文件(" + file.getPath() + ")失败");
		}
		
		return gitCommit(repository, filename, String.format(RepoPath.add_file_msg, filename));
	}
	
	public boolean renameRepository(String repoId, String repoName) {
		return Convert.toBoolean(repoDao.updateByRepoId(repoId, repoName));
	}
	
	public boolean deleteRepository(String repoId) {
		Repo record = new Repo();
		record.setRepoId(repoId);
		record.setState(true);
		record.setDeleteTime(Calendar.getInstance().getTime());
		return Convert.toBoolean(repoDao.markRepositoryDeleted(record));
	}
	
	public List<FileInfo> getDirectory(String repoName, String path) throws IOException {
		
		Repository repository = getRepository(repoName);
		RevCommit revCommit = getRevCommit(repository, "");
		RevTree tree = revCommit.getTree();
		String working = repository.getWorkTree().getPath();
		List<FileInfo> infos = new ArrayList<FileInfo>();

		if (path.isEmpty()) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(false);
				
				while (treeWalk.next()) {
					String filename = treeWalk.getNameString();
					String type = treeWalk.isSubtree() ? "dir" : "file";
					File file = new File(working, treeWalk.getPathString());
					long size = FS.DETECTED.lastModified(file);
					long mtime = FS.DETECTED.length(file);
					
					infos.add(new FileInfo(type, filename, size, mtime));
				}
			}
		} else {
			try (TreeWalk treeWalk = getTreeWalk(repository, tree, path)) {
				
				if ((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
					throw new IllegalStateException(treeWalk.getNameString() + " 不是目录项");
				}
				
				try (TreeWalk dirWalk = new TreeWalk(repository)) {
					dirWalk.addTree(treeWalk.getObjectId(0));
					dirWalk.setRecursive(false);
					while (dirWalk.next()) {
						String filename = treeWalk.getNameString();
						String type = treeWalk.isSubtree() ? "dir" : "file";
						File file = new File(working, treeWalk.getPathString());
						long size = FS.DETECTED.lastModified(file);
						long mtime = FS.DETECTED.length(file);
						
						infos.add(new FileInfo(type, filename, size, mtime));
					}
				}
			}
		}
		return infos;
	}
	
	public boolean createDirectory(String repoName, String path) {
		/*
		 * 在仓库下创建一个空目录，由于 git 不会追踪空目录，因此可以不用进行 git commit 操作
		 */
		String dirPath = RepoPath.getRepositoryPath(repoName, path);
		File file = new File(dirPath);
		if (!file.exists() && !file.isDirectory()) {
			return file.mkdir();
		}
		return false;
	}
	
	public boolean renameDirectory(String repoName, String oldPath, String newPath) throws IOException {
		String oldFullPath = RepoPath.getRepositoryPath(repoName, oldPath);
		String newFullPath = RepoPath.getRepositoryPath(repoName, newPath);
		
		File file = new File(oldFullPath);
		if (file.exists()) {
			boolean flag = file.renameTo(new File(newFullPath));
			if (flag == false) {
				return false;
			}
			
			Repository repository = getRepository(repoName);
			return gitCommit(repository, ".", "rename");
		}
		
		return false;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	/// 私有函数
	//////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取仓库对象
	 * @param parent 父目录
	 * @param repo 仓库名
	 * @return 仓库对象
	 * @throws IOException
	 */
	private static Repository getRepository(String repoName) throws IOException {
		String path = RepoPath.getRepositoryPath(repoName);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(path, RepoPath.git)).readEnvironment().build();
	}
	
	private static Repository getRepository(String parent, String repo) throws IOException {
		String path = RepoPath.getRepositoryPath(parent, repo);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(path, RepoPath.git)).readEnvironment().build();
	}

	/**
	 * 获取目录遍历树结构
	 * @param repository 仓库对象
	 * @return 遍历对象
	 * @throws IOException
	 */
	private static RevTree getTree(Repository repository) throws IOException {
		ObjectId lastCommit = repository.resolve(RepoPath.HEAD);
		LOG.info("ObjectId: {}", lastCommit.getName());
		try (RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(lastCommit);

			RevTree tree = commit.getTree();
			LOG.info("最后提交时间: {}", new Date(commit.getCommitTime()));
			LOG.info("目录: {}", tree);

			return tree;
		}
	}

	/**
	 * 非递归遍历仓库
	 * @param repository 仓库对象
	 * @param tree 遍历树对象
	 * @throws IOException
	 */
	private static void getFile(Repository repository, RevTree tree) throws IOException {
		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			treeWalk.addTree(tree);
			treeWalk.setRecursive(false);
			while (treeWalk.next()) {
				LOG.info("路径: {}", treeWalk.getPathString());
				// ObjectLoader loader =
				// repository.open(treeWalk.getObjectId(0));
			}
		}
	}
	
	/**
	 * 修改仓库用户配置信息
	 * @param repository 仓库对象
	 * @param name 用户名
	 * @param email 用户邮箱
	 * @throws IOException
	 */
	private static void setRepositoryConfig(Repository repository, String name, String email) throws IOException {
		StoredConfig config = repository.getConfig();
		config.setString("user", null, "name", name);
		config.setString("user", null, "email", email);
		config.save();
		LOG.info("修改仓库用户名为({})，邮件地址为({})", name, email);
	}
	
	/**
	 * 获取仓库基础信息
	 * @param repo 仓库对象
	 * @return
	 * @throws IOException
	 */
	private static RepoInfo getRepositoryInfo(Repo repo) throws IOException {
		RepoInfo info = new RepoInfo(repo);
		Repository repository = getRepository(repo.getUserEmail(), repo.getRepoId());
		File library = new File(repository.getWorkTree().toString());
		info.setModifyTime(FS.DETECTED.lastModified(library));
		info.setRepoSize(FS.DETECTED.length(library));
		return info;
	}
	
	/**
	 * 获取指定提交的信息
	 * @param repository 仓库对象
	 * @param commit 提交记录，若 commit 为空，读取最新提交记录
	 * @return
	 * @throws IOException
	 */
	private static RevCommit getRevCommit(Repository repository, String commit) throws IOException {
		if (commit.isEmpty()) {
			final ObjectId head = repository.resolve(RepoPath.HEAD);
			commit = head.getName();
		}
		
		try (RevWalk revWalk = new RevWalk(repository)) {
			return revWalk.parseCommit(ObjectId.fromString(commit));
		}
	}
	
	/**
	 * 获取指定路径的遍历树
	 * @param repository 仓库对象
	 * @param tree
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static TreeWalk getTreeWalk(Repository repository, RevTree tree, String path) throws IOException {
		TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);
		if (treeWalk == null) {
			throw new FileNotFoundException(path + " 未找到");
		}
		return treeWalk;
	}
	
	/**
	 * 提交修改记录
	 * @param repository 仓库对象
	 * @param filePattern 提交时的文件模式
	 * @param message 提交信息
	 * @return true - 成功, false - 失败
	 */
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

package cn.signit.service.db.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.signit.controller.api.RestStatus;
import cn.signit.dao.mysql.RepoMapper;
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
import cn.signit.entry.CommitHistory;
import cn.signit.entry.DirOperation;
import cn.signit.entry.FileInfo;
import cn.signit.entry.RepoInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.Convert;
import cn.signit.utils.repo.RepoPath;
import cn.signit.utils.repo.RepoUtils;
import cn.signit.utils.repo.TreeFilterType;


@Service("repoService")
public class RepoServiceImpl implements RepoService {

	private final static Logger LOG = LoggerFactory.getLogger(RepoServiceImpl.class);
	
	@Resource
	private RepoMapper repoDao;
	@Resource
	private TreeFilterType filterType;
	
	public int isRepositoryExists(String userEmail, String repoName) {
		Repo repo = repoDao.selectByRepoNameAndUserEmail(repoName, userEmail);
		if (repo == null) {
			return -1;
		}
		
		return repo.getState() ? 1 : 0;
	}
	
	public RepoInfo createRepository(User user, String repo) {
		String repoId = UUID.randomUUID().toString();
		Repo record = new Repo(repoId, repo, user.getEmail());
		if (!Convert.toBoolean(repoDao.insert(record))) {
			LOG.warn("用户 {} 已存在 {} 仓库", user.getEmail(), repo);
			return null;
		}
		
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
			
			RepoInfo info = new RepoInfo(record);
			File library = new File(repository.getWorkTree().toString());
			info.setModifyTime(FS.DETECTED.lastModified(library));
			info.setRepoSize(FS.DETECTED.length(library));
			return info;
			
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public List<RepoInfo> getRepositoriesInfo(User user, boolean deleted) throws IOException {
		List<Repo> repositories = repoDao.selectByUserEmailAndState(user.getEmail(), deleted);
		
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
	
	public boolean permanentDeleteRepository(String user, String repoId) throws IOException {
		if (!Convert.toBoolean(repoDao.deleteByRepoId(repoId))) {
			return false;
		}
		String path = RepoPath.getRepositoryPath(user, repoId);
		return RepoUtils.deleteFile(path);
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
	
	public RestStatus createDirectory(String repoName, String path) {
		/*
		 * 在仓库下创建一个空目录，由于 git 不会追踪空目录，因此可以不用进行 git commit 操作
		 */
		String dirPath = RepoPath.getRepositoryPath(repoName, path);
		File file = new File(dirPath);
		if (file.exists() && file.isFile()) {
			return RestStatus.FILE_EXISTS;
		}
		if (file.exists() && file.isDirectory()) {
			return RestStatus.DIR_EXISTS;
		}
		return file.mkdir() ? RestStatus.SUCCESS : RestStatus.FAILED;
	}
	
	public RestStatus renameDirectory(String repoName, String oldPath, String newPath) throws IOException {
		String oldFullPath = RepoPath.getRepositoryPath(repoName, oldPath);
		String newFullPath = RepoPath.getRepositoryPath(repoName, newPath);
		
		// 检测新建的文件夹是否存在
		File nfile = new File(newFullPath);
		if (nfile.exists() && nfile.isFile()) {
			return RestStatus.FILE_EXISTS;
		}
		if (nfile.exists() && nfile.isDirectory()) {
			return RestStatus.DIR_EXISTS;
		}
		
		File file = new File(oldFullPath);
		boolean flag = file.renameTo(new File(newFullPath));
		if (flag == false) {
				return RestStatus.RENAME_FAILED;
		}
		Repository repository = RepoUtils.getRepository(repoName);
		return gitCommit(repository, ".", "rename") ? RestStatus.SUCCESS : RestStatus.FAILED;
	}
	
	public List<CommitHistory> getRepositoryHistory(String repoName) throws IOException {
		Repository repository = getRepository(repoName);
		return getCommitInfo(repository);
	}
	
	public List<FileInfo>  getHistoryByCommit(String repoName, String commit, String path) throws IOException {
		Repository repository = getRepository(repoName);
		return readRepositoryElementAt(repository, commit, path);
	}
	
	public List<FileInfo> getFileByCategory(String user, String repoId, String category) throws IOException {
		Repository repository = RepoUtils.getRepository(user, repoId);
		RevCommit revCommit = RepoUtils.getRevCommit(repository, "");
		RevTree tree = revCommit.getTree();
		TreeFilter filter = filterType.getFilter(category);
		String working = repository.getWorkTree().getAbsolutePath();
		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(filter);
			List<FileInfo> infos = new ArrayList<FileInfo>();
			while (treeWalk.next()) {
				String name = treeWalk.getNameString();
				String type = treeWalk.isSubtree() ? "dir" : "file";
				File file = new File(working, treeWalk.getPathString());
				long size =  FS.DETECTED.length(file);
				long mtime = FS.DETECTED.lastModified(file);
				infos.add(new FileInfo(type, name, size, mtime));
			}
			return infos;
		}
	}
	
	public boolean move(String srcRepo, String dstRepo, String srcPath, String dstPath, String name) throws IOException {
		/* 
		 * 同一仓库同一目录下不支持移动
		 */
		if (srcRepo.equals(dstRepo) && srcPath.equals(dstPath)) {
			LOG.warn("不支持在同仓库同目录下的移动操作");
			return false;
		}
		
		srcPath = RepoPath.getRepositoryPath(srcRepo, srcPath);
		dstPath = RepoPath.getRepositoryPath(dstRepo, dstPath);
		int flag = moveFileTo(srcPath, dstPath, name);
		
		if (Convert.toBoolean(flag & 0x01)) {
			// 拷贝到目的目录成功，提交变更
			Repository repository = getRepository(dstRepo);
			gitCommit(repository, ".", String.format(RepoPath.add_file_msg, name));
			if (Convert.toBoolean(flag & 0x10)) {
				// 删除源目录信息成功，提交变更
				repository = getRepository(srcRepo);
				gitCommit(repository, ".", String.format(RepoPath.del_file_msg, name));
			}
			return true;
		}
		
		return false;
	}
	
	public boolean copy(String srcRepo, String dstRepo, String srcPath, String dstPath, String name) throws IOException {
		/* 
		 * 同一仓库同一目录下不支持复制 
		 */
		if (srcRepo.equals(dstRepo) && srcPath.equals(dstPath)) {
			LOG.warn("不支持在同仓库同目录下的复制操作");
			return false;
		}
		
		srcPath = RepoPath.getRepositoryPath(srcRepo, srcPath, name);
		dstPath = RepoPath.getRepositoryPath(dstRepo, dstPath);
		
		boolean flag = copyFileTo(srcPath, dstPath, name);
		if (!flag) {
			LOG.warn("文件(夹)复制失败");
			return false;
		}
		
		Repository repository = getRepository(dstRepo);
		return gitCommit(repository, ".", String.format(RepoPath.add_file_msg, name));
	}
	
	public boolean delete(String repoName, List<DirOperation> dels) throws IOException {
		String repoPath = RepoPath.getRepositoryPath(repoName);
		String deleted = " ";
		for (DirOperation dirOperation : dels) {
			deleted += dirOperation.getPath() + ";";
			deleteFile(repoPath, dirOperation.getPath());
		}
		Repository repository = getRepository(repoName);
		return gitCommit(repository, ".", String.format(RepoPath.del_file_msg, deleted));
	}
	
	public boolean uploadFiles(String repoName, String path, MultipartFile[] files) throws IOException {
		
		String fullRepoName = RepoPath.getRepositoryPath(repoName);
		String commitMessage = " ";
		for (MultipartFile file : files) {
			String filename = file.getOriginalFilename();
			commitMessage += filename + "; ";
			filename = RepoPath.contact(path, filename);
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(fullRepoName, filename));
		}
		Repository repository = getRepository(repoName);
		return gitCommit(repository, ".", String.format(RepoPath.add_file_msg, commitMessage));
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
			LOG.info("提交成功: {}", message);
			return true;
		} catch (NoFilepatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.warn("提交失败: {}", message);
		return false;
	}
	
	/**
	 * 复制文件(夹)在特定目录中
	 * @param src 源路径
	 * @param dst 目录路径
	 * @param name 待复制的文件(夹)
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	public static boolean copyFileTo(String src, String dst, String name) throws IOException {
		File source = new File(src, name);
		if (!source.exists()) {
			LOG.warn("{}/{} 不存在", src, name);
			throw new FileNotFoundException(src + "/" + name + "不存在");
		}

		if (source.isFile()) {
			File destination = new File(dst, name);
			InputStream inputStream = new FileInputStream(source);
			FileOutputStream outpuSteam = new FileOutputStream(destination);
			byte[] buffer = new byte[1024 * 1024];
			int length = 0;
			while ((length = inputStream.read(buffer)) != -1) {
				outpuSteam.write(buffer, 0, length);
			}
			inputStream.close();
			outpuSteam.flush();
			outpuSteam.close();
		} else {
			File folder = new File(dst, name);
			if (!folder.mkdir()) {
				LOG.warn("创建文件夹({})失败", folder.getPath());
				return false;
			}

			for (File f : source.listFiles()) {
				copyFileTo(source.getAbsolutePath(), folder.getAbsolutePath(), f.getName());
			}

			return true;
		}
		return false;
	}
	
	/**
	 * 移动文件或文件夹
	 * @param src 源目录路径
	 * @param dst 目的目录路径
	 * @param name 待移动的文件或文件夹
	 * @return 0x01 - 文件拷贝成功, 0x11 - 文件(夹)移动成功, 0x00 - 失败 
	 * @throws IOException
	 */
	public static int moveFileTo(String src, String dst, String name) throws IOException {
		int ret = 0;
		if (copyFileTo(src, dst, name)) {
			ret |= 0x01;
			if (deleteFile(src, name)) {
				ret |= 0x10;
			}
		}
		
		return ret;
	}
	
	/**
	 * 删除文件获取目录
	 * @param parent 父目录路径名
	 * @param name 待删除的文件或目录名
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	public static boolean deleteFile(String parent, String name) throws IOException {
		File file = new File(parent, name);
		if (file.isFile()) {
			return file.delete();
		} 
		
		for (File f : file.listFiles()) {
			deleteFile(file.getAbsolutePath(), f.getName());
		}
		
		return file.delete();
	}
	
	/**
	 * 获取仓库的提交记录
	 * @param repository 仓库对象
	 * @return 提交记录列表
	 * @throws IOException
	 */
	public static List<CommitHistory> getCommitInfo(Repository repository) throws IOException {
		List<CommitHistory> histories = new ArrayList<CommitHistory>();
		
		Collection<Ref> allRefs = repository.getAllRefs().values();
		try (RevWalk walk = new RevWalk(repository)) {
			for (Ref ref : allRefs) {
				walk.markStart(walk.parseCommit(ref.getObjectId()));
			}
			
			for (RevCommit commit : walk) {
				CommitHistory history = new CommitHistory();
				history.setCommitId(commit.name());
				history.setCommitter(commit.getCommitterIdent().getName());
				history.setEmail(commit.getCommitterIdent().getEmailAddress());
				history.setCommitTime(commit.getCommitTime());
				history.setMessage(commit.getFullMessage());
				histories.add(history);
			}
		}
		
		return histories;
	}
	
	/**
	 * 读取仓库中的历史记录文件信息
	 * @param repository 仓库对象
	 * @param commit 提交记录的 SHA-1 值
	 * @param path 路径
	 * @return 文件信息列表
	 * @throws IOException
	 */
	private static List<FileInfo> readRepositoryElementAt(Repository repository, String commit, String path) throws IOException {
		RevCommit revCommit = getRevCommit(repository, commit);
		RevTree tree = revCommit.getTree();
		List<FileInfo> infos = null;
		
		if (path.isEmpty()) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(false);
				infos = getFilesFromTreeWalk(repository, treeWalk);
			}
		} else {
			try (TreeWalk treeWalk = getTreeWalk(repository, tree, path)) {
				if ((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
					throw new IllegalStateException("不是目录项目");
				}
				
				try (TreeWalk dirWalk = new TreeWalk(repository)) {
					dirWalk.addTree(treeWalk.getObjectId(0));
					dirWalk.setRecursive(false);
					infos = getFilesFromTreeWalk(repository, dirWalk);
				}
			}
		}
		return infos;
	}
	
	/**
	 * 读取文件信息
	 * @param repository 仓库对象
	 * @param tree 遍历树对象
	 * @return 文件信息列表
	 * @throws IOException
	 */
	private static List<FileInfo> getFilesFromTreeWalk(Repository repository, TreeWalk tree) throws IOException {
		List<FileInfo> infos = new ArrayList<FileInfo>();
		
		while (tree.next()) {
			boolean isDir = tree.isSubtree();
			String type = isDir ? "dir" : "file";
			String filename = tree.getNameString();
			Long size = null;
			if (!isDir) {
				ObjectId objectId = tree.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				size = loader.getSize();
			}
			
			FileInfo fileInfo = new FileInfo(type, filename, size, null);
			infos.add(fileInfo);
		}
		
		return infos;
	}
}

package cn.signit.service.db.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.RepoMapper;
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
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
	
	public Repo createRepository(User user, String repo) {
		String path = RepoPath.repo + user.getEmail() + RepoPath.sep + repo;
		
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			builder.setGitDir(new File(path, RepoPath.git));
			Repository repository = builder.build();
			repository.create();
			
			try (Git git = new Git(repository)) {
				String commit_msg = String.format(RepoPath.create_repo_msg, repo);
				git.commit().setMessage(commit_msg).call();
			}

			LOG.info("用户 {} 创建仓库 {} 成功", user.getEmail(), repo);
			
			// 更新数据库
			String uuid = UUID.randomUUID().toString();
			Repo record = new Repo(uuid, repo, user.getEmail());
			if (Convert.toBoolean(repoDao.insert(record))) {
				LOG.info("用户 {} 更新仓库({})信息成功", user.getEmail(), repo);
				return record;
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

	public boolean addFile(String parent, String repo, String filename) {

		try {
			Repository repository = getRepository(parent, repo);
			try (Git git = new Git(repository)) {
				LOG.info("Repository Directory: {}", repository.getDirectory());
				File tmp = new File(repository.getDirectory().getParent(), filename);
				if (!tmp.createNewFile()) {
					throw new IOException("Cound not create file " + tmp);
				}
				git.add().addFilepattern(filename).call();
				git.commit().setMessage(String.format(RepoPath.add_file_msg, filename)).call();
			}
		} catch (NoFilepatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
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
	private static Repository getRepository(String parent, String repo) throws IOException {
		String path = RepoPath.repo + repo;
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

}

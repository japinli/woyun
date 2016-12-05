package cn.signit.service.db;

import java.io.IOException;
import java.util.List;

import cn.signit.domain.mysql.User;
import cn.signit.entry.FileInfo;
import cn.signit.entry.RepoInfo;

public interface RepoService {
	
	/**
	 * 查询用户是否存在相同的仓库
	 * @param userEmail 用户邮件地址
	 * @param repo 仓库名
	 * @return true - 仓库已存在，false - 仓库不存在
	 */
	boolean isRepositoryExists(String userEmail, String repo);

	/**
	 * 新建仓库
	 * @param user 用户对象
	 * @param repo 仓库名
	 * @return 成功返回 Repo 对象，失败返回 null
	 */
	RepoInfo createRepository(User user, String repo);
	
	/**
	 * 获取用户所有仓库信息
	 * @param user 用户对象
	 * @return
	 */
	List<RepoInfo> getRepositoriesInfo(User user) throws IOException;
	
	/**
	 * 查看仓库下的文件信息
	 * @param parent 父目录
	 * @param repo 仓库名
	 * @return true - 成功，false - 失败
	 */
	boolean listRepository(String parent, String repo);
	
	/**
	 * 向仓库中添加文件
	 * @param parent 父目录
	 * @param repo 仓库名
	 * @param filename 文件名
	 * @return true - 成功，false - 失败
	 * @throws IOException 
	 */
	boolean addFile(String parent, String repo, String filename) throws IOException;
	
	/**
	 * 重命名仓库
	 * @param repoId 仓库ID
	 * @param repoName 新仓库名
	 * @return true - 成功, false - 失败
	 */
	boolean renameRepository(String repoId, String repoName);
	
	/**
	 * 删除用户仓库
	 * * @param repoId 仓库ID
	 * * @return true - 成功, false - 失败
	 */
	boolean deleteRepository(String repoId);
	
	/**
	 * 获取指定仓库及路径下的文件信息
	 * @param repoName 仓库名
	 * @param path 路径名
	 * @return 文件信息列表
	 */
	List<FileInfo> getDirectory(String repoName, String path) throws IOException;
	
	/**
	 * 创建目录
	 * @param repoName 仓库名
	 * @param path 在指定仓库下的目录全路径
	 * @return true - 成功, false - 失败
	 */
	boolean createDirectory(String repoName, String path);
	
	/**
	 * 重命名目录
	 * @param repoName 仓库名
	 * @param oldPath 待重命名的目录
	 * @param newPath 重命名后的目录
	 * @return
	 * @throws IOException 
	 */
	boolean renameDirectory(String repoName, String oldPath, String newPath) throws IOException;
}

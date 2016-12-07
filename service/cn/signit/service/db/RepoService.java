package cn.signit.service.db;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cn.signit.domain.mysql.User;
import cn.signit.entry.CommitHistory;
import cn.signit.entry.DirOperation;
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
	
	/**
	 * 获取仓库历史变更记录
	 * @param repoName 仓库名
	 * @return 历史变更记录列表
	 * @throws IOException
	 */
	List<CommitHistory> getRepositoryHistory(String repoName) throws IOException;
	
	/**
	 * 通过提交的记录获取目录信息
	 * @param repoName 仓库名
	 * @param commit 提交的记录 SHA-1 值
	 * @param path 路径
	 * @return 文件信息列表
	 * @throws IOException
	 */
	List<FileInfo>  getHistoryByCommit(String repoName, String commit, String path) throws IOException;
	
	/**
	 * 移动
	 * @param srcRepo 源仓库ID
	 * @param dstRepo 目录仓库ID
	 * @param srcPath 源仓库下的路径
	 * @param dstPath 目地仓库下的路径
	 * @param name 待移动的文件(夹)
	 * @return true - 成功, false - 失败
	 * @throws IOException 
	 */
	boolean move(String srcRepo, String dstRepo, String srcPath, String dstPath, String name) throws IOException;
	
	/**
	 * 复制
	 * @param srcRepo 源仓库ID
	 * @param dstRepo 目录仓库ID
	 * @param srcPath 源仓库下的路径
	 * @param dstPath 目地仓库下的路径
	 * @param name 待复制的文件(夹)
	 * @return true - 成功, false - 失败 
	 * @throws IOException 
	 */
	boolean copy(String srcRepo, String dstRepo, String srcPath, String dstPath, String name) throws IOException;
	
	/**
	 * 删除文件(夹)
	 * @param repoName 仓库名
	 * @param pathname 待删除的文件(夹)在仓库下的路径名
	 * @return true - 成功, false - 失败
	 * @throws IOException
	 */
	boolean delete(String repoName, List<DirOperation> dels) throws IOException;
	
	/**
	 * 上传文件
	 * @param repoName 仓库名
	 * @param path 
	 * @param files
	 * @return
	 * @throws IOException 
	 */
	boolean uploadFiles(String repoName, String path, MultipartFile[] files) throws IOException;
}

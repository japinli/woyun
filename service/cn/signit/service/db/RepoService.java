package cn.signit.service.db;

import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;

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
	Repo createRepository(User user, String repo);
	
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
	 */
	boolean addFile(String parent, String repo, String filename);
}

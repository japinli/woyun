package cn.signit.service.db;

public interface RepoService {

	/**
	 * 新建仓库
	 * @param repo 仓库名
	 * @return true - 成功，false - 失败
	 */
	boolean createRepository(String repo);
}

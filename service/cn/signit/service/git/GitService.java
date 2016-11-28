package cn.signit.service.git;

public interface GitService {

	/**
	 * 创建用户仓库
	 * @param dir 仓库目录
	 * @return true - 成功， false - 失败
	 */
	boolean createRepository(String dir);
}

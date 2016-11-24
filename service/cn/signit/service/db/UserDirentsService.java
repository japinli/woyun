package cn.signit.service.db;


public interface UserDirentsService {

	/**
	 * 创建用户目录
	 * @param uid 用户ID
	 * @param path 用户目录路径
	 * @return true - 成功, false - 失败
	 */
	boolean createDirectory(Long uid, String path);
}

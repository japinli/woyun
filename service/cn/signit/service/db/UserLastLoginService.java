package cn.signit.service.db;

public interface UserLastLoginService {

	/**
	 * 更新或插入用户最后登录时间
	 * @param uid 用户 ID
	 * @param username 用户名称
	 */
	void updateLastLoginTime(Long uid, String username);
}

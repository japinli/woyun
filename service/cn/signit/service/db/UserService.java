package cn.signit.service.db;

import cn.signit.domain.mysql.ProveInfo;
import cn.signit.domain.mysql.User;

/**
*用户账户相关服务
* @ClassName UserService
* @author Liwen
* @date 2016年11月9日-上午10:42:23
* @version (版本号)
* @see (参阅)
*/
public interface UserService {
	
	public User getUser(String account);
	
	public User getUser(Long userid);
	
	public User getUser(User user);
	
	public Long addUserAndGetId(User user);
	
	public boolean updateUser(User user);
	
	/**
	 * 根据用户Id获取用户的身份证明信息
	 */
	public ProveInfo getUserProveInfo(Long userId);

}

package cn.signit.service.resovle.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.UserMapper;
import cn.signit.domain.mysql.User;
import cn.signit.service.UserService;

/**
*用户基础信息服务类
* @ClassName UserServiceImpl
* @author Liwen
* @date 2016年11月11日-下午2:53:24
* @version (版本号)
* @see (参阅)
*/
@Service("userService")
public class UserServiceImpl  implements UserService{

	@Resource
	private UserMapper userDao;
	
	/**
	* 根据用户名获取用户对象 
	*@param account 用户名
	*@return 成功返回用户对象，否则返回 null
	*@see (参阅)
	*/
	@Override
	public User getUser(String account) {
		return userDao.selectByAvaiableName(account);
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param userid
	*@return
	*@see (参阅)
	*/
	@Override
	public User getUser(Long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param user
	*@return
	*@see (参阅)
	*/
	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param user
	*@return
	*@see (参阅)
	*/
	@Override
	public Long addUserAndGetId(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param user
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}

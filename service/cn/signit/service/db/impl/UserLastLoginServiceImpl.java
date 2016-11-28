package cn.signit.service.db.impl;


import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.UserLastLoginMapper;
import cn.signit.domain.mysql.UserLastLogin;
import cn.signit.service.NormalService;
import cn.signit.service.db.UserLastLoginService;

@Service("userLastLoginService")
public class UserLastLoginServiceImpl extends NormalService implements UserLastLoginService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserLastLoginServiceImpl.class);

	@Resource
	private UserLastLoginMapper userLastLoginDao;
	
	public void updateLastLoginTime(Long uid, String username) {
		UserLastLogin lastLogin = userLastLoginDao.selectByUserId(uid);
		Date date = Calendar.getInstance().getTime();
		int ret = 0;
		if (lastLogin == null) {
			lastLogin = new UserLastLogin();
			lastLogin.setUid(uid);
			lastLogin.setLastLogin(date);
			ret = userLastLoginDao.insert(lastLogin);
		} else {
			lastLogin.setLastLogin(date);
			ret = userLastLoginDao.updateByPrimaryKey(lastLogin);
		}
		
		LOG.info("用户{} 最后登录时间 {}, 数据库更新 {}", username, date.toString(), toBoolean(ret) ? "成功" : "失败");
	}
}

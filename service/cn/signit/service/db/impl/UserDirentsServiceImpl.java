package cn.signit.service.db.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.UserDirentsMapper;
import cn.signit.domain.mysql.UserDirents;
import cn.signit.service.db.UserDirentsService;

@Service("userDirentsService")
public class UserDirentsServiceImpl implements UserDirentsService {

	@Resource
	private UserDirentsMapper userDirentsDao;
	
	public boolean createDirectory(Long uid, String path) {
		UserDirents dirents = userDirentsDao.selectByUidAndPath(uid, path);
		if (dirents != null) { // 文件夹已存在
			return false;
		}
		
		dirents = new UserDirents();
		dirents.setUid(uid);
		dirents.setPath(path);
		
		int ret = userDirentsDao.insert(dirents);
		if (ret == 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean deleteDirectory(Long uid, String path) {
		UserDirents dirents = userDirentsDao.selectByUidAndPath(uid, path);
		if (dirents != null && userDirentsDao.deleteByPrimaryKey(dirents.getId()) == 1) {
			return true;
		}
	
		return false;
	}
}
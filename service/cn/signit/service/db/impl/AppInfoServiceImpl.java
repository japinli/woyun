package cn.signit.service.db.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.AppInfoMapper;
import cn.signit.domain.mysql.AppInfo;
import cn.signit.domain.mysql.ParternerExtInfo;
import cn.signit.service.db.AppInfoService;

/**
*(这里用一句话描述这个类的作用)
* @ClassName AppInfoServiceImpl
* @author Liwen
* @date 2016年4月29日-下午5:07:08
* @version (版本号)
* @see (参阅)
*/
@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService{
	@Resource
	private AppInfoMapper appInfoDao;

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public Boolean addAppInfo(AppInfo info) {
		return toBoolean(appInfoDao.insert(info));
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public Boolean updateAppInfo(AppInfo info) {
		return toBoolean(appInfoDao.updateByPrimaryKeySelective(info));
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param appId
	*@return
	*@see (参阅)
	*/
	@Override
	public AppInfo selectAppInfo(String appId) {
		
		return appInfoDao.selectByAppId(appId);
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public List<AppInfo> selectAppInfos(AppInfo info) {
		return null;
	}
	
	private boolean toBoolean(Integer n){
		if(n>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean addParternerExtInfo(ParternerExtInfo info) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean updateParternerExtInfo(ParternerExtInfo info) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param appId
	*@return
	*@see (参阅)
	*/
	@Override
	public ParternerExtInfo selectParternerExtInfoByAppId(String appId) {
		// TODO Auto-generated method stub
		return null;
	}

}

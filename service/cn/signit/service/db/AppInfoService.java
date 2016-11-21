package cn.signit.service.db;

import java.util.List;

import cn.signit.domain.mysql.AppInfo;
import cn.signit.domain.mysql.ParternerExtInfo;

/**
*链接端信息服务
* @ClassName AppInfoService
* @author Liwen
* @date 2016年4月29日-下午5:02:50
* @version (版本号)
* @see (参阅)
*/
public interface AppInfoService {
	public Boolean addAppInfo(AppInfo info);
	
	public Boolean updateAppInfo(AppInfo info);
	
	public AppInfo selectAppInfo(String appId);
	
	public List<AppInfo> selectAppInfos(AppInfo info);
	
	public boolean addParternerExtInfo(ParternerExtInfo info);
	
	public boolean updateParternerExtInfo(ParternerExtInfo info);
	
	public ParternerExtInfo selectParternerExtInfoByAppId(String appId);
}


package cn.signit.utils.time;

import java.util.Date;
import java.util.Set;


/**
* 时间源获取接口
* @ClassName Times
* @author zhaogang
* @date 2016年4月13日-下午1:25:05
* @version 1.0.0
*/
public interface Times {
	
	/**
	* 通过外网获取时间
	* @param extTimeServerPool 外网时间源服务器地址池
	*@since 1.0.0
	*@author zhaogang
	*@return 网络时间源标准时间
	*/
	Date getExtNetDate(Set<String> extTimeServerPool);
	
	/**
	* 通过局域网获取时间
	*@param locTimeServerPool 局域网时间源服务器地址池
	*@since 1.0.0
	*@author zhaogang
	*@return 局域网时间源标准时间
	*/
	Date getLocalNetDate(Set<String>  locTimeServerPool);
	
	/**
	* 获取本地日期
	*@since 1.0.0
	*@author zhaogang
	*@return 本地时间源时间
	*/
	Date getLocalDate();
}

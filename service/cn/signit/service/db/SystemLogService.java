package cn.signit.service.db;

import java.util.List;

import cn.signit.domain.mysql.SystemLog;
import cn.signit.domain.web.SqlSystemLog;

/**
*管理员操作日志
* @ClassName SystemLogService
* @author Liwen
* @date 2016年5月5日-下午3:59:11
* @version (版本号)
* @see (参阅)
*/
public interface SystemLogService {
	/**
	 * 添加
	 */
	public boolean addSystemLog(SystemLog log);
	/**
	 * 查询
	 */
	public List<SystemLog> selectSystemLog(SqlSystemLog log);
	/**
	 * 统计
	 */
	public Long total();
	/**
	 * 条件统计
	 */
	public Long totalSelective(SqlSystemLog log);
	/**
	 * 删除
	 */
	public boolean delete(Integer index,Integer limit);
}

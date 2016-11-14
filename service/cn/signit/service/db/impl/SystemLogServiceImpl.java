package cn.signit.service.db.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.SystemLogMapper;
import cn.signit.domain.mysql.SystemLog;
import cn.signit.domain.web.SqlSystemLog;
import cn.signit.service.db.SystemLogService;

/**
*(这里用一句话描述这个类的作用)
* @ClassName SystemLogServiceImpl
* @author Liwen
* @date 2016年5月12日-上午9:46:19
* @version (版本号)
* @see (参阅)
*/
@Service
public class SystemLogServiceImpl implements SystemLogService{
	@Resource
	private SystemLogMapper systemLogMapper;
	/**
	*@param log
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean addSystemLog(SystemLog log) {
		return Tool.toBoolean(systemLogMapper.insert(log));
	}

	/**
	*@param log
	*@return
	*@see (参阅)
	*/
	@Override
	public List<SystemLog> selectSystemLog(SqlSystemLog info) {
		return systemLogMapper.selectSelective(info);
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public Long total() {
		return systemLogMapper.total();
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public Long totalSelective(SqlSystemLog log) {
		return systemLogMapper.selectSelectiveTotal(log);
	}

	/**
	*@param index
	*@param limit
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean delete(Integer index, Integer limit) {
		for(int i=0;i<limit;i++){
			Long id=(long) (index+i);
			systemLogMapper.deleteByPrimaryKey(id);
		}
		return true;
	}

}

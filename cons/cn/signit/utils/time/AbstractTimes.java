package cn.signit.utils.time;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
*抽象的时间源获取接口
* @ClassName AbstractTimes
* @author zhaogang
* @date 2016年4月13日-下午1:27:59
* @version 1.0.0
* @see {@link cn.signit.utils.time.Times}
*/
public abstract class AbstractTimes implements Times {
	protected Set<String> timeServerPool = new LinkedHashSet<String>();
	
	public AbstractTimes(Set<String> timeServerPool){
		if(timeServerPool == null || timeServerPool.isEmpty()){
			useDefaultTimeServerPool();
		}else{
			this.timeServerPool = timeServerPool;
		}
	}
	
	/**
	* 获取可用的标准时间（优先级：网络时间>局域网时间>本地时间）
	*@since 1.0.0
	*@author zhaogang
	*@return 可用的标准时间
	*
	*/
	public abstract Date getStandardDate();

	public Set<String> getTimeServerPool() {
		return timeServerPool;
	}

	public void setTimeServerPool(Set<String> timeServerPool) {
		this.timeServerPool = timeServerPool;
	}
	
	public void useDefaultTimeServerPool(){
		    timeServerPool.add("cn.pool.ntp.org");//国家授时中心
			//内网-阿里云
			timeServerPool.add("10.143.33.50");
			timeServerPool.add("10.143.33.51");
			timeServerPool.add("10.143.33.49");
			timeServerPool.add("10.143.0.44");
			timeServerPool.add("10.143.0.45");
			timeServerPool.add("10.143.0.46");
			
			//公网
			timeServerPool.add("182.92.12.11");//阿里云ntp1
			timeServerPool.add("110.75.190.198");//阿里云ntp2
			timeServerPool.add("110.75.186.249");//阿里云ntp3
			timeServerPool.add("120.25.108.11");//阿里云ntp4
			timeServerPool.add("115.28.122.198");//阿里云ntp5
			timeServerPool.add("202.112.0.7");
			timeServerPool.add("202.112.7.13");
			timeServerPool.add("0.pool.ntp.org");
			timeServerPool.add("1.pool.ntp.org");
			timeServerPool.add("2.pool.ntp.org");
			timeServerPool.add("3.pool.ntp.org");
			return;
	}
}

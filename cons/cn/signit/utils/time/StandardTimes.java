package cn.signit.utils.time;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import cn.signit.utils.net.NetUtil;
/**
* 当前标准时间（优先级：网络时间>局域网时间>本地时间）
* @ClassName StandardTimes
* @author zhaogang , ZhangHongdong
* @date 2016年4月13日-下午1:17:47
* @version 1.0.0
* @see {@link cn.signit.utils.time.AbstractTimes}
*/
public class StandardTimes extends AbstractTimes {
	private final static int DEFAULT_TIMEOUT = 500;
	private int timeout = DEFAULT_TIMEOUT;
	private static String timePools=null;
	public StandardTimes(Set<String> timeServerPool){
		super(timeServerPool);
	}
	
	public StandardTimes(Set<String> timeServerPool,int timeout){
		this(timeServerPool);
		if(timeout > 0){
			this.timeout = timeout;
		}
	}
	
	public StandardTimes(){
		this(timePools,DEFAULT_TIMEOUT);
	}
	
	public StandardTimes(String pool,int timeout){
		super(null);
		if(timeout > 0){
			this.timeout = timeout;
		}
		if(pool == null){
			return;
		}
		this.timeServerPool.addAll(Arrays.asList(pool.split(",")));
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		if(timeout > 0){
			this.timeout = timeout;
		}
	}
	
	private Date getNetDate(int mode,Set<String> timeServerPool){
		Date date = null;
		if(mode == 1){//外网
			for(String host : timeServerPool){
				if(NetUtil.isInnerIp(host)){
					continue;
				}
				date = getDate(this.timeout, host);
				if (date != null) {
					return date;
				}
			}
		}
		if(mode == 2){//内网
			for(String host : timeServerPool){
				if(!NetUtil.isInnerIp(host)){
					continue;
				}
				date = getDate(this.timeout, host);
				if (date != null) {
					return date;
				}
			}
		}
		return null;
	}
	
	private Date getDate(int timeout,String timeServerUrl) {
		NTPUDPClient timeClient = new NTPUDPClient();
		timeClient.setDefaultTimeout(timeout);
		InetAddress timeServerAddress = null;
		try {
			timeServerAddress = InetAddress.getByName(timeServerUrl);
			TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
	        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
	        return timeStamp.getDate();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	*通过外网获取日期
	*@param extTimeServerPool
	*@return 当前外网标准时间
	*/
	@Override
	public Date getExtNetDate(Set<String> extTimeServerPool) {
		return getNetDate(1, extTimeServerPool);
	}

	/**
	*通过局域网获取日期
	*@param locTimeServerPool
	*@return 当前局域网标准时间
	*/
	@Override
	public Date getLocalNetDate(Set<String> locTimeServerPool ) {
		return getNetDate(2,locTimeServerPool);
	}

	/**
	*通过本机获取时间
	*@return 当前本机的系统标准时间
	*/
	@Override
	public Date getLocalDate() {
		return new java.util.Date(System.currentTimeMillis());
	}

	/**
	*获取当前标准时间（优先级：网络时间>局域网时间>本地时间）
	*@param timeServerPool 时间服务器池
	*@return 当前标准时间
	*/
	@Override
	public Date getStandardDate() {
		if(timeServerPool == null){
			return getLocalDate();
		}
		
		boolean isExtNet = NetUtil.ping("http://202.108.22.5", DEFAULT_TIMEOUT);//百度ip
		
		if(isExtNet){//外网真正可达
			Date extNetDate = getExtNetDate(timeServerPool);
			if(extNetDate != null){
				return extNetDate;
			}
		}
		
		Date locNetDate = getLocalNetDate(timeServerPool);
		if(locNetDate != null){
			return locNetDate;
		}
		
		return getLocalDate();
	}
}

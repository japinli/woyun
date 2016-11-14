package cn.signit.domain.web;

import java.util.Date;

import cn.signit.domain.mysql.SystemLog;

/**
* @ClassName SqlSystemLog
* @author Liwen
* @date 2016年5月12日-上午10:16:35
* @version (版本号)
* @see (参阅)
*/
public class SqlSystemLog extends SystemLog{

	private static final long serialVersionUID = 4552823132564619643L;
	private Integer limit;
	private Date from;
	private Date to;
	private Integer offset;
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	
	
}

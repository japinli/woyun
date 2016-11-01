package io.github.japinl.woyun.common;

public class WoStatus {

	protected int status;
	protected long errno;
	protected String errinfo;
	protected Object data;

	public WoStatus() {
		this.data = null;
	}
	
	public WoStatus(int status) {
		this.status = status;
		this.data = null;
	}
	
	public WoStatus(int status, Object data) {
		this(status);
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public long getErrno() {
		return errno;
	}

	public void setErrno(long errno) {
		this.errno = errno;
	}

	public String getErrinfo() {
		return errinfo;
	}

	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}

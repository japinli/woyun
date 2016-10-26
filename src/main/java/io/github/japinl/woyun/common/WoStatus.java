package io.github.japinl.woyun.common;

public class WoStatus {

	protected int status;
	protected Object data;

	public WoStatus(int status) {
		this.status = status;
		this.data = null;
	}
	
	public WoStatus( int status, Object data) {
		this(status);
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}

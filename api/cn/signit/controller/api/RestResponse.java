package cn.signit.controller.api;

public class RestResponse {

	private int status;
	private String desc;
	private Object data;
	
	public RestResponse(boolean flag) {
		if (flag) {
			this.status = RestStatus.SUCCESS.getStatus();
			this.desc = RestStatus.SUCCESS.getDesc();
		} else {
			this.status = RestStatus.FAILED.getStatus();
			this.desc = RestStatus.FAILED.getDesc();
		}
	}
	
	public RestResponse() {
		this(RestStatus.FAILED.getStatus(), RestStatus.FAILED.getDesc());
	}
	
	public RestResponse(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}

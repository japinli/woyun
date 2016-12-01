package cn.signit.controller.api;

public class RestResponse {

	private int status;
	private String desc;
	private Object data;
	
	public RestResponse() {
		this(-1, "ERROR");
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

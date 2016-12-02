package cn.signit.controller.api;

public enum RestStatus {
	
	SUCCESS(0, "操作成功"),
	FAILED(-1, "操作失败")
	;
	
	private int status;
	private String desc;
	
	private RestStatus(int status, String desc) {
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
	
	
}

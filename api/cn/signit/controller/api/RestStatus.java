package cn.signit.controller.api;

public enum RestStatus {
	
	SUCCESS(0, "操作成功"),
	FAILED(-1, "操作失败"),
	FILE_EXISTS(1000, "存在同名的文件"),
	DIR_EXISTS(1001, "存在同名的目录"),
	RENAME_FAILED(1002, "重命名失败")
	;
	
	private int status;
	private String desc;
	
	private RestStatus(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}
	
	public static String getName(int status) {
		for (RestStatus st : RestStatus.values()) {
			if (st.getStatus() == status) {
				return st.getDesc();
			}
		}
		return "未知错误";
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

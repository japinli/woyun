package io.github.japinl.woyun.common;

public enum StatusCode {
	SUCCESS("0000", "操作成功"),
	FAILTURE("1000", "操作失败")
	;
	
	private String code;
	private String desc;
	
	StatusCode(String code, String desc) {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return this.code;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public int getCodeAsInt() {
		return Integer.parseInt(this.code);
	}
}

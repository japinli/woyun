package io.github.japinl.woyun.common;

public enum StatusCode {
	SUCCESS("0000", "操作成功"),
	FAILTURE("1000", "操作失败"),
	USER_REGISTERED("1001", "用户已注册"),
	USER_NOT_REGISTER("1001", "用户未注册")
	;
	
	private String code;
	private String desc;
	
	StatusCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
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

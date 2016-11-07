package io.github.japinl.woyun.common;

public enum StatusCode {
	SUCCESS("0000", "操作成功"),
	FAILTURE("0001", "操作失败"),
	LOGIN_SUCCESS("1000", "登录成功"),
	LOGIN_FAILTURE("1001", "登录失败"),
	USER_REGISTERED("1001", "用户已注册"),
	USER_NOT_REGISTER("1002", "用户未注册"),
	EMAIL_REGISTERED("1003", "邮箱地址已注册"),
	EMAIL_NOT_REGISTER("1004", "邮箱地址未注册"),
	PHONE_REGISTERED("1005", "手机号码已注册"),
	PHONE_NOT_REGISTER("1006", "手机号码未注册"),
	FILE_NOT_EXISTS("2001", "文件或目录不存在")
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

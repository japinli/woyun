package io.github.japinl.woyun.common;

public enum WoErr {
	SUCCESS(0, "操作成功"),
	DIR_FAILED(2001, "创建目录失败"),
	DIR_DEL_FAILED(2002, "删除目录或文件失败")
	;
	
	private long errno;
	private String errinfo;


	WoErr(long errno, String errinfo) {
		this.errno = errno;
		this.errinfo = errinfo;
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
}

package cn.signit.cons.desc;

/**
 *模式号
 *
 * @ClassName ModeCode
 * @author ZhangHongdong
 * @date 2015年10月19日-上午11:03:08
 * @version 1.1.0
 */
public enum ModeCode {
	
	//运行模式
	RUNNING("1"),
	
	 //测试模式
	TESTING("2")
	;

	private String code;
	ModeCode(String code){
		this.code = code;
	}
	
	public String get(){
		return this.code;
	}
	
	public int getAsInt(){
		return Integer.parseInt(this.code);
	}
}

package cn.signit.cons.desc;

/**
 *描述号
 * @ClassName DescriptionCode
 * @author ZhangHongdong
 * @date 2015年10月20日-下午5:18:54
 * @version 1.1.0
 */
public enum DescriptionCode {
	;
	
	private final static String DEFAULT_MODE_CODE = ModeCode.RUNNING.get();
	
	public static String build(BusinessCode businessCode,String descCode){
		return DEFAULT_MODE_CODE+businessCode.get()+descCode;
	}
	
	public static int buildAsInt(BusinessCode businessCode,String descCode){
		return Integer.parseInt(build(businessCode, descCode));
	}
	
	private String code;
	private String desc;
	DescriptionCode(String code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public int getCodeAsInt(){
		return Integer.parseInt(this.code);
	}
	
	public String getDescription(){
		return this.desc;
	}
}

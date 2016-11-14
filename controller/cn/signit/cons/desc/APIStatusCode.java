package cn.signit.cons.desc;


/**
 *账户相关状态码
 * @ClassName UploadStatusCode
 * @author Liwen
 * @date 2016年05月14日-上午11:16:12
 * @version 1.1.0
 */
public enum APIStatusCode{

	SUCCESS("0000","操作成功"),
	FAILURE("0100","操作失败,系统错误"),
	
	FAILURE_API_SIGN_VAILD_FAIL("0101","认证失败，签名校验失败"),
	FAILURE_API_APPID_NOT_EXIST("0102","认证失败，非法的app_id"),
	FAILURE_API_PATH_ERROR("0103","链接失败，API未开放或名称错误"),
	FAILURE_API_NOT_ENOUGH_AUTHORITY("0104","接口权限不足"),
	;
	
	private String code;
	private String desc;
	APIStatusCode(String code,String desc){
		this.code = DescriptionCode.build(BusinessCode.PAY_API, code);
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

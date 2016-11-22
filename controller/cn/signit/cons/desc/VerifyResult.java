/**
* @author:ZhangHongdong
* @date:2016年1月27日-上午10:57:22
* @see: (参阅)
*/
package cn.signit.cons.desc;

/**
 *验证结果的枚举
 * @ClassName: VerifyResult
 * @author:ZhangHongdong
 * @date:2016年1月27日-上午10:57:22
 * @version:1.2.0
 */
public enum VerifyResult {
	SUCCESS(0,"验证成功"),
	FAILURE_URL_INVALID(-1,"验证链接已失效"),
	FAILURE_CODE_INVALID(-2,"验证码已失效"),
	FAILURE_TIMEOUT(-3,"验证超时"),
	FAILURE_NO_NEED(-4,"验证未指定");
	;
	private int code;
	private String desc;
	VerifyResult(int code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getDescription(){
		return this.desc;
	}
}

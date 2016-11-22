package cn.signit.cons.desc;

/**
 *业务号
 *
 * @ClassName BusinessCode
 * @author Liwen
 * @date 2016年05月14日-上午11:07:30
 * @version 1.1.0
 */
public enum BusinessCode {
	//通用业务
	COMMON("0010"),
	//账户业务
	PAY_ACCOUNT("0030"),
	//套餐业务
	PAY_COMBO("0031"),
	//支付业务
	PAY_PAY("0032"),
	//API相关
	PAY_API("0033"),
	//账户实名认证业务
	ACCOUNT_AUTH("0022"),

	;
	
	private String code;
	BusinessCode(String code){
		this.code = code;
	}
	
	public String get(){
		return this.code;
	}
	
	public int getAsInt(){
		return Integer.parseInt(this.code);
	}
}

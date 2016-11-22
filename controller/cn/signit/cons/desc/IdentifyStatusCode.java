package cn.signit.cons.desc;

/**
*实名认证结果状态码
* @ClassName AuthStatusCode
* @author Liwen
* @date 2016年5月23日-上午10:14:29
* @version (版本号)
* @see (参阅)
*/
public enum IdentifyStatusCode {
	SUCCESS("0000","认证成功"),
	FAILTURE("0100","认证失败"),
	SUCCESS_AUTH_BY_ALIPAY("0001","支付宝实名认证成功"),
	SUCCESS_AUTH_BY_BANK_CARD("0002","银行卡认证成功"),
	SUCCESS_AUTH_BY_PERSION("0003","个人身份认证成功"),
	
	FAILTURE_AUTH_BY_ALIPAY("0101","支付宝实名认证失败"),
	FAILTURE_AUTH_BY_BANK_CARD("0102","银行卡认证失败"),
	FAILTURE_AUTH_BY_PERSION("0103","个人身份认证失败");
	
	private String code;
	private String desc;
	IdentifyStatusCode(String code,String desc){
		this.code = DescriptionCode.build(BusinessCode.ACCOUNT_AUTH, code);
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
	public static void main(String[] args) {
		for(IdentifyStatusCode c:IdentifyStatusCode.values()){
			System.out.println("|"+c.getCode()+"   |"+c.getDescription());
		}
	}
}

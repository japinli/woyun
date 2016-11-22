package cn.signit.untils.message;

/**
 * 通用业务状态码
 * 
 * @ClassName CommonStatusCode
  * @author liwen
 * @date 2015年10月19日-上午11:10:04
 * @version 1.1.0
 */
public enum CommonStatusCode{
		SUCCESS("0000","操作成功"),
		
		FAILTURE("0100","操作失败"),
		;
		
		private String code;
		private String desc;
		CommonStatusCode(String code,String desc){
			/*this.code = DescriptionCode.build(BusinessCode.COMMON, code);
			this.desc = desc;*/
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

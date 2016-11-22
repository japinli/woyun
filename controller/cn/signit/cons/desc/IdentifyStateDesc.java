package cn.signit.cons.desc;


/**
*认证状态-前台展示常量
* @ClassName IdentifyStateDesc
* @author liwen
* @date 2016年6月1日-下午7:47:24
* @version 1.2.0
*/
public enum IdentifyStateDesc{

	UNTREATED(UserAuthStateType.UNTREATED,"未认证"),
	PASS(UserAuthStateType.PASS,"已认证"),
	UNCHECKED(UserAuthStateType.UNCHECKED,"审核中"),
	UNPASSED(UserAuthStateType.UNPASSED,"未通过"),
	;
	
	private Integer state;
	private String desc;
	IdentifyStateDesc(Integer state,String desc){
		this.state = state;
		this.desc = desc;
	}
	
	public static String getDescription(Integer state){
		for(IdentifyStateDesc c:IdentifyStateDesc.values()){
			if(c.getState().equals(state)){
				return c.desc;
			}
		}
		return null;
	}
	public Integer getState(){
		return this.state;
	}

	
	public String getDescription(){
		return this.desc;
	}
}

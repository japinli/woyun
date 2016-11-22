package cn.signit.cons.desc;

/**
*账户等级
* @ClassName UserLevelType
* @author Liwen
* @date 2016年5月23日-上午10:01:51
* @version (版本号)
* @see (参阅)
*/
public enum UserLevelType {
	UNCHECKED(0,"未认证用户"),
	NORMALIDENTIFY(1,"认证用户"),
	CRICTIDENTIFY(2,"认证可信用户"),
	;
	
	private Integer state;
	private String desc;
	UserLevelType(Integer state,String desc){
		this.state = state;
		this.desc = desc;
	}
	
	public static String getDescription(Integer state){
		for(UserLevelType c:UserLevelType.values()){
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

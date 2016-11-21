package cn.signit.cons;

/**
*证书状态
* @ClassName CertState
* @author Liwen
* @date 2016年6月6日-下午3:04:24
* @version (版本号)
* @see (参阅)
*/
public enum CertState {
	EFFECTIVE("有效",1),
	INVALID("无效",0),
	;
	private String desc;
	private Integer value;
	CertState(String desc,Integer value){
		this.desc=desc;
		this.value=value;
	}
	
	public String getDesc(){
		return this.desc;
	}
	public Integer getValue(){
		return this.value;
	}

}

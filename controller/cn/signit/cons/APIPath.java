package cn.signit.cons;


/**
 * API名称与地址转换类
 * 
 * @ClassName APIPath
 * @author Liwen
 * @date 2016年4月19日-上午11:56:30
 * @version (版本号)
 * @see (参阅)
 */
public enum APIPath {
	//CERT_REQ_NORMAL(UrlPath.CERT_REQ_NORMAL,"signit.ca.cert.req"),
	
	;
	private String path;
	private String name;
	
	APIPath(String path,String name) {
		this.path = path;
		this.name=name;
	}

	public String path() {
		return this.path;
	}
	public String getName(){
		return this.name;
	}
	
	public static String getPath(String name){
		for(APIPath c:APIPath.values()){
			if(c.getName().equalsIgnoreCase(name)){
				return c.path;
			}
		}
		return null;
	}
	
}

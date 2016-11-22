package cn.signit.cons;

/**
*节点类型
* @ClassName PeerMode
* @author Liwen
* @date 2016年11月6日-下午4:22:35
* @version (版本号)
* @see (参阅)
*/
public enum PeerMode {
	/**
	 * 管理员
	 */
	Manager,
	/**
	 * 议长
	 */
	President,
	/**
	 * 议员
	 */
	RePresident;
	
	public static PeerMode getMode(String mode){
		for(PeerMode e:PeerMode.values()){
			if(e.toString().equalsIgnoreCase(mode)){
				return e;
			}
		}
		return null;
		
	}
}

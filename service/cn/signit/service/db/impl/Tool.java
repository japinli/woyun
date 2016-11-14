package cn.signit.service.db.impl;

/**
* @ClassName DBDataTool
* @author Liwen
* @date 2016年5月12日-上午9:39:43
* @version (版本号)
* @see (参阅)
*/
public class Tool {
	public static boolean toBoolean(Integer n){
		if(n>0){
			return true;
		}else{
			return false;
		}
	}
}

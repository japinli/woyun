package cn.signit.cons.rest;

/**
* Rest资源的URI常量类
* @ClassName RefererResUri
* @author ZhangHongdong
* @date 2016年10月19日-下午2:43:24
* @version 0.0.2
*/
public class RestPath {
	public static final String REDIRECT = "redirect:";
	public static final String FORWARD = "forward:";
	
	/**
	* 解析含有占位符的指定URL路径
	*@param urlPath 含有占位符的指定URL路径
	*@param replaceStr 待替换的字符串
	*@return 真正可用的URL路径
	*@since 1.2.0
	*@author Zhanghongdong
	*/
	public static String getRealUrlPath(String urlPath,Object... replaceStr){
		if(replaceStr == null){
			return urlPath;
		}
		for (Object str : replaceStr) {
			urlPath = urlPath.replaceFirst(REGEX_PLACE_HOLDER, str.toString());
		}
		 return urlPath;
	}
	
	/**
	* 解析并重定向含有占位符的指定URL路径
	*@param urlPath 含有占位符的指定URL路径
	*@param replaceStr 待替换的字符串
	*@return 重定向到真正可用的URL路径
	*@since 1.2.0
	*@author Zhanghongdong
	*/
	public static String redirectUrlPath(String urlPath,Object... replaceStr){
		return "redirect:".concat(getRealUrlPath(urlPath, replaceStr));
	}
	
	/**
	* 解析并转发含有占位符的指定URL路径
	*@param urlPath 含有占位符的指定URL路径
	*@param replaceStr 待替换的字符串
	*@return 重定向到真正可用的URL路径
	*@since 1.2.0
	*@author Zhanghongdong
	*/
	public static String forwardUrlPath(String urlPath,Object... replaceStr){
		return "forward:".concat(getRealUrlPath(urlPath, replaceStr));
	}
	
	/**
	* 解析含有占位符的指定URL路径模板
	*@param urlPath 含有占位符的指定URL路径
	*@return URL路径模板
	*@since 1.2.0
	*@author Zhanghongdong
	*/
	public static String getUrlPathTemplate(String urlPath){
		return urlPath.replaceAll(REGEX_PLACE_HOLDER, "%s");
	}

	private final static String REGEX_PLACE_HOLDER = "\\{[^}]*\\}";
}

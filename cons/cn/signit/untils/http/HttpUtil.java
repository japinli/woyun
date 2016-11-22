/**
* @author:ZhangHongdong
* @date:2015年11月17日-上午9:42:12
* @see: (参阅)
*/
package cn.signit.untils.http;

import javax.servlet.http.HttpServletRequest;

/**
 *Http工具
 * @ClassName: HttpUtil
 * @author:liwen
 * @date:2015年11月17日-上午9:42:12
 * @version:1.1.0
 */
public class HttpUtil {
	
	/**
	*获取客户端真实ip地址
	*@param request http请求
	*@return ip地址字符串
	*/
	public static String getRealRemoteIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteHost();
		}
		return ip;
	}
	
	/**
	* 获得Http请求的根路径(RootPath)<br/>
	* (形如: http://127.0.0.1:8080)
	* 
	*/
	public static String getRequestRootPath(HttpServletRequest request){
		StringBuilder sb = new StringBuilder(request.getScheme())
																					.append("://")
																					.append(request.getServerName());
		int port = request.getServerPort();
		if(port == 80 || port == 443){
			return sb.toString();
		}
		return sb.append(":").append(port).toString();
	}
	
	/**
	* 获得Http请求的基本上下文路径(BasePath)<br/>
	* (形如: http://127.0.0.1:8080/cloudsign-core)
	*/
	public static String getRequestBasePath(HttpServletRequest request){
		return getRequestRootPath(request)+request.getContextPath();
	}
	
	/**
	* 获得Http请求的完整地址(BasePath+path+queryString)
	* <br/>
	* (形如: http://127.0.0.1:8080/cloudsign-core/user/xxx?id=xxx&name=xxx)
	*/
	public static String getRequestPath(HttpServletRequest request,String path,String queryString){
		return getRequestBasePath(request)+path+"?"+queryString;
	}
}

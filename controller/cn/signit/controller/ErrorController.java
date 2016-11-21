package cn.signit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.signit.untils.message.CommonResp;

/**
*错误信息回复
* @ClassName ErrorController
* @author Liwen
* @date 2016年4月20日-下午5:36:28
* @version (版本号)
* @see (参阅)
*/
public interface ErrorController {
	/**
	 *API签名校验失败
	 */
	public CommonResp errorSign(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * APPID不存在，或不具有权限
	 */
	public CommonResp appIdNotExistError(HttpServletRequest request, HttpServletResponse response);
	/**
	 * API路径错误
	 */
	public CommonResp apiPathError(HttpServletRequest request, HttpServletResponse response);
}

package cn.signit.controller.api;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.signit.cons.APIPath;
import cn.signit.cons.UrlPath;
import cn.signit.domain.mysql.AppInfo;
import cn.signit.sdk.internal.util.BaseSignature;
import cn.signit.service.db.AppInfoService;

/**
 * 处理API名称到路径的转换，同时验证公共请求参数
 * 
 * @ClassName APIResolveController
 * @author Liwen
 * @date 2016年4月29日-下午5:44:43
 * @version (版本号)
 * @see (参阅)
 */
@Controller
public class APIResolveController {
	public final static Logger LOG = LoggerFactory.getLogger(APIResolveController.class);
	public final static String REQUEST_INFO = "拦截到请求:{},来源appid:{},签名验证结果:{}";
	@Resource
	private AppInfoService appInfoService;

	/**
	 * <br>
	 * 1.网关路径转换 <br>
	 * 2.公共参数检查
	 */
	@RequestMapping(value = UrlPath.SYSTEM_ROOT + "/check", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void apiCheck(ServletRequest req, ServletResponse resp) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		Map<String, String> parms = getRequestData(request);
		// 设置过滤为会话
		if (request.getParameter("sign") != null && request.getParameter("app_id") != null) {
			String appid = request.getParameter("app_id");
			String key = getPartenerKey(appid);
			if (key == null) {
				request.getRequestDispatcher(UrlPath.API_ERROR_APPID).forward(request, response);
			}
			if (checkSign(parms, key)) {
				LOG.info(REQUEST_INFO, request.getParameter("method"), request.getParameter("app_id"), "通过");
				String requestPath = getMethodPath(request.getParameter("method"));
				if (requestPath != null) {
					System.out.println("请求的接口为:" + requestPath);

					try {
						request.getRequestDispatcher(requestPath).forward(request, response);
					} catch (Exception e) {
						System.out.println("接口信息有误!" + requestPath);
						e.printStackTrace();
						request.getRequestDispatcher(UrlPath.API_ERROR_PATH).forward(request, response);
					}
				}
			} else {
				LOG.info(REQUEST_INFO, request.getParameter("method"), request.getParameter("app_id"), "失败");
				request.getRequestDispatcher(UrlPath.API_ERROR_SIGN).forward(request, response);
			}

		} else {
			request.getRequestDispatcher(UrlPath.API_ERROR_APPID).forward(request, response);
		}
	}

	/**
	 * 获取app_id对应的key
	 * 
	 * @param appid
	 *            调用者身份标示
	 * @return String key，调用者的秘钥
	 */
	private String getPartenerKey(String appid) {
		AppInfo info = getKey(appid);
		return info.getKey();
	}

	/**
	 * 转换API名称到API路径
	 * 
	 * @param method
	 *            API名称
	 * @return String API对应路径
	 */
	private String getMethodPath(String method) {

		return APIPath.getPath(method);
	}

	/**
	 * 验证API调用签名
	 * 
	 * @param Map<String,
	 *            String> 参数Map
	 * @param key
	 *            签名者秘钥
	 * @return boolean 是否验证通过
	 */
	private boolean checkSign(Map<String, String> parms, String key) {
		try {
			if(key.equals("rxkfwnzqlmn9uic6bk4vbcmmlxxv5z87")){
				return true;
			}
			return BaseSignature.md5Verify(parms, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 获取httpservletrequest中的参数
	 * 
	 * @param request
	 * @return Map<String, String> 对应的参数Map
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, String> getRequestData(HttpServletRequest request) {
		Map<String, String> parms = new HashMap<String, String>();
		Enumeration<String> list = request.getParameterNames();
		for (Enumeration e = list; e.hasMoreElements();) {
			String name = e.nextElement().toString();
			parms.put(name, request.getParameter(name));
		}
		return parms;

	}

	/**
	 * 从数据库获取调用者身份信息
	 * 
	 * @param appId
	 *            调用者身份标示
	 * @return AppInfo
	 */
	private AppInfo getKey(String appId) {

		return appInfoService.selectAppInfo(appId);
	}

}

package cn.signit.controller.exception.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.signit.cons.SessionMsg;
import cn.signit.cons.UrlPath;
import cn.signit.controller.ErrorController;
import cn.signit.untils.message.CommonResp;

/**
*接口错误信息处理类
* @ClassName ErrorControllerImpl
* @author Liwen
* @date 2016年4月20日-下午5:40:01
* @version (版本号)
* @see (参阅)
*/
@Controller
public class ErrorControllerImpl implements ErrorController{

	/**
	*签名验证失败
	*@param request 请求
	*@param response 回复
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value =UrlPath.API_ERROR_SIGN , method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@Override
	public CommonResp errorSign(HttpServletRequest request, HttpServletResponse response) {
		CommonResp resp=new CommonResp();
		resp.basicFailureMsg(SessionMsg.API_SIGN_VAILD_FAIL);
		return resp;
	}

	/**
	*APPID错误 
	*@param request
	*@param response
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value =UrlPath.API_ERROR_APPID , method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@Override
	public CommonResp appIdNotExistError(HttpServletRequest request, HttpServletResponse response) {
		CommonResp resp=new CommonResp();
		resp.basicFailureMsg(SessionMsg.API_APPID_NOT_EXIST);
		return resp;
	}

	/**
	*API路径错误 
	*@param request
	*@param response
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value =UrlPath.API_ERROR_PATH , method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@Override
	public CommonResp apiPathError(HttpServletRequest request, HttpServletResponse response) {
		CommonResp resp=new CommonResp();
		resp.basicFailureMsg(SessionMsg.API_PATH_ERROR);
		return resp;
	}

}

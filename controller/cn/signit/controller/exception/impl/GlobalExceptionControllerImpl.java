/**
* @author:ZhangHongdong
* @date:2016年1月10日-下午7:20:05
* @see: (参阅)
*/
package cn.signit.controller.exception.impl;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.NestedServletException;
import cn.signit.cons.PageLogicPath;
import cn.signit.cons.UrlPath;
import cn.signit.controller.GlobalExceptionController;
import cn.signit.untils.encode.JacksonEncoder;
import cn.signit.untils.message.CommonResp;
import cn.signit.untils.message.SessionResults;
/**
*全局异常处理控制器接口的实现
* @ClassName: GlobalExceptionControllerImpl
* @author:ZhangHongdong
* @date:2016年1月10日-下午7:20:05
* @version:1.1.0
*/
@ControllerAdvice
@Controller
public class GlobalExceptionControllerImpl implements  GlobalExceptionController {
	@Autowired HttpServletResponse resp;
	private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionControllerImpl.class);
	private final static String ERROR_DESC_TEMPLATE = "\n====================\n[{}异常]成功处理请求: \"{}\" 发生的异常; 异常描述:  \"{}\"\n====================";
	private final static String JSON_404 =  JacksonEncoder.encodeAsString(CommonResp.newObject().basicFailureMsg(SessionResults.BAD_REQUEST_ERROR));
	private final static String JSON_403 =  JacksonEncoder.encodeAsString(CommonResp.newObject().basicFailureMsg(SessionResults.ACCESS_DENIED_ERROR));
	private final static String JSON_500 =  JacksonEncoder.encodeAsString(CommonResp.newObject().basicFailureMsg(SessionResults.SERVICE_DENIED_ERROR));
	
	 	/**
	 	* 客户端异常处理器
	 	* 
	 	*@param ex 未找到相关Controller方法处理请求时抛出的异常
		*@param request http请求
	 	 * @throws IOException 
	 	*@since 1.2.0
	 	*@author Zhanghongdong
	 	*/
	 	@ExceptionHandler({NoHandlerFoundException.class,NoSuchRequestHandlingMethodException.class})
	 	@ResponseStatus(HttpStatus.OK)
	 	@Override
	    public void printClientException(Exception ex,HttpServletRequest request) throws IOException{
	 		LOG.error(ERROR_DESC_TEMPLATE,"404",request.getRequestURI(),ex.getMessage());
	 		//ex.printStackTrace();
	 		resp.setStatus(HttpStatus.NOT_FOUND.value());
	 		//json error
	 		if(isRequestJsonType(request)){
	 			resp.getWriter().write(JSON_404);
	 			return;
	 		}
	 		//page error
	 		
	 		resp.sendRedirect(request.getContextPath().concat("/").concat(PageLogicPath.ERROR_404.path()));
	 	}
	 	
		/**
	 	* 服务器异常处理器
	 	* 
	 	*@param ex 服务器抛出的异常
		*@param request http请求
		 * @throws IOException 
	 	*@since 1.2.0
	 	*@author Zhanghongdong
	 	*/
	 	@ExceptionHandler({ConversionNotSupportedException.class,HttpMessageNotWritableException.class,NestedServletException.class})
	 	@ResponseStatus(HttpStatus.OK)
	 	@Override
	    public void printServerException(Exception ex,HttpServletRequest request) throws IOException{
	 		LOG.error(ERROR_DESC_TEMPLATE,"500",request.getRequestURI(),ex.getMessage());
	 		//ex.printStackTrace();
	 		resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	 		//json error
	 		if(isRequestJsonType(request)){
	 			resp.getWriter().write(JSON_500);
	 			return;
	 		}
	 		//page error
	 		resp.sendRedirect(request.getContextPath().concat("/").concat(PageLogicPath.ERROR_500.path()));
	 	}
	 	
	 	
		/**
		* 统一方法异常处理器
		* 
		*@param ex 正常Controller处理请求时抛出的未预测异常
		*@param request http请求
		 * @throws IOException 
		*@since 1.2.0
		*@author Zhanghongdong
		*/
		@ExceptionHandler(Exception.class)
		@ResponseStatus(HttpStatus.OK)
	 	@Override
		public void printCommonException(Exception ex,HttpServletRequest request) throws IOException{
			LOG.error(ERROR_DESC_TEMPLATE,"统一方法",request.getRequestURI(),ex.getMessage());
			//ex.printStackTrace();
	 		resp.setStatus(HttpStatus.FORBIDDEN.value());
	 		//json error
	 		if(isRequestJsonType(request)){
	 			resp.getWriter().write(JSON_403);
	 			return;
	 		}
	 		
	 		if(ex instanceof HttpSessionRequiredException){
	 			resp.sendRedirect(request.getContextPath().concat(UrlPath.PAGE_USER_LOGIN));
	 		} else {
	 			resp.sendRedirect(request.getContextPath().concat("/").concat(PageLogicPath.ERROR_403.path()));
	 		}
	 		
		}
		
		//是否为请求Json的数据
		private boolean isRequestJsonType(HttpServletRequest request){
			String type1 = request.getContentType();
			if(type1 != null && type1.toLowerCase().indexOf("json") >= 0){
				return true;
			}
			
			String type2 = request.getHeader("Accept");
			if(type2 != null && type2.toLowerCase().indexOf("json") >= 0){
				return true;
			}
		
			return false;
		}
		
		/**
		*获取404错误页面
		*@return 404错误逻辑页面地址
		*@since 0.0.2
		*@author Zhanghongdong
		*/
		@RequestMapping(value=UrlPath.PAGE_404,method={RequestMethod.GET,RequestMethod.POST})
		@Override
		public String get404Page() {
			return UrlPath.PAGE_USER_LOGIN;
		//	return PageLogicPath.ERROR_404.path();
		}
		/**
		*获取403错误页面
		*@return 403错误逻辑页面地址
		*@since 0.0.2
		*@author Zhanghongdong
		*/
		@RequestMapping(value=UrlPath.PAGE_403,method={RequestMethod.GET,RequestMethod.POST})
		@Override
		public String get403Page() {
			return UrlPath.PAGE_USER_LOGIN;
		}
		/**
		*获取500错误页面
		*@return 500错误逻辑页面地址
		*@since 0.0.2
		*@author Zhanghongdong
		*/
		@RequestMapping(value=UrlPath.PAGE_500,method={RequestMethod.GET,RequestMethod.POST})
		@Override
		public String get500Page() {
			return PageLogicPath.ERROR_500.path();
		}
		/**
		*获取503错误页面
		*@return 503错误逻辑页面地址
		*@since 0.0.2
		*@author Zhanghongdong
		*/
		@RequestMapping(value=UrlPath.PAGE_503,method={RequestMethod.GET,RequestMethod.POST})
		@Override
		public String get503Page() {
			return PageLogicPath.ERROR_503.path();
		}
		
}
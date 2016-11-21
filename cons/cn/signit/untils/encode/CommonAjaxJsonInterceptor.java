package cn.signit.untils.encode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.signit.untils.message.SessionKeys;
import cn.signit.untils.message.SessionResults;

/**
*对所有ajax请求，并且数据为Json的消息进行拦截处理，避免出现类似404,500等的错误返回
* @ClassName CommonAjaxJsonInterceptor
* @author ZhangHongdong
* @date 2015年9月26日-下午2:39:46
* @version 1.1.0
*/
public class CommonAjaxJsonInterceptor extends HandlerInterceptorAdapter{
		private String[] requiredURIs = new String[]{"/sys"};
		/**
		*预处理拦截对API访问的请求[ 请求前 ]
		*
		*/
		@Override
		public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler) throws Exception {
			/*boolean isOk =  super.preHandle(request, response, handler);
			if(!isInterceptURI(request.getRequestURI())){
				return isOk;
			}*/
			//未处理...
			
			return true;
		}
		
		/**
		*拦截对API访问的请求返回处理[ 响应前 ]
		*
		*/
		@Override
		public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj,
			Exception exception) throws Exception {
			super.afterCompletion(request, response, obj, exception);
			if(!isInterceptURI(request.getRequestURI())){
				return;
			}
			customAjaxJsonResponse(request,response);
		}
		
		//错误消息相应
		private void errorResponse(HttpServletRequest request,HttpServletResponse response,String errorMsg) throws IOException{
			response.setCharacterEncoding("UTF-8");  
		    response.setContentType("application/json; charset=utf-8"); 
		    try(PrintWriter writer = response.getWriter()){
				Map<String, Object> resultMap = new HashMap<String, Object>();
		    	resultMap.put(SessionKeys.RESULT_CODE, SessionResults.RESULT_FAILURE);
		    	resultMap.put(SessionKeys.RESULT_DESC,errorMsg);
		    	writer.append(JacksonEncoder.encodeAsString(resultMap));
		    }
		}
		
		//自定义响应
		private void customAjaxJsonResponse(HttpServletRequest request,HttpServletResponse response) throws IOException{
			int respStatus = response.getStatus();
			if(respStatus > 399){//即使页面或服务器出错也会以json方式返回
				//客户端错误
				if(respStatus >= 400 && respStatus < 500 && isAjaxRequest(request) && isRequestJsonType(request)){
					response.reset();
					response.setStatus(200);
					errorResponse(request, response, SessionResults.SERVICE_DENIED_ERROR);
					return;
				}
				//服务器错误
				if(respStatus > 500 && isAjaxRequest(request) && isRequestJsonType(request)){
					response.reset();
					response.setStatus(200);
					errorResponse(request, response, SessionResults.SYSTEM_EXCEPTION);
					return;
				}
			}
		}
		
		//是否为拦截的URI
		private boolean isInterceptURI(String reqUri){
			if(reqUri == null){
				return false;
			}
			for (String uri : requiredURIs) {
				if(reqUri.indexOf(uri) >= 0){
					return true;
				}
			}
			return false;
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
		* 检测是否为ajax请求
		*/
		private boolean isAjaxRequest(HttpServletRequest request){
			return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
		}
		
		/**
		* 添加新的拦截uri
		* 
		*@param uris 待拦截的uri数组
		*/
		public CommonAjaxJsonInterceptor requiredURIs(String... uris){
			if(uris == null){
				return this;
			}
			int origiArrLen = this.requiredURIs.length;
			int inputArrLen =  uris.length;
			Set<String> uriSet = new HashSet<>(origiArrLen+inputArrLen);
			uriSet.addAll(Arrays.asList(uris));
			uriSet.addAll(Arrays.asList(requiredURIs));
			String[] tempStrs = new String[uriSet.size()];
			this.requiredURIs = uriSet.toArray(tempStrs);
			return this;
		}
}

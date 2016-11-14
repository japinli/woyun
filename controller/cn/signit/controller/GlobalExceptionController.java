package cn.signit.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
*全局异常处理控制器接口
* @ClassName GlobalExceptionController
* @author ZhangHongdong
* @date 2016年4月13日-下午1:30:32
* @version 1.2.0
*/
public interface GlobalExceptionController {
	
	/**
	* 统一方法异常处理器
	* 
	*@param ex 正常Controller处理请求时抛出的未预测异常
	*@param request http请求
	*@throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	*@since 1.2.0
	*@author Zhanghongdong
	*/
	public void printCommonException(Exception ex,HttpServletRequest request) throws IOException;
	
 	/**
 	* 客户端异常处理器
 	* 
 	*@param ex 未找到相关Controller方法处理请求时抛出的异常
	*@param request http请求
 	 *@throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
 	*@since 1.2.0
 	*@author Zhanghongdong
 	*/
	  public void printClientException(Exception ex,HttpServletRequest request) throws IOException;
	  
		/**
	 	* 服务器异常处理器
	 	* 
	 	*@param ex 服务器抛出的异常
		*@param request http请求
	 	*@throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 	*@since 1.2.0
	 	*@author Zhanghongdong
	 	*/
	 public void printServerException(Exception ex,HttpServletRequest request) throws IOException ;
	 
	
}

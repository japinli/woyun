package io.github.japinl.woyun.config;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话过滤器
 * 
 * @ClassName: SessionFilter
 * @author:ZhangHongdong
 * @date:2014年12月5日-下午6:26:15
 * @version:1.0.0
 */
public class SessionFilter implements Filter {
	public final static Logger LOG = LoggerFactory.getLogger(SessionFilter.class);
	private final static String FILTERED_REQUEST = "@signit.cn@RequestFiltered";
	//private final static String MAIN_PAGE_TEMPLATE = "%s%s";
	//private final static String INDEX_URI = "/user/login";
	private final static String[] PAGE_CONTENT_TYPES = new String[]{"html","htm","xhtml","xhtm","shtml","shtm","xml","jsp"};
	private final static int  TYPES_LEN = PAGE_CONTENT_TYPES.length;
	private final static String[] PROTECTED_RES_KEY_WORDS = new String[]{"/sys","/upload"};
	private final static int  PROTECTED_RES_LEN = PROTECTED_RES_KEY_WORDS.length;
	private final static String[] OPEN_PROTECTED_RES_KEY_WORDS = new String[]{"/callback","/inner"};
	private final static int  OPEN_PROTECTED_RES_LEN = OPEN_PROTECTED_RES_KEY_WORDS.length;
	/*
	 * 执行过滤
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		
		String url = request.getRequestURI();
		
		String path = url.substring(url.indexOf("/list/") + "/list/".length());
		request.setAttribute("path", path);
		request.getRequestDispatcher("/internal-list").forward(request, resp);
		//chain.doFilter(request, resp);
	}
	
	/**
	* 获得Http请求的RootPath
	*/
	private String getRequestRootPath(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	}
	
	/**
	* 获得Http请求的BasePath
	*/
	private String getRequestBasePath(HttpServletRequest request) {
		return getRequestRootPath(request)+request.getContextPath();
	}

	/**
	* 检测请求是否为受保护资源
	*/
	private boolean isProtectedResources(String requestURI) {
		if(requestURI == null){
			return false;
		}
		for (int i = 0; i < PROTECTED_RES_LEN; i++) {
			if (requestURI.indexOf(PROTECTED_RES_KEY_WORDS[i]) >= 0) {
				for (int j = 0; j < OPEN_PROTECTED_RES_LEN; j++) {
					if(requestURI.indexOf(OPEN_PROTECTED_RES_KEY_WORDS[j]) >= 0){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	* 检测请求是否为页面形式的资源（缺省为false）
	*/
	private boolean isPageResources(HttpServletRequest request){
		if(request == null){
			return false;
		}
		
		String reqAcceptHeader = request.getHeader("Accept");
		String websocketHeader = request.getHeader("Upgrade");
		
		if(reqAcceptHeader == null){
			return false;
		}
		
		if(websocketHeader != null){
			return false;
		}
		
		reqAcceptHeader = reqAcceptHeader.toLowerCase();
		
		for (int i = 0; i < TYPES_LEN; i++) {
			if(reqAcceptHeader.indexOf(PAGE_CONTENT_TYPES[i]) >= 0){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	* 检测请求是否为非页面形式的资源（缺省为true）
	*/
	private boolean isNoPageResources(HttpServletRequest request){
		return !isPageResources(request);
	}
	
	/**
	* 检测是否为ajax请求
	*/
	private boolean isAjaxRequest(HttpServletRequest request){
		return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {}

	@Override
	public void destroy() {}
}

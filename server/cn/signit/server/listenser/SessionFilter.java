package cn.signit.server.listenser;

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

import cn.signit.cons.UrlPath;


/**
 * 会话过滤器,执行对API的签名过滤
 * 
 * @ClassName: SessionFilter
 * @author:liwen
 * @date:2014年12月5日-下午6:26:15
 * @version:1.0.0
 */
public class SessionFilter implements Filter {
	public final static Logger LOG = LoggerFactory.getLogger(SessionFilter.class);
	public final static String REQUEST_INFO = "拦截到请求:{},来源appid:{},签名验证结果:{}";

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		// 设置过滤为会话
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// 设置过滤为会话
		request.getRequestDispatcher(UrlPath.SYSTEM_ROOT+"/check").forward(request, response);

	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}

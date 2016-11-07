package io.github.japinl.woyun.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Web应用程序共有属性（相当于web.xml中的servlet，filter，listener等）
 *
 * @ClassName CommonWebAppAttributes
 * @author ZhangHongdong
 * @date 2015年8月26日-下午2:41:57
 * @version 1.1.0
 */
public class CommonWebAppAttributes {
	private ServletContext context;

	public CommonWebAppAttributes() {
	}

	public CommonWebAppAttributes(ServletContext context) {
		this.context = context;
	}

	public void initAll(List<WebListener> webListeners, List<WebFilter> webFilters, List<WebServlet> webServlets) {
		initListeners(webListeners);
		initFilters(webFilters);
		initServlets(webServlets);
	}

	public void initAll(WebListener webListener, WebFilter webFilter, WebServlet webServlet) {
		initListener(webListener);
		initFilter(webFilter);
		initServlet(webServlet);
	}

	@SuppressWarnings("unchecked")
	public void initListeners(List<WebListener> webListeners) {
		init();
		for (WebListener webListener : webListeners) {
			if(webListener.getParameterName() != null && webListener.getParameterValue() != null){
				this.context.setInitParameter(webListener.getParameterName(), webListener.getParameterValue());
			}
			Object listenerObj = webListener.getListenerClass();
			if (listenerObj instanceof String) {
				this.context.addListener((String) listenerObj);
			} else if(listenerObj instanceof EventListener){
				this.context.addListener((EventListener)listenerObj);
			}else if (listenerObj instanceof Class<?>) {
				this.context.addListener((Class<? extends EventListener>) listenerObj);
			} else {
				exception(listenerObj.getClass());
			}
		}
	}

	public void initListeners(WebListener... webListeners) {
		initListeners(Arrays.asList(webListeners));
	}

	public void initListener(WebListener webListener) {
		initListeners(webListener);
	}

	@SuppressWarnings("unchecked")
	public void initFilters(List<WebFilter> webFilters) {
		init();
		for (WebFilter webFilter : webFilters) {
			FilterRegistration.Dynamic dynamic = null;
			Object filterObj = webFilter.getFillterClass();
			if (filterObj instanceof Filter) {
				dynamic = this.context.addFilter(webFilter.getFilterName(), (Filter) filterObj);
			} else if (filterObj instanceof String) {
				dynamic = this.context.addFilter(webFilter.getFilterName(), (String) filterObj);
			} else if (filterObj instanceof Class<?>) {
				dynamic = this.context.addFilter(webFilter.getFilterName(), (Class<? extends Filter>) filterObj);
			} else {
				exception(filterObj.getClass());
			}
			dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false,webFilter.urlPattern);
			dynamic.setAsyncSupported(webFilter.isAsyncSupported());
			if (webFilter.getInitParameters() != null && !webFilter.getInitParameters().isEmpty()) {
				dynamic.setInitParameters(webFilter.getInitParameters());
			}
		}
	}

	public void initFilters(WebFilter... webFilters) {
		initFilters(Arrays.asList(webFilters));
	}

	public void initFilter(WebFilter webFilter) {
		initFilters(webFilter);
	}

	@SuppressWarnings("unchecked")
	public void initServlets(List<WebServlet> webServlets) {
		init();
		for (WebServlet webServlet : webServlets) {
			ServletRegistration.Dynamic dynamic = null;
			Object servletObj = webServlet.getServletClass();
			if (servletObj instanceof Servlet) {
				dynamic = this.context.addServlet(webServlet.getServletName(), (Servlet) servletObj);
			} else if (servletObj instanceof String) {
				dynamic = this.context.addServlet(webServlet.getServletName(), (String) servletObj);
			} else if (servletObj instanceof Class<?>) {
				dynamic = this.context.addServlet(webServlet.getServletName(), (Class<? extends Servlet>) servletObj);
			} else {
				exception(servletObj.getClass());
			}
			dynamic.setLoadOnStartup(webServlet.getLoadOnStartup());
			dynamic.addMapping(webServlet.getUrlPattern());
			dynamic.setAsyncSupported(webServlet.isAsyncSupported());
			//dynamic.setMultipartConfig(new MultipartConfigElement(FilePathAdapter.adapt("/tmp"),52428800l,20971520l,2097152));
		}
	}

	public void initServlets(WebServlet... webServlets) {
		initServlets(Arrays.asList(webServlets));
	}

	public void initServlet(WebServlet webServlet) {
		initServlets(webServlet);
	}

	public void initWelcome() {

	}

	private void init() {
		if (this.context == null) {
			throw new RuntimeException("ServletContext can be not null");
		}
	}

	private void exception(Class<?> clazz) {
		throw new RuntimeException(clazz.getName() + " is unknown");
	}

	public ServletContext getServletContext() {
		return context;
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * web监听器
	 *
	 * @ClassName WebListener
	 * @author ZhangHongdong
	 * @date 2015年8月26日-下午3:09:42
	 * @version 1.1.0
	 */
	public class WebListener {
		private String parameterName;
		private String parameterValue;
		private Object listenerClass;

		public WebListener(Class<? extends EventListener> listenerClass) {
			this.listenerClass = listenerClass;
		}

		public WebListener(String listenerClass) {
			this.listenerClass = listenerClass;
		}

		public <T> WebListener(T listenerClass) {
			this.listenerClass = listenerClass;
		}

		public WebListener(Class<? extends EventListener> listenerClass, String parameterName, String parameterValue) {
			super();
			this.parameterName = parameterName;
			this.parameterValue = parameterValue;
			this.listenerClass = listenerClass;
		}

		public WebListener(String listenerClass, String parameterName, String parameterValue) {
			super();
			this.parameterName = parameterName;
			this.parameterValue = parameterValue;
			this.listenerClass = listenerClass;
		}

		public <T> WebListener(T listenerClass, String parameterName, String parameterValue) {
			super();
			this.parameterName = parameterName;
			this.parameterValue = parameterValue;
			this.listenerClass = listenerClass;
		}

		public String getParameterName() {
			return parameterName;
		}

		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}

		public String getParameterValue() {
			return parameterValue;
		}

		public void setParameterValue(String parameterValue) {
			this.parameterValue = parameterValue;
		}

		public Object getListenerClass() {
			return listenerClass;
		}

		public <T> void setListenerClass(T listenerClass) {
			this.listenerClass = listenerClass;
		}
		
		public String toString(){
			Map<String, Object> map = new HashMap<String, Object>();
			Field[] fields = this.getClass().getDeclaredFields();
			try {
				for (Field field : fields) {
					map.put(field.getName(), field.get(this));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return map.toString();
		}
	}

	/**
	 * web过滤器
	 *
	 * @ClassName WebFilter
	 * @author ZhangHongdong
	 * @date 2015年8月26日-下午3:24:16
	 * @version 1.1.0
	 */
	public class WebFilter {
		private String filterName;
		private Object fillterClass;
		private boolean isAsyncSupported;
		private Map<String, String> initParameters;
		private String[] urlPattern;

		public WebFilter(String filterName, Class<? extends Filter> fillterClass, boolean isAsyncSupported,
				String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, String fillterClass, boolean isAsyncSupported, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, Filter fillterClass, boolean isAsyncSupported, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, Class<? extends Filter> fillterClass, boolean isAsyncSupported,
				Map<String, String> initParameters, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, String fillterClass, boolean isAsyncSupported,
				Map<String, String> initParameters, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, Filter fillterClass, boolean isAsyncSupported,
				Map<String, String> initParameters, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, Class<? extends Filter> fillterClass, boolean isAsyncSupported,
				String initParamName, String initParamValue, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, String fillterClass, boolean isAsyncSupported, String initParamName,
				String initParamValue, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public WebFilter(String filterName, Filter fillterClass, boolean isAsyncSupported, String initParamName,
				String initParamValue, String... urlPattern) {
			super();
			this.filterName = filterName;
			this.fillterClass = fillterClass;
			this.isAsyncSupported = isAsyncSupported;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public String getFilterName() {
			return filterName;
		}

		public void setFilterName(String filterName) {
			this.filterName = filterName;
		}

		public Object getFillterClass() {
			return fillterClass;
		}

		public <T> void setFillterClass(T fillterClass) {
			this.fillterClass = fillterClass;
		}

		public String[] getUrlPattern() {
			return urlPattern;
		}

		public void setUrlPattern(String... urlPattern) {
			this.urlPattern = urlPattern;
		}

		public boolean isAsyncSupported() {
			return isAsyncSupported;
		}

		public void setAsyncSupported(boolean isAsyncSupported) {
			this.isAsyncSupported = isAsyncSupported;
		}

		public Map<String, String> getInitParameters() {
			return initParameters;
		}

		public void setInitParameters(Map<String, String> initParameters) {
			this.initParameters = initParameters;
		}

	}

	/**
	 * web Servlet应用程序
	 *
	 * @ClassName WebServlet
	 * @author ZhangHongdong
	 * @date 2015年8月26日-下午3:18:49
	 * @version 1.1.0
	 */
	public static class WebServlet {
		private String servletName;
		private Object servletClass;
		private int loadOnStartup;
		private boolean isAsyncSupported;
		private Map<String, String> initParameters;
		private String[] urlPattern;

		public WebServlet(String servletName, Class<? extends Servlet> servletClass, boolean isAsyncSupported,
				int loadOnStartup, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, String servletClass, boolean isAsyncSupported, int loadOnStartup,
				String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, Servlet servletClass, boolean isAsyncSupported, int loadOnStartup,
				String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, Class<? extends Servlet> servletClass, boolean isAsyncSupported,
				int loadOnStartup, Map<String, String> initParameters, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, String servletClass, boolean isAsyncSupported, int loadOnStartup,
				Map<String, String> initParameters, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, Servlet servletClass, boolean isAsyncSupported, int loadOnStartup,
				Map<String, String> initParameters, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			this.initParameters = initParameters;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, Class<? extends Servlet> servletClass, boolean isAsyncSupported,
				int loadOnStartup, String initParamName, String initParamValue, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, String servletClass, boolean isAsyncSupported, int loadOnStartup,
				String initParamName, String initParamValue, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public WebServlet(String servletName, Servlet servletClass, boolean isAsyncSupported, int loadOnStartup,
				String initParamName, String initParamValue, String... urlPattern) {
			super();
			this.servletName = servletName;
			this.servletClass = servletClass;
			this.isAsyncSupported = isAsyncSupported;
			this.loadOnStartup = loadOnStartup;
			Map<String, String> initParamMap = new HashMap<String, String>();
			initParamMap.put(initParamName, initParamValue);
			this.initParameters = initParamMap;
			this.urlPattern = urlPattern;
		}

		public String getServletName() {
			return servletName;
		}

		public void setServletName(String servletName) {
			this.servletName = servletName;
		}

		public Object getServletClass() {
			return servletClass;
		}

		public <T> void setServletClass(T servletClass) {
			this.servletClass = servletClass;
		}

		public int getLoadOnStartup() {
			return loadOnStartup;
		}

		public void setLoadOnStartup(int loadOnStartup) {
			this.loadOnStartup = loadOnStartup;
		}

		public String[] getUrlPattern() {
			return urlPattern;
		}

		public void setUrlPattern(String... urlPattern) {
			this.urlPattern = urlPattern;
		}

		public boolean isAsyncSupported() {
			return isAsyncSupported;
		}

		public void setAsyncSupported(boolean isAsyncSupported) {
			this.isAsyncSupported = isAsyncSupported;
		}

		public Map<String, String> getInitParameters() {
			return initParameters;
		}

		public void setInitParameters(Map<String, String> initParameters) {
			this.initParameters = initParameters;
		}
	}
}

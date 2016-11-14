/**
* @author ZhangHongdong
* @date 2014年12月29日-上午10:43:53
* @see (参阅)
*/
package cn.signit.untils.http;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *session监听器
 * @ClassName SessionListener
 * @author:liwen
 * @date 2014年12月29日-上午10:43:53
 * @version 1.0.0
 */
public class SessionListener implements HttpSessionAttributeListener {
	public final static Logger LOG = LoggerFactory.getLogger(SessionListener.class);
	private Object loginUser;
	public SessionListener(Object loginUser){
		LOG.info("======================>>  启动自定义会话监听器 ( {} )",SessionListener.class.getName());
		this.loginUser = loginUser;
	}
	
	public SessionListener(Class<?> loginUser){
		LOG.info("======================>>  启动自定义会话监听器 ( {} )",SessionListener.class.getName());
		if(loginUser == null){
			this.loginUser = new Object();
		}
		try {
			this.loginUser = loginUser.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 *添加session属性
	 *
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		//LOG.info("添加HttpSession属性===>> {}",event.getName());
		if(event.getValue().getClass().getName().equals(loginUser.getClass().getName()) ){
			setLoginUser(event.getValue());
			LOG.info("准备添加===>> {}",getLoginUser().toString());
		}
	}

	/**
	 *移除session属性
	 *
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		//LOG.info("移除HttpSession属性===>> {}",event.getName());
		if(event.getValue().getClass().getName().equals(loginUser.getClass().getName()) ){
			setLoginUser(event.getValue());
			LOG.info("准备移除===>> {}",getLoginUser().toString());
		}
	}

	/**
	 *替换session属性
	 *
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		//LOG.info("替换HttpSession属性===>> {}",event.getName());
		if(event.getValue().getClass().getName().equals(loginUser.getClass().getName()) ){
			setLoginUser(event.getValue());
			LOG.info("准备替换===>> {}",getLoginUser().toString());
		}
	}

	public Object getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(Object loginUser) {
		this.loginUser = loginUser;
	}
}

/**
* @author ZhangHongdong
* @date 2015年3月15日-下午5:54:02
* @see (参阅)
*/
package cn.signit.server.listenser;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName LogbackConfigListener
 * @author ZhangHongdong
 * @date 2015年3月15日-下午5:54:02
 * @version (版本号)
 * @see (参阅)
 */
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogbackConfigListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        LogbackWebConfigure.initLogging(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event) {
        LogbackWebConfigure.shutdownLogging(event.getServletContext());
    }
}
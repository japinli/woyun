/**
* @author ZhangHongdong
* @date 2015年9月6日-上午11:21:55
* @see (参阅)
*/
package cn.signit.server;


import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 *初始化Spring Security（相当于web.xml中的Spring Security配置）
 * @ClassName SecurityInitializer
 * @author ZhangHongdong
 * @date 2015年9月6日-上午11:21:55
 * @version 1.1.0
 */
@Order(1)
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer{

}

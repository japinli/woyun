package cn.signit.controller.forensic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.signit.untils.message.SessionKeys;

/**
*用户账户相关控制
* @ClassName UserController
* @author Liwen
* @date 2016年5月5日-下午4:25:45
* @version (版本号)
* @see (参阅)
*/
@SessionAttributes(SessionKeys.LOGIN_USER)
public class UserController {
	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 
	 * 
	 * 获取用户登陆页
	 *
	 * @see: (参阅)
	 * @since eSignServer v2.0
	 */
	@RequestMapping(value={"","/login"},method=RequestMethod.GET)
	public String getIndexPage(){
		
		return null;
	}
	

}

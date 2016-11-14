package cn.signit.controller.user;

import org.springframework.ui.Model;

import cn.signit.domain.mysql.User;


/**
*用户页面定义
* @ClassName InUserPageController
* @author Liwen
* @date 2016年11月11日-下午2:42:32
* @version (版本号)
* @see (参阅)
*/
public interface InUserPageController {
	//登录页
	public String getLoginPage(String error,String logout,Model model);

	//主页
	public String getHomePage(User user,Model model);

	
	//文档中心页
	public String getDocumentPage(User user,Model model);
	
	//个人中心页
	public String getMyInfoPage(User user,Model model);
}

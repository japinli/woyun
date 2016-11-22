package cn.signit.controller.evidences;

import org.springframework.ui.Model;

import cn.signit.domain.mysql.User;

/**
*保全信息相关页面
* @ClassName InEvidenceController
* @author Liwen
* @date 2016年11月11日-下午4:46:56
* @version (版本号)
* @see (参阅)
*/
public interface InEvidenceController {
	
	/**
	 * 获取保全信息中心页面
	 */
	public String getEvidencePage(User user,Model model);
	
	public String getSignaturePage(User user,String docId,Model model);
	
	public String getSignSelfSianaturePage(User user,String docId,Model model);
	
	public String getSignSelfPage(User user,Model model);
	
	public String getVerificationPage(User user,String docId,Model model);
}

package cn.signit.controller.dirents;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.signit.cons.rest.RestPath;
import cn.signit.domain.mysql.User;
import cn.signit.service.db.UserDirentsService;
import cn.signit.untils.message.CommonResp;
import cn.signit.untils.message.SessionKeys;

@Controller
@SessionAttributes({SessionKeys.LOGIN_USER})
public class DirentsController {
	
	public final static Logger LOG = LoggerFactory.getLogger(DirentsController.class);
	
	@Resource
	private UserDirentsService direntsServie;
	
	@RequestMapping(value=RestPath.REST_NEW_DIR,method=RequestMethod.POST)
	@ResponseBody
	public CommonResp createDirecotry(@ModelAttribute(SessionKeys.LOGIN_USER) User user, String path) {
		boolean flag = direntsServie.createDirectory(user.getId(), path);
		if (flag) {
			return new CommonResp().basicSuccessMsg("新建目录成功");
		}
		
		return new CommonResp().basicFailureMsg("新建目录失败");
	}
	
	@RequestMapping(value=RestPath.REST_DEL_DIR, method=RequestMethod.DELETE)
	@ResponseBody
	public CommonResp deleteDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, String path) {
		boolean flag = direntsServie.deleteDirectory(user.getId(), path);
		if (flag) {
			return new CommonResp().basicSuccessMsg("删除目录成功");
		}
		
		return new CommonResp().basicFailureMsg("删除目录失败");
	}
}
package cn.signit.controller.repo;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


import cn.signit.cons.UrlPath;
import cn.signit.domain.mysql.User;
import cn.signit.entry.RepoInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.message.SessionKeys;

@Controller
@SessionAttributes({SessionKeys.LOGIN_USER})
public class RepoController {

	@Resource
	private RepoService repoService;
	
	@RequestMapping(value=UrlPath.REPO_LIST, method=RequestMethod.GET)
	@ResponseBody
	public List<RepoInfo> listRepositories(@ModelAttribute(SessionKeys.LOGIN_USER)User user) throws IOException {
		return repoService.getRepositoriesInfo(user);
	}
}

package cn.signit.controller.repo;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


import cn.signit.cons.UrlPath;
import cn.signit.controller.api.RestResponse;
import cn.signit.controller.api.RestStatus;
import cn.signit.domain.mysql.Repo;
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
	public RestResponse listRepositories(@ModelAttribute(SessionKeys.LOGIN_USER)User user) throws IOException {
		RestResponse response = new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
		response.setData(repoService.getRepositoriesInfo(user));
		return response;
	}
	
	@RequestMapping(value=UrlPath.REPO_NEW, method=RequestMethod.POST)
	@ResponseBody
	public RestResponse createRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @RequestBody Repo repo) {
		RestResponse response = new RestResponse();
		RepoInfo repoInfo = repoService.createRepository(user, repo.getRepoName());
		if (repo != null) {
			response.setData(repoInfo);
			response.setStatus(RestStatus.SUCCESS.getStatus());
			response.setDesc(RestStatus.SUCCESS.getDesc());
		}
		return response;
	}
	
}

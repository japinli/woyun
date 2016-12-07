package cn.signit.controller.repo;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.signit.cons.UrlPath;
import cn.signit.controller.api.RestResponse;
import cn.signit.controller.api.RestStatus;
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
import cn.signit.entry.RepoInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.RepoPath;
import cn.signit.untils.message.SessionKeys;

@Controller
@SessionAttributes({ SessionKeys.LOGIN_USER })
public class RepoController {

	private final static Logger LOG = LoggerFactory.getLogger(RepoController.class);

	@Resource
	private RepoService repoService;

	@RequestMapping(value = UrlPath.REPO_LIST, method = RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepositories(@ModelAttribute(SessionKeys.LOGIN_USER) User user) throws IOException {
		LOG.info("用户({})请求获取所有仓库信息", user.getEmail());
		RestResponse response = new RestResponse(true);
		response.setData(repoService.getRepositoriesInfo(user));
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_NEW, method = RequestMethod.POST)
	@ResponseBody
	public RestResponse createRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @RequestBody Repo repo) {
		LOG.info("用户({})请求新建({})仓库)", user.getEmail(), repo.getRepoName());
		RestResponse response = new RestResponse();
		RepoInfo repoInfo = repoService.createRepository(user, repo.getRepoName());
		if (repo != null) {
			response.setData(repoInfo);
			response.setStatus(RestStatus.SUCCESS.getStatus());
			response.setDesc(RestStatus.SUCCESS.getDesc());
		}
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_RENAME, method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse renameRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @RequestBody Repo repo) {
		LOG.info("用户({})请求修改仓库({})名为({})", user.getEmail(), repo.getRepoId(), repo.getRepoName());

		boolean flag = repoService.renameRepository(repo.getRepoId(), repo.getRepoName());
		return new RestResponse(flag);
	}

	@RequestMapping(value = UrlPath.REPO_DELETE, method = RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @RequestBody Repo repo) {
		LOG.info("用户({})请求删除({})仓库", user.getEmail(), repo.getRepoName());
		boolean flag = repoService.deleteRepository(repo.getRepoId());
		return new RestResponse(flag);
	}

	@RequestMapping(value = UrlPath.REPO_GET_HISTORY, method = RequestMethod.GET)
	@ResponseBody
	public RestResponse getRepositoryHistory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId) throws IOException {

		LOG.info("用户({})请求获取仓库({})历史变更记录", user.getEmail(), repoId);
		String repoName = RepoPath.contact(user.getEmail(), repoId);
		RestResponse response = new RestResponse(true);
		response.setData(repoService.getRepositoryHistory(repoName));
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_VIEW_HISTORY, method = RequestMethod.GET)
	@ResponseBody
	public RestResponse viewHistoryByCommitId(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestParam String commitId, @RequestParam String path)
			throws IOException {
		RestResponse response = new RestResponse(true);
		String repoName = RepoPath.contact(user.getEmail(), repoId);
		response.setData(repoService.getHistoryByCommit(repoName, commitId, path));
		return response;
	}

}

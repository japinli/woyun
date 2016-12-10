package cn.signit.controller.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import cn.signit.dao.mysql.RepoMapper;
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
import cn.signit.entry.FileInfo;
import cn.signit.entry.RepoInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.message.SessionKeys;
import cn.signit.utils.repo.RepoPath;

@Controller
@SessionAttributes({ SessionKeys.LOGIN_USER })
public class RepoController {

	private final static Logger LOG = LoggerFactory.getLogger(RepoController.class);

	@Resource
	private RepoService repoService;
	@Resource
	private RepoMapper repoDao;
	
	@RequestMapping(value=UrlPath.REPO_CHECK, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse checkRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @RequestParam String repoName) {
		boolean flag = repoService.isRepositoryExists(user.getEmail(), repoName);
		RestResponse response = new RestResponse(true);
		response.setData(flag);
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_LIST, method = RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepositories(@ModelAttribute(SessionKeys.LOGIN_USER) User user) throws IOException {
		LOG.info("用户({})请求获取所有仓库信息", user.getEmail());
		RestResponse response = new RestResponse(true);
		response.setData(repoService.getRepositoriesInfo(user, false));
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
	
	@RequestMapping(value=UrlPath.REPO_LIST_DELETED,method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse getDeletedRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user) throws IOException {
		RestResponse response = new RestResponse(true);
		response.setData(repoService.getRepositoriesInfo(user, true));
		return response;
	}
	
	@RequestMapping(value=UrlPath.REPO_PERMANENT_DELETE,method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse permanentDeleteRepository(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@RequestParam List<String> repoId) throws IOException {
		for (String id : repoId) {
			repoService.permanentDeleteRepository(user.getEmail(), id);
		}
		return new RestResponse(true);
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
	
	@RequestMapping(value=UrlPath.REPO_SHOW_BY_CATEGORY, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse getByCategory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @PathVariable String category) 
			throws IOException {
		
		List<Repo> repos = repoDao.selectByUserEmail(user.getEmail());
		List<RepoCategoryInfo> categoryInfos = new ArrayList<RepoCategoryInfo>();
		
		for (Repo repo : repos) {
			List<FileInfo> infos = new ArrayList<FileInfo>();
			infos = repoService.getFileByCategory(user.getEmail(), repo.getRepoId(), category);
			categoryInfos.add(new RepoCategoryInfo(repo.getRepoId(), repo.getRepoName(), infos));
		}
		RestResponse response = new RestResponse(true);
		response.setData(categoryInfos);
		return response;
	}
	
	@RequestMapping(value=UrlPath.REPO_SHOW_REPO_ID_CATEGORY, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse getByRepoCategory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, 
			@PathVariable("repo-id") String repoId, @PathVariable String category) 
			throws IOException {
		
		RestResponse response = new RestResponse(true);
		response.setData(repoService.getFileByCategory(user.getEmail(), repoId, category));
		return response;
	}
	
	public static class RepoCategoryInfo {
		private String repoId;
		private String repoName;
		List<FileInfo> fileInfo;
		
		public RepoCategoryInfo(String repoId, String repoName, List<FileInfo> infos) {
			this.repoId = repoId;
			this.repoName = repoName;
			this.fileInfo = infos;
		}
		
		public String getRepoId() {
			return repoId;
		}
		public void setRepoId(String repoId) {
			this.repoId = repoId;
		}
		public String getRepoName() {
			return repoName;
		}
		public void setRepoName(String repoName) {
			this.repoName = repoName;
		}
		public List<FileInfo> getFileInfo() {
			return fileInfo;
		}
		public void setFileInfo(List<FileInfo> fileInfo) {
			this.fileInfo = fileInfo;
		}
	}

}

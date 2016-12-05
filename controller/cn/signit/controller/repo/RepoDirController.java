package cn.signit.controller.repo;

import java.io.IOException;
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
import cn.signit.domain.mysql.User;
import cn.signit.entry.DirOperation;
import cn.signit.entry.FileInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.RepoPath;
import cn.signit.untils.message.SessionKeys;

@Controller
@SessionAttributes({SessionKeys.LOGIN_USER})
public class RepoDirController {

	private final static Logger LOG = LoggerFactory.getLogger(RepoDirController.class);
	
	@Resource
	private RepoService repoService;
	
	@RequestMapping(value=UrlPath.REPO_LIST_DIRS, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepositoryDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, 
			@PathVariable("repo-id") String repoId, @RequestParam("path") String path) throws IOException {
		
		LOG.info("用户({})请求获取仓库({})下({})的内容", user.getEmail(), repoId, path);
		
		String repoName = RepoPath.contact(user.getEmail(), repoId);
		List<FileInfo> infos = repoService.getDirectory(repoName, path);
		if (infos == null || infos.isEmpty()) {
			return new RestResponse(false);
		}
		
		RestResponse response = new RestResponse(true);
		response.setData(infos);
		return response;
	}
	
	@RequestMapping(value=UrlPath.REPO_MAKE_DIR, method=RequestMethod.POST)
	@ResponseBody
	public RestResponse createDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, 
			@PathVariable("repo-id") String repoId, @RequestBody DirOperation dirInfo) {
		
		LOG.info("用户({})请求在仓库({})下新建({})目录", user.getEmail(), repoId, dirInfo.getName());
		
		String repoPath = RepoPath.contact(user.getEmail(), repoId);
		String dirPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getName());
		boolean flag = repoService.createDirectory(repoPath, dirPath);
		
		return new RestResponse(flag);
	}
	
	@RequestMapping(value=UrlPath.REPO_DIR_RENAME, method=RequestMethod.PUT)
	@ResponseBody
	public RestResponse renameDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user, 
			@PathVariable("repo-id") String repoId, @RequestBody DirOperation dirInfo) throws IOException {
		
		LOG.info("用户({})请求在仓库({})下重命名({})目录为({})", user.getEmail(), repoId, dirInfo.getName(), dirInfo.getNewName());
		
		String repoName = RepoPath.contact(user.getEmail(), repoId);
		String oldPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getName());
		String newPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getNewName());
		boolean flag = repoService.renameDirectory(repoName, oldPath, newPath);
		
		return new RestResponse(flag);
	}
}

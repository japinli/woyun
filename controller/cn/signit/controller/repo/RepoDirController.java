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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import cn.signit.cons.UrlPath;
import cn.signit.controller.api.RestResponse;
import cn.signit.controller.api.RestStatus;
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
	
	@RequestMapping(value=UrlPath.REPO_DIR_OR_FILE_OPERATION, method=RequestMethod.POST)
	@ResponseBody
	public RestResponse directoryOrFileOperation(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@RequestBody DirOperation dirInfo) throws IOException {
		
		LOG.info("用户({})请求将仓库({})下的({} {}) {} 到 ({}) 仓库下的 ({})",
				user.getEmail(), dirInfo.getSrcRepoId(), dirInfo.getSrcPath(), dirInfo.getName(),
				dirInfo.getDstRepoId(), dirInfo.getDstPath());
		
		String srcRepoName = RepoPath.contact(user.getEmail(), dirInfo.getSrcRepoId());
		String dstRepoName = RepoPath.contact(user.getEmail(), dirInfo.getDstRepoId());
		
		RestResponse response = new RestResponse(false);
		boolean flag = false;
		
		if (dirInfo.getOperation().equals("copy")) {
			flag = repoService.copy(srcRepoName, dstRepoName, dirInfo.getSrcPath(), dirInfo.getDstPath(), dirInfo.getName());
		} else if (dirInfo.getOperation().equals("move")) {
			flag = repoService.move(srcRepoName, dstRepoName, dirInfo.getSrcPath(), dirInfo.getDstPath(), dirInfo.getName());
		} else {
			response.setDesc(dirInfo.getOperation() + ": 未知的操作类型");
		}
		
		if (flag) {
			response.setStatus(RestStatus.SUCCESS.getStatus());
			response.setDesc(RestStatus.SUCCESS.getDesc());
		}
		
		return response;
	}
	
	@RequestMapping(value=UrlPath.REPO_DEL_FILE_OR_DIR, method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteFileOrDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestBody List<DirOperation> dels) throws IOException {
		boolean flag = repoService.delete(RepoPath.contact(user.getEmail(), repoId), dels);
		return new RestResponse(flag);
	}
	
	@RequestMapping(value=UrlPath.REPO_UPLOAD_FILE, method=RequestMethod.POST)
	@ResponseBody
	public RestResponse uploadFiles(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @PathVariable("repo-id") String repoId,
			@RequestPart(name="file") MultipartFile[] files, String path) throws IOException {
		
		String repoName = RepoPath.contact(user.getEmail(), repoId);
		boolean flag = repoService.uploadFiles(repoName, path, files);
		return new RestResponse(flag);
	}
}

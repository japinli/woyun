package cn.signit.controller.repo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
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
import cn.signit.domain.mysql.Repo;
import cn.signit.domain.mysql.User;
import cn.signit.entry.DirOperation;
import cn.signit.entry.FileInfo;
import cn.signit.service.db.RepoService;
import cn.signit.untils.Zip;
import cn.signit.untils.http.HttpUtil;
import cn.signit.untils.message.SessionKeys;
import cn.signit.utils.repo.RepoPath;
import cn.signit.utils.repo.RepoUtils;

@Controller
@SessionAttributes({ SessionKeys.LOGIN_USER })
public class RepoDirController {

	private final static Logger LOG = LoggerFactory.getLogger(RepoDirController.class);

	@Resource
	private RepoService repoService;

	@RequestMapping(value = UrlPath.REPO_LIST_DIRS, method = RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepositoryDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestParam("path") String path) throws IOException {

		LOG.info("用户({})请求获取仓库({})下({})的内容", user.getEmail(), repoId, path);

		String repoName = RepoPath.contact(user.getEmail(), repoId);
		List<FileInfo> infos = RepoUtils.getDirectoryInfo(RepoPath.getRepositoryPath(repoName, path));

		RestResponse response = new RestResponse(true);
		response.setData(infos);
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_MAKE_DIR, method = RequestMethod.POST)
	@ResponseBody
	public RestResponse createDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestBody DirOperation dirInfo) throws IOException {

		LOG.info("用户({})请求在仓库({})下新建({})目录", user.getEmail(), repoId, dirInfo.getName());

		String repoPath = RepoPath.contact(user.getEmail(), repoId);
		String dirPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getName());
		RestStatus status = repoService.createDirectory(repoPath, dirPath);
		RestResponse response = new RestResponse(status);
		if (status.getStatus() == 0) {
			response.setData(RepoUtils.getFileInfo(RepoPath.getRepositoryPath(repoPath, dirPath)));
		}
		return response;
	}

	@RequestMapping(value = UrlPath.REPO_DIR_RENAME, method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse renameDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestBody DirOperation dirInfo) throws IOException {

		LOG.info("用户({})请求在仓库({})下重命名({})目录为({})", user.getEmail(), repoId, dirInfo.getName(), dirInfo.getNewName());

		String repoName = RepoPath.contact(user.getEmail(), repoId);
		String oldPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getName());
		String newPath = RepoPath.contact(dirInfo.getPath(), dirInfo.getNewName());
		RestStatus status = repoService.renameDirectory(repoName, oldPath, newPath);

		return new RestResponse(status);
	}

	@RequestMapping(value = UrlPath.REPO_DIR_OR_FILE_OPERATION, method = RequestMethod.POST)
	@ResponseBody
	public RestResponse directoryOrFileOperation(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@RequestBody DirOperation dirInfo) throws IOException {

		LOG.info("用户({})请求将仓库({})下的({} {}) {} 到 ({}) 仓库下的 ({})", user.getEmail(), dirInfo.getSrcRepoId(),
				dirInfo.getSrcPath(), dirInfo.getName(), dirInfo.getDstRepoId(), dirInfo.getDstPath());

		String srcRepoName = RepoPath.contact(user.getEmail(), dirInfo.getSrcRepoId());
		String dstRepoName = RepoPath.contact(user.getEmail(), dirInfo.getDstRepoId());

		RestResponse response = new RestResponse(false);
		boolean flag = false;

		if (dirInfo.getOperation().equals("copy")) {
			flag = repoService.copy(srcRepoName, dstRepoName, dirInfo.getSrcPath(), dirInfo.getDstPath(),
					dirInfo.getName());
		} else if (dirInfo.getOperation().equals("move")) {
			flag = repoService.move(srcRepoName, dstRepoName, dirInfo.getSrcPath(), dirInfo.getDstPath(),
					dirInfo.getName());
		} else {
			response.setDesc(dirInfo.getOperation() + ": 未知的操作类型");
		}

		if (flag) {
			response.setStatus(RestStatus.SUCCESS.getStatus());
			response.setDesc(RestStatus.SUCCESS.getDesc());
		}

		return response;
	}

	@RequestMapping(value = UrlPath.REPO_DEL_FILE_OR_DIR, method = RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteFileOrDirectory(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestBody List<DirOperation> dels) throws IOException {
		boolean flag = repoService.delete(RepoPath.contact(user.getEmail(), repoId), dels);
		return new RestResponse(flag);
	}

	@RequestMapping(value = UrlPath.REPO_UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public RestResponse uploadFiles(@ModelAttribute(SessionKeys.LOGIN_USER) User user,
			@PathVariable("repo-id") String repoId, @RequestPart(name = "file") MultipartFile[] files, String path,
			HttpServletResponse response) throws IOException {

		String repoName = RepoPath.contact(user.getEmail(), repoId);
		boolean flag = repoService.uploadFiles(repoName, path, files);
		return new RestResponse(flag);
	}

	@RequestMapping(value = UrlPath.REPO_DOWNLOAD_FILE, method = RequestMethod.GET)
	public void downloadFiles(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @PathVariable("repo-id") String repoId,
			@RequestParam String path, @RequestParam List<String> name, HttpServletResponse response)
			throws IOException {

		String repoName = RepoPath.contact(user.getEmail(), repoId);
		String filename = null; /** 待下载的文件名 */
		String fullpath = null; /** 待下载的文件绝对路径 */

		if (name.size() == 1) {
			filename = name.get(0);
			fullpath = RepoPath.getRepositoryPath(repoName, path, filename);
			File file = new File(RepoPath.getRepositoryPath(repoName, path), filename);
			if (file.isDirectory()) {
				filename = Zip.zip(file.getName(), file.getAbsolutePath());
				fullpath = RepoPath.getTemp(filename);
			}
		} else {
			File dirFile = new File(RepoPath.getRepositoryPath(repoName, path));
			String dirName = dirFile.getName();
			String ppath = dirFile.getAbsolutePath();
			List<String> paths = new ArrayList<String>();
			for (String i : name) {
				paths.add(RepoPath.contact(ppath, i));
			}

			filename = Zip.zip(dirName, paths);
			fullpath = RepoPath.getTemp(filename);
		}

		HttpUtil.sendFile(response, filename, fullpath);
	}
	
	@RequestMapping(value=UrlPath.REPO_DOWNLOAD_HISTORY, method=RequestMethod.GET)
	public void downloadHistoryFiles(@ModelAttribute(SessionKeys.LOGIN_USER) User user, @PathVariable("repo-id") String repoId,
			@RequestParam String commitId, @RequestParam String path, @RequestParam List<String> name, 
			HttpServletResponse response)
			throws IOException {
		
		Repository repository = RepoUtils.getRepository(user.getEmail(), repoId);
		RevCommit revCommit = RepoUtils.getRevCommit(repository, commitId);
		RevTree tree = revCommit.getTree();
		
		// 多文件打包下载
		if (name.size() > 1) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilterGroup.createFromStrings(name));
				
				Zip zip = new Zip(path);
				zip.init();
				while (treeWalk.next()) {
					ObjectId objectId = treeWalk.getObjectId(0);
					ObjectLoader loader = repository.open(objectId);
					zip.update(treeWalk.getPathString(), loader.getBytes());
				}
				
				HttpUtil.sendFile(response, zip.zip(), zip.getZipFile());
			}
			return;  // 退出函数
		} 
		
		try (TreeWalk treeWalk = RepoUtils.getTreeWalk(repository, tree, name.get(0))) {
			if ((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
				// 非目录直接下载
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				loader.copyTo(response.getOutputStream());
				HttpUtil.sendFile(response, treeWalk.getNameString());
				return; // 退出函数
			}
			
			// 目录打包下载
			try (TreeWalk dirWalk = new TreeWalk(repository)) {
				dirWalk.addTree(treeWalk.getObjectId(0));
				dirWalk.setRecursive(true);
				
				Zip zip = new Zip(treeWalk.getNameString());
				zip.init();
				while (dirWalk.next()) {
					ObjectId objectId = dirWalk.getObjectId(0);
					ObjectLoader loader = repository.open(objectId);
					zip.update(dirWalk.getPathString(), loader.getBytes());
				}
				
				HttpUtil.sendFile(response, zip.zip(), zip.getZipFile());
			}
		}
	}
}

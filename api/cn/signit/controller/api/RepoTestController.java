package cn.signit.controller.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GIT 测试
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.signit.entry.DirOperation;
import cn.signit.service.db.RepoService;
import cn.signit.untils.RepoPath;
import cn.signit.untils.Zip;
import cn.signit.untils.http.HttpUtil;
import cn.signit.utils.repo.RepoUtils;

@Controller
public class RepoTestController {
	
	private final static Logger LOG = LoggerFactory.getLogger(RepoTestController.class);

	@Resource
	private RepoService repoService;
	
	@RequestMapping(value="/repo/create", method=RequestMethod.GET)
	@ResponseBody
	public RestResponse createRepository(@RequestParam("path") String path) {
		RestResponse response = new RestResponse();
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		try {
			//repoService.createRepository(uuid, path);
			response.setStatus(RestStatus.SUCCESS.getStatus());
			response.setDesc(String.format(RepoPath.create_repo_msg, path));
		} catch (IllegalStateException e) {
			// NOTE: I do not know how to check the repository exists.
			response.setDesc(String.format(RepoPath.exists_repo_msg, path));
		}
		
		return response;
	}
	
	@RequestMapping(value="/repo/list", method=RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepository(@RequestParam("path") String path) {
		RestResponse response = new RestResponse();
		
		repoService.listRepository(null, path);
		
		return response;
	}
	
	@RequestMapping(value="/repo/add/", method=RequestMethod.GET)
	@ResponseBody
	public RestResponse addFile(@RequestParam("path") String path, @RequestParam("filename") String filename) throws IOException {
		RestResponse response = new RestResponse();
		
		repoService.addFile(null, path, filename);
		
		return response;
	}
	
	@RequestMapping(value="/repo/add/", method=RequestMethod.POST)
	@ResponseBody
	public RestResponse postTest(@RequestBody DirOperation dir) {
		
		LOG.info(dir.getPath() + " " + dir.getName());
		RestResponse response = new RestResponse();
		return response;
	}
	
	/**
	 * 测试数组上传
	 * @param dirs
	 * @return
	 */
	@RequestMapping(value="/test/array", method=RequestMethod.POST)
	@ResponseBody
	public RestResponse testArray(@RequestBody List<DirOperation> dirs) {
		for (DirOperation d : dirs) {
			LOG.info("{}", d.getPath());
		}
		return new RestResponse(false);
	}
	/**
	 * 上传文件测试
	 * @param files 文件对象
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/test/upload", method=RequestMethod.POST)
	@ResponseBody
	public RestResponse testUpload(@RequestPart(name="file") MultipartFile[] files, String path) throws IOException {

		LOG.info("上传路径: {}", path);
		
		for (MultipartFile file : files) {
			String filename = file.getOriginalFilename();
			long filesize = file.getSize();
			LOG.info("文件名: {}, 大小: {}", filename, filesize);
			
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("/home/japin/woyun-repo", filename));
		}
		return new RestResponse(false);
	}
	
	@RequestMapping(value="/test/download", method=RequestMethod.GET)
	@ResponseBody
	public void testDownload(HttpServletResponse response, @RequestParam String dir, @RequestParam List<String> dirents) throws IOException {
		
		response.setContentType("multipart/form-data");
		
		if (dirents.size() == 1) {
			/* 单个文件下载 */
			String filename = dirents.get(0);
			File file = new File(dir, filename);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = response.getOutputStream();
			int b = 0;
			byte[] buffer = new byte[1024 * 1024];
			while (b != -1) {
				b = inputStream.read(buffer);
				outputStream.write(buffer, 0, b);
			}
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} else {
			/* 多个文件打包下载 */
			LOG.info(RepoPath.tmp);
		}
	}
	
	@Resource
	private RepoService RepoService;
	
	@RequestMapping(value="/test/view/history", method=RequestMethod.GET)
	public void viewHistory(@RequestParam String commit, @RequestParam String path) throws IOException {
		Repository repository = openRepository("/home/japin/woyun-repo/testgit/.git");
		RevCommit revCommit = RepoUtils.getRevCommit(repository, commit);
		RevTree tree = revCommit.getTree();
		
		if (path.isEmpty()) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {
					LOG.info("{}", treeWalk.getPathString());
				}
			}
		} else {
			try (TreeWalk treeWalk = getTreeWalk(repository, tree, path)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {
					LOG.info("{}", treeWalk.getPathString());
				}
			}
		}
	}
	
	@RequestMapping(value="/test/download/history", method=RequestMethod.GET)
	public void downloadHistory(HttpServletResponse response, @RequestParam String commit,
			@RequestParam String path, @RequestParam List<String> names)
			throws IOException {

		Repository repository = openRepository("/home/japin/woyun-repo/testgit/.git");
		RevCommit revCommit = RepoUtils.getRevCommit(repository, commit);
		RevTree tree = revCommit.getTree();
		
		// 多文件打包下载
		if (names.size() > 1) {
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilterGroup.createFromStrings(names));
				
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
		
		try (TreeWalk treeWalk = RepoUtils.getTreeWalk(repository, tree, names.get(0))) {
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
	
	public static TreeWalk getTreeWalk(Repository repository, RevTree tree, final String path) throws IOException {
		TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);
		if (treeWalk == null) {
			throw new FileNotFoundException("未找到指定的文件( " + path + ")");
		}
		return treeWalk;
	}
	
	public static Repository openRepository(String dir) throws IOException {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(dir)).readEnvironment().build();
	}
	
	public static RevCommit getRevCommit(Repository repository, String commit) throws IOException {
		try (RevWalk revWalk = new RevWalk(repository)) {
			return revWalk.parseCommit(ObjectId.fromString(commit));
		}
	}
}

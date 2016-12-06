package cn.signit.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.signit.entry.DirOperation;
import cn.signit.service.db.RepoService;
import cn.signit.untils.RepoPath;

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
}

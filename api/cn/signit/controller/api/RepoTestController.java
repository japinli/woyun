package cn.signit.controller.api;

import java.util.UUID;

import javax.annotation.Resource;

/**
 * GIT 测试
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.signit.service.db.RepoService;
import cn.signit.untils.RepoPath;

@Controller
public class RepoTestController {

	@Resource
	private RepoService repoService;
	
	@RequestMapping(value="/repo/create", method=RequestMethod.GET)
	@ResponseBody
	public RestResponse createRepository(@RequestParam("path") String path) {
		RestResponse response = new RestResponse();
		String uuid = UUID.randomUUID().toString();
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
	public RestResponse addFile(@RequestParam("path") String path, @RequestParam("filename") String filename) {
		RestResponse response = new RestResponse();
		
		repoService.addFile(null, path, filename);
		
		return response;
	}
}

package cn.signit.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.signit.domain.mysql.Repo;

@Controller
public class RestApiController {

	public final static Logger LOG = LoggerFactory.getLogger(RestApiController.class);
	
	/**
	 * 测试与服务器是否正常连接
	 */
	@RequestMapping(value=RestApiPath.ping, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse ping() {
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
	
	/**
	 * 新建仓库
	 */
	@RequestMapping(value=RestApiPath.new_repo, method=RequestMethod.POST)
	@ResponseBody
	public RestResponse createRepository() {
		LOG.info("create repository");
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
	
	/**
	 * 列出用户所包含的所有仓库信息
	 * @return
	 */
	@RequestMapping(value=RestApiPath.list_repos, method=RequestMethod.GET)
	@ResponseBody
	public RestResponse listRepositories() {
		LOG.info("list repositories");
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
	
	/**
	 * 删除指定的仓库
	 * @param id 仓库唯一标识
	 * @return
	 */
	@RequestMapping(value=RestApiPath.del_repo, method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteRepository(@PathVariable String id) {
		LOG.info("delete repository");
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
	
	/**
	 * 重命名仓库名称
	 * @param id 仓库唯一标识
	 * @param repo 序列化的仓库信息
	 * @return
	 */
	@RequestMapping(value=RestApiPath.rename_repo, method=RequestMethod.PUT)
	@ResponseBody
	public RestResponse renameRepository(@PathVariable String id, @RequestBody Repo repo) {
		LOG.info("rename {} repository to {}", id, repo.getRepoName());
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
	
	/**
	 * 在指定的仓库下新建目录
	 * @param id 仓库唯一标识
	 * @return
	 */
	@RequestMapping(value=RestApiPath.new_dir,method=RequestMethod.POST)
	@ResponseBody
	public RestResponse createDirectory(@PathVariable String id) {
		LOG.info("create directory at {}", id);
		return new RestResponse(RestStatus.SUCCESS.getStatus(), RestStatus.SUCCESS.getDesc());
	}
}

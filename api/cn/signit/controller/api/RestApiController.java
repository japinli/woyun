package cn.signit.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}

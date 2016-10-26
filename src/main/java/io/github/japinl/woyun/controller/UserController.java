package io.github.japinl.woyun.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.UserService;
import io.github.japinl.woyun.common.Response;
import io.github.japinl.woyun.common.SessionResults;
import io.github.japinl.woyun.common.StatusCode;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/checkuser")
	@ResponseBody
	public Response checkUser(@Param("name") String name) {
		Response response = Response.getInstance();
		
		boolean flag = userService.isUsernameRegister(name);

		if (flag == false) {
			response.setResultCode(StatusCode.USER_NOT_REGISTER.getCode());
			response.setResultDesc(StatusCode.USER_NOT_REGISTER.getDesc());
		} else {
			response.setResultCode(StatusCode.USER_REGISTERED.getCode());
			response.setResultDesc(StatusCode.USER_REGISTERED.getDesc());
		}
		
		response.setStatus(SessionResults.RESULT_SUCCESS);
		
		return response;
	}
}

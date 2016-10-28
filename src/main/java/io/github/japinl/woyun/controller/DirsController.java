package io.github.japinl.woyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.DirsService;
import io.github.japinl.woyun.common.WoStatus;

@Controller
public class DirsController {
	
	@Autowired
	private DirsService dirsService;
	/*
	 * @breif 列出给定目录下的所有文件及目录信息
	 */
	@RequestMapping(value = "/dirs/{dir}", method = RequestMethod.GET)
	@ResponseBody
	public WoStatus listDirs(@PathVariable("dir") String dir) {
		return new WoStatus(0);
	}
	
	/*
	 * @brief 创建目录
	 * @param dir [in] 待创建的目录名
	 * @return {"status": 0} 成功， {"status": 1} 失败
	 * */
	@RequestMapping(value = "/dirs/{dir}", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus createDir(@PathVariable("dir") String dir) {
		boolean flag = dirsService.createDirectory(dir);
		return new WoStatus(flag ? 0 : 1);
	}
	
	/*
	 * @brief 删除目录
	 * */
	@RequestMapping(value = "/dirs/{dir}", method = RequestMethod.DELETE)
	@ResponseBody
	public WoStatus deleteDir(@PathVariable("dir") String dir) {
		boolean success = dirsService.deleteDirectory(dir);
		return new WoStatus(success ? 0 : 1);
	}
}

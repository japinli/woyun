package io.github.japinl.woyun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.DirsService;
import io.github.japinl.woyun.common.WoErr;
import io.github.japinl.woyun.common.WoStatus;
import io.github.japinl.woyun.utils.FileEntry;

@Controller
public class DirsController {
	
	@Autowired
	private DirsService dirsService;
	/*
	 * @breif 列出给定目录下的所有文件及目录信息
	 */
	@RequestMapping(value = "/dirs/list", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus listDirs(@RequestBody FileEntry entry) {
		WoStatus status = new WoStatus();
		List<FileEntry> entries = dirsService.listDirectory(entry.getPath());
		
		status.setStatus(0);
		status.setErrno(WoErr.SUCCESS.getErrno());
		status.setErrinfo(WoErr.SUCCESS.getErrinfo());
		status.setData(entries);
		return status;
	}
	
	/*
	 * @brief 创建目录
	 * @param dir [in] 待创建的目录名
	 * @return {"status": 0} 成功， {"status": 1} 失败
	 * */
	@RequestMapping(value = "/dirs/create", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus createDir(@RequestBody FileEntry entry) {
		WoStatus status = new WoStatus(1);

		boolean success = dirsService.createDirectory(entry.getPath());
		if (success) {
			status.setStatus(0);
			status.setErrno(WoErr.SUCCESS.getErrno());
			status.setErrinfo(WoErr.SUCCESS.getErrinfo());
		} else {
			status.setErrno(WoErr.DIR_FAILED.getErrno());
			status.setErrinfo(WoErr.DIR_FAILED.getErrinfo());
		}
		return status;
	}
	
	/*
	 * @brief 删除目录或文件
	 * */
	@RequestMapping(value = "/dirs/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public WoStatus deleteDir(@RequestBody FileEntry entry) {
		WoStatus status = new WoStatus(1);
		
		boolean success = dirsService.deleteDirectory(entry.getPath());
		if (success) {
			status.setStatus(0);
			status.setErrno(WoErr.SUCCESS.getErrno());
			status.setErrinfo(WoErr.SUCCESS.getErrinfo());
		} else {
			status.setErrno(WoErr.DIR_DEL_FAILED.getErrno());
			status.setErrinfo(WoErr.DIR_DEL_FAILED.getErrinfo());
		}
		return status;
	}
}

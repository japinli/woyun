package io.github.japinl.woyun.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.service.DirsService;
import io.github.japinl.woyun.common.UrlPath;
import io.github.japinl.woyun.common.WoErr;
import io.github.japinl.woyun.common.WoStatus;
import io.github.japinl.woyun.utils.FileEntry;

@Controller
@RequestMapping(value = UrlPath.REST_DIR_PATH)
public class DirsController {
	
	@Autowired
	private DirsService dirsService;
	
	/*
	 * @brief 创建目录
	 * */
	@RequestMapping(value = "/**", method = RequestMethod.POST)
	@ResponseBody
	public WoStatus createDirs(HttpServletRequest request) {
		WoStatus status = new WoStatus(1);
		String url = request.getRequestURI();
		String path = url.substring(url.indexOf(UrlPath.REST_DIR_PATH) + UrlPath.REST_DIR_PATH.length());
		
		if (dirsService.exists(path)) {
			status.setErrno(WoErr.FILE_EXIST.getErrno());
			status.setErrinfo(WoErr.FILE_EXIST.getErrinfo());
			return status;
		}
		
		boolean success = dirsService.createDirectory(path);
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
	
	@RequestMapping(value = "/**", method = RequestMethod.DELETE)
	@ResponseBody
	public WoStatus deleteDirs(HttpServletRequest request) {
		WoStatus status = new WoStatus(1);
		String url = request.getRequestURI();
		String path = url.substring(url.indexOf(UrlPath.REST_DIR_PATH) + UrlPath.REST_DIR_PATH.length());
		
		if (!dirsService.exists(path)) {
			status.setErrno(WoErr.FILE_NOT_EXIST.getErrno());
			status.setErrinfo(WoErr.FILE_NOT_EXIST.getErrinfo());
			return status;
		}
		
		boolean success = dirsService.deleteDirectory(path);
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
	
	/*
	 * @brief 列出指定目录下的所有文件信息
	 */
	@RequestMapping(value = "/**", method = RequestMethod.GET)
	@ResponseBody
	public WoStatus listDirs(HttpServletRequest req) {
		WoStatus status = new WoStatus(1);
		String url = req.getRequestURI();
		String path = url.substring(url.indexOf(UrlPath.REST_DIR_PATH) + UrlPath.REST_DIR_PATH.length());
		
		if (!dirsService.exists(path)) {
			status.setErrno(WoErr.FILE_NOT_EXIST.getErrno());
			status.setErrinfo(WoErr.FILE_NOT_EXIST.getErrinfo());
			return status;
		}
		
		List<FileEntry> entries = dirsService.listDirectory(path);
		status.setStatus(0);
		status.setErrno(WoErr.SUCCESS.getErrno());
		status.setErrinfo(WoErr.SUCCESS.getErrinfo());
		status.setData(entries);
		return status;
	}
}

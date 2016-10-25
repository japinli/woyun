package io.github.japinl.woyun.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.japinl.woyun.utils.HDFSUtil;

@Controller
public class FileController {
	
	@RequestMapping(value = "/create-directory", method = RequestMethod.GET, params = "directory")
	@ResponseBody
	public Map<String, Object> createDirectory(@RequestParam String directory) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean ok = HDFSUtil.createDirectory(directory);
		if (ok == true) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		return result;
	}
	
	@RequestMapping(value = "/create-file", method = RequestMethod.GET, params = "filename")
	@ResponseBody
	public Map<String, Object> createFile(@RequestParam String filename) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean ok = HDFSUtil.createFile(filename);
		if (ok == true) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		return result;
	}
	
	@RequestMapping(value = "/delete-directory", method = RequestMethod.GET, params = "directory")
	@ResponseBody
	public Map<String, Object> deleteDirectory(@RequestParam String directory) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean ok = HDFSUtil.deleteDirectory(directory);
		if (ok == true) {
			result.put("status", 0);
		} else {
			result.put("status", 1);
		}
		return result;
	}
}

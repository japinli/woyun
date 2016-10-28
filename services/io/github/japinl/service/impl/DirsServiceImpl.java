package io.github.japinl.service.impl;

import org.springframework.stereotype.Service;

import io.github.japinl.service.DirsService;
import io.github.japinl.woyun.utils.HDFSUtil;

/*
 * TODO: 添加实际的目录操作(HDFS)
 * */

@Service
public class DirsServiceImpl implements DirsService {
	
	public boolean createDirectory(String name) {
		return HDFSUtil.createDirectory(name);
	}
	
	public boolean deleteDirectory(String name) {
		return HDFSUtil.deleteDirectory(name);
	}
}

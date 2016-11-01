package io.github.japinl.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.japinl.service.DirsService;
import io.github.japinl.woyun.utils.FileEntry;
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
	
	public List<FileEntry> listDirectory(String path) {
		return HDFSUtil.listDirectory(path);
	}
}

package io.github.japinl.service;

import java.util.List;

import io.github.japinl.woyun.utils.FileEntry;

public interface DirsService {
	boolean createDirectory(String name);
	boolean deleteDirectory(String name);
	boolean exists(String path);
	List<FileEntry> listDirectory(String path);
}

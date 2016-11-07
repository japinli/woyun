package io.github.japinl.woyun.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {
	private final static String HDFS_URL = "hdfs://localhost:9000";
	// private final static long serialVersionUID = 1L;
	private final static Configuration HDFS_CONF = new Configuration();

	/*
	 * @brief 创建目录
	 * 
	 * @param path 目录名
	 * 
	 * @return 成功返回 true ，否则返回 false
	 */
	public static boolean createDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.mkdirs(new Path(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * @brief 删除目录
	 * 
	 * @param path 目录名
	 * 
	 * @return 成功返回 true ，否则返回 false
	 */
	public static boolean deleteDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.delete(new Path(path), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<FileEntry> listDirectory(String path) {
		List<FileEntry> entries = new ArrayList<FileEntry>();
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			if (fileSystem.isDirectory(new Path(path))) {
				FileStatus[] fileStatus = fileSystem.listStatus(new Path(path));
				for (FileStatus status : fileStatus) {
					FileEntry entry = new FileEntry();
					entry.setPath(status.getPath().toUri().getPath().toString());
					entry.setFilename(status.getPath().getName());
					entry.setIsdir(status.isDirectory());
					entry.setMtime(status.getModificationTime());
					entry.setSize(status.getLen());
					entries.add(entry);
				}
			} else {
				FileStatus status = fileSystem.getFileStatus(new Path(path));
				FileEntry entry = new FileEntry();
				entry.setPath(path);
				entry.setFilename(status.getPath().getName());
				entry.setIsdir(status.isDirectory());
				entry.setMtime(status.getModificationTime());
				entry.setSize(status.getLen());
				entries.add(entry);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return entries;
	}
	
	public static boolean exists(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.exists(new Path(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}

package io.github.japinl.woyun.utils;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {
	private final static String HDFS_URL = "hdfs://localhost:9000";
//	private final static long serialVersionUID = 1L;
	private final static Configuration HDFS_CONF = new Configuration();
	
	/*
	 * @brief 创建目录
	 * @param path 目录名
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
	 * @param path 目录名
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
}

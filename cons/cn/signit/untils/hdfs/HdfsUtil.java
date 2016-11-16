package cn.signit.untils.hdfs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.signit.conf.ConfigProps;


public class HdfsUtil {
	
	private final static Logger LOG = LoggerFactory.getLogger(HdfsUtil.class);
	private final static String HDFS_URL = ConfigProps.get("hadoop.hadoop_url");
	private final static Configuration HDFS_CONF = new Configuration();
	
	/**
	 * 新建目录
	 * @param path 目录路径
	 * @return 创建成功返回true，否则返回false
	 */
	public static boolean newDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.mkdirs(new Path(path));
		} catch (IOException e) {
			LOG.error("new direcory " + path + " failed!");
		}
		return false;
	}
	
	/**
	 * 删除目录
	 * @param path 待删除的目录路径
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.delete(new Path(path), true);
		} catch (IOException e) {
			LOG.error("delete direcory " + path + " failed!");
		}
		return false;
	}
}

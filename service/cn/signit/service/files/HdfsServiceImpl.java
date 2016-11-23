package cn.signit.service.files;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import cn.signit.conf.ConfigProps;

@Service("hdfsServiceImpl")
public class HdfsServiceImpl implements HdfsService {

	private final static String HDFS_URL = ConfigProps.get("hadoop.url");
	private final static Configuration HDFS_CONF = new Configuration();
	
	public String createDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			if (fileSystem.mkdirs(new Path(path))) {
				FileStatus status = fileSystem.getFileStatus(new Path(path));
				return status.getPath().toUri().getPath().toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteDirectory(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(HDFS_URL), HDFS_CONF);
			return fileSystem.delete(new Path(path), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}

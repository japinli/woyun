package cn.signit.service.files;

public interface HdfsService {

	String createDirectory(String path);
	boolean deleteDirectory(String path);
}

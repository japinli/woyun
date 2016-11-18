package cn.signit.hdfs;

public class FileItem {
	
	private String path;   // 文件路径
	private String name;   // 文件名
	private long mtime;    // 文件修改时间
	private long size;     // 文件大小
	private boolean isdir; // 是否是目录
	
	public FileItem() {
		
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isIsdir() {
		return isdir;
	}

	public void setIsdir(boolean isdir) {
		this.isdir = isdir;
	}
}

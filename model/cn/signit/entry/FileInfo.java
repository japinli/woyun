package cn.signit.entry;

public class FileInfo {

	private String path;
	private String type;
	private String filename;
	private Long size;
	private Long mtime;
	
	public FileInfo(String type, String filename, Long size, Long mtime) {
		this.type = type;
		this.filename = filename;
		this.size = size;
		this.mtime = mtime;
	}
	public FileInfo(String path, String type, String filename, Long size, Long mtime) {
		this(type, filename, size, mtime);
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getMtime() {
		return mtime;
	}
	public void setMtime(long mtime) {
		this.mtime = mtime;
	}
}

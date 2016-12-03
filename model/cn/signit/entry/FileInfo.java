package cn.signit.entry;

public class FileInfo {

	private String type;
	private String filename;
	private long size;
	private long mtime;
	
	public FileInfo(String type, String filename, long size, long mtime) {
		this.type = type;
		this.filename = filename;
		this.size = size;
		this.mtime = mtime;
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

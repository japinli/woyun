package io.github.japinl.woyun.utils;

public class FileEntry {

	private String path;
	private String filename;
	private long mtime;
	private long size;
	private boolean isdir;
	
	public FileEntry() {
		
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
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

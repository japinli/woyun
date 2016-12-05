package cn.signit.entry;

public class DirOperation {

	/**
	 * operation:
	 * mkdir - create a new directory.
	 * rename - rename the directory.
	 * move - move a directory or file into another directory.
	 * copy - copy a directory or file into another directory.
	 */
	private String operation;
	private String path;
	private String name;
	private String newName;
	
	private String srcRepoId;
	private String dstRepoId;
	private String srcPath;
	private String dstPath;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
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
	
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getSrcRepoId() {
		return srcRepoId;
	}
	public void setSrcRepoId(String srcRepoId) {
		this.srcRepoId = srcRepoId;
	}
	
	public String getDstRepoId() {
		return dstRepoId;
	}
	public void setDstRepoId(String dstRepoId) {
		this.dstRepoId = dstRepoId;
	}
	
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	
	public String getDstPath() {
		return dstPath;
	}
	public void setDstPath(String dstPath) {
		this.dstPath = dstPath;
	}
	

}

package cn.signit.entry;

public class DirOperation {

	/**
	 * operation:
	 * mkdir - create a new directory.
	 * rename - rename the directory.
	 */
	private String operation;
	private String path;
	private String name;
	private String newName;
	
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

}

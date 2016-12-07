package cn.signit.entry;

public class CommitHistory {

	private String commitId;
	private String committer;
	private String email;
	private String message;
	private long commitTime;
	
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	
	public String getCommitter() {
		return committer;
	}
	public void setCommitter(String committer) {
		this.committer = committer;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public long getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(long commitTime) {
		this.commitTime = commitTime;
	}

}

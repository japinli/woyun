package cn.signit.entry;

import java.util.Date;

import cn.signit.domain.mysql.Repo;

public class RepoInfo extends Repo {
	
	private Long modifyTime;

	private Long repoSize;

	public RepoInfo() {
		
	}
	
	public RepoInfo(Repo repo) {
		this.setId(repo.getId());
		this.setRepoId(repo.getRepoId());
		this.setRepoName(repo.getRepoName());
		this.setUserEmail(repo.getUserEmail());
		this.setCreateTime(repo.getCreateTime());
		this.setState(repo.getState());
		this.setDeleteTime(repo.getDeleteTime());
	}
	
	public Long getModifyTime() {
		return modifyTime;
	}
	
	public void setModifyTime(Date date) {
		this.modifyTime = date.getTime();
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getRepoSize() {
		return repoSize;
	}

	public void setRepoSize(Long repoSize) {
		this.repoSize = repoSize;
	}
}

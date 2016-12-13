package cn.signit.domain.mysql;

import java.util.Calendar;
import java.util.Date;

public class Repo {
    private Long id;

    private String repoId;

    private String repoName;

    private String userEmail;

    private Date createTime;

    private Boolean state;

    private Date deleteTime;
    
    private Long leftDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId == null ? null : repoId.trim();
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName == null ? null : repoName.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
    
    public Long getLeftDays() {
		return leftDays;
	}
	public void setLeftDays(Long leftDays) {
		this.leftDays = leftDays;
	}
    
    /**
     * 仓库构造函数
     * @param repoId 仓库唯一标识
     * @param repoName 仓库名
     * @param userEmail 用户邮件
     */
    public Repo(String repoId, String repoName, String userEmail) {
    	this.repoId = repoId;
    	this.repoName = repoName;
    	this.userEmail = userEmail;
    	this.state = false;
    	this.createTime = Calendar.getInstance().getTime();
    }
    
    public Repo() {
    	
    }
}
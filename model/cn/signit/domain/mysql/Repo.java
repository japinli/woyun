package cn.signit.domain.mysql;

public class Repo {
    private String repoId;

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId == null ? null : repoId.trim();
    }
}
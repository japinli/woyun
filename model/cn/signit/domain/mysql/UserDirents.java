package cn.signit.domain.mysql;

public class UserDirents {
    private Long id;

    private Long uid;

    private String path;

    private String ppath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getPpath() {
        return ppath;
    }

    public void setPpath(String ppath) {
        this.ppath = ppath == null ? null : ppath.trim();
    }
}
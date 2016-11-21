package cn.signit.domain.mysql;

public class UserFiles {
    private Long id;

    private Long uid;

    private String dir;

    private String locationDir;

    private String filename;

    private Integer mtime;

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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir == null ? null : dir.trim();
    }

    public String getLocationDir() {
        return locationDir;
    }

    public void setLocationDir(String locationDir) {
        this.locationDir = locationDir == null ? null : locationDir.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public Integer getMtime() {
        return mtime;
    }

    public void setMtime(Integer mtime) {
        this.mtime = mtime;
    }
}
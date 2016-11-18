package cn.signit.domain.mysql;

import java.util.Date;

import cn.signit.cons.OriginalSerialCodeMaker;

public class User {
    private Long id;

    private String originalSerialCode;

    private String email;

    private String realName;

    private String password;

    private String phone;

    private String locationCode;

    private Date registDate;

    private String ip;

    private String randomCode;

    private Boolean activated;

    private String rootDir;

    private String rootDirName;

    private String portrait;

    private Integer level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalSerialCode() {
        return originalSerialCode;
    }

    public void setOriginalSerialCode(String originalSerialCode) {
        this.originalSerialCode = originalSerialCode == null ? null : originalSerialCode.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode == null ? null : locationCode.trim();
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode == null ? null : randomCode.trim();
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir == null ? null : rootDir.trim();
    }

    public String getRootDirName() {
        return rootDirName;
    }

    public void setRootDirName(String rootDirName) {
        this.rootDirName = rootDirName == null ? null : rootDirName.trim();
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait == null ? null : portrait.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public String getNormalOrigiSerialCode() {
		return OriginalSerialCodeMaker.getSecretMake(this.originalSerialCode);
	}
    
    public String availableUserName(){
    	if (getRealName() != null && !getRealName().trim().equals("")) {
    		return getRealName();
    	} else if (getEmail() != null && !getEmail().trim().equals("")) {
			return getEmail();
		} else if (getPhone() != null && !getPhone().trim().equals("")) {
			return getPhone();
		}
		return "unknown";
	}
}
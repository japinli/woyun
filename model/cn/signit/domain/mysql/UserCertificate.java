package cn.signit.domain.mysql;

import java.io.InputStream;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserCertificate {
    private Long id;

    private String authCode;

    private String refCode;

    private String serialCode;

    private Date startTime;

    private Date endTime;

    private String fileId;

    private String descInfo;

    private String subject;

    private String issuer;

    private String storePassword;

    private String keyPassword;

    private Long validDay;

    private Long userId;

    private String md5;

    private byte[] data;
    
    //附加certificate真实数据输入流
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private InputStream certificateStream;
    
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private Integer offset;//业务字段
    
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private Integer limit;//业务字段
    
    public void attachCertStream(InputStream in){
    	this.certificateStream=in;
    }
    
    public InputStream getCertStream(){
    	return certificateStream;
    }
    
    public UserCertificate(){
    	
    }
    public UserCertificate(String authCode,String refCode,String serialCode,
    		String subject,String isSuer){
    	this.authCode=authCode;
    	this.refCode=refCode;
    	this.serialCode=serialCode;
    	this.subject=subject;
    	this.issuer=isSuer;
    }

    public Long getId() {
        return id;
    }

    public UserCertificate setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }

    public UserCertificate setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
        return this;
    }

    public String getRefCode() {
        return refCode;
    }

    public UserCertificate setRefCode(String refCode) {
        this.refCode = refCode == null ? null : refCode.trim();
        return this;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public UserCertificate setSerialCode(String serialCode) {
        this.serialCode = serialCode == null ? null : serialCode.trim();
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public UserCertificate setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public UserCertificate setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getFileId() {
        return fileId;
    }

    public UserCertificate setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
        return this;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public UserCertificate setDescInfo(String descInfo) {
        this.descInfo = descInfo == null ? null : descInfo.trim();
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public UserCertificate setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public UserCertificate setIssuer(String issuer) {
        this.issuer = issuer == null ? null : issuer.trim();
        return this;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public UserCertificate setStorePassword(String storePassword) {
        this.storePassword = storePassword == null ? null : storePassword.trim();
        return this;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public UserCertificate setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword == null ? null : keyPassword.trim();
        return this;
    }

    public Long getValidDay() {
        return validDay;
    }

    public UserCertificate setValidDay(Long validDay) {
        this.validDay = validDay;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserCertificate setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public UserCertificate setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public UserCertificate setData(byte[] data) {
        this.data = data;
        return this;
    }

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
    
    
}
package cn.signit.domain.mysql;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.signit.utils.time.StandardTimesFactory;

public class EvidenceInfo {
    private Long id;

    private String name;

    private Double size;

    /**
     * 记录时间(以时间戳服务器时间为准)
     */
    private Date time;

    /**
     * 对应文件数据库位置
     */
    private String location;

    private String originalMd5;

    private String serialCode;

    private Long blockId;
    
    private Boolean isEncrypted;

    private String encryptAlgorithm;

    /**
     * 存放经过Base64编码后的P7签名
     */
    private byte[] userSign;

    private byte[] tsaAuth;
    //附加location真实数据输入流
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private InputStream originalStream;
    
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private Integer offset;//业务字段
    
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private Integer limit;//业务字段
    
    /**
     * 为了制作默克尔树,散列算法由merkle树统一指定
     */
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private String hashValue;
    
    
    public EvidenceInfo attachFile(InputStream is){
    	this.originalStream=is;
    	return this;
    }
    
    /**
     * 添加时间戳
     */
    public EvidenceInfo attachTimeStamp(byte[] timestamps){
    	this.tsaAuth=timestamps;
    	return this;
    }
    
    public EvidenceInfo init(){
    	this.serialCode=UUID.randomUUID().toString();
    	this.time=StandardTimesFactory.getDefault().getStandardDate();
    	return this;
    }
    
    public Long getId() {
        return id;
    }

    public EvidenceInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EvidenceInfo setName(String name) {
        this.name = name == null ? null : name.trim();
        return this;
    }

    public Double getSize() {
        return size;
    }

    public EvidenceInfo setSize(Double size) {
        this.size = size;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public EvidenceInfo setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public EvidenceInfo setLocation(String location) {
        this.location = location == null ? null : location.trim();
        return this;
    }

    public String getOriginalMd5() {
        return originalMd5;
    }

    public EvidenceInfo setOriginalMd5(String originalMd5) {
        this.originalMd5 = originalMd5 == null ? null : originalMd5.trim();
        return this;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public EvidenceInfo setSerialCode(String serialCode) {
        this.serialCode = serialCode == null ? null : serialCode.trim();
        return this;
    }

    public Long getBlockId() {
        return blockId;
    }

    public EvidenceInfo setBlockId(Long blockId) {
        this.blockId = blockId;
        return this;
    }

	public Boolean getIsEncrypted() {
		return isEncrypted;
	}

	public EvidenceInfo setIsEncrypted(Boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
		return this;
	}

	public String getEncryptAlgorithm() {
		return encryptAlgorithm;
	}

	public EvidenceInfo setEncryptAlgorithm(String encryptAlgorithm) {
		this.encryptAlgorithm = encryptAlgorithm;
		return this;
	}

	public byte[] getUserSign() {
		return userSign;
	}

	public EvidenceInfo setUserSign(byte[] userSign) {
		this.userSign = userSign;
		return this;
	}

	public byte[] getTsaAuth() {
		return tsaAuth;
	}

	public EvidenceInfo setTsaAuth(byte[] tsaAuth) {
		this.tsaAuth = tsaAuth;
		return this;
	}

	public InputStream getOriginalStream() {
		return originalStream;
	}

	public EvidenceInfo setOriginalStream(InputStream originalStream) {
		this.originalStream = originalStream;
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

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
    
	
    
}
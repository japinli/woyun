package cn.signit.domain.mysql;

import java.util.Date;

public class ProveInfo {
    private Long id;

    private Long userId;

    private Integer type;

    private String name;

    private String phone;

    private String email;

    private String registCode;

    private String orgCode;

    private Integer fillInPerson;

    private String legalPersonName;

    private String legalPersonIdCardCode;

    private Integer legalPersonIdCardValid;

    private Date legalPersonIdCardValidDate;

    private String agentName;

    private String agentIdCardCode;

    private Integer agentIdCardValid;

    private Date agentIdCardValidDate;

    private String businessLicencePic;

    private String orgCodePic;

    private Integer legalPersonIdCardType;

    private String legalPersonIdCardPic1;

    private String legalPersonIdCardPic2;

    private Integer agentIdCardType;

    private String agentIdCardPic1;

    private String agentIdCardPic2;

    private String agentTrustInstrumentPic;

    private Date submitDate;

    private Date passDate;

    private Integer state;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRegistCode() {
        return registCode;
    }

    public void setRegistCode(String registCode) {
        this.registCode = registCode == null ? null : registCode.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Integer getFillInPerson() {
        return fillInPerson;
    }

    public void setFillInPerson(Integer fillInPerson) {
        this.fillInPerson = fillInPerson;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName == null ? null : legalPersonName.trim();
    }

    public String getLegalPersonIdCardCode() {
        return legalPersonIdCardCode;
    }

    public void setLegalPersonIdCardCode(String legalPersonIdCardCode) {
        this.legalPersonIdCardCode = legalPersonIdCardCode == null ? null : legalPersonIdCardCode.trim();
    }

    public Integer getLegalPersonIdCardValid() {
        return legalPersonIdCardValid;
    }

    public void setLegalPersonIdCardValid(Integer legalPersonIdCardValid) {
        this.legalPersonIdCardValid = legalPersonIdCardValid;
    }

    public Date getLegalPersonIdCardValidDate() {
        return legalPersonIdCardValidDate;
    }

    public void setLegalPersonIdCardValidDate(Date legalPersonIdCardValidDate) {
        this.legalPersonIdCardValidDate = legalPersonIdCardValidDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName == null ? null : agentName.trim();
    }

    public String getAgentIdCardCode() {
        return agentIdCardCode;
    }

    public void setAgentIdCardCode(String agentIdCardCode) {
        this.agentIdCardCode = agentIdCardCode == null ? null : agentIdCardCode.trim();
    }

    public Integer getAgentIdCardValid() {
        return agentIdCardValid;
    }

    public void setAgentIdCardValid(Integer agentIdCardValid) {
        this.agentIdCardValid = agentIdCardValid;
    }

    public Date getAgentIdCardValidDate() {
        return agentIdCardValidDate;
    }

    public void setAgentIdCardValidDate(Date agentIdCardValidDate) {
        this.agentIdCardValidDate = agentIdCardValidDate;
    }

    public String getBusinessLicencePic() {
        return businessLicencePic;
    }

    public void setBusinessLicencePic(String businessLicencePic) {
        this.businessLicencePic = businessLicencePic == null ? null : businessLicencePic.trim();
    }

    public String getOrgCodePic() {
        return orgCodePic;
    }

    public void setOrgCodePic(String orgCodePic) {
        this.orgCodePic = orgCodePic == null ? null : orgCodePic.trim();
    }

    public Integer getLegalPersonIdCardType() {
        return legalPersonIdCardType;
    }

    public void setLegalPersonIdCardType(Integer legalPersonIdCardType) {
        this.legalPersonIdCardType = legalPersonIdCardType;
    }

    public String getLegalPersonIdCardPic1() {
        return legalPersonIdCardPic1;
    }

    public void setLegalPersonIdCardPic1(String legalPersonIdCardPic1) {
        this.legalPersonIdCardPic1 = legalPersonIdCardPic1 == null ? null : legalPersonIdCardPic1.trim();
    }

    public String getLegalPersonIdCardPic2() {
        return legalPersonIdCardPic2;
    }

    public void setLegalPersonIdCardPic2(String legalPersonIdCardPic2) {
        this.legalPersonIdCardPic2 = legalPersonIdCardPic2 == null ? null : legalPersonIdCardPic2.trim();
    }

    public Integer getAgentIdCardType() {
        return agentIdCardType;
    }

    public void setAgentIdCardType(Integer agentIdCardType) {
        this.agentIdCardType = agentIdCardType;
    }

    public String getAgentIdCardPic1() {
        return agentIdCardPic1;
    }

    public void setAgentIdCardPic1(String agentIdCardPic1) {
        this.agentIdCardPic1 = agentIdCardPic1 == null ? null : agentIdCardPic1.trim();
    }

    public String getAgentIdCardPic2() {
        return agentIdCardPic2;
    }

    public void setAgentIdCardPic2(String agentIdCardPic2) {
        this.agentIdCardPic2 = agentIdCardPic2 == null ? null : agentIdCardPic2.trim();
    }

    public String getAgentTrustInstrumentPic() {
        return agentTrustInstrumentPic;
    }

    public void setAgentTrustInstrumentPic(String agentTrustInstrumentPic) {
        this.agentTrustInstrumentPic = agentTrustInstrumentPic == null ? null : agentTrustInstrumentPic.trim();
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getPassDate() {
        return passDate;
    }

    public void setPassDate(Date passDate) {
        this.passDate = passDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}
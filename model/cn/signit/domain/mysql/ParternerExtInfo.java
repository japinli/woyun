package cn.signit.domain.mysql;

import java.io.Serializable;
import java.util.Date;

public class ParternerExtInfo implements Serializable{

	private static final long serialVersionUID = -1361236376065820488L;

	private Long id;

    private String partenerId;

    private String notifyUrl;

    private String serverIp;

    private Date lastContact;

    private String mark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartenerId() {
        return partenerId;
    }

    public void setPartenerId(String partenerId) {
        this.partenerId = partenerId == null ? null : partenerId.trim();
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    public Date getLastContact() {
        return lastContact;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }
}
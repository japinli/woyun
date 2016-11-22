/**
* @author ZhangHongdong
* @date 2015年9月16日-下午1:53:07
* @see (参阅)
*/
package cn.signit.service.signca.wapper;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

/**
 * 对证书申请的DN字段的封装
 * @ClassName DNRequestWapper
 * @author ZhangHongdong,
 * @date 2015年9月16日-下午1:53:07
 * @author Liwen
 * @version 1.1.0
 */
public class DNRequestWrapper {
	/** 用户真实姓名 */
	private String realName;
	/** 用户组织单位 */
	private String orgUnit;
	/** 用户组织名称 */
	private String orgName;
	/** 用户所在区/市/县 */
	private String locName;
	/** 用户所在省份 */
	private String stateName;
	/** 用户所在国家,为国家名称的2个字英文字母的简称（如：中国==CN）*/
	private String country;
	/**用户邮件地址*/
	private String email;
	/**用户验证的手机号*/
	private String phone;
	
	public DNRequestWrapper() {
		this.realName = "undefined";
		this.orgUnit = "";
		this.orgName="";
		this.locName="";
		this.stateName="";
		this.country = "CN";
		this.email="";
		this.phone="";
	}
	
	/**
	 *  构造方法
	 * @param realName 真实姓名-CN
	 */
	public DNRequestWrapper(String realName) {
		this(realName, null, null, null, null, null);
	}
	/**
	 *  构造方法
	 * @param realName 真实姓名-CN
	 * @param email 邮件地址-E
	 */
	public DNRequestWrapper(String realName,String email) {
		this(realName, null,null,null,null,"CN",email);
	}
	
	/**
	 *  构造方法
	 * @param realName 真实姓名-CN
	 * @param orgUnit 单位-OU
	 * @param orgName 组织-O
	 * @param locName 市/区-L
	 * @param stateName  州/省-ST
	 */
	public DNRequestWrapper(String realName, String orgUnit, String orgName,
			String locName, String stateName){
		this(realName, orgUnit, orgName, locName, stateName,null);
	}
	
	/**
	 *  构造方法
	 * @param realName 真实姓名-CN
	 * @param orgUnit 单位-OU
	 * @param orgName 组织-O
	 * @param locName 市/区-L
	 * @param stateName  州/省-ST
	 * @param country 国家-C
	 */
	public DNRequestWrapper(String realName, String orgUnit, String orgName,
			String locName, String stateName, String country) {
		this(realName,orgUnit,orgName,locName,stateName,country,null);
	}
	/**
	 *  构造方法
	 * @param realName 真实姓名-CN
	 * @param orgUnit 单位-OU
	 * @param orgName 组织-O
	 * @param locName 市/区-L
	 * @param stateName  州/省-ST
	 * @param country 国家-C
	 * @param email 邮件地址-E
	 */
	public DNRequestWrapper(String realName, String orgUnit, String orgName,
			String locName, String stateName, String country,String email) {
		this(realName,orgUnit,orgName,locName,stateName,country,email,null);
	}
	/**
	 * 构造方法
	 * @param realName 真实姓名-CN
	 * @param orgUnit 单位-OU
	 * @param orgName 组织-O
	 * @param locName 市/区-L
	 * @param stateName  州/省-ST
	 * @param country 国家-C
	 * @param email 邮件地址-E
	 * @param phone 联系电话-P
	 */
	public DNRequestWrapper(String realName, String orgUnit, String orgName,
			String locName, String stateName, String country,String email,String phone) {
		this();
		this.realName = realName;
		this.orgUnit = orgUnit;
		this.orgName = orgName;
		this.locName = locName;
		this.stateName = stateName;
		if(!isEmpty(country)){
			this.country = country;
		}
		this.email=email;
		this.phone=phone;
	}
	
	/**
	 * 构建X500Name
	 * @return X500Name
	 */
	public X500Name toX500DN(){
		X500NameBuilder builder=new X500NameBuilder();
		if(!isEmpty(realName)){
			builder.addRDN(BCStyle.CN, realName);
		}
		if(!isEmpty(orgUnit)){
			builder.addRDN(BCStyle.OU, orgUnit);
		}
		if(!isEmpty(orgName)){
			builder.addRDN(BCStyle.O, orgName);
		}
		if(!isEmpty(locName)){
			builder.addRDN(BCStyle.L, locName);
		}
		if(!isEmpty(stateName)){
			builder.addRDN(BCStyle.ST, stateName);
		}
		if(!isEmpty(country)){
			builder.addRDN(BCStyle.C, country);
		}
		if(!isEmpty(email)){
			builder.addRDN(BCStyle.E, email);
		}
		if(!isEmpty(phone)){
			builder.addRDN(BCStyle.TELEPHONE_NUMBER, phone);
		}
		return builder.build();
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getOrgUnit() {
		return orgUnit;
	}
	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLocName() {
		return locName;
	}
	public void setLocName(String locName) {
		this.locName = locName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	private boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
}

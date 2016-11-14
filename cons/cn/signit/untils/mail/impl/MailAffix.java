/**
 * @author:Liwen
 * @date:2016年3月24日-下午1:23:05
 * @see: (参阅)
 */
package cn.signit.untils.mail.impl;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: MailAffix
 * @author: Liwen   
 * @date:2016年3月24日-下午1:23:05 
 * @version:(版本号)
 * @see: (参阅)
 */
public class MailAffix {
	 private String affixpath =null; //附件地址
	 private byte[] affixbyte;//附件字节流
	 private String affixbyteType;//附件字节流类型
	 private String affixName = ""; //附件名称
	 
	 public MailAffix(String affixpath,String affixName){
		 this.affixpath=affixpath;
		 this.affixName=affixName;
	 }
	 
	 public MailAffix(byte[] affixbyte,String mimeType,String affixName){
		 this.affixbyte=affixbyte;
		 this.affixbyteType=mimeType;
		 this.affixName=affixName;
		 
	 }
	 
	 public MailAffix(File file){
		 if(file.isFile()){
			 this.affixpath=file.getAbsolutePath();
			 this.affixName=file.getName();
		 }
	 }
	 
	public String getaffixpath() {
		return affixpath;
	}
	public void setaffixpath(String affixpath) {
		this.affixpath =affixpath;
	}
	public byte[] getAffixbyte() {
		return affixbyte;
	}
	public void setAffixbyte(byte[] affixbyte) {
		this.affixbyte = affixbyte;
	}
	public String getAffixbyteType() {
		return affixbyteType;
	}
	public void setAffixbyteType(String affixbyteType) {
		this.affixbyteType = affixbyteType;
	}
	public String getAffixName() {
		return affixName;
	}
	public void setAffixName(String affixName) {
		this.affixName = affixName;
	}
	 
	 
}

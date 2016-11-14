/**
* @author ZhangHongdong
* @date 2015年7月24日-上午11:43:56
* @see (参阅)
*/
package cn.signit.domain.mongo.file;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 *映射文件存储数据库的通用项
 * @ClassName LocalStoreFile
 * @author ZhangHongdong
 * @date 2015年7月24日-上午11:43:56
 * @version 1.1.0
 */
public class LocalStoreFile implements Cloneable{
	
	public Object clone(){
		LocalStoreFile o = null;
		try{
			o = (LocalStoreFile)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		 }
		 return o;
	} 
	
	/**
	*文件名
	*
	*@Fields filename 文件名
	*/
	@Transient
	protected String filename;
	
	/**
	*文件大小（单位：字节(byte)）
	*
	*@Fields length 文件大小（单位：字节(byte)）
	*/
	@Transient
	protected Long length;
	
	/**
	*文件分割块的大小（默认：262144字节（256KB）；单位：字节(byte)）
	*
	*@Fields chunkSize 文件分割块的大小（默认：262144字节（256KB）；单位：字节(byte)）
	*/
	@Transient
	protected Long chunkSize = 262144l;
	
	/**
	*文件上传日期
	*
	*@Fields uploadDate 上传日期
	*/
	@Transient
	protected Date uploadDate;
	
	/**
	*文件MD5值
	*
	*@Fields md5 文件MD5值
	*/
	@Transient
	protected String md5;
	
	/**
	*文件的MIME类型
	*
	*@Fields contentType 文件的MIME类型
	*/
	@Transient
	protected String contentType;
	
	/**
	* 文件别名
	*
	*@Fields aliases 文件别名
	*/
	protected List<String> aliases;
	
	/**
	 * 映射文件数据库中该文件的"_id"键
	 *
	 * @Fields id 映射文件数据库中该文件的"_id"键
	 */
	@Indexed(unique = true)
	@Id
	protected Object id;
	
	/**
	*备份id,映射文件数据库中该备份文件的"_id"键
	*
	*@Fields backupId 备份文件id,映射文件数据库中该备份文件的"_id"键
	*/
	@Indexed(unique = true)
	protected Object backupId;
	
	/**
	*自定义id,映射文件数据库中的MetaData"_id"键
	*
	*@Fields customId 自定义文件id,映射文件数据库中MetaData的"_id"键
	*/
	@Transient
	protected Object customId;

	/**
	 * 将要映射到的本地路径
	 *
	 * @Fields path 将要映射到的本地路径
	 */
	@Indexed(unique = true)
	protected String path;
	
	/**
	*文件类型编码
	*
	*@Fields typeCode 文件类型编码（默认值10000,代表该文件的类型为普通的文件）
	*/
	protected Integer typeCode = 10000; 
	
	/**
	 * 将要映射到指定的URI
	 *
	 * @Fields path 将要映射到的URI
	 */
	@Indexed(unique = true)
	protected String uri;
	
	/**
	*数据内容输入流
	*
	*@Fields contentStream  内容输入流
	*/
	@Transient
	protected InputStream contentStream;
	
	/**
	 * 文件加入网络的时间戳
	 * @Fields timeStamp 时间戳
	 */
	@Indexed(unique = true)
	protected String timesStamp;
	
	/**
	 * 文件的用户签名
	 * @Fields signature 签名
	 */
	@Indexed(unique = true)
	protected String signatures;
	
	public LocalStoreFile(){
		
	}
	
	
	/**
	*用于存储文件时的构造方法
	*/
	@PersistenceConstructor
	public LocalStoreFile(String filename, String contentType, List<String> aliases,
			String path, Integer typeCode, String uri, InputStream contentStream) {
		super();
		this.filename = filename;
		this.contentType = contentType;
		this.aliases = aliases;
		this.path = path;
		this.typeCode = typeCode;
		this.uri = uri;
		this.contentStream = contentStream;
	}
	/**
	*用于存储文件时的构造方法
	*/
	@PersistenceConstructor
	public LocalStoreFile(Object id,String filename, String contentType, List<String> aliases,
			String path, Integer typeCode, String uri, InputStream contentStream) {
		this(filename, contentType, aliases, path, typeCode, uri, contentStream);
		this.id = id;
		this.customId = id;
	}
	
	/**
	*用于存储文件时的构造方法
	*/
	@PersistenceConstructor
	public LocalStoreFile(Object id,Object backupId,String filename, String contentType, List<String> aliases,
			String path, Integer typeCode, String uri, InputStream contentStream) {
		this(filename, contentType, aliases, path, typeCode, uri, contentStream);
		this.id = id;
		this.customId = id;
		this.backupId = backupId;
	}

	
	public LocalStoreFile fileName(String filename){
		this.filename = filename;
		return this;
	}
	
	public LocalStoreFile length(Long length){
		this.length = length;
		return this;
	}
	
	public LocalStoreFile chunkSize(Long chunkSize){
		this.chunkSize = chunkSize;
		return this;
	}
	
	public LocalStoreFile uploadDate(Date uploadDate){
		this.uploadDate = uploadDate;
		return this;
	}
	
	public LocalStoreFile md5(String md5){
		this.md5 = md5;
		return this;
	}
	
	public LocalStoreFile contentType(String contentType){
		this.contentType = contentType;
		return this;
	}
	
	public LocalStoreFile aliases(List<String> aliases){
		this.aliases = aliases;
		return this;
	}
	
	public LocalStoreFile id(Object id){
		this.id = id;
		return this;
	}
	
	public LocalStoreFile backupId(Object backupId){
		this.backupId = backupId;
		return this;
	}
	
	public LocalStoreFile path(String path){
		this.path = path;
		return this;
	}
	
	public LocalStoreFile typeCode(Integer typeCode){
		this.typeCode = typeCode;
		return this;
	}
	
	public LocalStoreFile uri(String uri){
		this.uri = uri;
		return this;
	}
	
	public LocalStoreFile contentStream(InputStream contentStream){
		this.contentStream = contentStream;
		return this;
	}
	
	public LocalStoreFile customId(Object customId){
		this.customId = customId;
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Long getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Long chunkSize) {
		this.chunkSize = chunkSize;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getBackupId() {
		return backupId;
	}

	public void setBackupId(Object backupId) {
		this.backupId = backupId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public InputStream getContentStream() {
		return contentStream;
	}

	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}

	public Object getCustomId() {
		return this.customId;
	}

	public void setCustomId(Object customId) {
		this.customId = customId;
	}


	public String getTimeStamp() {
		return timesStamp;
	}


	public void setTimeStamp(String timeStamp) {
		this.timesStamp = timeStamp;
	}


	public String getSignature() {
		return signatures;
	}


	public void setSignature(String signature) {
		this.signatures = signature;
	}
	
	
}

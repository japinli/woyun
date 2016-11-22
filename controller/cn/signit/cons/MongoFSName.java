/**
* @author ZhangHongdong
* @date 2015年7月27日-下午8:03:11
* @see (参阅)
*/
package cn.signit.cons;

/**
 *MongoDB文件名相关常量
 * @ClassName MongoFSFile
 * @author ZhangHongdong
 * @date 2015年7月27日-下午8:03:11
 * @version 1.1.0
 */
public final class MongoFSName {
	public final static String ID = "_id";
	public final static String MD5 = "md5";
	public final static String CHUNK_SIZE = "chunkSize";
	public final static String FILENMAE = "filename";
	public final static String CONTENT_TYPE = "contentType";
	public final static String UPLOAD_DATE = "uploadDate";
	public final static String ALIASES = "aliases";
	public final static String PATH = "path";
	public final static String URI = "uri";
	public final static String TYPE_CODE = "typeCode";
	public final static String BACKUP_ID = "backupId";
	public final static String METADATA = "metadata";
	public final static String METADATA_ID = METADATA+"."+ID;
	public final static String METADATA_CLASS = METADATA+"._class";
	public final static String METADATA_PATH = METADATA+"."+PATH;
	public final static String METADATA_URI = METADATA+"."+URI;
	public final static String METADATA_TYPE_CODE = METADATA+"."+TYPE_CODE;
	public final static String METADATA_BACKUP_ID = METADATA+"."+BACKUP_ID;
}

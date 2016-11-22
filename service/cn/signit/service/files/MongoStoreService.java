/**
* @author:ZhangHongdong
* @date:2016年2月21日-下午5:02:31
* @see: (参阅)
*/
package cn.signit.service.files;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.mongodb.gridfs.GridFSDBFile;

import cn.signit.cons.MongoFSName;
import cn.signit.domain.mongo.file.LocalStoreFile;
import cn.signit.untils.file.FilePathAdapter;
import cn.signit.untils.zip.ZipTool;
/**
 *基于Mongo数据库的文件存储
 * @ClassName: MongoStoreService
 * @author:ZhangHongdong
 * @date:2016年2月4日-下午5:02:31
 * @version:1.2.0
 */
@Service("mongoStoreService")
public class MongoStoreService implements StoreService {
	@Resource
	private GridFsTemplate gridFsTemplate;
	
	/**
	* 保存单个文件
	* 
	*@param storeFile 文件数据的封装
	*@return 保存后的文件id
	*/
	@Override
	public Object save(LocalStoreFile storeFile) {
		return gridFsTemplate.store(storeFile.getContentStream(),storeFile.getFilename(),storeFile.getContentType(),storeFile).getId();
	}

	/**
	* 保存多个文件
	* 
	*@param storeFiles 文件数据的封装的列表
	*@return 保存后的文件id的列表
	*/
	@Override
	public List<Object> saves(List<LocalStoreFile> storeFiles) {
		List<Object> idList = new ArrayList<Object>();
		if(storeFiles == null || storeFiles.isEmpty()){
			return idList;
		}
		for (LocalStoreFile storeFile : storeFiles) {
			idList.add(save(storeFile));
		}
		return idList;
	}

	/**
	* 获取单个文件
	* 
	*@param fileId 文件id
	*@return 该文件数据的封装
	*/
	@Override
	public LocalStoreFile get(Object fileId) {
		GridFSDBFile dbFile =  gridFsTemplate.findOne(Query.query(Criteria.where(MongoFSName.ID).is(fileId)));
		if(dbFile == null){//从元数据_id中获取
			dbFile = gridFsTemplate.findOne(Query.query(Criteria.where(MongoFSName.METADATA_ID).is(fileId)));
			if(dbFile == null){	//从path中获取
				dbFile = gridFsTemplate.findOne(Query.query(Criteria.where(MongoFSName.METADATA_PATH).is(fileId)));
				if(dbFile == null){//适配路径
					dbFile = gridFsTemplate.findOne(Query.query(Criteria.where(MongoFSName.METADATA_PATH).is(FilePathAdapter.adapt(fileId.toString()))));
				}
			}
		}
		LocalStoreFile sf = convert(dbFile);
		return sf;
	}
	
	/**
	* 获取多个文件
	* 
	*@param fileIds 文件id列表
	*@return 该文件数据的封装列表
	*/
	@Override
	public List<LocalStoreFile> gets(List<Object> fileIds) {
		List<LocalStoreFile> fileList = new ArrayList<LocalStoreFile>();
		if(fileIds == null || fileIds.isEmpty()){
			return fileList;
		}
		Set<Criteria> cSet = new LinkedHashSet<>();
		for (Object fileId : fileIds) {
			if(fileId != null){
				if(fileId.toString().indexOf("signit") >= 0){
					cSet.add(Criteria.where(MongoFSName.METADATA_PATH).is(fileId));
					cSet.add(Criteria.where(MongoFSName.METADATA_PATH).is(FilePathAdapter.adapt(fileId.toString())));
				}else{
					cSet.add(Criteria.where(MongoFSName.ID).is(fileId));
				}
			}
		}
		if(cSet.isEmpty()){
			return fileList;
		}
		Criteria[] criterias = cSet.toArray(new Criteria[cSet.size()]);
		List<GridFSDBFile> gridFileList = gridFsTemplate.find(Query.query(new Criteria().orOperator(criterias)));
		for (GridFSDBFile dbFile : gridFileList) {
			LocalStoreFile sf = convert(dbFile);
			fileList.add(sf);
		}
		return fileList;
	}
	
	private LocalStoreFile convert(GridFSDBFile dbFile){
		if(dbFile == null){
			return null;
		}
		LocalStoreFile sf = new LocalStoreFile().id(dbFile.getId())
																	.backupId(dbFile.getMetaData().get(MongoFSName.BACKUP_ID))
																	.customId(dbFile.getMetaData().get(MongoFSName.ID))
																	.fileName(dbFile.getFilename())
																	.aliases(dbFile.getAliases())
																	.chunkSize(dbFile.getChunkSize())
																	.contentType(dbFile.getContentType())
																	.length(dbFile.getLength())
																	.md5(dbFile.getMD5())
																	.path(FilePathAdapter.adapt((String)dbFile.getMetaData().get(MongoFSName.PATH)))
																	.typeCode((Integer)dbFile.getMetaData().get(MongoFSName.TYPE_CODE))
																	.uploadDate(dbFile.getUploadDate())
																	.uri((String)dbFile.getMetaData().get(MongoFSName.URI))
																	.contentStream(dbFile.getInputStream());
		return sf;
	}

	/**
	* 删除多个文件
	* 
	*@param fileIds 文件id列表
	*@return 成功删除的文件个数
	*/
	@Override
	public int deletes(List<Object> fileIds) {
		if(fileIds == null){
			return 0;
		}
		Criteria[] criterias = new Criteria[fileIds.size()];
		int idx = 0;
		for (Object fileId : fileIds) {
			if(fileId != null){
				criterias[idx] = Criteria.where(MongoFSName.ID).is(fileId);
				idx++;
			}
		}
		gridFsTemplate.delete(Query.query(new Criteria().orOperator(criterias)));
		return criterias.length;
	}
	
	/**
	* 删除单个文件
	* 
	*@param fileId 文件id
	*@return 成功删除的文件个数
	*/
	@Override
	public int delete(Object fileId) {
		try {
			 gridFsTemplate.delete(Query.query(Criteria.where(MongoFSName.ID).is(fileId)));
			 return 1;
		} catch (Exception e) {
			 return 0;
		}
	}

	/**
	* 更新单个文件(默认不允许空值更新)
	* 
	*@param storeFile 文件数据的封装
	*@return 更新后的文件id
	*/
	@Override
	public Object update(LocalStoreFile storeFile) {
		if(storeFile == null){
			return null;
		}
		//查找原始文档
		LocalStoreFile sf = get(storeFile.getId());
		if(sf == null){
			return null;
		}
		//存储新文档
		Object newFileId = save(getNotNullStoreFile(sf, storeFile));
		if(newFileId == null){
			return null;
		}
		//删除旧文档
		int ret = delete(sf.getId());
		if(ret > 0){
			return newFileId;
		}else{
			return null;
		}
	}
	
	//通过原始存储文档信息构建新的存储文档
	private LocalStoreFile getNotNullStoreFile(LocalStoreFile oldFile,LocalStoreFile newFile){
		MethodAccess methodAccess = MethodAccess.get(newFile.getClass());
		String[] methodNames = methodAccess.getMethodNames();
		if (methodNames == null) {
			return oldFile;
		}
		StringBuilder sb = new StringBuilder();
		for (String methoName : methodAccess.getMethodNames()) {
			sb.delete(0, sb.length());
			// 提取方法名
			if (methoName.startsWith("set")) {
				Object fieldValue = null;
				sb = sb.append(methoName.substring(3));
				int cutTo = 0;
				try {
					cutTo = 3;
					fieldValue = methodAccess.invoke(newFile,
							sb.insert(0, "get").toString());
				} catch (Exception e1) {
					sb.delete(0, cutTo);
					try {
						cutTo = 2;
						fieldValue = methodAccess.invoke(newFile,
								sb.insert(0, "is").toString());
					} catch (Exception e2) {
						continue;
					}
				}
				if( fieldValue == null){
					Object oldFieldValue = MethodAccess.get(oldFile.getClass()).invoke(oldFile, sb.toString());
					methodAccess.invoke(newFile, methoName, oldFieldValue);
				}
			}
		}
		return newFile;
	}

	/**
	* 更新多个文件(默认不允许空值更新)
	* 
	*@param storeFiles 文件数据的封装列表
	*@return 更新后的文件id列表
	*/
	@Override
	public List<Object> updates(List<LocalStoreFile> storeFiles) {
		List<Object> idList = new ArrayList<Object>();
		if(storeFiles == null){
			return idList;
		}
		for (LocalStoreFile sf : storeFiles) {
			idList.add(update(sf));
		}
		return idList;
	}

	/**
	* 基本Mongo存储的单个文件压缩
	* 
	*@param fileId 文件Id
	*@return 返回压缩后的文件byte数组
	*@throws IOException 
	*
	 */
	@Override
	public byte[] zipCompress(Object fileId) throws IOException {
		Object[] fileIds = new Object[2];
		fileIds[0]=fileId;
		return zipCompress(fileIds);
	}

	/**
	* 基本Mongo存储的多个文件压缩
	* 
	*@param fileIds 文件Id数组
	*@return 返回压缩后的文件byte数组
	*@exception IOException
	 */
	@Override
	public byte[] zipCompress(Object[] fileIds) throws IOException {
		ByteArrayOutputStream baops = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baops);
		ZipTool zipTool = new ZipTool();
		for (Object fileId : fileIds) {
			LocalStoreFile gridFs = get(fileId);
			zipTool.compress(zipOut, gridFs.getContentStream(), gridFs.getFilename());
		}
		zipOut.close();
		byte[] byteArray = baops.toByteArray();
		baops.close();
		return byteArray;
	}
	
}

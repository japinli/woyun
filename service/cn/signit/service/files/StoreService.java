/**
* @author:ZhangHongdong
* @date:2016年2月21日-下午5:00:10
* @see: (参阅)
*/
package cn.signit.service.files;

import java.io.IOException;
import java.util.List;

import cn.signit.domain.mongo.file.LocalStoreFile;


/**
 *文件存储服务接口
 * @ClassName: StoreService
 * @author:ZhangHongdong
 * @date:2016年2月4日-下午5:00:10
 * @version:1.2.0
 */
public interface StoreService {
		/**
		* 保存单个文件
		* 
		*@param storeFile 文件数据的封装
		*@return 保存后的文件id
		*/
		public  Object save(LocalStoreFile storeFile);
		
		/**
		* 保存多个文件
		* 
		*@param storeFiles 文件数据的封装的列表
		*@return 保存后的文件id的列表
		*/
		public  List<Object> saves(List<LocalStoreFile> storeFiles);
		
		/**
		* 获取单个文件
		* 
		*@param fileId 文件id
		*@return 该文件数据的封装
		*/
		public LocalStoreFile get(Object fileId);
		
		/**
		* 获取多个文件
		* 
		*@param fileIds 文件id列表
		*@return 该文件数据的封装列表
		*/
		public List<LocalStoreFile> gets(List<Object> fileIds);
		
		/**
		* 删除单个文件
		* 
		*@param fileId 文件id
		*@return 成功删除的文件个数
		*/
		public int delete(Object fileId);
		
		/**
		* 删除多个文件
		* 
		*@param fileIds 文件id列表
		*@return 成功删除的文件个数
		*/
		public int deletes(List<Object> fileIds);
		
		/**
		* 更新单个文件(默认不允许空值更新)
		* 
		*@param storeFile 文件数据的封装
		*@return 更新后的文件id
		*/
		public Object update(LocalStoreFile storeFile);
		
		/**
		* 更新多个文件(默认不允许空值更新)
		* 
		*@param storeFiles 文件数据的封装列表
		*@return 更新后的文件id列表
		*/
		public  List<Object> updates(List<LocalStoreFile> storeFiles);
		
		/**
		* 基本Mongo存储的单个文件压缩
		* 
		*@param fileId 文件Id
		*@return 返回压缩后的文件byte数组
		*@throws IOException 
		*
		 */
		public byte[] zipCompress(Object fileId) throws IOException;

		/**
		* 基本Mongo存储的多个文件压缩
		* 
		*@param fileIds 文件Id数组
		*@return 返回压缩后的文件byte数组
		*@exception IOException
		 */
		public byte[] zipCompress(Object[] fileIds)throws IOException;
}

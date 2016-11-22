/**
* @author ZhangHongdong
* @date 2015年7月29日-下午3:08:58
* @see (参阅)
*/
package cn.signit.dao.mongo.file;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cn.signit.domain.mongo.file.LocalStoreFile;


/**
 *文件存储的访问对象（相当于DAO）
 * @ClassName StoreFileRepository
 * @author ZhangHongdong
 * @date 2015年7月29日-下午3:08:58
 * @version 1.1.0
 */
@Repository
public interface StoreFileRepository extends MongoRepository<LocalStoreFile, ObjectId> {}

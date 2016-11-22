/**
 * 
 */
package cn.signit.server.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import jodd.util.CsvUtil;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import cn.signit.conf.ConfigProps;


/**
 *添加Mongo数据库的支持
 * @ClassName: DataConfig
 * @author:ZhangHongdong
 * @date:2015年11月25日-上午9:11:55
 * @version:1.0.0
 */
@Configuration
@EnableMongoRepositories(BaseConfig.BASE_PKG_NAME+"dao.mongo")
@EnableTransactionManagement
@Import(BaseConfig.class)
public class MongoConfig extends AbstractMongoConfiguration{
	private final static Logger LOG = LoggerFactory.getLogger(MongoConfig.class);
	
	/**
	 * +--------------------------------------------------------+
	 * |																		|
	 * |基于MongoDB的数据库配置				|
	 * |																		|
	 * +--------------------------------------------------------+
	 */
	/**
	* 配置MongoDB连接的客户端
	* 
	*@return 名称为"mongoClient"的mongoDB数据库客户端bean
	*/
	@Override
	@Bean
	public Mongo mongo()  throws Exception{
		LOG.info("======================>>   配置MongoDB连接的客户端 ( {} ) ",MongoClient.class.getName());
		/*MongoCredential mc = MongoCredential.createCredential(props.getString("db.mongo.username"), props.getString("db.mongo.auth_dbname"), props.getString("db.mongo.password").toCharArray());*/
		MongoClient mongo = new MongoClient(
				loadReplicaSetServers(),mongoClientOptions()
				/*,Arrays.asList(mc)*/);
		return mongo;
	}
	
	//集群-副本集方式
	private List<ServerAddress> loadReplicaSetServers(){
		String replicaSetStr = ConfigProps.get("db.mongo.replica_set_servers");
		if(replicaSetStr == null || "".equals(replicaSetStr.trim())){
			return Arrays.asList(new ServerAddress(ConfigProps.get("db.mongo.host"),ConfigProps.getInt("db.mongo.port")));
		}
		List<String> servers = Arrays.asList(CsvUtil.toStringArray(replicaSetStr));
		List<ServerAddress> serversList = new ArrayList<>(servers.size());
		for (String server : servers) {
			String[] items = server.split(":");
			serversList.add(new ServerAddress(items[0].trim(), Integer.parseInt(items[1].trim())));
		}
		return serversList;
	}
	
	private MongoClientOptions mongoClientOptions(){
		return MongoClientOptions.builder()//.requiredReplicaSetName(ConfigProps.get("db.mongo.replica_set_name"))
											//.writeConcern(WriteConcern.valueOf(ConfigProps.get("db.mongo.write_concern")))
											//读写分离-优先从secondary从库中读
											//.readPreference(ReadPreference.secondaryPreferred())
											.build();
	}
	
	@Override
	protected String getDatabaseName() {
		return ConfigProps.get("db.mongo.auth_dbname");
	}
	
	/**
	*配置MongoDb模板操作对象
	* 
	*@return 名称为"mongoTemplate"的模板操作对象bean
	*/
	@Override
	@Bean
	public MongoTemplate mongoTemplate()  throws Exception{
		LOG.info("======================>>   配置MongoDb模板操作对象 ( {} ) ",MongoTemplate.class.getName());
		return new MongoTemplate(mongo(), getDatabaseName());
	}
	
	/**
	*配置MongoDb文件模板操作对象
	* 
	*@return 名称为"gridFsTemplate"的文件模板操作对象bean
	 * @throws Exception 
	*/
	@Bean
	public GridFsTemplate gridFsTemplate() {
		LOG.info("======================>>   配置MongoDb文件模板操作对象 ( {} ) ",GridFsTemplate.class.getName());
		GridFsTemplate gt = null;
		try {
			gt = new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gt;
	}
	
	@Override
	protected String getMappingBasePackage() {
		return ConfigProps.get("db.mongo.mapping_base_package");
	}
}


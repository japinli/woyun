package cn.signit.server.conf;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.pool.DruidDataSource;

import cn.signit.conf.ConfigProps;



/**
*数据库配置
* @ClassName MySqlConfig
* @author Liwen
* @date 2016年5月12日-下午5:03:29
* @version (版本号)
* @see (参阅)
*/
@Configuration
@Import(BaseConfig.class)
public class DataSourceConfig {
	private final static Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);
	/**
	* 配置数据源（采用DRUID数据源）
	* 
	* @return 名称为"dataSource"的数据源bean
	*/
	@SuppressWarnings("static-access")
	@Bean(initMethod="init",destroyMethod="close")
	public DataSource dataSource() {
		LOG.info("======================>>  配置数据源( {} ) ",DruidDataSource.class.getName());
		DruidDataSource dataSource = new DruidDataSource(true);
		/*数据库连接*/
		dataSource.setUrl(ConfigProps.get("db.mysql.jdbc_url"));
		dataSource.setDriverClassName(ConfigProps.get("db.mysql.jdbc_driver"));
		dataSource.setUsername(ConfigProps.get("db.mysql.jdbc_user"));
		dataSource.setPassword(ConfigProps.get("db.mysql.jdbc_password"));
		/*连接配置*/
		//连接池最大连接数
		dataSource.setMaxActive(20);
		//连接池最大等待时间
		dataSource.setMaxWait(60000);
		//数据库测试使用
		dataSource.setValidationQuery(ConfigProps.get("db.mysql.validation_query"));
		//其他配置采用默认
		
		return dataSource;
	}
}

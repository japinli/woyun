/**
* @author ZhangHongdong
* @date 2015年7月9日-下午1:03:24
* @see (参阅)
*/
package cn.signit.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jodd.props.Props;

/**
 *基于Jodd的系统配置属性
 * @ClassName JoddConfigProps
 * @author ZhangHongdong
 * @date 2015年7月9日-下午1:03:24
 * @version 1.1.0
 */
public class ConfigProps extends CommonConfigure{
	public final static Logger LOG = LoggerFactory.getLogger(ConfigProps.class);
	private final static String DEFAULT_PROPS_PATH = "/props/config.props";
	private static String DEFAULT_PROPS_ABS_PATH = null;
	private final static Props PROPS = loadProps(DEFAULT_PROPS_PATH);
	static{  
		DEFAULT_PROPS_ABS_PATH = ConfigProps.class.getResource(DEFAULT_PROPS_PATH).getFile();
		if(DEFAULT_PROPS_ABS_PATH != null){
			//LOG.info("基于Jodd的系统配置属性列表加载========> 成功 ( {} )",DEFAULT_PROPS_ABS_PATH);
		}else{
			//LOG.info("基于Jodd的系统配置属性列表加载========> 失败 ( {} )",DEFAULT_PROPS_ABS_PATH);
		}
	}
	
	/**
	* 获取当前使用的配置文件路径
	* 
	*@return 配置文件路径
	*/
	public static String getPropertiesPath(){
		return DEFAULT_PROPS_ABS_PATH;
	}
	
	public static Props getProps(){
		return PROPS;
	}
	
	public static String get(String key){
		return PROPS.getValue(key);
	}
	
	//获得整型值
	public static int getInt(String key){
		return Integer.parseInt(get(key));
	}
	
	//获得长整型值
	public static long getLong(String key){
		return Long.parseLong(get(key));
	}
	
	//获得浮点型值
	public static float getFloat(String key){
		return Float.parseFloat(get(key));
	}
	
	//获得双精度型值
	public static double getDouble(String key){
		return Double.parseDouble(get(key));
	}
	
	//获得布尔值
	public static boolean getBoolean(String key){
		return Boolean.parseBoolean(get(key));
	}		
	
}

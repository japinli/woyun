/**
* @author ZhangHongdong
* @date 2015年2月9日-下午2:59:17
* @see (参阅)
*/
package cn.signit.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import jodd.props.Props;

import cn.signit.untils.file.FileHandler;
import cn.signit.untils.file.ReusableStreamHandler;

/**
 * 公共配置常量类
 * 
 * @ClassName CommonConfigure
 * @author ZhangHongdong
 * @date 2015年2月9日-下午2:59:17
 * @version 1.0.0
 */
public class CommonConfigure implements Serializable{
	private static final long serialVersionUID = 4633632119623481759L;
	private static String RES_DIR_PREFIX = "";
	public final static String RES_DIR_SUFFIX = "res";
	public final static String RES_ROOT_DIR = "%s/.signit/%s";
	
	/**
	 * 获得资源配置的基本目录<br/>
	 * 
	 * @return 资源文件路径
	 */
	public static Path getResourceBase() {
		if (RES_DIR_PREFIX.equals("")) {
			RES_DIR_PREFIX = System.getProperty("user.home");
		}
		String pathStr = String.format(RES_ROOT_DIR, RES_DIR_PREFIX, RES_DIR_SUFFIX);
		return Paths.get(pathStr);
	}
	
	/**
	* 加载资源为Properties
	* 
	*@param propertiesPath
	*@return
	*/
	protected static Properties loadProperties(String propertiesPath){
		Properties props = load(new Properties(), propertiesPath);
		if(props == null){
			throw new IllegalArgumentException("parameters error");
		}
		return props;
	}
	
	/**
	* 加载资源为Props
	* 
	*@param propsPath
	*@return
	*/
	protected static Props loadProps(String propsPath){
		Props props =  load(new Props(), propsPath);
		if(props == null){
			throw new IllegalArgumentException("parameters error");
		}
		return props;
	}
	
	private static <T> T load(T propType,String propPath){
		URL url = CommonConfigure.class.getResource(propPath);
		if(url == null){
			return null;
		}
		String protocol = url.getProtocol();
		//优先通过从ClassPath中加载
		if("file".equals(protocol)){
			try {
				return getPropFromStream(propType,url.openStream());
			} catch (IOException e) {
				return null;
			}
		}else if("jar".equals(protocol)){//通过从Jar包文件中加载
			JarURLConnection jarUrl;
			try {
				jarUrl = (JarURLConnection)url.openConnection();
				InputStream is = ReusableStreamHandler.reuse(jarUrl.getInputStream());
				propType = getPropFromStream(propType,is);
				//提取到ClassPath下
				addPropToClassPath(is, propPath);
				return propType;
			} catch (IOException e1) {
				return null;
			}
		}else if("http".equals(protocol) || "https".equals(protocol)){//通过HTTP方式加载
			HttpURLConnection httpUrl;
			try {
				httpUrl = (HttpURLConnection)url.openConnection();
				InputStream is = ReusableStreamHandler.reuse(httpUrl.getInputStream());
				propType = getPropFromStream(propType,is);
				//提取到ClassPath下
				addPropToClassPath(is, propPath);
				return propType;
			} catch (IOException e1) {
				return null;
			}
		}else{
			throw new NullPointerException("not found");
		}
	}
	
	private static <T> T getPropFromStream(T propType,InputStream is) throws IOException{
		try(InputStream newIs = is){
			if(propType instanceof Props){
				((Props)propType).load(newIs);
			}else if(propType instanceof Properties){
				((Properties)propType).load(newIs);
			}
			return propType;
		} 
	}
	
	private static void addPropToClassPath(InputStream is,String propPath) throws IOException{
		File targetFile = new File(CommonConfigure.class.getResource("/").getFile()+propPath);
		FileHandler.createFile(targetFile);
		FileHandler.writeStream2File(ReusableStreamHandler.reuse(is), targetFile);
		if(targetFile.length() > 0){
			ReusableStreamHandler.reuseFinish(is);
		}
	}

	
}

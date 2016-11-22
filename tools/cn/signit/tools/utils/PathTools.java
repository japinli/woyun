package cn.signit.tools.utils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 路径转换
* @ClassName PathTools
* @author Liwen
* @date 2016年5月11日-下午13:52:28
* @version (版本号)
* @see (参阅)
*/
public class PathTools {
	
	/**
	 * @param path 配置路径
	 * @return path 真实路径
	 */
	public static Path getPath(String path){
		Path inPath=null;
		try {
			inPath = Paths.get(PathTools.class.getResource(path).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return inPath;
	}
}

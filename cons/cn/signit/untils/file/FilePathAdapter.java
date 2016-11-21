/**
* @author ZhangHongdong
* @date 2015年8月4日-下午4:45:48
* @see (参阅)
*/
package cn.signit.untils.file;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *文件路径根据操作系统的适配器（支持windows/linux/mac/solaris）
 * @ClassName FilePathAdapter
 * @author ZhangHongdong
 * @date 2015年8月4日-下午4:45:48
 * @version 1.1.0
 */
public class FilePathAdapter {
	public final static Pattern WIN_PATH_PATTERN = Pattern.compile("^[a-zA-Z]:([\\\\|/][^\\\\/<>|:*?\"]+)+$");//^[a-zA-Z]:(\\\\[^\\\\/<>|:*?\"]+)+$
	public final static Pattern UNIX_PATH_PATTERN = Pattern.compile("^(/).+(/$)?");
	private final static String OS_ROOT_DIR =  FileSystems.getDefault().getRootDirectories().iterator().next().toString();
	 private final static String OS =  System.getProperty("os.name").toLowerCase();
	private static enum OSType{
		 UNIX_LINUX_1("nux"),
		 UNIX_LINUX_2("nix"),
		 UNIX_LINUX_3("aix"),
		 WINDOWS("win"),
		 MAC("mac"),
		 SOLARIS("sunos"),
		 UNKNOWN("unknown");
		 private String flag;
		 OSType(String flag){
			 this.flag = flag;
		 }
		 public String getFlag(){
			 return flag;
		 }
		 public static OSType currentOsType(){
			 for (OSType os : values()) {
				if(OS.indexOf(os.getFlag()) >= 0){
					return os;
				}
			}
			 return OSType.UNKNOWN;
		 }
	}

	/**
	* 适配指定操作系统下的文件系统路径
	* 
	*@param path 待适配路径
	*@return 适配后的文件路径。获取失败，则返回null
	*/
	public static String adapt(String path){
		if(path == null){
			return null;
		}
		OSType currentOS = OSType.currentOsType();
		if(OSType.UNIX_LINUX_1 == currentOS ||
			OSType.UNIX_LINUX_2 == currentOS ||
			OSType.UNIX_LINUX_3 == currentOS ||
			OSType.MAC == currentOS ||
			OSType.SOLARIS == currentOS){//linux or mac or solaris
					Matcher m = WIN_PATH_PATTERN.matcher(path);
					if(m.find()){//windows路径字符串-->linux路径字符串
						path = path.substring(2).replace(FileSeparator.WIN_CHAR, FileSeparator.UNIX_CHAR);
					}else{
						path = path.replace(FileSeparator.WIN_CHAR, FileSeparator.UNIX_CHAR);
					}
		}else if(OSType.WINDOWS == currentOS ){//windows
					Matcher m = UNIX_PATH_PATTERN.matcher(path);
					if(m.find()){//linux路径字符串-->windows路径字符串
						path = new StringBuilder(path.replace(FileSeparator.UNIX_CHAR, FileSeparator.WIN_CHAR)).deleteCharAt(0).insert(0, OS_ROOT_DIR).toString();
					}
		}
		return path;
	}
	
	/**
	* 适配指定操作系统下的文件系统路径为PATH
	*@param path 待适配路径
	*@return 适配后的PATH对象。获取失败，则返回null
	*@since:1.2.0
	*/
	public static Path adaptForPath(String path){
		String osPath = adapt(path);
		if(osPath == null){
			return null;
		}
		return Paths.get(osPath);
	}
	
	/**
	* 适配指定操作系统下的文件系统路径为URI
	*@param path 待适配路径
	*@return 适配后的URI。获取失败，则返回null
	*@since:1.2.0
	*/
	public static URI adaptForUri(String path){
		if(path == null){
			return null;
		}
		return adaptForPath(path).toUri();
	}
	
	/**
	* 适配指定操作系统下的文件系统路径为URI的PATH
	*@param path 待适配路径
	*@return 适配后的URI的PATH。获取失败，则返回null
	*@since:1.2.0
	*/
	public static String adaptForUriPath(String path){
		if(path == null){
			return null;
		}
		return adaptForUri(path).getPath();
	}
	
	/**
	*  适配指定操作系统下的多个文件系统路径
	* 
	*@param paths 待适配路径数组
	*@return 适配后的文件路径数组。获取失败，则返回null
	*/
	public static String[] adapt(String... paths){
		if(paths == null){
			return null;
		}
		String[] retPaths = new String[paths.length];
		int index = 0;
		for (String path : paths) {
			retPaths[index] = adapt(path);
			index++;
		}
		return retPaths;
	}
	
	/**
	*  适配指定操作系统下的多个文件系统路径
	* 
	*@param paths 待适配路径列表
	*@return 适配后的文件路径列表
	*/
	public static List<String> adapt(List<String> paths){
		int index = 0;
		List<String> retList = new ArrayList<>();
		for (String path : paths) {
			retList.add(index, adapt(path));
			index++;
		}
		return retList;
	}
}

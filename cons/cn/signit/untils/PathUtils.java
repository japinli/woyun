package cn.signit.untils;

public class PathUtils {

	/** 用户家目录 */
	public static final String home = System.getProperty("user.home");
	
	/** 用户工作目录 */
	public static final String dir = System.getProperty("user.dir");
	
	/** 路径分隔符 */
	public static final String sep = System.getProperty("file.separator");
	
	/** 仓库目录 */
	public static final String repo = dir + sep + "woyun-repo/";
	
	/** 默认同步库 */
	public static final String git = "我的文档";
}

package cn.signit.untils;

public class RepoPath {

	/** 用户家目录 */
	public static final String home = System.getProperty("user.home");
	
	/** 用户工作目录 */
	public static final String dir = System.getProperty("user.dir");
	
	/** 路径分隔符 */
	public static final String sep = System.getProperty("file.separator");
	
	/** 仓库目录 */
	public static final String repo = dir + sep + "woyun-repo/";
	
	/** 保存数据的目录 */
	public static final String data = "data";
	
	/** 数据记录目录 */
	public static final String git = ".git";
	
	/** 默认仓库目录名 */
	public static final String default_repo = "我的资料库";
	
	//////////////////////////////////////////////////////////////////////
	///  Repo path
	/////////////////////////////////////////////////////////////////////
	
	/** 最后提交 */
	public static final String HEAD = "HEAD";
	
	/** 主分支 */
	public static final String MASTER = "master";
	
	/** 本地分支 */
	public static final String LOCAL = "local";
	
	//////////////////////////////////////////////////////////////////////
	///  Repo format message
	/////////////////////////////////////////////////////////////////////
	
	public static final String create_repo_msg = "新建 \"%s\" 资料库";
	public static final String exists_repo_msg = "资料库 \"%s\" 已存在";
	public static final String add_file_msg = "添加 \"%s\" ";
}

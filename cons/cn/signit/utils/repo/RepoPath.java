package cn.signit.utils.repo;

import java.nio.file.Paths;

public class RepoPath {

	/** 用户家目录 */
	public static final String home = System.getProperty("user.home");
	
	/** 用户工作目录 */
	public static final String dir = System.getProperty("user.dir");
	
	/** 路径分隔符 */
	public static final String sep = System.getProperty("file.separator");
	
	/** 临时文件夹 */
	public static final String tmp = System.getProperty("java.io.tmpdir");
	
	/** 仓库目录 */
	public static final String repo = Paths.get(dir, "woyun-repo").toString();
	
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
	public static final String del_file_msg = "删除 \"%s\" ";
	
	/**
	 * 路径拼接
	 * @param first 第一个路径
	 * @param more 更多的路径
	 * @return 拼接后的路径
	 */
	public static String contact(String first, String ... more) {
		return Paths.get(first, more).toString();
	}
	
	/**
	 * 获取仓库路径
	 * @param more 多级目录
	 * @return 仓库的完整路径
	 */
	public static String getRepositoryPath(String ... more) {
		return Paths.get(repo, more).toString();
	}
	
	/**
	 * 构建临时文件目录
	 * @param more 多级目录
	 * @return 完整路径
	 */
	public static String getTemp(String ... more) {
		return Paths.get(tmp, more).toString();
	}
}

package cn.signit.controller.api;

/**
 * Rest API 接口定义
 */
public class RestApiPath {
	
	/** API 版本 */
	public final static String api = "/v1";
	
	/** 测试服务器状态 */
	public final static String ping = api + "/ping";

	/** 授权 */
	public final static String auth = api + "/auth";
	
	/** 测试授权是否成功 */
	public final static String auth_ping = auth + "/ping";
	
	/** 注册帐号 */
	public final static String register = api + "/account";
	
	/** 新建仓库 */
	public final static String new_repo = api + "/repo";
	
	/** 列出当前用户的仓库信息 */
	public final static String list_repos = api + "/repos";
	
	/** 删除指定的仓库 */
	public final static String del_repo = api + "/repos/{id}";
	
	/** 重命名仓库 */
	public final static String rename_repo = api + "/repos/{id}";
	
	/** 新建目录 */
	public final static String new_dir = api + "/repos/{id}/dir";
	
	/** 重命名目录 */
	public final static String rename_dir = api + "/repos/{id}/dir";
}
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
}
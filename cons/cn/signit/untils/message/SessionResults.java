/**
* @author ZhangHongdong
* @date 2015年8月14日-下午3:46:30
* @see (参阅)
*/
package cn.signit.untils.message;

/**
 *会话结果相关的常量类
 * @ClassName SessionResults
 * @author ZhangHongdong
 * @date 2015年8月14日-下午3:46:30
 * @version 1.1.0
 */
public final class SessionResults {
	/**
	 * ------------------------------------------------------------SUCCESS
	 */
	/**
	*0 - int
	*/
	public final static int RESULT_SUCCESS = 0;
	/**
	*数据获取成功 - String
	*/
	public final static String GET_DATA_SUCCESS = "数据获取成功";
	
	/**
	*操作成功 - String
	*/
	public final static String PROCESS_SUCCESS = "操作成功";
	
	/**
	*验证成功 - String
	*/
	public final static String USER_VERIFY_SUCCESS = "验证成功";
	
	/**
	*安全退出 - String
	*/
	public final static String USER_LOGOUT_SUCCESS = "安全退出";
	
	/**
	*注册成功 - String
	*/
	public final static String USER_REGIST_SUCCESS = "注册成功";
	
	
	/**
	 * ------------------------------------------------------------FAILURE
	 */
	/**
	*1 - int
	*/
	public final static int RESULT_FAILURE = 1;
	/**
	*数据获取失败 - String
	*/
	public final static String GET_DATA_FAILURE = "数据获取失败";
	/**
	*操作失败 - String
	*/
	public final static String PROCESS_FAILURE = "操作失败";
	/**
	*存在失败操作 - String
	*/
	public final static String PROCESS_EXSIT_FAILURE = "存在失败操作";
	/**
	*系统异常 - String
	*/
	public final static String SYSTEM_EXCEPTION = "系统异常";
	/**
	*系统繁忙 - String
	*/
	public final static String SYSTEM_BUSY_EXCEPTION = "系统繁忙";
	/**
	*未知异常 - String
	*/
	public final static String UNKNOWN_EXCEPTION = "未知异常";
	/**
	*空数据异常 - String
	*/
	public final static String NULL_DATA_EXCEPTION = "空数据异常";
	/**
	*参数错误 - String
	*/
	public final static String PARAMETERS_ERROR = "参数错误";
	/**
	*填写错误 - String
	*/
	public final static String INPUT_ERROR = "填写错误";
	/**
	*类型错误 - String
	*/
	public final static String TYPE_ERROR = "类型错误";
	/**
	*禁止访问 - String
	*/
	public final static String ACCESS_DENIED_ERROR = "禁止访问";
	/**
	*拒绝服务 - String
	*/
	public final static String SERVICE_DENIED_ERROR = "拒绝服务";
	/**
	*请求错误 - String
	*/
	public final static String BAD_REQUEST_ERROR = "请求错误 ";
	/**
	*签名密码口令错误 - String
	*/
	public final static String PRI_KEY_ACCESS_DENIED_ERROR = "签名密码口令错误";
	/**
	*用户不存在 - String
	*/
	public final static String USER_NOT_EXIST_ERROR = "用户不存在";
	/**
	*用户已存在 - String
	*/
	public final static String USER_EXISTED_ERROR = "用户已存在";
	/**
	*用户未激活- String
	*/
	public final static String USER_NOT_ACTIVATED_ERROR = "用户未激活";
	/**
	*用户名或密码错误 - String
	*/
	public final static String USER_NAME_OR_PWD_ERROR = "用户名或密码错误";
	/**
	*验证码错误 - String
	*/
	public final static String USER_CAPTCHA_ERROR = "验证码错误";
	/**
	*验证码不可为空 - String
	*/
	public final static String USER_CAPTCHA_NULL_ERROR = "验证码不可为空";
	/**
	*验证失败 - String
	*/
	public final static String USER_VERIFY_FAILURE = "验证失败";
	/**
	*文档不存在 - String
	*/
	public final static String DOC_NOT_EXIST_ERROR = "文档不存在";
	
	/**
	*证书不存在 - String
	*/
	public final static String CERT_NOT_EXIST_ERROR = "证书不存在";
	/**
	*证书已停用 - String
	*/
	public final static String CERT_DISABLED_ERROR = "证书已停用";
	/**
	*证书已吊销 - String
	*/
	public final static String CERT_REVOKED_ERROR = "证书已吊销";
	/**
	*证书未审核- String
	*/
	public final static String CERT_UNCHECKED_ERROR = "证书未审核";
	/**
	*证书未绑定印章 - String
	*/
	public final static String CERT_UNBIND_SEAL_ERROR = "证书未绑定印章";
	/**
	*印章不存在 - String
	*/
	public final static String SEAL_NOT_EXIST_ERROR = "印章不存在";
	/**
	*印章未审核- String
	*/
	public final static String SEAL_UNCHECKED_ERROR = "印章未审核";
	/**
	*印章未绑定到证书- String
	*/
	public final static String SEAL_UNBOUND_CERT_ERROR = "印章未绑定到证书";
	/**
	*已绑定证书的印章无法删除 - String
	*/
	public final static String SEAL_BOUND_DELETE_ERROR = "已绑定证书的印章无法删除";
	
	public final static String PARAMETERS_REDUNDANCY ="参数冗余，有参数不应该有值";
	/**
	 * ------------------------------------------------------------FAILURE-TEMPLATE
	 */
	/**
	*%s名称过长 - String
	*/
	public final static String NAME_TOO_LONG_ERROR_TEMPLATE = "%s名称过长";
	/**
	*%s尺寸过大 - String
	*/
	public final static String SIZE_TOO_LONG_ERROR_TEMPLATE = "%s尺寸过大";
	/**
	*%s算法错误 - String
	*/
	public final static String ALGOR_ERROR_TEMPLATE = "%s算法错误";
	/**
	*%s编码错误 - String
	*/
	public final static String ENCODE_ERROR_TEMPLATE = "%s编码错误";
	/**
	*%s类型不匹配 - String
	*/
	public final static String TYPE_NOT_MATCH_ERROR_TEMPLATE = "%s类型不匹配";
	
	/**
	*%s密码错误 - String
	*/
	public final static String PASSWORD_ERROR_TEMPLATE = "%s密码错误";
}
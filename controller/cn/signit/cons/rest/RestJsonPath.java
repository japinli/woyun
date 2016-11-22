package cn.signit.cons.rest;

/**
* Rest接口数据（基于JSON数据）资源的URI路径常量类
* @ClassName RestJsonPath
* @author ZhangHongdong
* @date 2016年10月19日-下午3:26:43
* @version 0.0.2
*/
public final class RestJsonPath extends RestPath{
//-----------------------------------------------------------------------------------------------------------
// ROOT资源路径
//-----------------------------------------------------------------------------------------------------------
	public final static String V_1 = "v1";
	public final static String ROOT_PREFIX = "/ws-rest";
	public final static String ROOT = ROOT_PREFIX+"/"+V_1;
	public static enum Root{
		VERSION_1(V_1,ROOT_PREFIX,ROOT);
		private String version;
		private String rootPrefix;
		private String root;
		Root(String version,String rootPrefix,String root){
			this.version = version;
			this.rootPrefix = rootPrefix;
			this.root = root;
		}
		public String version(){
			return this.version;
		}
		public String rootPrefix(){
			return this.rootPrefix;
		}
		public String root(){
			return this.root;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 公共资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 公共资源接口地址
	*/
	public final static String COMMON = ROOT+"/common";
	/**
	* 通用发送信息服务接口地址
	*/
	public final static String COMMON_SEND_MSG = COMMON+"/{send-method:^(?:phone|email)$}/send";
	/**
	* 通用校验已发信息服务接口地址
	*/
	public final static String COMMON_VERIFY_MSG = COMMON+"/{send-method:^(?:phone|email)$}/verify";
	/**
	* 通用获取验证码图片服务接口地址
	*/
	public final static String COMMON_CAPTCHA = COMMON+"/captcha/image";

	
	
	
		
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 用户资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 用户资源列表接口地址
	*/
	public final static String USERS = ROOT+"/users";
	/**
	* 指定用户信息资源接口地址
	*/
	public final static String USER = USERS+"/{user-id}";
	/**
	 *用户登录资源路径
	 */
	public final static String USER_LOGIN = USERS + "/{login-method}/login";
	/**
	* 指定用户信息检测服务接口地址
	*/
	public final static String USER_CHECK = USER+"/check";
	/**
	* 指定用户实名认证资源列表接口地址
	*/
	public final static String USER_AUTH = USER+"/auth";
	/**
	* 用户实名认证-银行卡
	*/
	public final static String USER_BANKCARD_AUTH = USER+"/bankcard-auth";
	/**
	* 用户实名认证-自动认证-支付宝，POST
	*/
	public final static String USER_ALIPAY_AUTH = USER+"/person-auth/auto-mode/alipay";
	/**
	* 用户指定实名认证检验接口地址
	*/
	public final static String USER_AUTH_CHECK = USER+"/auth-check";
	/**
	* 用户指定实名认证图片资源接口地址
	*/
	public final static String USER_AUTH_PICS = USER+"/auth-pics";
	/**
	* 用户指定实名认证指定图片资源接口地址
	*/
	public final static String USER_AUTH_PIC = USER+"/auth-pics/{pic-id}";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 签章资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 用户签章资源列表接口地址
	*/
	public final static String USER_SEALS = USER+"/seals";
	/**
	* 用户签章资源列表接口地址
	*/
	public final static String USER_SEALS_JSON = USER_SEALS+"/json";
	/**
	* 用户签章资源列表接口地址
	*/
	public final static String USER_SEALS_FORM = USER_SEALS+"/form";
	
	
	/**
	* 用户指定签章资源接口地址
	*/
	public final static String USER_SEAL = USER_SEALS+"/{seal-id}";
	/**
	* 用户指定签章资源下载服务接口地址
	*/
	public final static String USER_SEAL_DOWNLOAD = USER_SEAL+"/download";	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//-----------------------------------------------------------------------------------------------------------
// 文档资源路径
//-----------------------------------------------------------------------------------------------------------
	
	/**
	*文档列表资源接口地址
	*/
	public final static String DOCS = ROOT+"/documents";
	/**
	* 指定文档资源接口地址
	*/
	public final static String DOC = DOCS+"/{doc-id}";
	/**
	* 文档图像列表资源接口地址
	*/
	public final static String DOC_IMAGES = DOC+"/images";
	/**
	 * 文档指定图像资源接口地址
	 */
	public final static String DOC_IMAGE = DOC_IMAGES+"/{image-id}";
	/**
	* 用户文档列表资源接口地址
	*/
	public final static String USER_DOCS = USER+"/documents";
	/**
	* 用户指定文档资源接口地址
	*/
	public final static String USER_DOC = USER_DOCS+"/{doc-id}";
	/**
	* 用户指定文档资源下载服务接口地址
	*/
	public final static String USER_DOC_DOWNLOAD = USER_DOC+"/download";
	/**
	* 用户指定PDF文档签名服务接口地址
	*/
	public final static String USER_DOC_SIGN = USER_DOC+"/pdf/sign";
	/**
	* 用户指定PDF文档所有签名域验证服务接口地址
	*/
	public final static String USER_DOC_VERIFIES = USER_DOC+"/pdf/verify";
	/**
	* 用户指定PDF文档指定签名域验证服务接口地址
	*/
	public final static String USER_DOC_VERIFY = USER_DOC_VERIFIES+"/{field-name}";
	/**
	* 用户pdf文档表单相关操作(解析、填写、创建)服务接口地址
	*/
	public final static String USER_DOC_FORMS = USER_DOC+"/pdf/forms";
	/**
	* 用户文档图像列表资源接口地址
	*/
	public final static String USER_DOC_IMAGES = USER_DOC+"/images";
	/**
	 * 用户文档指定图像资源接口地址
	 */
	public final static String USER_DOC_IMAGE = USER_DOC_IMAGES+"/{image-id}";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 证书资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 用户证书列表资源接口地址
	*/
	public final static String USER_CERTS = USER+"/certificates";
	/**
	* 用户指定证书资源接口地址
	*/
	public final static String USER_CERT = USER_CERTS+"/{cert-id}";
	/**
	* 用户服务器端证书申请接口地址
	*/
	public final static String USER_CERT_REQUEST_BY_SERVER = USER_CERTS+"/server-side/request";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 信封资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	* 用户信封列表资源接口地址
	*/
	public final static String USER_ENVS = USER+"/envelopes";
	/**
	* 用户指定信封资源
	*/
	public final static String USER_ENV = USER_ENVS+"/{envelope-id}";
	/**
	* 用户指定信封基本信息资源
	*/
	public final static String USER_ENV_BASIC_INFO = USER_ENV+"/basic-info";
	/**
	* 当前信封中接收者资源
	*/
	public final static String USER_ENV_RECS = USER_ENV+"/recipients";
	/**
	* 当前接收者资源
	*/
	public final static String USER_ENV_REC = USER_ENV_RECS+"/{recipient-id}";
	/**
	* 接收者表单集合资源
	*/
	public final static String USER_ENV_REC_FORMS = USER_ENV_REC+"/forms";
	/**
	* 接收者拒签信封
	*/
	public final static String USER_ENV_REC_REJECT = USER_ENV_REC+"/reject";
	/**
	* 接收者填写表单资源
	*/
	public final static String USER_ENV_REC_FILLOUT = USER_ENV_REC_FORMS+"/fillout";
	/**
	* 接收者确认
	*/
	public final static String USER_ENV_REC_CONFIRM = USER_ENV_REC+"/confirm";
	/**
	* 当前信封中文档资源 * 
	*/
	public final static String USER_ENV_DOCS = USER_ENV+"/documents";
	
	/**
	* 信封中当前文档资源
	*/
	public final static String USER_ENV_DOC = USER_ENV_DOCS+"/{doc-id}";
	/**
	* 打包下载信封中文档
	*/
	public final static String USER_ENV_DOCS_DOWNLOAD = USER_ENV_DOCS+"/download";
	/**
	* 当前信封form方式上传文档
	*/
	public final static String USER_ENV_DOCS_FORM = USER_ENV_DOCS+"/form";
	/**
	* 当前信封json方式上传文档
	*/
	public final static String USER_ENV_DOCS_JSON = USER_ENV_DOCS+"/json";
	/**
	* 当前信封中接收者表单资源
	*/
	public final static String USER_ENV_RECS_FORMS = USER_ENV_RECS+"/forms";
	/**
	* 启动信封
	*/
	public final static String USER_ENV_START = USER_ENV+"/start";
	/**
	* 信封凭证资源
	*/
	public final static String USER_ENV_AUDIT = USER_ENV+"/audit";
	/**
	* 撤销信封资源
	*/
	public final static String USER_ENV_REVOKE = USER_ENV+"/revoke";
	/**
	* 未签提醒
	*/
	public final static String USER_ENV_NOTIFY = USER_ENV+"/notify"; 
	/**
	* 检查信封
	*/
	public final static String USER_ENV_CHECK = USER_ENV+"/check";  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 接收者资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	 * 指定用户收件人信息资源接口地址
	 */	
	public final static String USER_RECS = USER+"/recipients";
	/**
	 * 用户指定收件人信息资源接口地址
	 */		
	public final static String USER_REC = USER_RECS+"/{recipient-id}";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 发票资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	 * 用户发票列表资源接口地址
	 */
	public final static String USER_INVOICES = USER+"/invoices";
	/**
	 * 指定用户发票资源接口地址
	 */
	public final static String USER_INVOICE = USER_INVOICES+"/{invoice-id}";
	/**
	 * 用户最近使用发票记录地址
	 */
	public final static String USER_RECENT = USER_INVOICES+"/recent";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//-----------------------------------------------------------------------------------------------------------
// 增值税资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	 * 指定用户增值税资源接口地址
	 */
	public final static String USER_VAT_INFOS = USER+"/vat-infos";
	/**
	 * 用户指定增值税资源接口地址
	 */
	public final static String USER_VAT_INFO = USER_VAT_INFOS+"/{vat-info-id}";
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------
// 资费资源路径
//-----------------------------------------------------------------------------------------------------------
	/**
	 * 用户资费资源列表接口地址
	 */
	public final static String USER_PAYMENTS = USER+"/payments";
	/**
	 * 用户可用套餐资源列表接口地址
	 */
	public final static String USER_PAY_COMBOS = USER_PAYMENTS+"/combos";
	/**
	 * 用户可用指定套餐资源接口地址
	 */
	public final static String USER_PAY_COMBO = USER_PAY_COMBOS+"/{combo-id}";
	/**
	 * 账户购买套餐权限校验
	 */
	public final static String USER_PAY_COMBO_CHECK = USER_PAY_COMBOS+"/check/match";
	/**
	 * 购买套餐
	 */
	public final static String USER_PAY_COMBO_BUY = USER_PAY_COMBOS+"/buy";
	/**
	 * 检查支付结果
	 */
	public final static String USER_PAY_COMBO_BUY_RESULT_CHECK = USER_PAY_COMBOS+"/buy/check/{order-id}";
	/**
	 * 支付的二维码/页面
	 */
	public final static String USER_PAY_COMBO_BCASHIER = USER_PAYMENTS+"/cashier/{from:^(?:wxpay|alipay)$}/{type:^(?:page|qrcode)$}/{order-id}";
	/**
	 * 用户剩余套餐资源接口地址
	 */
	public final static String USER_PAY_COMBO_REMAIN = USER_PAY_COMBOS+"/remain";
	/**
	 * 用户使用中套餐资源接口地址
	 */
	public final static String USER_PAY_COMBO_IN_USE = USER_PAY_COMBOS+"/effect";
	
	/**
	 * 用户消费账单列表资源接口地址
	 */
	public final static String USER_PAY_BILLS = USER_PAYMENTS+"/bills";
	/**
	 * 用户消费账单列表资源接口地址
	 */
	public final static String USER_PAY_CHECK = USER_PAYMENTS+"/check";
	/**
	 * 用户签名消费账单列表资源接口地址
	 */
	public final static String USER_PAY_BILLS_SIGN = USER_PAY_BILLS+"/sign";
	/**
	 * 用户支付账单列表资源接口地址
	 */
	public final static String USER_PAY_BILLS_PAY = USER_PAY_BILLS+"/pay";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//-----------------------------------------------------------------------------------------------------------
// WebSocket资源路径
//-----------------------------------------------------------------------------------------------------------
	public final static String WS_SIGN_PAD = "/sign-pads/{sign-pad-room}";
	public final static String WS_SIGN_PAD_SHOW = WS_SIGN_PAD+"/show";
	public final static String WS_SIGN_PAD_DRAW = WS_SIGN_PAD+"draw";
	public final static String WS_SIGN_PAD_CLEAR = WS_SIGN_PAD+"/clear";
	public final static String WS_SIGN_PAD_CREATE = WS_SIGN_PAD+"/create";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//													华丽的分割线
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	
}

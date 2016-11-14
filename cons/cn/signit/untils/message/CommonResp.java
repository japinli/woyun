/**
 * @author ZhangHongdong
 * @date 2015年9月11日-上午10:56:07
 * @see (参阅)
 */
package cn.signit.untils.message;

/**
 * 通用响应消息
 * 
 * @ClassName CommonResp
 * @author liwen
 * @date 2015年9月11日-上午10:56:07
 * @version 1.1.0
 */
public class CommonResp {
	protected String resultStatusCode;
	protected int resultCode;
	protected String resultDesc;
	protected Object resultData;
	

	public CommonResp() {
		this.resultStatusCode = CommonStatusCode.FAILTURE.getCode();
		this.resultCode = SessionResults.RESULT_FAILURE;
		this.resultDesc = CommonStatusCode.FAILTURE.getDescription();
		this.resultData = null;
	}
	
	public static CommonResp getInstance(){
		return new CommonResp();
	}
	
	public static CommonResp getInstance(Object resultData){
		return new CommonResp(resultData);
	}
	
	public static CommonResp getInstance(String resultStatusCode, int resultCode,
			String resultDesc, Object resultData){
		return new CommonResp(resultStatusCode,resultCode,resultDesc,resultData);
	}
	public static CommonResp newObject(){
		return new CommonResp();
	}
	public CommonResp(String resultStatusCode, int resultCode,
			String resultDesc, Object resultData) {
		this();
		if(resultStatusCode != null){
			this.resultStatusCode = resultStatusCode;
		}
		if(resultCode > 0){
			this.resultCode = resultCode;
		}
		if(resultDesc != null){
			this.resultDesc = resultDesc;
		}
		this.resultData = resultData;
	}

	public CommonResp(Object resultData) {
		this(null, 0, null, resultData);
		this.resultData = resultData;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getResultStatusCode() {
		return resultStatusCode;
	}

	public void setResultStatusCode(String resultStatusCode) {
		this.resultStatusCode = resultStatusCode;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	// ---------失败
	public <T extends CommonResp> T basicFailureMsg() {
		return basicFailureMsg(null, SessionResults.RESULT_FAILURE, null, null);
	}

	public <T extends CommonResp> T basicFailureMsg(Object errorData) {
		return basicFailureMsg(null, SessionResults.RESULT_FAILURE, null,
				errorData);
	}

	public <T extends CommonResp> T basicFailureMsg(String errorDesc) {
		return basicFailureMsg(null, SessionResults.RESULT_FAILURE, errorDesc,
				null);
	}

	public Object basicFailureMsg(int errorCode, String errorDesc) {
		return basicFailureMsg(null, errorCode, errorDesc, null);
	}

	public Object basicFailureMsg(String errorStatusCode, int errorCode) {
		return basicFailureMsg(errorStatusCode, errorCode, null, null);
	}

	public Object basicFailureMsg(int errorCode, Object errorData) {
		return basicFailureMsg(null, errorCode, null, errorData);
	}

	public Object basicFailureMsg(String errorStatusCode, Object errorData) {
		return basicFailureMsg(errorStatusCode, SessionResults.RESULT_FAILURE,
				null, errorData);
	}

	public Object basicFailureMsg(String errorStatusCode, int errorCode,
			Object errorData) {
		return basicFailureMsg(errorStatusCode, errorCode, null, errorData);
	}

	public Object basicFailureMsg(String errorStatusCode, int errorCode,
			String errorDesc) {
		return basicFailureMsg(errorStatusCode, errorCode, errorDesc, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends CommonResp> T basicFailureMsg(String errorStatusCode,
			int errorCode, String errorDesc, Object errorData) {
		if (errorStatusCode != null) {
			this.resultStatusCode = errorStatusCode;
		}
		if (errorCode != SessionResults.RESULT_FAILURE) {
			this.resultCode = errorCode;
		}
		if (errorDesc != null) {
			this.resultDesc = errorDesc;
		}
		this.resultData = errorData;
		return (T) this;
	}

	// ---------成功
	public <T extends CommonResp> T basicSuccessMsg() {
		return basicSuccessMsg(null, SessionResults.RESULT_SUCCESS, null, null);
	}

	public <T extends CommonResp> T basicSuccessMsg(Object okData) {
		return basicSuccessMsg(null, SessionResults.RESULT_SUCCESS, null,
				okData);
	}

	public <T extends CommonResp> T basicSuccessMsg(String okDesc) {
		return basicSuccessMsg(null, SessionResults.RESULT_SUCCESS, okDesc,
				null);
	}

	public Object basicSuccessMsg(int okCode, String okDesc) {
		return basicSuccessMsg(null, okCode, okDesc, null);
	}

	public Object basicSuccessMsg(String okStatusCode, int okCode) {
		return basicSuccessMsg(okStatusCode, okCode, null, null);
	}

	public Object basicSuccessMsg(int okCode, Object okData) {
		return basicSuccessMsg(null, okCode, null, okData);
	}

	public Object basicSuccessMsg(String okStatusCode, Object okData) {
		return basicSuccessMsg(okStatusCode, SessionResults.RESULT_SUCCESS,
				null, okData);
	}

	public Object basicSuccessMsg(String okStatusCode, int okCode, Object okData) {
		return basicSuccessMsg(okStatusCode, okCode, null, okData);
	}

	public Object basicSuccessMsg(String okStatusCode, int okCode, String okDesc) {
		return basicSuccessMsg(okStatusCode, okCode, okDesc, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends CommonResp> T basicSuccessMsg(String okStatusCode,
			int okCode, String okDesc, Object okData) {
		if (okCode == SessionResults.RESULT_SUCCESS) {
			this.resultCode = okCode;
			if (okStatusCode == null
					|| okStatusCode.equals(CommonStatusCode.FAILTURE.getCode())) {
				this.resultStatusCode = CommonStatusCode.SUCCESS.getCode();
			} else {
				this.resultStatusCode = okStatusCode;
			}

			if (okDesc == null
					|| okDesc
							.equals(CommonStatusCode.FAILTURE.getDescription())) {
				this.resultDesc = CommonStatusCode.SUCCESS.getDescription();
			} else {
				this.resultDesc = okDesc;
			}
			this.resultData = okData;
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends CommonResp> T build() {
		return (T) this;
	}

	@Override
	public String toString() {
		return "CommonResp [resultStatusCode=" + resultStatusCode
				+ ", resultCode=" + resultCode + ", resultDesc=" + resultDesc
				+ ", resultData=" + resultData + "]";
	}
}

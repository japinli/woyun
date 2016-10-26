package io.github.japinl.woyun.common;

public class Response {

	protected int status;
	protected String resultCode;
	protected String resultDesc;
	protected Object resultData;
	
	public Response() {
		this.status = SessionResults.RESULT_FAILTURE;
		this.resultCode = StatusCode.FAILTURE.getCode();
		this.resultDesc = StatusCode.FAILTURE.getDesc();
		this.resultData = null;
	}
	
	public Response(int status, String resultCode, String resultDesc, Object resultData) {
		this();
		if (resultCode != null) {
			this.resultCode = resultCode;
		}
		
		if (status > 0) {
			this.status = status;
		}
		
		if (resultDesc != null) {
			this.resultDesc = resultDesc;
		}
		
		this.resultData = resultData;
	}
	
	public Response(Object resultData) {
		this(0, null, null, resultData);
		this.resultData = resultData;
	}
	
	public static Response getInstance() {
		return new Response();
	}
	
	public static Response getInstance(int status, String resultCode, String resultDesc, Object resultData) {
		return new Response(status, resultCode, resultDesc, resultData);
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
	@Override
	public String toString() {
		return "Response [status:" + status + ", resultCode:" + resultCode +
				", resultDesc:" + resultDesc + ", resultData:" + resultData + "]";
	}
}

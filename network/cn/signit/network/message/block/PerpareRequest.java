package cn.signit.network.message.block;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*共识提案请求
* @ClassName PerpareRequest
* @author Liwen
* @date 2016年11月5日-下午4:34:48
* @version (版本号)
* @see (参阅)
*/
public class PerpareRequest implements BaseRequest<PrepareResponse>,ReqMessage{
	private Long blockHight;//区块高度
	private Long viewId;//视图编号
	private Long peerId;//发送的节点编号
	private String blockSignature;//包含merkle根及其签名值等,经过Base64编码后的值
	
	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiMethodName() {
		return null;
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public Map<String, String> getTextParams() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public Class<PrepareResponse> getResponseClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@param apiVersion
	*@see (参阅)
	*/
	@Override
	public void setApiVersion(String apiVersion) {
		// TODO Auto-generated method stub
		
	}

}

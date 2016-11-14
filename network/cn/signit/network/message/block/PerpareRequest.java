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

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiMethodName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public Map<String, String> getTextParams() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public Class<PrepareResponse> getResponseClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param apiVersion
	*@see (参阅)
	*/
	@Override
	public void setApiVersion(String apiVersion) {
		// TODO Auto-generated method stub
		
	}

}

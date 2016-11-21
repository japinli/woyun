package cn.signit.network.message.info;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*新的保全信息添加进入网络节点的通知，用于节点间同步
* @ClassName InfoUploadActivityNotify
* @author Liwen
* @date 2016年10月14日-下午2:25:42
* @version (版本号)
* @see (参阅)
*/
public class InfoUploadNotify implements BaseRequest<InfoUploadResponse>,ReqMessage{

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
	public Class<InfoUploadResponse> getResponseClass() {
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

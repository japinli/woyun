package cn.signit.network.message.peers;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*用于封装现有存货节点列表信息
*<br>仅仅全局主节点会用到的通知
*<br>用于更新每个节点上的节点列表信息
* @ClassName PeersListNotify
* @author Liwen
* @date 2016年11月5日-下午4:26:42
* @version (版本号)
* @see (参阅)
*/
public class PeersListNotify implements BaseRequest<PeersListReciveResponse>,ReqMessage{

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
	public Class<PeersListReciveResponse> getResponseClass() {
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

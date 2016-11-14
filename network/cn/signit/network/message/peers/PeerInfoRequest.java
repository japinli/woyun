package cn.signit.network.message.peers;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*节点存活状态检查，信息查询
*<br>用于获取指定节点的最新状态信息，以及基础信息(编号，名称，公钥证书等等)
* @ClassName IsAliveNotify
* @author Liwen
* @date 2016年11月5日-下午4:29:35
* @version (版本号)
* @see (参阅)
*/
public class PeerInfoRequest implements BaseRequest<PeerInfoResponse>,ReqMessage{

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
	public Class<PeerInfoResponse> getResponseClass() {
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

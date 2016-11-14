package cn.signit.network.message.block;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*区块发布通知
*<br>任意节点将完整区块信息发布至网络中
*<br>PS:根据2016年11月4日讨论结果，此处发布的信息仅包含mekrle根，不包含区块具体信息
* @ClassName BlockCreateNotify
* @author Liwen
* @date 2016年11月5日-下午4:41:19
* @version (版本号)
* @see (参阅)
*/
public class BlockCreateNotify implements BaseRequest<BlockCreateResponse>,ReqMessage{

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
	public Class<BlockCreateResponse> getResponseClass() {
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

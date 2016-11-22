package cn.signit.network.message.peers;

import java.util.Map;

import cn.signit.network.message.ReqMessage;
import cn.signit.sdk.BaseRequest;

/**
*用于广播通知节点加入、移除等相关Peers活动通知
*<br>通过定义type来区分不同的操作
*<br>1:join,节点加入，添加节点信息。
*<br>2:leave,节点断线，可选择处理。
*<br>3:remove,节点移除，清除该节点的信任信息
* @ClassName PeersJoinRequest
* @author Liwen
* @date 2016年10月14日-上午10:54:29
* @version (版本号)
* @see (参阅)
*/
public class PeersActivityNotify implements BaseRequest<PeersActivityResponse>,ReqMessage{
	private String apiVersion="1.0";
	private Integer notifyType;//通知类型。1:join,节点加入，添加节点信息。2:leave,节点断线，可选择处理。3:remove,节点移除，清除该节点的信任信息。
	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiMethodName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public String getApiVersion() {
		return apiVersion;
	}

	/**
	*
	*@return
	*@see (参阅)
	*/
	@Override
	public Class<PeersActivityResponse> getResponseClass() {
		return PeersActivityResponse.class;
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
	*@param apiVersion
	*@see (参阅)
	*/
	@Override
	public void setApiVersion(String apiVersion) {
		this.apiVersion=apiVersion;
		
	}

}

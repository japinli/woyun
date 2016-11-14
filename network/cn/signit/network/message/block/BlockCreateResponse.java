package cn.signit.network.message.block;

import cn.signit.network.message.RespMessage;
import cn.signit.sdk.BaseResponse;

/**
*区块发布接收确认
*<br>任意节点将完整区块信息发布至网络中
*<br>PS:根据2016年11月4日讨论结果，此处发布的信息仅包含mekrle根，不包含区块具体信息
* @ClassName BlockCreateResponse
* @author Liwen
* @date 2016年11月5日-下午4:41:54
* @version (版本号)
* @see (参阅)
*/
public class BlockCreateResponse extends BaseResponse implements RespMessage{

}

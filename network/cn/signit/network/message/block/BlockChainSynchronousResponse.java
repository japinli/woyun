package cn.signit.network.message.block;

import cn.signit.network.message.RespMessage;
import cn.signit.sdk.BaseResponse;

/**
*区块链信息同步回复
*<br>议员执行新区块计算时，与主节点执行区块链信息同步
*<br>同步的信息包括：区块链高度h,当前视图编号v,以及缺失的区块链信息
* @ClassName BlockChainSynchronousResponse
* @author Liwen
* @date 2016年11月5日-下午4:45:50
* @version (版本号)
* @see (参阅)
*/
public class BlockChainSynchronousResponse extends BaseResponse implements RespMessage{

}

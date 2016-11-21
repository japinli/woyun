package cn.signit.controller;

import cn.signit.sdk.BaseResponse;

/**
* @ClassName ServerConnectController
* @author Liwen
* @date 2016年5月5日-下午4:27:29
* @version (版本号)
* @see (参阅)
*/
public interface ServerConnectController {
	/**
	 * 执行P2P节点新增或移除，查询处理。
	 */
	public BaseResponse memberPeersHandle();
	/**
	 * 执行保全信息查询操作处理
	 */
	public BaseResponse selectHandle();
	/**
	 * 执行新的保全文档添加操作处理
	 */
	public BaseResponse addHandle();
}

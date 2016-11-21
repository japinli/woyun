package cn.signit.forensic.block;

/**
*基于共识算法所需完成的相关操作
* @ClassName ConsensusInterface
* @author Liwen
* @date 2016年11月5日-下午5:07:28
* @version (版本号)
* @see (参阅)
*/
public interface ConsensusInterface {

	/**
	 * 接收议长提案
	 * <br>需校验非法交易，如存在非法交易，则放弃共识，更换视图
	 */
	public void recivePerpareRequest();
	
	/**
	 * 计算自身提案
	 */
	public void compute();
	
	/**
	 * 接收议员的回复
	 */
	public void recivePerpareResponse();
	
	/**
	 * 是否达成共识
	 */
	public boolean isConsensusComplete();
	
	/**
	 * 更换视图
	 */
	public void changeView();
}

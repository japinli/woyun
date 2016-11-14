package cn.signit.forensic.block;

/**
*定义内存换存区块的相关方法
* @ClassName BlockInterface
* @author Liwen
* @date 2016年11月5日-下午5:01:32
* @version (版本号)
* @see (参阅)
*/
public interface CacheInterface {
	/**
	 * 向缓存中添加新的文件信息
	 * <br>需校验交易是否合法，不合法则予以抛弃
	 */
	public boolean addInfo(Object o);
	/**
	 * 移除缓存中指定文件信息
	 */
	public void removeInfo(Object o);
	/**
	 * 重置缓存块，用于新的共识
	 */
	public void reset();
}

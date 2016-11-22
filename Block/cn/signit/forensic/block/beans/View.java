package cn.signit.forensic.block.beans;

/**
*共识算法-视图
* @ClassName View
* @author Liwen
* @date 2016年11月14日-下午1:52:14
* @version (版本号)
* @see (参阅)
*/
public class View {
	public final static int k=1;
	/**
	 * 视图编号
	 */
	private int id;
	
	public View(){
		init();
	}

	public View init(){
		this.id=0;//视图编号从0开始
		return this;
	}
	
	public View change(){
		this.id+=k;
		return this;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}

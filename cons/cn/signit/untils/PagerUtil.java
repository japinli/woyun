/**
* @author ZhangHongdong
* @date 2015年2月2日-上午9:59:50
* @see (参阅)
*/
package cn.signit.untils;

/**
 * 分页工具
 * @ClassName PagerUtil
 * @author ZhangHongdong
 * @date 2015年2月2日-上午9:59:50
 * @version 1.0.0
 */
public class PagerUtil {
		private int currentPage;
		private int perPageRows;
		private long totalRows;
		
		/**
		*分页工具构造器
		* @param currentPage 当前页
		* @param perPageRows 每页显示的行数
		* @param totalRows 总行数
		*/
		public PagerUtil(int currentPage, int perPageRows, long totalRows) {
			super();
			if(currentPage < 1){
				new Throwable("当前页数不应小于1").printStackTrace();
				return;
			}
			
			if(perPageRows < 1){
				new Throwable("每页显示的行数不应小于1").printStackTrace();
				return;
			}
			
			this.currentPage = currentPage;
			this.perPageRows = perPageRows;
			this.totalRows = totalRows;
		}
		
		/**
		*分页工具构造器
		* @param paging 按照 "[currentPage],[perPageRows]"构造的查询字符串
		* @param totalRows 总行数
		*/
		public PagerUtil(String paging, int totalRows) {
			super();
			setPaging(paging);
			if(currentPage < 1){
				new Throwable("当前页数不应小于1").printStackTrace();
				return;
			}
			
			if(perPageRows < 1){
				new Throwable("每页显示的行数不应小于1").printStackTrace();
				return;
			}
			
			//this.currentPage = currentPage;
			//this.perPageRows = perPageRows;
			this.totalRows = totalRows;
		}
		
		/**
		* 获得总页数
		*@return 总页数
		*/
		public int getTotalPages(){
			if(getTotalRows() == 0){
				return 1;
			}else{
				return (int) ((getTotalRows() - 1)/getPerPageRows() + 1);
			}
		}
		
		/**
		* 获得开始索引
		*@return 开始索引
		*/
		public int getStartIndex(){
			return (getCurrentPage() - 1) * getPerPageRows();
		}
		
		/**
		* 获得结束索引
		*@return 结束索引
		*/
		public int getEndIndex(){
			return getCurrentPage() * getPerPageRows();
		}
		
		/**
		*获得索引总数
		*@return 索引总数
		*/
		public int getTotalIndex(){
			return getEndIndex() - getStartIndex();
		}
		
		public long getTotalRows() {
			return totalRows;
		}
		public void setTotalRows(long totalRows) {
			this.totalRows = totalRows;
		}
		public int getCurrentPage() {
			return currentPage;
		}
		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}
		public int getPerPageRows() {
			return perPageRows;
		}
		public void setPerPageRows(int perPageRows) {
			this.perPageRows = perPageRows;
		}
		
		public void setPaging(String paging){
			String[] pageString=paging.split(",");
			try{
				this.currentPage=Integer.parseInt(pageString[0]);
				this.perPageRows=Integer.parseInt(pageString[1]);
			}catch(Exception e){
				new Throwable("参数错误").printStackTrace();
			}
			
		}
		
		public static void main(String[] args) {
			PagerUtil util = new PagerUtil("1,30", 0);
			//PagerUtil util = new PagerUtil(1, 30, 0);
			System.out.println("总页数====> "+util.getTotalPages());
			System.out.println("开始索引====> "+util.getStartIndex());
			System.out.println("结束索引====> "+util.getEndIndex());
		}
}

/**
 * @author ZhangHongdong
 * @date 2015年1月6日-下午1:31:07
 * @see (参阅)
 */
package cn.signit.untils.file.mutifile;

/**
 * 文件上传进度信息
 * 
 * @ClassName ProgressEntity
  * @author liwen
 * @date 2015年1月6日-下午1:31:07
 * @version 1.0.0
 */
public class ProgressEntity {
	/*已经上传到服务器的字节数*/
	private long pBytesRead = 0L;

	/*所有文件的总大小*/
	private long pContentLength = 0L;

	/*第几个文件*/
	private int pItems;

	public long getpBytesRead() {
		return pBytesRead;
	}

	public void setpBytesRead(long pBytesRead) {
		this.pBytesRead = pBytesRead;
	}

	public long getpContentLength() {
		return pContentLength;
	}

	public void setpContentLength(long pContentLength) {
		this.pContentLength = pContentLength;
	}

	public int getpItems() {
		return pItems;
	}

	public void setpItems(int pItems) {
		this.pItems = pItems;
	}

	@Override
	public String toString() {
		return "ProgressEntity [pBytesRead=" + pBytesRead + ", pContentLength="
				+ pContentLength + ", pItems=" + pItems + "]";
	}
}

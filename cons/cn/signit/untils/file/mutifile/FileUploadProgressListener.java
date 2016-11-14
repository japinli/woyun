/**
* @author ZhangHongdong
* @date 2015年1月6日-下午1:22:49
* @see (参阅)
*/
package cn.signit.untils.file.mutifile;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 *文件上传进度监听器
 * @ClassName FileUploadProgressListener
  * @author liwen
 * @date 2015年1月6日-下午1:22:49
 * @version 1.0.0
 */
public class FileUploadProgressListener implements ProgressListener {

	private HttpSession session;  
    public FileUploadProgressListener() {  
    }  
    public FileUploadProgressListener(HttpSession _session) {  
        session=_session;  
        ProgressEntity ps = new ProgressEntity();  
        session.setAttribute("upload_ps", ps);  
    }  
	
	/**
	 *更新监听器状态信息
	 *@param pBytesRead 已经上传到服务器的字节数
	 *@param pContentLength 所有文件的总大小
	 *@param pItems 第几个文件
	 *@see org.apache.commons.fileupload.ProgressListener.update(long pBytesRead, long pContentLength, int pItems)
	 */
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		 ProgressEntity ps = (ProgressEntity) session.getAttribute("upload_ps");  
	        ps.setpBytesRead(pBytesRead);  
	        ps.setpContentLength(pContentLength);  
	        ps.setpItems(pItems);  
	        //更新  
	        session.setAttribute("upload_ps", ps);  
	}

}

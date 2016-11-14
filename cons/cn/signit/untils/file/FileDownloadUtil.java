/**
* @author ZhangHongdong
* @date 2015年4月25日-下午3:39:16
* @see (参阅)
*/
package cn.signit.untils.file;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *文件下载工具类
 * @ClassName FileDownloadUtil
 * @author ZhangHongdong
 * @date 2015年4月25日-下午3:39:16
 * @version 1.1.0
 */
public final class FileDownloadUtil {
		/**
		* 获得客户端浏览器类型，进行相应编码，保证下载文件名不出现乱码
		* 
		*@param request http请求对象
		*@param response  http响应对象
		*@param fileName 原始文件名
		*@return 编码后的文件名
		*/
		public static String getEncodedFileName(HttpServletRequest request, HttpServletResponse response, String fileName){
			String userAgent = request.getHeader("User-Agent");
		    String rtn = "";
		    try {
		        String newFilename = URLEncoder.encode(fileName, "UTF-8");
		        // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
		        rtn = "filename=\"" + newFilename + "\"";
		        if (userAgent != null) {
		            userAgent = userAgent.toLowerCase();
		            // IE浏览器，只能采用URLEncoder编码
		            if (userAgent.indexOf("msie")  != -1 || userAgent.indexOf("trident") != -1) {
		                rtn = "filename=\"" + newFilename + "\"";
		                response.setContentType("application/x-msdownload;");   
		        	    return rtn;
		            }
		            // Safari浏览器，只能采用ISO编码的中文输出
		            else if (userAgent.indexOf("safari") != -1) {
		                rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
		                response.setContentType("applicatoin/octet-stream;");   
		        	    return rtn;
		            }
		            // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
		            else if (userAgent.indexOf("applewebkit") != -1) {
		                newFilename = MimeUtility.encodeText(fileName, "UTF-8", "B");
		                rtn = "filename=\"" + newFilename + "\"";
		                response.setContentType("application/x-msdownload;");   
		        	    return rtn;
		            }
		            // Opera浏览器只能采用filename*
		            else if (userAgent.indexOf("opr") != -1 || userAgent.indexOf("opera") != -1) {
		                rtn = "filename*=UTF-8''" + newFilename;
		                response.setContentType("application/x-msdownload;");   
		        	    return rtn;
		            }
		            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
		            else if (userAgent.indexOf("firefox") != -1 || userAgent.indexOf("mozilla") != -1) {
		                rtn = "filename*=UTF-8''" + newFilename;
		                response.setContentType("application/x-msdownload;");   
		        	    return rtn;
		            }
		        }
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    }
	        response.setContentType("application/x-msdownload;");   
		    return rtn;
		}
}

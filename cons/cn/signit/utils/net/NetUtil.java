/**
* @author ZhangHongdong
* @date 2015年4月20日-下午3:29:19
* @see (参阅)
*/
package cn.signit.utils.net;

/**
 *网络工具类
 * @ClassName NetUtil
 * @author ZhangHongdong
 * @date 2015年4月20日-下午3:29:19
 * @version 1.1.0
 */
import java.io.IOException;  
import java.net.HttpURLConnection;
import java.net.InetAddress;  
import java.net.ServerSocket;
import java.net.Socket;  
import java.net.URL;
import java.net.UnknownHostException;  

import com.google.common.net.InetAddresses;
  
public class NetUtil {  
      
    /*** 
     *  判断本地主机某端口是否被占用
     *  
     * @param port 需要检测的端口号（0～65535）
     */  
    public static boolean isLocalPortUsing(int port){  
    	if(port < 0 || port > 65535){
    		throw new RuntimeException("端口范围只能是0～65535");
    	}
        boolean flag = true;  
        try {  
            flag = isPortUsing("127.0.0.1", port);  
        } catch (Exception e) {  
        }  
        return flag;  
    }  
    /*** 
     *  判断某主机某端口是否被占用 
     * @param host 主机名
     * @param port port 需要检测的端口号（0～65535）
     * @throws UnknownHostException  
     */  
    public static boolean isPortUsing(String host,int port) throws UnknownHostException{  
    	if(port < 0 || port > 65535){
    		throw new RuntimeException("端口范围只能是0～65535");
    	}
        boolean flag = false;  
        InetAddress theAddress = InetAddress.getByName(host);  
        try {  
            Socket socket = new Socket(theAddress,port);  
            flag = true;  
            socket.close();
        } catch (IOException e) {  
        }  
        return flag;  
    }  
    
    /**
    * 随机找到一个本地可用的端口
    * 
    *@return 可用端口号
    */
    public static int findRandomOpenPortOnAllLocalInterfaces()  {
        try (
            ServerSocket socket = new ServerSocket(0);
        ) {
          return socket.getLocalPort();
        } catch (IOException e) {
        	throw new RuntimeException("I/O异常");
		}
      }
    
    /**
     * 通过有效地发送一个HEAD请求，如果响应的http状态码在200-399之间，则返回<code>true</code>，否则返回<code>false</code>
     * 
     * @param url 将要进行ping的HTTP URL
     * @param timeout 表示http连接超时和相应读取的超时（单位：毫秒）
     * @return <code>true</code>如果通过HEAD请求的HTTP URL在指定的超时时间范围内 返回的相应状态码为200-399；否则<code>false</code>.
     */
    public static boolean ping(String url, int timeout) {
        //防止SSL证书无效的异常抛出
        url = url.replaceFirst("^https", "http");
        if(!url.startsWith("http")){
        	url = "http://"+url;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return  responseCode <= 399;
        } catch (IOException exception) {
            return false;
        }
    }
    
    /**
    * 判断ip是否为内网ip
    * 
    *@param ipAddr 待判断的ip地址(支持ip地址和域名)
    *@return true 是内网ip, false 不是内网ip
     * @throws UnknownHostException 
    */
    public static boolean isInnerIp(String ipAddr){
    	    int address;
			try {
				address = InetAddresses.coerceToInteger(InetAddress.getByName(ipAddr));
				return (((address >>> 24) & 0xFF) == 10)
		    	            || ((((address >>> 24) & 0xFF) == 172) 
		    	              && ((address >>> 16) & 0xFF) >= 16 
		    	              && ((address >>> 16) & 0xFF) <= 31)
		    	            || ((((address >>> 24) & 0xFF) == 192) 
		    	              && (((address >>> 16) & 0xFF) == 168));
			} catch (UnknownHostException e) {
				return false;
			}
    }
}  
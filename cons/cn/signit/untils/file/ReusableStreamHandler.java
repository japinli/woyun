/**
* @author ZhangHongdong
* @date 2015年7月29日-上午10:18:37
* @see (参阅)
*/
package cn.signit.untils.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *可复用流助手
 *
 * @ClassName ReusableStreamHandler
 * @author ZhangHongdong
 * @date 2015年7月29日-上午10:18:37
 * @version 1.1.0
 */
public class ReusableStreamHandler {
	private ReusableStreamHandler(){}
	
    private static class ReusableBufferedInputStream  extends BufferedInputStream{
		public ReusableBufferedInputStream(InputStream in) {
			super(in);
			super.mark(Integer.MAX_VALUE);
		}
		
		@Override
		public void close() throws IOException {
			super.reset();
		}
		
		public void finish() throws IOException{
			super.close();
		}
    }
    
	/**
	* 复用输入流
	* 
	*@param is 输入流
	*
	*@return 可复用的输入流
	*/
	public static InputStream reuse(InputStream is){
		if(is == null){
			return null;
		}
		try {
			if(is.markSupported()){
				is.reset();
				return is;
			}
		} catch (IOException e) {}
		return new ReusableBufferedInputStream(is);
	}
	
	/**
	* 完成复用流的使用，关闭该复用流
	* 
	*@param is 复用流
	*
	*@throws IOException
	*/
	public static void reuseFinish(InputStream is) throws IOException{
		if(is == null){
			return;
		}
		if(is.markSupported() && is instanceof ReusableBufferedInputStream){
			((ReusableBufferedInputStream)is).finish();
		}else{
			is.close();
		}
	}
}

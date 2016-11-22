package cn.signit.normal.test;

import java.io.UnsupportedEncodingException;

/**
*(这里用一句话描述这个类的作用)
* @ClassName StringTest
* @author Liwen
* @date 2016年11月21日-上午11:15:45
* @version (版本号)
* @see (参阅)
*/
public class StringTest {

	/**
	* (这里用一句话描述这个方法的作用)
	*@param args
	*@see (参阅)
	*@since (此方法开始于哪个版本)
	*@author Liwen
	 * @throws UnsupportedEncodingException 
	*
	*/
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str="%E4%B8%AA%E4%BA%BA%E6%88%90%E7%BB%A9.pdf";
		String   mytext2   =   java.net.URLDecoder.decode(str,   "utf-8");
		System.out.println(mytext2);
	}

}

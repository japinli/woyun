/**
* @author:ZhangHongdong
* @date:2014年12月11日-下午2:57:36
* @see: (参阅)
*/
package cn.signit.tools.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import cn.signit.untils.file.FileHandler;


/**
 *	Base64工具类
 * @ClassName: Base64Utils
 * @author:ZhangHongdong
 * @date:2014年12月11日-下午2:57:36
 * @version:1.0.0
 */
public class Base64Utils {
		public static byte[] decode(String base64Str){
			return DatatypeConverter.parseBase64Binary(base64Str);
		}
		
		public static byte[] decode4URL(String base64Str){
			return DatatypeConverter.parseBase64Binary(base64Str.replace('-', '+').replace('_', '/'));
		}
		
		public static String encode(byte[] strBytes){
			return DatatypeConverter.printBase64Binary(strBytes);
		}
		
		public static String encode(InputStream is) throws IOException{
			byte[] buf = new byte[4096];
			int len;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while((len = is.read(buf)) > 0){
				baos.write(buf, 0, len);
			}
			baos.flush();
			String base64Out = DatatypeConverter.printBase64Binary(baos.toByteArray());
			if(baos != null){
				baos.close();
			}
			if(is != null){
				is.close();
			}
			return base64Out;
		}
		
		public static String encode4URL(byte[] strBytes){
			String tempBase64Str = DatatypeConverter.printBase64Binary(strBytes);
			return tempBase64Str.replace('+','-').replace('/', '_');
		}
		
		public static String toMD5Str(String base64Str){
			return MD5Utils.toMD5(base64Str);
		}
		
		public static void main(String[] args) throws IOException {
			OutputStream os = new FileOutputStream("/home/zhd/seal-base64.txt");
			os.write(encode(FileHandler.readFile2Bytes("/home/zhd/seal.png")).getBytes());
			os.flush();
			os.close();
		}
}

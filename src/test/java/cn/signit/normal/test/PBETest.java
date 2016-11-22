package cn.signit.normal.test;
import cn.signit.pkcs.PKCS;
import cn.signit.pkcs.crypto.PBECoder;
import cn.signit.pkcs.digests.MD5;
import cn.signit.pkcs.digests.MD5FileUtil;
import cn.signit.sdk.internal.util.codec.Base64;
import cn.signit.tools.utils.MD5Utils;

import java.io.File;
import java.io.FileInputStream;
import cn.signit.untils.file.FileHandler;

/**
*(这里用一句话描述这个类的作用)
* @ClassName PBETest
* @author Liwen
* @date 2016年11月17日-下午1:54:13
* @version (版本号)
* @see (参阅)
*/
public class PBETest {

	/**
	* (这里用一句话描述这个方法的作用)
	*@param args
	*@see (参阅)
	*@since (此方法开始于哪个版本)
	*@author Liwen
	 * @throws Exception 
	*
	*/
	public static void main(String[] args) throws Exception {
		PKCS.init();
		String todata="asdfasdfadsf";
		//byte[] toEncrypto=FileHandler.readStream2Bytes(new FileInputStream("H:\\pdf\\PDF测试 (4).pdf"));
		byte[] toEncrypto=todata.getBytes();
		String password="123456abcd";
		System.out.println("原文："+toEncrypto);   
		System.out.println("密码："+password);
		  //初始化盐    
       // byte[] salt=PBECoder.initSalt();    
		//byte[] salt=MD5FileUtil.getFileRealMD5Bytes(new File("H:\\pdf\\PDF测试 (4).pdf"));
		byte[] salt=MD5Utils.toMD5(password).getBytes();
        System.out.println("盐："+Base64.encodeBase64String(salt));
      //加密数据    
        byte[] data=PBECoder.encrypt(toEncrypto, password, salt);    
        System.out.println("加密后："+Base64.encodeBase64String(data));
        //FileHandler.writeBytes2File(data, new File("H:\\pdf\\crypto_.pdf"));
        //解密数据    
        data=PBECoder.decrypt(data, password, salt);    
        System.out.println("解密后："+new String(data));
	}

}

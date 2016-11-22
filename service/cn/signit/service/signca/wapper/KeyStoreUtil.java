package cn.signit.service.signca.wapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cn.signit.tools.utils.Base64Utils;

/**
 * 秘钥库工具类
 * 
 * @ClassName KeyStoreUtil
 * @author Liwen
 * @date 2016年5月19日-下午4:48:06
 * @version (版本号)
 * @see (参阅)
 */
public class KeyStoreUtil {
	public static final String KEY_STORE_TYPE_PFX = "PKCS12";
	public static final String KEY_STORE_TYPE_JSK = "jks";
	public static String TYPE = KEY_STORE_TYPE_PFX;

	/**
	 * 解析Base64编码的秘钥库
	 * @param base64KeyStore base64编码的秘钥库
	 * @param password 秘钥库访问密码
	 * @param type 秘钥库初始化类型 jsk,pkcs12
	 * @return 解析后的keystore
	 */
	public static KeyStore toKeyStore(String base64KeyStore, String password, String type) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(type);
		byte[] keyStoreByte = Base64Utils.decode(base64KeyStore);
		keyStore.load(new ByteArrayInputStream(keyStoreByte), password.toCharArray());
		return keyStore;
	}

	/**
	 * 将秘钥库转换为base64编码
	 * @param keyStore 秘钥库
	 * @param storePwd 秘钥库访问密码
	 * @return 经过base64编码后的秘钥库
	 */
	public static String getBase64KeyStore(KeyStore keyStore, char[] storePwd) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		keyStore.store(out, storePwd);
		out.close();
		return Base64Utils.encode(out.toByteArray());
	}

	/**
	 * 获取字节流型秘钥库
	 * @param ksStream 秘钥库输入流
	 * @param storePwd 秘钥库访问密码
	 * @param type,秘钥库初始化类型,jks,pksc12
	 * @return byte[]型秘钥库
	 */
	public static byte[] getKeyStoreByte(InputStream ksStream, char[] storePwd, String type) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(type, "BC");
		keyStore.load(ksStream, storePwd);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		keyStore.store(out, storePwd);
		out.close();
		return out.toByteArray();
	}

	/**
	 * 从已初始化的秘钥库中枚举证书列表
	 * @param ks 秘钥库
	 * @return List<X509Certificate>
	 */
	public static List<X509Certificate> getCerts(KeyStore ks) {
		try {
			List<X509Certificate> list = new ArrayList<X509Certificate>();
			Enumeration<String> alias = ks.aliases();
			while (alias.hasMoreElements()) {
				String alia = alias.nextElement();
				list.add((X509Certificate) ks.getCertificate(alia));
			}
			return list;
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从已初始化的秘钥库中枚举用户昵称
	 * @param ks 已初始化的秘钥库
	 * @return 用户昵称
	 */
	public static String getKeyAlia(KeyStore ks) throws Exception {
		Enumeration<String> alias = ks.aliases();
		while (alias.hasMoreElements()) {
			String alia = alias.nextElement();
			if (ks.isKeyEntry(alia)) {
				return alia;
			}
		}
		return null;
	}

	/**
	 * 从已初始化的秘钥库中枚举用户证书列表
	 * @param ks 秘钥库
	 * @return Certificate[]
	 */
	public static Certificate[] getCertChain(KeyStore ks) throws Exception {
		return ks.getCertificateChain(getKeyAlia(ks));
	}

	/**
	 * 从已初始化的秘钥库中枚举用户证书
	 * @param ks 秘钥库
	 * @return Certificate
	 */
	public static Certificate getCert(KeyStore ks) throws Exception {
		return ks.getCertificate(getKeyAlia(ks));

	}

	/**
	 * 从秘钥库中枚举用户私钥
	 * @param ks
	 * @param keyPwd 秘钥库访问密码
	 * @return PrivateKey 用户私钥
	 */
	public static PrivateKey getPrivateKey(KeyStore ks, String keyPwd) throws Exception {
		return (PrivateKey) ks.getKey(getKeyAlia(ks), keyPwd.toCharArray());
	}

}

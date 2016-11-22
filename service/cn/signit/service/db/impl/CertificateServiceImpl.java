package cn.signit.service.db.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.bouncycastle.operator.ContentSigner;
import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.UserCertificateMapper;
import cn.signit.domain.mongo.file.LocalStoreFile;
import cn.signit.domain.mysql.UserCertificate;
import cn.signit.pkcs.cert.X509Signer;
import cn.signit.pkcs.x509.keystore.KeyStoreUtil;
import cn.signit.service.db.CertificateSerivce;
import cn.signit.service.files.StoreService;
import cn.signit.service.signca.CertRequestService;
import cn.signit.service.signca.wapper.P12CertRequestWrapper;
import cn.signit.service.signca.wapper.P12CertResp;

/**
*证书服务类
* @ClassName CertificateServiceImpl
* @author Liwen
* @date 2016年11月18日-下午2:03:11
* @version (版本号)
* @see (参阅)
*/
@Service("certificateService")
public class CertificateServiceImpl implements CertificateSerivce{

	@Resource
	private CertRequestService certRequestService;
	@Resource
	private StoreService certFileService;
	@Resource
	private UserCertificateMapper userCertDao;
	/**
	*@param userId
	*@param p12
	*@return
	*@see (参阅)
	*/
	@Override
	public Long requestCert(Long userId, P12CertRequestWrapper p12) {
		//获取用户信息
		
		//申请证书
		P12CertResp p12CertResp=certRequestService.doP12CertRequest(p12);
		if(p12CertResp == null){
			return -1L;
		}
		//存储至文件数据库
		LocalStoreFile certFile=new LocalStoreFile(p12CertResp.getSerialCode().concat(".p12"),
				"application/x-pkcs12", null, null, 
				3000, null, new ByteArrayInputStream(p12CertResp.getPfxOutBytes()));
		String fileId=String.valueOf(certFileService.save(certFile));
		//包装证书信息
		UserCertificate cert=new UserCertificate(p12CertResp.getAuthorizationCode(),
				p12CertResp.getReferenceNumber(),p12CertResp.getSerialCode(),
				p12CertResp.getSubject(),p12CertResp.getIssuer())
				.setStartTime(p12CertResp.getStartTime())
				.setEndTime(p12CertResp.getEndTime())
				.setFileId(fileId)
				.setUserId(userId);
		return userCertDao.insertSelectiveAndGetId(cert);
	}

	/**
	*@param userId
	*@return
	*@see (参阅)
	*/
	@Override
	public UserCertificate getUserCert(Long userId,boolean withData) {
		List<UserCertificate> lists=userCertDao.selectSelective(new UserCertificate().setUserId(userId));
		if(lists.size()>0){
			UserCertificate cert=lists.get(0);
			if(!withData){
				return cert;
			}else{
				LocalStoreFile certFile=getCertFile(cert.getFileId());
				cert.attachCertStream(certFile.getContentStream());
				return cert;
			}
		}
		return null;
	}
	
	private LocalStoreFile getCertFile(String fileId){
		List<LocalStoreFile> storeFiles=getStoreFiles(certFileService,fileId);
		for(LocalStoreFile file:storeFiles){
			String id=file.getId().toString();
			if(id.equals(fileId)){
				return file;
			}
		}
		return null;
	}
	
	
	/**
	* 获取存储文件集合
	*@param storeService 存储服务接口
	*@param fileIds 文件id列表
	*@return 存储文件集合
	*/
	public static List<LocalStoreFile> getStoreFiles(StoreService storeService,Object... fileIds){
		List<Object> ids = new ArrayList<Object>();
		ids.addAll(Arrays.asList(fileIds));
		return storeService.gets(ids);
	}

	/**
	*@param userId
	*@return
	*@see (参阅)
	*/
	@Override
	public ContentSigner getUserSigner(Long userId,String password) {
		UserCertificate certificate=getUserCert(userId,true);
		try {
			//根据用户秘钥库初始化签名提供者
			KeyStore userKeyStore=getKeyStore(certificate.getCertStream(), password.toCharArray());
			return new X509Signer("SHA1WithRSAEncryption")
					.getSigner(KeyStoreUtil.getPrivateKey(userKeyStore, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private  KeyStore getKeyStore(InputStream ksStream,char[] storePwd)throws Exception{
		KeyStore keyStore=KeyStore.getInstance("PKCS12");
		keyStore.load(ksStream, storePwd);
		return keyStore;
	}

	/**
	*@param userId
	*@param password
	*@return
	*@see (参阅)
	*/
	@Override
	public KeyStore getUserKeyStore(Long userId, String password) {
		UserCertificate certificate=getUserCert(userId,true);
		try {
			return getKeyStore(certificate.getCertStream(), password.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

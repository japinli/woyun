package cn.signit.controller.evidences;

import java.security.KeyStore;
import java.security.MessageDigest;

import javax.annotation.Resource;

import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.tsp.TimeStampToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.signit.cons.PageLogicPath;
import cn.signit.cons.UrlPath;
import cn.signit.cons.desc.IdentifyStateDesc;
import cn.signit.cons.desc.UserLevelType;
import cn.signit.cons.rest.RestJsonPath;
import cn.signit.controller.beans.EvidenceSelectData;
import cn.signit.controller.beans.ExtendUploadData;
import cn.signit.controller.beans.UserInfoShow;
import cn.signit.controller.beans.VerifyData;
import cn.signit.domain.mysql.EvidenceInfo;
import cn.signit.domain.mysql.ProveInfo;
import cn.signit.domain.mysql.User;
import cn.signit.pkcs.cert.X509CertSigner;
import cn.signit.pkcs.crypto.PBECoder;
import cn.signit.pkcs.p7.BcPkcs7Factory;
import cn.signit.pkcs.x509.tools.SignVerify;
import cn.signit.service.db.CertificateSerivce;
import cn.signit.service.db.EvidencesService;
import cn.signit.service.db.UserService;
import cn.signit.tools.utils.MD5Utils;
import cn.signit.untils.UnicodeUtil;
import cn.signit.untils.file.FileHandler;
import cn.signit.untils.message.CommonResp;
import cn.signit.untils.message.SessionKeys;
import cn.signit.utils.DecimalFormator;
import cn.signit.utils.time.StandardTimes;
import cn.signit.utils.timestamp.TSAClient;

/**
*保全信息相关实现
* @ClassName EvidenceControllerImpl
* @author Liwen
* @date 2016年11月11日-下午4:47:45
* @version (版本号)
* @see (参阅)
*/
@Controller
@SessionAttributes({SessionKeys.LOGIN_USER})
public class EvidenceControllerImpl implements InEvidenceController,EvidenceController{
	@Resource
	private UserService userService;
	@Resource
	private EvidencesService evidencesService;
	@Resource
	private StandardTimes standTime;
	@Resource
	private TSAClient tsaClient;
	@Resource
	private CertificateSerivce certService;

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_DOC_HOME,method={RequestMethod.GET,RequestMethod.POST})
	@Override
	public String getEvidencePage(@ModelAttribute(SessionKeys.LOGIN_USER)User user, Model model) {
		User nowUser=userService.getUser(user.getId());
		model.addAttribute("userInfo", getDisplayMyInfo(nowUser));
		model.addAttribute(SessionKeys.LOGIN_USER, nowUser);
		return PageLogicPath.DOC_HOME.path();
	}

	/**
	*@param user
	*@param multipartFiles
	*@param data
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=RestJsonPath.USER_DOCS,method=RequestMethod.POST)
	@Override
	public CommonResp upload(@PathVariable(UrlPath.PV_USER_ID)Long userId,@RequestPart(name="file",required=false) MultipartFile[] multipartFiles, 
			String properties) {
		
		ExtendUploadData data1 = null;
		if(properties!=null){
			data1=JSONObject.parseObject(properties,ExtendUploadData.class);
		}
		int success=0;
		for(MultipartFile file : multipartFiles){//对每一个文件进行单独处理
			//存储至数据库
			try {
				EvidenceInfo info=commonsUploadSupport(userId,file,data1);
				evidencesService.addEvidencesAndGetId(userId,info);
				//执行区块链相关操作
				
				success++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new CommonResp().basicSuccessMsg("上传成功");//用于测试的简单返回信息
	}

	private EvidenceInfo commonsUploadSupport(Long userId,MultipartFile multipartFiles,ExtendUploadData data) throws Exception{
			//初始化基本信息
			EvidenceInfo info=new EvidenceInfo().init()
					.setName(UnicodeUtil.utf8ToString(multipartFiles.getOriginalFilename()))
					.setSize(DecimalFormator.getDouble(multipartFiles.getSize()/1024));
					
			//文件加密
			if(data.isEncrypted()){//加密
				info.setIsEncrypted(data.isEncrypted())
				.attachFile(FileHandler.readBytes2Stream(
						PBECoder.encrypt(
								multipartFiles.getBytes(),
								data.getPassword(),
								MD5Utils.toMD5(data.getPassword()).getBytes())));//使用用户密码的MD5作为盐
			}else{
				info.attachFile(FileHandler.readBytes2Stream(multipartFiles.getBytes()));
			}
			
			//用户签名
			/*KeyStore kStore=certService.getUserKeyStore(userId, data.getSignPassword());
			BcPkcs7Factory factory=BcPkcs7Factory.initSigner(
					new X509CertSigner("SHA1WithRSAEncryption").load(kStore, data.getSignPassword()));
		    info.setUserSign(factory.sign(FileHandler.readStream2Bytes(info.getOriginalStream())));*/
			//申请时间戳
		    MessageDigest digest=tsaClient.getMessageDigest();
			byte[] digested=digest.digest(FileHandler.readStream2Bytes(info.getOriginalStream()));//对待存储的对象执行摘要
			byte[] timeTokenbyte=tsaClient.getTimeStampToken(digested);
			//设置时间戳信息
			info.setTsaAuth(timeTokenbyte);
			TimeStampToken timeToken=new TimeStampToken(new CMSSignedData(timeTokenbyte));
			//以时间戳服务器时间为准，设置用户保全信息的时间
			info.setTime(timeToken.getTimeStampInfo().getGenTime());
			return info;
	}
	
	/**
	*@param user
	*@param data
	*@return
	*@see (参阅)
	*/
	@Override
	public CommonResp getEvidences(Long userid, EvidenceSelectData data) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@param user
	*@param data
	*@return
	*@see (参阅)
	*/
	@Override
	public CommonResp getEvidence(Long userid, EvidenceSelectData data) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	*@param user
	*@param data
	*@return
	*@see (参阅)
	*/
	@Override
	public CommonResp verify(Long userid, VerifyData data) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@param user
	*@param data
	*@return
	*@see (参阅)
	*/
	@Override
	public CommonResp deleteEvidence(Long userid, EvidenceSelectData data) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*@param user
	*@param data
	*@return
	*@see (参阅)
	*/
	@Override
	public CommonResp updateEvidence(Long userid, EvidenceSelectData data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 个人信息中心数据封装
	 */
	private UserInfoShow getDisplayMyInfo(User user){
		UserInfoShow show=new UserInfoShow();
		show.setAccount(availableAccount(user));
		show.setAccountType("普通账户");
		show.setEmail(user.getEmail());
		show.setPhone(user.getPhone());
		show.setRealName(user.getRealName());
		show.setEmailIdentify(show.getEmailIdentify());
		show.setPhoneIdentify(show.getPhoneIdentify());
		show.setIdentifyLevel(UserLevelType.getDescription(user.getLevel()));
		return identifyInfoHandle(show,user);
	}
	
	/**
	 * 处理身份认证信息
	 * @param info
	 * @param user
	 * @return UserInfoShow 处理后的信息
	 */
	private UserInfoShow identifyInfoHandle(UserInfoShow info,User user){
		
		int state=IdentifyStateDesc.UNTREATED.getState();//初始化状态为未认证
		info.setIdentify(IdentifyStateDesc.getDescription(state));
		ProveInfo proveInfo=userService.getUserProveInfo(user.getId());
		if(proveInfo!=null){
			return initByEnterpriseProveInfo(info,proveInfo);
		}
		return info;
	}
	
	/**
	 * 根据企业认证信息初始化账户的相关信息
	 */
	private UserInfoShow initByEnterpriseProveInfo(UserInfoShow info,ProveInfo ep){
		info.setRealName(ep.getName());//展示的信息，账户真实姓名为认证的企业名称
		if(ep.getPhone()!=null){//根据06.16晚讨论的结果，如果账户已经绑定的手机登录账号，那么判断为已认证。如果账户的认证资料中包含手机号，那么也认为是已认证。
			info.setPhoneIdentify(IdentifyStateDesc.PASS.getDescription());
		}
		info.setIdentify(IdentifyStateDesc.getDescription(ep.getState()));
		return info;
		
	}
	/**
	 * 获取用户账户名
	 * @param user
	 * @return 账户
	 */
	public String availableAccount(User user){
		if(user.getEmail() != null && !user.getEmail().trim().equals("")){
			return user.getEmail();
		}
		if(user.getPhone() != null && !user.getPhone().trim().equals("")){
			return user.getPhone();
		}
		return "unknown";
	}

	/**
	*@param user
	*@param docId
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_DOC_ANY_SIGN,method=RequestMethod.GET)
	@Override
	public String getSignaturePage( @ModelAttribute(SessionKeys.LOGIN_USER)User user,@RequestParam(UrlPath.PV_DOC_ID)String docId,Model model) {
		
		return PageLogicPath.DOC_SIGN.path();
	}

	/**
	* 获取文档自签名页面
	*@param model 需要动态填充的数据模型
	*@return 创建文档自签名页面逻辑地址
	*@param user
	*@param docId
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_DOC_SIGN_SELF_SIGN,method=RequestMethod.GET)
	@Override
	public String getSignSelfSianaturePage(@ModelAttribute(SessionKeys.LOGIN_USER)User user,@RequestParam(UrlPath.PV_DOC_ID)String docId,Model model) {
		// TODO Auto-generated method stub
		return PageLogicPath.DOC_SIGN_SELF_SIGN.path();
	}

	/**
	*@param user
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_DOC_SIGN_SELF,method=RequestMethod.GET)
	@Override
	public String getSignSelfPage(@ModelAttribute(SessionKeys.LOGIN_USER)User user,Model model) {
		User nowUser=userService.getUser(user.getId());
		model.addAttribute("userInfo", getDisplayMyInfo(nowUser));
		model.addAttribute(SessionKeys.LOGIN_USER, nowUser);
		return PageLogicPath.DOC_SIGN_SELF.path();
	}

	/**
	*@param user
	*@param docId
	*@param model
	*@return
	*@see (参阅)
	*/
	@RequestMapping(value=UrlPath.PAGE_DOC_VERIFY,method=RequestMethod.GET)
	@Override
	public String getVerificationPage(@ModelAttribute(SessionKeys.LOGIN_USER)User user,@RequestParam(UrlPath.PV_DOC_ID)String docId,Model model) {
		
		return PageLogicPath.DOC_VERIFY.path();
	}


	
}

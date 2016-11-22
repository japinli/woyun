package cn.signit.controller.evidences;

import org.springframework.web.multipart.MultipartFile;

import cn.signit.controller.beans.EvidenceSelectData;
import cn.signit.controller.beans.ExtendUploadData;
import cn.signit.controller.beans.VerifyData;
import cn.signit.domain.mysql.User;
import cn.signit.untils.message.CommonResp;

/**
*保全信息相关基本处理
* @ClassName EvidenceController
* @author Liwen
* @date 2016年11月11日-下午4:36:26
* @version (版本号)
* @see (参阅)
*/
public interface EvidenceController {
	/**
	 * 用户保全信息上传
	 */
	public CommonResp upload(Long userid,MultipartFile[] multipartFiles,String data);
	
	/**
	 * 获取指定保全信息列表
	 */
	public CommonResp getEvidences(Long userid,EvidenceSelectData data);
	
	/**
	 * 获取指定保全信息详细信息
	 */
	public CommonResp getEvidence(Long userid,EvidenceSelectData data);
	
	/**
	 * 指定保全信息验证
	 */
	public CommonResp verify(Long userid,VerifyData data);
	
	/**
	 * 删除指定保全信息?允许删除吗？
	 */
	public CommonResp deleteEvidence(Long userid,EvidenceSelectData data);
	
	/**
	 * 更新指定保全信息?允许更新吗？
	 */
	public CommonResp updateEvidence(Long userid,EvidenceSelectData data);
}

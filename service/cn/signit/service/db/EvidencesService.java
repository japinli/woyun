package cn.signit.service.db;

import java.util.List;

import cn.signit.domain.mysql.EvidenceInfo;

/**
*用户保全信息服务
* @ClassName EvidencesService
* @author Liwen
* @date 2016年11月16日-下午5:00:17
* @version (版本号)
* @see (参阅)
*/
public interface EvidencesService {
	/**
	 * 新增保全信息
	 */
	public boolean addEvidences(Long userid,EvidenceInfo info);
	/**
	 * 新增保全信息并且获取id
	 */
	public Long addEvidencesAndGetId(Long userid,EvidenceInfo info);
	/**
	 * 根据ID查询指定保全信息
	 */
	public EvidenceInfo getEvidence(Long evidence_id);
	/**
	 * 条件查询指定保全信息
	 */
	public EvidenceInfo getEvidence(EvidenceInfo info);
	
	/**
	 * 获取指定用户的所有保全信息
	 */
	public List<EvidenceInfo> getUserEvidences(Long userid);
	/**
	 * 获取指定用户的指定条件的保全信息
	 */
	public List<EvidenceInfo> getUserEvidences(Long userid,EvidenceInfo info);
}

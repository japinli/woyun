package cn.signit.service.db.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import cn.signit.dao.mysql.EvidenceInfoMapper;
import cn.signit.dao.mysql.ReUserEvidenceMapper;
import cn.signit.domain.mongo.file.LocalStoreFile;
import cn.signit.domain.mysql.EvidenceInfo;
import cn.signit.domain.mysql.ReUserEvidence;
import cn.signit.service.db.EvidencesService;
import cn.signit.service.files.StoreService;

/**
*
* @ClassName EvidencesServiceImpl
* @author Liwen
* @date 2016年11月16日-下午5:29:10
* @version (版本号)
* @see (参阅)
*/
@Service("evidencesService")
public class EvidencesServiceImpl implements EvidencesService{
	
	@Resource
	private ReUserEvidenceMapper reUserEvidenceDao;
	@Resource
	private EvidenceInfoMapper evidenceDao;
	@Resource
	private StoreService storeService;
	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean addEvidences(Long userid,EvidenceInfo info) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public Long addEvidencesAndGetId(Long userid,EvidenceInfo info) {
		//存储值文件数据库
		List<Object> ids=storeService.saves(convert(info));
		//存储至本地数据库
		if(ids.isEmpty())
			return -1L;
		
		int cnt=0;
		for(Object id:ids){
			switch (cnt) {
			case 0:
				info.setLocation(id.toString());
				break;

			default:
				break;
			}
			cnt++;
		}
		evidenceDao.insertAndGetId(info);
		Long id=info.getId();
		//生成关联表信息
		if(id!=null){
			ReUserEvidence re=new ReUserEvidence();
			re.setId(id);
			re.setUserId(userid);
			reUserEvidenceDao.insertSelective(re);
			return 0L;
		}else{
			return -1L;
		}
	}
  
	
	private List<LocalStoreFile> convert(EvidenceInfo info){
		List<LocalStoreFile> storeFiles=new ArrayList<>();
		int typeCode = 10000;
		String docBaseName = FilenameUtils.getBaseName(info.getName());
		String docType = FilenameUtils.getExtension(info.getName()).toLowerCase();
		if(info.getOriginalStream()!=null){//文件数据不为空
			LocalStoreFile file1=new LocalStoreFile(info.getLocation(),
					info.getName(),docType,Arrays.asList(info.getSerialCode().concat("0"))
					,null,typeCode,null,info.getOriginalStream());
			storeFiles.add(file1);
		}
		return storeFiles;
	}
	
	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param id
	*@return
	*@see (参阅)
	*/
	@Override
	public EvidenceInfo getEvidence(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public EvidenceInfo getEvidence(EvidenceInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param userid
	*@return
	*@see (参阅)
	*/
	@Override
	public List<EvidenceInfo> getUserEvidences(Long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@param userid
	*@param info
	*@return
	*@see (参阅)
	*/
	@Override
	public List<EvidenceInfo> getUserEvidences(Long userid, EvidenceInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	*//**
	 * 执行普通信息转换至存储信息
	 *//*
	private List<LocalStoreFile> convert(EvidenceInfo info){
		return null;
		
	}*/
	
}

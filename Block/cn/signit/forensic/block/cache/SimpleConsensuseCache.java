package cn.signit.forensic.block.cache;

import java.util.ArrayList;
import java.util.List;

import cn.signit.domain.mysql.EvidenceInfo;
import cn.signit.forensic.block.BlockCache;
import cn.signit.forensic.block.ConsensusInterface;
import cn.signit.forensic.block.beans.View;

/**
*共识达成过程简单缓存实现
* @ClassName SimpleConsensuseCache
* @author Liwen
* @date 2016年11月6日-下午4:15:47
* @version (版本号)
* @see (参阅)
*/
public class SimpleConsensuseCache implements BlockCache,ConsensusInterface{
	
	//当前区块高度
	private int blockheight;
	//当前视图
	private View view;
	//当前待记录的列表
	private List<EvidenceInfo> records;
	
	private List<EvidenceInfo> remainRecords;

	public SimpleConsensuseCache(){
		records=new ArrayList<>();
		remainRecords=new ArrayList<>();
	}
	
	public int getBlockheight() {
		return blockheight;
	}
	public void setBlockheight(int blockheight) {
		this.blockheight = blockheight;
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public List<EvidenceInfo> getRecords() {
		return records;
	}
	public void setRecords(List<EvidenceInfo> records) {
		this.records = records;
	}

	/**
	*@param o
	*@return
	*@see (参阅)
	*/
	@Override
	public SimpleConsensuseCache addInfo(Object o) {
		if(o instanceof EvidenceInfo){
			this.records.add((EvidenceInfo)o);
		}else{
			throw new IllegalArgumentException("无法识别的保全对象信息");
		}
		return this;
	}

	/**
	*@param o
	*@see (参阅)
	*/
	@Override
	public SimpleConsensuseCache removeInfo(Object o) {
		EvidenceInfo info;
		if(o instanceof EvidenceInfo){
			info=(EvidenceInfo)o;
		}else{
			throw new IllegalArgumentException("无法识别的保全对象信息");
		}
		if(records.size()>0){
			for(EvidenceInfo temp:records){
				if(info.equals(temp)){
					records.remove(temp);
				}
			}
		}
		return this;
		
	}

	/**
	*@see (参阅)
	*/
	@Override
	public SimpleConsensuseCache reset() {
		if(blockheight!=0){
			this.blockheight+=1;
		}else{
			this.blockheight=0;
		}
		this.view=new View();
		if(remainRecords.size()>0){
			this.records=remainRecords;
		}else{
			records=new ArrayList<>();
		}
		return this;
	}

	/**
	*@see (参阅)
	*/
	@Override
	public void recivePerpareRequest() {
		// TODO Auto-generated method stub
		
	}

	/**
	*@see (参阅)
	*/
	@Override
	public void compute() {
		// TODO Auto-generated method stub
		
	}

	/** 
	*@see (参阅)
	*/
	@Override
	public void recivePerpareResponse() {
		// TODO Auto-generated method stub
		
	}

	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public boolean isConsensusComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	*@see (参阅)
	*/
	@Override
	public void changeView(View view) {
		this.view=view;
		
	}

}

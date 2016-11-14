package cn.signit.controller.api;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import cn.signit.controller.ServerConnectController;
import cn.signit.sdk.BaseResponse;
import cn.signit.service.db.AppInfoService;


/**
*服务器间通信相关
* @ClassName ApiCertControllerImpl
* @author Liwen
* @date 2016年5月12日-上午11:37:54
* @version (版本号)
* @see (参阅)
*/
@Controller
public class ServerConnectControllerImpl implements ServerConnectController{
	@Resource
	private  AppInfoService appInfoService;

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public BaseResponse memberPeersHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public BaseResponse selectHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	*(这里用一句话描述这个重写方法的作用) 
	*@return
	*@see (参阅)
	*/
	@Override
	public BaseResponse addHandle() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

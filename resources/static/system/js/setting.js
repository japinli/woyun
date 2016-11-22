var RED_COLOR = "#F0555A";
var ORANGE_COLOR = "orange";
var GREEN_COLOR = "#39b94e";
$(document).ready(function(){
	var roleCode = $("#_user-role-code").val();
	 //设置用户角色
	 setUserRole(roleCode);
	 //初始化页面表单元素
	// initAccountSettingForm(roleCode);
	 if( roleCode == 20000 ){
		 //个人
		 initAccountSettingForm(function(data){
			 var code = data.resultCode;
			 if( code == 0 ){
				 showPersonInfo(data);
				
			 }
		 });
		 
	 }else if( roleCode == 20001 ){
		 //企业
		 initAccountSettingForm(function(data){
			 var code = data.resultCode;
			 if( code == 0 ){
				 showEnterInfo(data);
				
			 }
		 });
		 
	 }	//end else
	 
	 $("#js_modify_dialog").dialog({ //通用确认框
	        autoOpen: false,
	        width: 380,
	        modal: true
	    });
});

function setUserRole(roleCode){
	if(roleCode == 20000){
		$("#enterpriseAuthItem").empty();
	}else if(roleCode==20001){
		$("#personAuthItem").empty();
	}
}

function initAccountSettingForm(callback){
	requestServer({
        requestURL: '/user/sys/users/setting/base/info/get',
        requestType: 'GET',
       // async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
           // var code = data.resultCode;
           // var desc = data.resultDesc;
        	if(callback){
        		callback(data);
        	}
            hideLoading();
        }
    });
}

//企业信息展示
function showEnterInfo( data ){
		$("#js_setall_container").html(
			    '<div class="setInfoBlock1">'+
				    '<p><span>我的账号：</span><span id="infoBlock1LoginName">'+ data.user.loginName +'</span></p>'+
					'<p><span>认证等级：</span><span id="infoBlock1AuthLevel">'+ fillInAuthLevel( data.user.authLevel ) +'</span></p>'+
					'<p><span>账户类型：</span><span id="infoBlock1ServiceType">'+ fillInServiceType( data.user.serviceType ) +'</span></p>'+
					/*'<p><span class="setInfoTitle">账户余额：</span><span class="acountMon">0</span><a class="setPayLink" href="user/sys/account/payment">充值</a></p>'+*/
					'<p class="setInfoSmall"><span class="">购买套餐</span><a class="setPayLink" href="/user/sys/account/combo">点击升值套餐</a></p>'+
				'</div>'+
				'<div class="setInfoBlock2">'+
				   '	<div class="setInfoItem" id="enterpriseAuthItem">'+
						'<i class="enterprise-icon"></i>'+
						'<p class="setInfo-header">企业实名认证</p>'+
						'<p id="infoBlock2EnterpriseAuthContent">'+
		    				'<span class="setNoContent">未认证</span>'+
		    				'<a class="setFuncCss" href="user/sys/users/setting/auth/enterprise">立即认证</a>'+
						'</p>'+
					'</div>'+
					'<div class="setInfoItem">'+
						'<i class="email-icon"></i>'+
						'<p class="setInfo-header">邮箱认证</p>'+
						'<p id="infoBlock2EmailAuthContent">'+
							'<span class="setNoContent">未认证</span>'+
							'<a class="setFuncCss" href="">立即认证</a>'+
						'</p>'+
					'</div>'+
					'<div class="setInfoItem">'+
						'<i class="phone-icon"></i>'+
						'<p class="setInfo-header">手机认证</p>'+
						'<p id="infoBlock2PhoneAuthContent">'+
							'<span class="setNoContent">未认证</span>'+
							'<a class="setFuncCss" href="">立即认证</a>'+
						'</p>'+
					'</div>'+
				'</div>'
			);
		
			//购买套餐
			showCombo(data.user.isRealNameAuth, $(".setPayLink"), data.user.authState);
			
			//基本信息上部-右边
			fillInAuth($("#infoBlock2EnterpriseAuthContent"),data.user.isRealNameAuth,'user/sys/users/setting/auth/enterprise',data.user.authState);
			otherAuth(false, $("#infoBlock2EmailAuthContent"), data.user.isEmailAuth, data.user.email, 'user/sys/account/info/resetEmail');
			otherAuth(true, $("#infoBlock2PhoneAuthContent"), data.user.isPhoneAuth, null, 'user/sys/account/info/resetPhone');
			//fillInAuth($("#infoBlock2EmailAuthContent"),data.user.isEmailAuth,'user/sys/account/info/resetEmail',null);
			//fillInAuth($("#infoBlock2PhoneAuthContent"),data.user.isPhoneAuth,'user/sys/account/info/resetPhone',null);
			
			$("#js_setinfo_container").html(
					'<tbody>'+
						'<tr>'+
							'<th>登录邮箱地址：<span id="setTableEmail">未知</span></th>'+
							'<td><a>修改</a></td>'+
						'</tr>'+
						'<tr>'+
							'<th>登录密码：<span id="setTablePwd">未知</span></th>'+
							'<td><a>修改</a></td>'+
						'</tr>'+
						'<tr>'+
							'<th>登录手机号码：<span id="setTablePhone">未知</span></th>'+
							'<td><a>修改</a></td>'+
						'</tr>'+
						/*<tr>
							<th>真实姓名：<span id="setTableRealName">未知</span></th>
							<td><a>查看</a></td>
						</tr>
						<tr>
							<th>身份证号：<span id="setTableIdCardCode"> 未知</span></th>
							<td><a>查看</a></td>
						</tr>*/
				'</tbody>'
			);
			//基本信息下部
			fillInBaseInfo($("#setTableEmail"),data.user.email,'#','#','user/sys/account/info/resetEmail','#',data.user.isEmailAuth);
			fillInBaseInfo($("#setTablePhone"),data.user.phone,'#','#','user/sys/account/info/resetPhone','#',data.user.isPhoneAuth);
			fillInBaseInfo($("#setTablePwd"),data.user.hiddenPassword,'#','#','user/sys/account/info/resetPwd','#',null);
			//fillInBaseInfo($("#setTableRealName"),data.user.realName,'#','#','#','#',null);
			//fillInBaseInfo($("#setTableIdCardCode"),data.user.idCardCode,'#','#','#','#',null);
			
			//支付信息
			// getUserPayInfo($(".acountMon"));
}

//个人信息展示
function showPersonInfo(data){
	var perNameUrl = 'user/sys/users/setting/auth/person' ;
	$("#js_setall_container").html(
			 '<div class="setInfoBlock1">'+
			    '<p><span class="setInfoTitle">我的账号：</span><span id="infoBlock1LoginName">'+ data.user.loginName +'</span></p>'+
				'<p><span class="setInfoTitle">认证等级：</span><span id="infoBlock1AuthLevel">'+ fillInAuthLevel( data.user.authLevel ) +'</span></p>'+
				'<p><span class="setInfoTitle">账户类型：</span><span id="infoBlock1ServiceType">'+ fillInServiceType( data.user.serviceType ) +'</span></p>'+
				/*'<p><span class="setInfoTitle">账户余额：</span><span class="acountMon">0</span><a class="setPayLink" href="user/sys/account/payment">充值</a></p>'+*/
				'<p class="setInfoSmall"><span class="">购买套餐</span><a class="setPayLink" href="/user/sys/account/combo">点击升值套餐</a></p>'+
			'</div>'+
			'<div class="setInfoBlock2">'+
				'<div class="setInfoItem" id="personAuthItem">'+
					'<i class="person-icon"></i>'+
					'<p class="setInfo-header">个人实名认证</p>'+
					'<p id="infoBlock2PersonAuthContent">'+
						'<span class="setNoContent">未认证</span>'+
						'<a class="setFuncCss" href="'+ perNameUrl +'">立即认证</a>'+
					'</p>'+
				'</div>'+
				'<div class="setInfoItem">'+
					'<i class="email-icon"></i>'+
					'<p class="setInfo-header">邮箱认证</p>'+
					'<p id="infoBlock2EmailAuthContent">'+
						'<span class="setNoContent">未认证</span>'+
						'<a class="setFuncCss" href="">立即认证</a>'+
					'</p>'+
				'</div>'+
				'<div class="setInfoItem">'+
					'<i class="phone-icon"></i>'+
					'<p class="setInfo-header">手机认证</p>'+
					'<p id="infoBlock2PhoneAuthContent">'+
						'<span class="setNoContent">未认证</span>'+
						'<a class="setFuncCss" href="">立即认证</a>'+
					'</p>'+
				'</div>'+
			'</div>'
		);
	
	//购买套餐
	showCombo(data.user.isRealNameAuth, $(".setPayLink"), data.user.authState);
	//基本信息上部-右边
	fillInAuth($("#infoBlock2PersonAuthContent"),data.user.isRealNameAuth,perNameUrl,data.user.authState);
	otherAuth(false, $("#infoBlock2EmailAuthContent"), data.user.isEmailAuth, data.user.email, 'user/sys/account/info/resetEmail');
	otherAuth(true, $("#infoBlock2PhoneAuthContent"), data.user.isPhoneAuth, null, 'user/sys/account/info/resetPhone');
	//fillInAuth($("#infoBlock2EmailAuthContent"),data.user.isEmailAuth,'user/sys/account/info/resetEmail',null);
	//fillInAuth($("#infoBlock2PhoneAuthContent"),data.user.isPhoneAuth,'user/sys/account/info/resetPhone',null);
	
	$("#js_setinfo_container").html(
			'<tbody>'+
				'<tr>'+
					'<th>登录邮箱地址：<span id="setTableEmail">未知</span></th>'+
					'<td><a>修改</a></td>'+
				'</tr>'+
				'<tr>'+
					'<th>登录手机号码：<span id="setTablePhone">未知</span></th>'+
					'<td><a>修改</a></td>'+
				'</tr>'+
				'<tr>'+
					'<th>登录密码：<span id="setTablePwd">未知</span></th>'+
					'<td><a>修改</a></td>'+
				'</tr>'+
				'<tr>'+
					'<th>真实姓名：<span id="setTableRealName">未知</span></th>'+
					'<td></td>'+
				'</tr>'+
				'<tr>'+
					'<th>身份证号：<span id="setTableIdCardCode"> 未知</span></th>'+
					'<td></td>'+
				'</tr>'+
		'</tbody>'
	);
	
	//基本信息下部
	fillInBaseInfo($("#setTableEmail"),data.user.email,'#','#','user/sys/account/info/resetEmail','#',data.user.isEmailAuth);
	fillInBaseInfo($("#setTablePhone"),data.user.phone,'#','#','user/sys/account/info/resetPhone','#',data.user.isPhoneAuth);
	fillInBaseInfo($("#setTablePwd"),data.user.hiddenPassword,'#','#','user/sys/account/info/resetPwd','#',null);
	fillInAuthInfo( data.user.isRealNameAuth, data.user.authState, $("#setTableRealName"), data.user.realName, perNameUrl , data.user.authState);
	fillInAuthInfo( data.user.isRealNameAuth, data.user.authState, $("#setTableIdCardCode"), data.user.idCardCode, perNameUrl, data.user.authState);
	
	//支付信息
	// getUserPayInfo($(".acountMon"));
}

//认证状态——添加充值、购买套餐入口
function showCombo(isAuth, $link, option){
	if(isAuth){
		$link.show();
	}else{
		if( option == 2 ){
			//已认证
			$link.show();
		}else{
			return false;
		}
	}
}

//填充认证级别
function fillInAuthLevel(authLevelCode){
	//认证级别：0-弱;1-中等;2-强;
	if(authLevelCode == 0){
		return '<span style="color:'+RED_COLOR+';">弱</span>';
	}else if(authLevelCode == 1){
		return '<span style="color:'+ORANGE_COLOR+';">中等</span>';
	}else if(authLevelCode == 2){
		return '<span style="color:'+GREEN_COLOR+';">强</span>';
	}else{
		return '';
	}
}

//填充服务类型
function fillInServiceType(serviceTypeCode){
	//服务类型：0-个人;1-企业;-1-非法用户
	if(serviceTypeCode == 0){
		return '个人';
	}else if(serviceTypeCode == 1){
		return '企业';
	}else if(serviceTypeCode == -1){
		return '非法用户';
	}else{
		return '';
	}
}

//填充实名认证状态
function fillInAuth(authContent,isAuth,url,option){
	if(isAuth){
		authContent.find('span').css('color',GREEN_COLOR).html('已认证');
		authContent.find('a').empty();
	}else{
		if(authContent.attr('id') == "infoBlock2PersonAuthContent" || authContent.attr('id')  == "infoBlock2EnterpriseAuthContent"){
			//0-未认证;1-认证中;2-已认证;3-认证未通过
			switch (option) {
			case 0:
				authContent.find('span').css('color',RED_COLOR).html('未认证');
				authContent.find('a').css('color',GREEN_COLOR).attr('href',url).text('立即认证');
				break;
			case 1:
				authContent.find('span').css('color',ORANGE_COLOR).html('审核中');
				authContent.find('a').empty();
				break;
			case 2:
				authContent.find('span').css('color',GREEN_COLOR).html('已认证');
				authContent.find('a').empty();
				break;
			case 3:
				authContent.find('span').css('color',RED_COLOR).html('认证未通过');
				authContent.find('a').css('color',GREEN_COLOR).attr('href',url).text('重新认证');
				break;
			default:
				break;
			}
		}else{
			authContent.find('span').css('color',RED_COLOR).html('未认证');
			authContent.find('a').css('color',GREEN_COLOR).attr('href',url).text('立即认证');
		}
	}
}

//填充其他认证状态
function otherAuth(istel, authContent, isAuth, value, url){
	if(istel){
		if( isAuth ){
			authContent.find('span').css('color',GREEN_COLOR).html('已认证');
			authContent.find('a').empty();
		}else{
			authContent.find('span').css('color',RED_COLOR).html('未认证');
			authContent.find('a').css('color',GREEN_COLOR).attr('href',url).text('立即认证');
		}
	}else{
		if( value ){
			authContent.find('span').css('color',GREEN_COLOR).html('已认证');
			authContent.find('a').empty();
		}else{
			authContent.find('span').css('color',RED_COLOR).html('未认证');
			authContent.find('a').css('color',GREEN_COLOR).attr('href',url).text('立即认证');
		}
	}
}

//填充下部信息修改
function fillInBaseInfo(baseInfoContent,baseInfo,setUrl,deleteUrl,updateUrl,getUrl,option){
	//配置项非空时
	if(baseInfo != null && baseInfo != "" && baseInfo != "null"){
		baseInfoContent.text(baseInfo);
		//配置邮箱地址或手机号（支持修改和验证操作）
		if(baseInfoContent.attr('id') == "setTableEmail" || baseInfoContent.attr('id') == "setTablePhone" ){
			if(option){
				baseInfoContent .parent('th')
												  .next()
												  .html('<a href="'+ updateUrl +'">修改</a>');
			}else{
				baseInfoContent .parent('th')
												  .next()
												  .html('<a href="'+ updateUrl +'">修改</a>');
			}
		}
		//配置登录密码（只能执行修改操作）
		if(baseInfoContent.attr('id') == "setTablePwd"){
			baseInfoContent .parent('th')
											   .next()
											   .html('<a href="'+ updateUrl +'">修改</a>');
		}
		
	}else{
		//配置项为空时（只能执行设置操作）
		baseInfoContent.css('color',RED_COLOR)
										  .parent('th')
										  .next()
										  .html('<a href="'+ updateUrl +'" style="color:'+ RED_COLOR +';">设置</a>');
									
	}
}

//实名认证部分字段
function  fillInAuthInfo(isAuth, authState, $elem, value, url,option){
	//0-未认证;1-认证中;2-已认证;3-认证未通过
	if( isAuth ){
		$elem.text( value );
		$elem.parent('th').next().html('<span>已认证</span>');
	}else{
		if( value != null && value != "" && value != "null" ){
			//$elem.text( value );
			switch (option){
			case 0:
				$elem.parent('th').next().html('<a href="'+ url +'">立即添加</a>');
				break;
			case 1:
				$elem.text( value );
				$elem.parent('th').next().html('<span>认证中</span>');
				break;
			case 2:
				$elem.text( value );
				$elem.parent('th').next().html('<span>已认证</span>');
				break;
			case 3:
				$elem.text( value );
				$elem.parent('th').next().html('<a href="'+ url +'">重新添加</a>');
				break;
			default:
				break;
			}
			
		}else{
			$elem.css('color',RED_COLOR)
					  .parent('th')
					  .next()
					  .html('<a href="'+ url +'" style="color:'+ RED_COLOR +';">立即添加</a>');
		}
	} 
}

//获取用户支付信息
//function getUserPayInfo($show){
//	 requestServer({
//	        requestURL: '/user/sys/account/info/query/userInfo',
//	        requestType: 'GET',
//	        beforeCallback: function(){
//	           showLoading();
//	        },
//	        successCallback: function(data){
//	            var code = data.resultCode;
//	            var desc = data.resultDesc;  
//	            if(code == 0){
//	            	if(data.data){
//	            		$show.text(parseFloat((data.data.balanceMoney).toFixed(2)));
//	            	}
	            	
//	            }else{
//	            	return false;
//	            } 
//     	hideLoading();
//	        }
//	    });
//}

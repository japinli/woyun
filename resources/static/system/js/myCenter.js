$(document).ready(function(){
	comboInfoDeal(true);	//初始化套餐信息
	isHasCert();		//初始有证书套餐信息 
	//绑定选择套餐事件
	$(".chooseCombo").unbind('click').click(function(){
/*		if (($(".authIdentify").text()=="未认证"||$(".authIdentify").text()=="未通过")&&$("#js_hasCertCombo").hasClass("comboTab-active")) {			
			confirmInfo("请先进行实名认证再购买有证书套餐！");
		}else if($(".authIdentify").text()=="审核中"&&$("#js_hasCertCombo").hasClass("comboTab-active")){
			confirmInfo("请等待实名认证审核成功再购买有证书套餐！");
		}else{*/
			//更改选择套餐链接
			$(".chooseCombo").attr("href",gfALLDATA("payHref")+"/combo.html");
		/*}else if ($("#js_hasNoCertCombo").hasClass("comboTab-active")) {
			//更改选择套餐链接
			$(".chooseCombo").attr("href",gfALLDATA("payHref")+"/combo.html");
		}*/
	})
	$(".authIdentify").unbind('click').click(function(){
		if ($(".authIdentify").text()=="未认证"||$(".authIdentify").text()=="未通过"){
			//底部备注信息
			var msg="<label class='fs12'>注：实名认证通过，<span class='free'>免费</span>颁发专有证书</label>"
			/*+"<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp企业实名认证通过，<span class='free'>免费</span>"
			+"颁发企业专有证书</label>"*/;
			//选择按钮链接
			var perIdentifyUrl=gfALLDATA("alipayHref")+"/person-auth.html";
			/*var enterpIdentifyUrl=gfALLDATA("alipayHref")+"/enterprise-auth.html";*/
			chooseMune("实名认证",perIdentifyUrl,msg);
		}
	})
});

//动态获取套餐剩余情况
function comboRest(isHasCert,callback){
	var jsonData={
		"withCert":isHasCert
	};
	requestServer({
    requestURL:gfALLDATA("payHref")+'/combos/remain',
    requestType: 'GET',
    requestData:jsonData,
    beforeCallback: function(){
      showLoading();
    },
    successCallback: function(data){
     	var code = data.resultCode;
    	if(code == 0){	//有套餐
	        hideLoading();
	        callback(data);
        }else{	//无套餐
        	hideLoading();
        	return;
        } 
    }
    });
}
//套餐信息更改
function comboInfo(data){
	$("#js_comboRestMount").text(data.comboMonut);
	$("#js_comboRestDays").text(data.comboDays);
	$("#js_buyComboInfo").text(data.comboName);
	var comboMonutRestPt=transeTwo(data.comboMonut/data.comboMonutTotle);
	var comboDaysRestPt=transeTwo(data.comboDays/data.comboDaysTotle);
	var thisMountCricle=$("#js_rightCricle-mount");
	var thisDaysCricle=$("#js_rightCricle-days");
	progressUpdate(comboMonutRestPt,thisMountCricle,1);//剩余签名数进度圈
	progressUpdate(comboDaysRestPt,thisDaysCricle,2);//剩余天数进度圈
}

//套餐剩余信息返回处理
function comboInfoDeal(isHasCert){
	var comboMsg={
		"comboMonut":0,
		"comboMonutTotle":1,
		"comboDays":0,
		"comboDaysTotle":1,
		"comboName":"无"
	}
	if (isHasCert) {
		comboRest(isHasCert,function(data){
			comboMsg.comboMonut=data.withCertRemain||0; //有证书剩余次数
			comboMsg.comboMonutTotle=data.withCertRemainTotal||1;	//有证书剩余总次数
			comboMsg.comboDays=data.comboWithCertInfo.comboDateReamin||0;		//剩余天数
			comboMsg.comboDaysTotle=data.comboWithCertInfo.comboDateTotal||1;		//剩余总天数
			comboMsg.comboName=data.comboWithCertInfo.comboName||"无";
			comboInfo(comboMsg);
		});
	}else{
		comboRest(isHasCert,function(data){
			comboMsg.comboMonut=data.noCertRemain||0; //无证书剩余次数
			comboMsg.comboMonutTotle=data.noCertRemainTotal||1;	//无证书剩余总次数
			comboMsg.comboDays=data.comboNoCertInfo.comboDateReamin||0;		//剩余天数
			comboMsg.comboDaysTotle=data.comboNoCertInfo.comboDateTotal||1;		//剩余总天数
			comboMsg.comboName=data.comboNoCertInfo.comboName||"无";
			comboInfo(comboMsg);
		});
	}
	comboInfo(comboMsg);	//初始化套餐或无套餐情况
}

//有无证书套餐切换
function isHasCert(){
	//切换套餐
	$("#js_hasCertCombo").unbind('click').click(function(){
		//切换状态
		if (!$("#js_hasCertCombo").hasClass("comboTab-active")) {
			$("#js_hasCertCombo").addClass("comboTab-active")
		}
		if ($("#js_hasNoCertCombo").hasClass("comboTab-active")) {
			$("#js_hasNoCertCombo").removeClass("comboTab-active");
		}
		//更改选择套餐链接
		//$(".chooseCombo").attr("href","/wesign/"+gfALLDATA("uRole")+"/payments/combo.html?withCert=true");
		//处理套餐信息
		comboInfoDeal(true);
	});
	$("#js_hasNoCertCombo").unbind('click').click(function(){
		//切换状态
		if (!$("#js_hasNoCertCombo").hasClass("comboTab-active")) {
			$("#js_hasNoCertCombo").addClass("comboTab-active")
		}
		if ($("#js_hasCertCombo").hasClass("comboTab-active")) {
			$("#js_hasCertCombo").removeClass("comboTab-active");
		}
		//更改选择套餐链接
		//$(".chooseCombo").attr("href","/wesign/"+gfALLDATA("uRole")+"/payments/combo.html?withCert=false");
		//处理套餐信息
		comboInfoDeal(false);
	});
}

//进度圈动态显示	type=1：签名数量	type=2:剩余时间
function progressUpdate(percent,thisCricle,type) {
	if (percent<=0.5) {
		var degree=percent*360;
		thisCricle.addClass("combo-bottom-bgc");
		if (type==1) {
			thisCricle.removeClass("combo-mount-bgc");
		}else{
			thisCricle.removeClass("combo-days-bgc");
		}
	}else if(percent>0.5){
		var degree=percent*360-180;
		if (type==1) {
			thisCricle.addClass("combo-mount-bgc");
		}else{
			thisCricle.addClass("combo-days-bgc");
		}
	}
	/*thisCricle[0].style.transform="rotate(-"+degree+"deg)";
	thisCricle[0].style.webkitTransform="rotate(-"+degree+"deg)";	//兼容Safari
	thisCricle[0].style.msTransform="rotate(-"+degree+"deg)";	//兼容ie*/
}

//转换数值
function transeTwo(num){
	return parseFloat(num.toFixed(2));
}

//提示信息
function confirmInfo( html ){
 	$(".tipMsg").dialog({ autoOpen: true,width: 400,modal: true});
    $('.tipMsgContent').html(html);
    $("#js_btn_group").html(
    	'<span class="confirmNo btn-border-default-m">我知道了</span>'
    );
    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });
}

/*以下为老界面函数
//获取用户基本信息
function getAccountInfo(callback){
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

//展示
function showCenterInfo(data, userType){
	var isAuth = data.user.isRealNameAuth;
	var authState = data.user.authState;
	var loginName = data.user.loginName;
	if(isAuth){
		showAuthInfo(loginName);
		
	}else{	//state: 0-未认证; 1-认证中; 2-已认证; 3-认证未通过
		if(authState == 0){
			showNoauthInfo(userType, loginName);
			
		}else if(authState == 1){
			showNoauthInfo(userType, loginName);
			
		}else if(authState == 2){
			showAuthInfo(loginName);
			
		}else if(authState == 3){
			showNoauthInfo(userType, loginName);
			
		}
	}	//end else
}

//未开通证书下的界面
function showNoauthInfo(userType, name){
	var userName = name;
	var authUrl = ["user/sys/users/setting/auth/person", "user/sys/users/setting/auth/enterprise"];		//userType: 0-个人， 1-企业
	var authStateArray = ["未认证","认证中","已认证","认证未通过"];	//state: 0-未认证; 1-认证中; 2-已认证; 3-认证未通过
	$("#js_mycenter_info").html(
			'<p class="lighting fs20">'+userName+'</p>'
			+'<p>当前使用服务：<span>无证书签名</span><a class="btn-solid-default mlr10" href="user/sys/document">免费试用</a></p>'
			+'<p>使用有证书签名，保障高安全性。<a class="mr35 btn-solid-primary" href="'+authUrl[userType]+'">升级服务</a></p>'
	);
	
	$("#js_mycenter_update").html(
			'<li><a class="updateGrade" href="'+authUrl[userType]+'"><i class="icon-account-box"></i><span>实名认证</span></a></li>'
			+'<li><a class="updateGrade" href="/user/sys/account/combo"><i class="icon-shopping"></i><span>购买套餐</span></a></li>'
			+'<li><a class="updateGrade" href="user/sys/document"><i class="icon-write-down"></i><span>签名</span></a></li>'
	);
}

//已开通证书下的界面
function showAuthInfo(name){
	var userName = name;
	var authUrl = ["user/sys/users/setting/auth/person", "user/sys/users/setting/auth/enterprise"];		//userType: 0-个人， 1-企业
	
	$("#js_mycenter_info").html(
			'<p class="lighting fs20">'+userName+'</p>'
			+'<p>当前使用服务：有证书签名</p>'
			+'<p><span class="js_combInfo">未购买套餐或套餐已过期</span><a class="btn-solid-primary mlr10" href="/user/sys/account/combo">购买套餐</a></p>'
	);
	
	getUserPayInfo( $(".js_combInfo"),function( result, $elem ){ 
		var signForever = result.balanceSignNumForever;
		var signNumer = result.balanceSignNumber;
		if( signForever > 0 || signNumer > 0 ){
			var allNumber = signForever + signNumer;
			$elem.text("您还有剩余签名 " + allNumber + " 个");
			
		}else{
			return false;
		}
	});
	
	$("#js_mycenter_update").html(
			'<li><a class="updateGrade active" href="user/sys/users/setting"><i class="icon-account-box"></i><span>实名认证</span></a></li>'
			+'<li><a class="updateGrade" href="/user/sys/account/combo"><i class="icon-shopping"></i><span>购买套餐</span></a></li>'
			+'<li><a class="updateGrade" href="user/sys/document"><i class="icon-write-down"></i><span>签名</span></a></li>'
	);
}

//获取用户支付信息
function getUserPayInfo( $elem, callback ){
	 requestServer({
	        requestURL: '/user/sys/account/info/query/userInfo',
	        requestType: 'GET',
	        beforeCallback: function(){
	           showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            var desc = data.resultDesc;
	          
	            if(code == 0){
	            	callback(data.data, $elem);
	            }else{
	            	return;
	            } 
         	hideLoading();
	        }
	    });
}
*/



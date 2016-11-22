$(document).ready(function(){

});

//获取用户信息
function getUserInfo(jsonData, callback ){
    requestServer({
	 	requestURL: gfALLDATA("userInfoHref"),
        requestType: 'get',
        requestData: jsonData,
        //async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	hideLoading();
            	callback( data.resultData );
            }else{
            	//showInfo(desc);
            	hideLoading();
            	
            } 
            
        }
    });
}

//更新用户基本资料
function updateUserInfo( sendData, callback ){
	 var jsonData = sendData;
	 requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'put',
        beforeCallback: function(){
           showLoading();
        },
        requestData: JSON.stringify( jsonData ),
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback(true, desc);
            }else{
            	callback(false, desc);
            } 
            hideLoading();
	    }
	});
}

//校验
function ResValidator(){
	var _this = this;
	
	appendHtml = function(elem, flag, text){
		if(elem.next().hasClass("errorClass")){
			if(flag){
				elem.next().html('<span class="errorClass"><i class="icon-checkmark" style="color:#33cc99;"></i></span>');
			}else{
				elem.next().html( '<span class="errorClass">'+ text +'</span>');
			}
			
		}else{
			elem.after('<span class="errorClass"></span>');
			if(flag){
				elem.next().html('<span class="errorClass"><i class="icon-checkmark" style="color:#33cc99;"></i></span>');
			}else{
				elem.next().html( '<span class="errorClass">'+ text +'</span>');
			}
		}
		
	}
	
	_this.validName =function($obj){ 	//用户名
		var reg = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{3,20}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == ""){
			pass = true;
		}else if(reg.test(value)){
			pass = true;
		}else{
			tip = "（1）长度3~20位；（2）只能包含汉字、字母、数字或下划线；（3）下划线不能开头或结尾";
			pass = false;
		}
		
		//appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.validPhone = function($obj){	//手机号码
		var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == "" ){
			tip = "请输入手机号码";
			pass = false;
			
		}else if( reg.test(value) ){
			pass = true;
			
		}else{
			tip = "请输入合法的11位手机号码";
			pass = false;
		}
		
		//appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.code = function( $obj , $tip){	//校验码
		
		if( $obj.val() == '' ){
			$tip.html('请输入验证码') ;
			return false;
		}else{
			$tip.html('');
			return true;
		}
	}
	
	_this.validPassword = function($obj){	//密码
		var minMaxLength = /^[\S]{6,50}$/,
        		upper = /^[A-Z]+$/,
        		lower = /^[a-z]+$/,
        		lowerUpper = /^[a-zA-Z]+$/,
        		number = /^[0-9]+$/,
        		special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;
		
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == ""){
			tip = "请输入密码";
			pass = false;
			
		}else if(!minMaxLength.test(value)){
			if(value.length > 50){
				tip = "密码长度不能超过50位" ;
			}else{
				tip = "密码长度至少6位不含空格的任意字符的组合" ;
			}
			pass = false;
			
		}else if(upper.test(value)){
			tip = "密码不能全为大写字母" ;
			pass = false;
			
		}else if(lower.test(value)){
			tip = "密码不能全为小写字母" ;
			pass = false;
			
		}else if(lowerUpper.test(value)){
			tip = "密码不能全为字母" ;
			pass = false;
			
		}else if(number.test(value)){
			tip = "密码不能全为数字" ;
			pass = false;
			
		}else if(special.test(value)){
			tip = "密码不能全为字符" ;
			pass = false;
		}else{
			pass = true;
		}
		
		appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.confirmPassword = function($prev, $obj){	//确认密码
		 var pass = false;
		 var tip = "";
		
		 if( $prev.val() == $obj.val() ){
			 pass = true;
		 }else{
			 tip = "密码确认错误" ;
			 pass =  false;
		 }
		 
		 appendHtml($obj, pass, tip);
	     return pass;
	}
	
	_this.realName = function($obj){	//真实姓名
		var str1 = /^[\u4e00-\u9fa5]{1,10}$/;  
        var str2 = /^[A-Za-z]+$/;
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
        	pass = true;
        	
        }else if( str2.test(value) ){
        	pass = true;
        	
        }else if(!value){
        	 tip = "请输入姓名" ;
        	 pass = false;
        }else{
        	 tip = "姓名格式书写错误" ;
        	 pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
	}
	
}

//密码校验
function verifyPwd(jsonData, callback){
	jsonData.param = getEncryptPassword(jsonData.param);
	$.ajax({
		url : gfALLDATA("userInfoHref")+'/check',
		type : 'get',
		async: 'false',
		dataType : 'json',
		data:jsonData,
		success : function(data) {
			var code = data.resultCode;
			var desc = data.resultDesc;
			if(code == 0){
				callback(true, desc);
			}else{
				callback(false, desc);
			}
		},
		error : function() {
			confirm("对不起，连接失败，请稍候再试！");
		}
	});
}

/* 加密密码 */
function getEncryptPassword(realPasswordInputVal) {
	return SparkMD5.hash(realPasswordInputVal);
}

//验证验证码
function verifyCode( verifyType, token, $tip,jsonData,callback){
	$.ajax({
		url : gfALLDATA("publicHref")+'/'+verifyType+'/verify',
		type : 'post',
		async: 'false',
		dataType : 'json',
		contentType: 'application/json',
		data: JSON.stringify( jsonData ),
		success : function(data) {
			var code = data.resultCode;
			var desc = data.resultDesc;
			
			if(code == 0){
				//$tip.html( desc ).removeClass().addClass("succssClass");
				callback(true);
			}else{
				//$tip.html( desc ).removeClass().addClass("errorClass");
				callback(false);
			}
		},
		error : function() {
			confirm("对不起，网络不给力或操作太频繁，连接失败！");
		}
	});
}

//获取验证码
function getActivate(verifyType, $getInput, $showError,jsonData){
	$.ajax({
		url : gfALLDATA("publicHref")+'/'+verifyType+'/send',
		type : 'POST',
		dataType : 'json',
		async: 'false',
		contentType: 'application/json',
		data: JSON.stringify( jsonData ),
		beforeSend:function(){
			$(".js_telcode_btn").addClass("disabled");
			$(".js_telcode_btn").html("正在获取验证码");
		},
		success : function(data) {
			var code = data.resultCode;
			var desc = data.resultDesc;
			var token = data.token;
			
			if(code == 0){
				$(".js-curtext-state").text('验证码已发送');
				$getInput.data('data', token).prop("disabled",false);
				$showError.text("").data("limit", false);
				loadTime(60);
				return true;
			}else if(code == 1) {
				$showError.data("limit", true);
				$(".js_telcode_btn").html("获取验证码");
				$(".js_telcode_text").text('验证码获取次数超过限制，请更换手机再试或明天再试');
			}else{
				$showError.text(desc);
			}
			$getInput.data('data', token).prop("disabled",true);
		},
		error : function() {
			alert("对不起，网络不给力，连接失败！");
		}
	});
}
//验证码有效期倒计时
function loadTime(secs){
	for(var i=secs;i>=0;i--) {
	  window.setTimeout('doUpdateTime(' + i + ')', (secs-i) * 1000); 
	} 
}

function doUpdateTime(num) { 
	$(".js_telcode_btn").html("重新获取（"+num+"）");
	
	if(num == 0) { 
		$(".js_telcode_btn").html("重新获取");
		$(".js_telcode_btn").removeClass("disabled");
		$(".js-curtext-state").data("limit", true);
	}
}

//选择菜单
/*nameLeft:左边按钮文字，nameRight:右边按钮文字，leftUrl:左边按钮链接，
rightUrl:右边按钮额链接,msg：提示文字*/
function chooseMune( nameLeft,nameRight,leftUrl,rightUrl,msg ){
    $(".tipMsg").dialog({ autoOpen: true,width: 400,modal: true});
    $('#js_btn_group').html(
        '<span class="confirmNo chooseLeft mr25">'+nameLeft+'</span>'
        +'<span class="confirmYes chooseRight">'+nameRight+'</span>'
    );
    $(".tipMsgContent").html(msg);
    $('.confirmNo').unbind('click').click(function(){ 
        window.location.href=leftUrl;
        $(".ui-dialog-titlebar-close").click();
    });
    $('.confirmYes').unbind('click').click(function(){ 
        window.location.href=rightUrl;
        $(".ui-dialog-titlebar-close").click();
    });
}
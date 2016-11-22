$(document).ready(function(){
	//动态加载，解决火狐自带记住密码功能带来的bug
	/*$(".js_active_elem").html('<input id="js_verifytel_pwd" class="verify-input" type="password"/>');*/
	getUserInfo({"fields": "null"}, function(data){
		var phone = data.phone;
		if( phone !=' ' && phone != null && phone != 'null'){
			$("#js_resetTel_block1").show();
			$("#js_current_tel").text( phone );
			
		}else{ 
			$("#js_resetTel_block2").show();
			resetPhone();
		}
	});
	
	//验证密码
	$("#js_submit_verifyTel").unbind('click').click(function(){
		var pwd = $("#js_verifytel_pwd").val();

		var jsonData={
			"type":2,
			"param":pwd
		}
		if( pwd ){
			verifyPwd(jsonData, function( flag, text ){
				if( flag ){
					$("#js_resetTel_block1").hide();
					$("#js_resetTel_block2").show();
					resetPhone();
				}else{
					$(".js_submit_resultTel").text("密码错误");
				}
			});
		}else{
			$(".js_submit_resultTel").text("请输入密码");
			return false;
		}
	});	
});

//重置手机
function resetPhone(){
	var telValidator = new ResValidator();
	var telFlag = {
		number: false,
		code: false,
      	codeResult: false,
      	numberResult:false,
      	oriPhone:""
	};
	
	//验证手机号
	$("#js_reset_curTel").focusout(function(){
		clearError($("#js_cur_result"));
		telFlag.number = telValidator.validPhone( $(this) );
		if( telFlag.number ){
			var phone = $("#js_reset_curTel").val();
			//如果手机号发生改变，验证码失效
			if(telFlag.oriPhone != "" && phone != telFlag.oriPhone){
				telFlag.oriPhone = "";
				telFlag.codeResult = false;
				//清空验证码
				$("#reset_telcode").val("");
				$("#reset-code-text").text("");
			}
			//检查手机号是否被占用
			requestValidTel( phone, function(result){
				if(result){
					telFlag.numberResult=false;
				}
				else{
					telFlag.numberResult=true;
				}
				appendHtml($("#js_reset_curTel"), telFlag.numberResult , "该手机号已被占用");
			});
		}else{
			appendHtml($("#js_reset_curTel"), false, "请输入正确的手机号码");
			return false;
		}	
	});
	
	//提交
	$("#js_save_curTel").unbind("click").click(function(){
		setTimeout(function(){
			var phone = $("#js_reset_curTel").val();
			var code = $("#reset_telcode").val();
			var jsonData = {
				"authMsg":"null",
				"newData":phone,
				"oldData":"null",
				"type":"3"
		 	};
			if( telFlag.code && telFlag.number&&telFlag.codeResult&&telFlag.numberResult){
				updateUserInfo( jsonData, function(code,desc){
					if(code){
						//禁用输入框
						$("#js_reset_curTel").attr('disabled',true);
						$("#reset_telcode").attr('disabled',true);
						$("#reset_getTelCode").unbind();
						$("#js_save_curTel").unbind();
						$("#js_cur_result").text(desc).removeClass().addClass("succssClass");		
					}else{
						$("#js_cur_result").text(desc).removeClass().addClass("errorClass");
					}
				});
			}else if( phone==""||code==""){
				$("#js_cur_result").text("请将信息填写完整").removeClass().addClass("errorClass");;
				return false;
			}else{
				return false;
			}
		},500);
	});
	
	//验证输入码
	$("#reset_telcode").focusout(function(){
		clearError($("#js_cur_result"));
		telFlag.code = telValidator.code( $(this), $("#reset-code-text") );
		var activateCode = $("#reset_telcode").val();
		var token = $("#reset_telcode").data("data");
		var $tip = $("#js_cur_result");
		var phone = $("#js_reset_curTel").val();
		var jsonData={
	      	"data":activateCode,
	      	"type":"code",
	      	"sendDest":phone
    	}
    	if( telFlag.code ){
      		if(telFlag.codeResult){
        		return;
	      	}else{
	        	verifyCode( "phone", token, $("#js_cur_result"),jsonData,function(dataResult){
         			telFlag.codeResult = dataResult;
	         		if (!dataResult) {
						$("#reset-code-text").text("验证码错误");
	         		}else{
	         			//验证正确，记录下手机号
	         			telFlag.oriPhone = phone;
	         		}
        		});
	      	}
      
    	}else{
     		return false;
    	}
	});
	
	//获取验证码
	$("#reset_getTelCode").unbind('click').click(function(){
		setTimeout(function(){
			if($("#reset_getTelCode").hasClass("disabled")){
				return false;
			}else{
				if( telFlag.number&&telFlag.numberResult){
					//清除验证码输入框中的信息与错误提示信息
					$("#reset_telcode").val("");
					$("#reset-code-text").text("");
					telFlag.codeResult=false;
					
					var phone = $("#js_reset_curTel").val();
					var jsonData={
	          		"sendDest":phone,
	          		"type":"1000"
	        		}
					getActivate( "phone", $("#reset_telcode"), $(".js-curtext-state"),jsonData );
				}else{
					return false;
				}
			}
		},500);
	});
}

//请求检查手机号是否已占用
function requestValidTel( phone , callback){
	var jsonData={
		"type":1,
		"param":"null"
	}
    requestServer({
        requestURL: gfALLDATA("baseHref")+'/wesign/'+gfALLDATA("uRole")+
        '/users/'+phone+'/check',
        requestType: 'get',
        requestData:jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            if(code){
            	callback( false );
            }else{
            	callback( true );
            }
            hideLoading();
        }
    });
}

//清除错误信息
function clearError(_this){
	$(_this).text("");
}
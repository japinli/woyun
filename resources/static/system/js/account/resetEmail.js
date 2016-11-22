$(document).ready(function(){
	//动态加载，解决火狐自带记住密码功能带来的bug
	$(".js_active_elem").html('<input id="js_verifyemail_pwd" class="verify-input" type="password"/>');
	getUserInfo({"fields": "null"}, function(data){
		var code = data.email ;
		if (code) {
			var email=data.email;
			$("#js_reset_email").attr("value",email);
			$("#js_reset_email").attr('disabled',true);
			$("#reset_emailCode").attr('disabled',true);
			//禁用点击事件
			$("#reset_getEmailCode").unbind();
			$("#js_save_resetEmail").unbind();
		}else{
			resetEmail();
		}
	});	
});

//修改邮箱
function resetEmail(){
	var emailValidator = new ResValidator();
	var emailFlag = {
		email: false,
		code: false,
      	codeResult: false,
      	emailResult:false,
      	oriEmail:""
	};
	$("#js_save_resetEmail").unbind('click').click(function(){
		setTimeout(function(){
			var emailValue = $("#js_reset_email").val();
			var codeValue = $("#reset_emailCode").val();
			var jsonData = {
				"authMsg":"null",
				"newData":emailValue,
				"oldData":"null",
				"type":"4"
		 	};
			if(emailFlag.email&&emailFlag.codeResult&&emailFlag.emailResult){
				updateUserInfo( jsonData, function( result, text ){
					if(result){
						//禁用输入框
						$("#js_reset_email").attr('disabled',true);
						$("#reset_emailCode").attr('disabled',true);
						$("#js_save_resetEmail").attr('disabled',true);
						//禁用点击事件
						$("#reset_getEmailCode").unbind();
						$("#js_save_resetEmail").unbind();
						$("#email_final").text(text).removeClass().addClass("succssClass");
					}else{
						$("#email_final").text(text).removeClass().addClass("errorClass");
					}
				});
			}else if(emailValue==""||codeValue==""){
				$("#email_final").text("请将信息填写完整！").removeClass().addClass("errorClass");
				return false;
			}else{
				return false;
			}
		},500);
	});

	//验证邮箱
	$("#js_reset_email").focusout(function(){
		clearError($("#email_final"));
		emailFlag.email = reValidEmail( $(this).val() );
		if( emailFlag.email ){
			var emailValue = $("#js_reset_email").val();
			//如果邮箱发生改变，验证码失效
			if(emailFlag.oriEmail != "" && emailValue != emailFlag.oriEmail){
				emailFlag.oriEmail = "";
				emailFlag.codeResult = false;
				$("#reset_emailCode").val("");
				$("#reset-code-text").text("");
			}
			//检查邮箱是否被占用
			requestValidEmail( emailValue, function(result){
				emailFlag.emailResult = !result;
				appendHtml($("#js_reset_email"), emailFlag.emailResult , "该邮箱已被占用");
			});			
		}else{
			$("#validEmail_result").text("请输入正确的邮箱");
			return false;
		}
	});

	//验证输入码
	$("#reset_emailCode").focusout(function(){
		clearError($("#email_final"));
		emailFlag.code = emailValidator.code( $(this), $("#reset-code-text") );
		var activateCode = $("#reset_emailCode").val();
		var token = $("#reset_emailCode").data("data");
		var $tip = $("#email_final");
		var email = $("#js_reset_email").val();
		var jsonData={
	      	"data":activateCode,
	      	"type":"code",
	      	"sendDest":email
    	}
    	if( emailFlag.code ){
      		if(emailFlag.codeResult){
        		return;
	      	}else{
	        	verifyCode( "email", token, $("#email_final"),jsonData,function(dataResult){
         			emailFlag.codeResult = dataResult;
	        		if (!dataResult) {
						$("#reset-code-text").text("验证码错误");
	         		}else{
	         			//验证正确，记录下邮箱
	         			emailFlag.oriEmail = email;
	         		}
        		});
	      	}
      
    	}else{
     		return false;
    	}
	});
	
	//获取验证码
	$("#reset_getEmailCode").unbind('click').click(function(){
		setTimeout(function(){
			if($("#reset_getEmailCode").hasClass("disabled")){
				return false;	
			}else{
				if( emailFlag.email && emailFlag.emailResult){
					//清除验证码输入框中的信息与错误提示信息
					$("#reset_emailCode").val("");
					$("#reset-code-text").text("");
					emailFlag.codeResult=false;
					
					var email = $("#js_reset_email").val();
					var jsonData={
	          		"sendDest":email,
	          		"type":"2000"
	        		}
					getActivate( "email", $("#reset_emailCode"), $(".js-curtext-state"),jsonData );
					
				}else{
					return false;
				}
			}
		},500);
	});
}

//请求检查邮箱是否已占用
function requestValidEmail( email , callback){
	var jsonData={
		"type":1,
	}
	requestServer({
        requestURL: gfALLDATA("baseHref")+'/wesign/'+gfALLDATA("uRole")+
        '/users/'+email+'/check',
        requestType: 'get',
        requestData:jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var resultCode = data.resultCode;
            if(resultCode){
            	callback( false );
            }else{
            	callback( true );
            }
            hideLoading();
        }
    });
}

//邮箱校验
function reValidEmail(value){
	var re=/^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	return value==' '? false : re.test(value) ;
}

//清除错误信息
function clearError(_this){
	$(_this).text("");
}
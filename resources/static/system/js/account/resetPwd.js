$(document).ready(function(){
	//解决火狐自带记住密码功能带来的bug,自动填入密码
	$('input[type="password"]').val('');
	var pwdflag = {
			pwd: false,
			newPwd: false,
			conPwd: false
	};
	var resValidator = new ResValidator();
	
	$("#js_reset_oldPwd").focusout(function(){
		pwdflag.pwd = resValidator.validPassword( $(this) );
		removeYes($(this));
	});
	
	$("#js_reset_newPwd").focusout(function(){
		pwdflag.newPwd = resValidator.validPassword( $(this) );
		if ($("#js_reset_conPwd").val()!="") {
			pwdflag.conPwd = resValidator.confirmPassword( $(this),$("#js_reset_conPwd"));
			removeYes($("#js_reset_conPwd"));
		}
		removeYes($(this));
	});
	
	$("#js_reset_conPwd").focusout(function(){
		pwdflag.conPwd = resValidator.confirmPassword( $("#js_reset_newPwd"), $(this) );
		removeYes($(this));
	});
	
	$("#js_save_resetPwd").unbind("click").click(function(){
		if (pwdflag.conPwd!=pwdflag.newPwd) {
			showInfo("请重新确认新密码！");
		}else if( pwdflag.pwd && pwdflag.newPwd && pwdflag.conPwd ){
			setTimeout(function(){
				var jsonData = {
					"authMsg":"null",
					"newData":getEncryptPassword($("#js_reset_conPwd").val().trim()),
					"oldData":getEncryptPassword($("#js_reset_oldPwd").val().trim()),
					"type":"1"
			 	};
			 	updateUserInfo( jsonData, function(code,desc){
			 		if(code){
/*			 			var curRest="密码";
			 			var accountSet=gfALLDATA("alipayHref")+"/my-info.html";
			 			var signature=gfALLDATA("alipayHref")+"/index.html";
			 			chooseMune("账号设置","前往签名",accountSet,signature,curRest+"修改成功!"
				 					+"<br><br>请选择返回账号设置或前往签名。");*/
						$("#js_reset_oldPwd").attr('disabled',true);
						$("#js_reset_newPwd").attr('disabled',true);
						$("#js_reset_conPwd").attr('disabled',true);
						$("#js_save_resetPwd").unbind();
						$("#js_savePwd_error").addClass("successClass").text("密码修改成功！");
						//showInfo(desc);
					}else{						
						$("#js_savePwd_error").addClass("errorClass").text("密码修改失败！");				
						//showInfo(desc);
					}
			 	});
		 	},700);
			//restUserPwd($("#js_reset_oldPwd").val().trim(), $("#js_reset_conPwd").val().trim());
		}else{
			return false;
		}
		   
	});
});

//去除勾勾
function removeYes(_this){
	if (_this.siblings(".errorClass").find("i").length>0) {
		_this.siblings(".errorClass").remove();
	}
}

//修改密码
/*function restUserPwd(oldPassword, newPassword){
	var newPwd = getEncryptPassword(newPassword);
	 requestServer({
	        requestURL: '/user/sys/users/setting/base/pwd/'+ getEncryptPassword(oldPassword)  +'/'+ newPwd +'/'+newPassword.length+'/reset',
	        requestType: 'post',
	        beforeCallback: function(){
	           showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            var desc = data.resultDesc;
	            var updatePwd = data.resultData;
	            if(code == 0){
	            	showInfo(desc);
	            	
	            }else{
	            	showInfo(desc);
	            } 
            	hideLoading();
	        }
	    });
}*/

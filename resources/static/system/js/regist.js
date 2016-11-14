$(document).ready(function() {
	var registBtnObj = $("#regSubmitBtn");
	$("#emailOrPhone").val("").focusout(function(){
		if (!isNaN($(this).val())&&$(this).val().length==11) {	//手机
			var tipSelector = $(this).siblings('span').eq(0);
			telValidator(this, tipSelector);
			if (tipSelector.text() == 'ok') {
				//ajaxRegEmailCheckServer(this, 1);
			} 
		}else if( $(this).val().indexOf("@") > -1 ){ //邮箱
			var tipSelector = $(this).siblings('span').eq(0);
			emailValidator(this, tipSelector);
			if (tipSelector.text() == 'ok') {
				//ajaxRegEmailCheckServer(this, 0);
			}
		}else{   //其他
			var tipSelector = $(this).siblings('span').eq(0);
			tipSelector.html('请输入正确的手机号或邮箱账号');
			styleControl(tipSelector);
		}
		$("#js_register_error").text("");		//清除注册提示
	})
	
	$("#invented_realName").val("").focusout(function(){		//昵称
		var tipSelector = $(this).siblings('span').eq(0);
		realNameValidator(this, tipSelector);
		$("#js_register_error").text("");		//清除注册提示
	});

	$("#userPassword").val("").focusout(function(){		//密码
		var tipSelector = $(this).siblings('span').eq(0);
		passwordValidator(this, tipSelector);
		if ($("#passwordAgain").val()!="") {
			var rePswTipSelector=$("#passwordAgain").siblings('span').eq(0);
			passwordAgainValidator($("#passwordAgain"), $("#userPassword"), rePswTipSelector);
		}
		$("#js_register_error").text("");		//清除注册提示
	}); 
	
	$("#passwordAgain").val("").focusout(function(){		//再次输入密码
		var tipSelector = $(this).siblings('span').eq(0);
		passwordAgainValidator(this, $("#userPassword"), tipSelector);
		$("#js_register_error").text("");		//清除注册提示
	});
	
	$("#regCaptcha").val("").focusout(function(){		//验证码
		var tipSelector = $(this).siblings('span').eq(1);
		captchaValidator(this, tipSelector);
		$("#js_register_error").text("");		//清除注册提示
	});
	
	registBtnObj.click(function(){
		finalValidate();
	});
	
	//清除错误提示
	$(".containerBox").click(function(event){
		$(".tipWarning").text("");
	});
	
	// 回车事件
	$("body").keydown(function(event) {
		if (event.keyCode == "13") {
			registBtnObj.click();
		}
	});
	
	//换验证码
	$(".js-change-verimg").click(function(){
		$("#js-show-verimg").attr( "src", URL_base + '/wesign/common/captcha/image?' + Math.random() ); 
	});
	
})

/*最终校验*/
function finalValidate(){
	var $emailMsg = $("#emailOrPhone").siblings('span').eq(0).text();
	var $telMsg = $("#emailOrPhone").siblings('span').eq(0).text();
	var $accountMsg='ok';
	var $passwordMsg = $("#userPassword").siblings('span').eq(0).text();
	var $passwordAgainMsg =$("#passwordAgain").siblings('span').eq(0).text();
	var $realNameMsg=$("#invented_realName").siblings('span').eq(0).text();
	var $verifyCode=$("#regCaptcha").siblings('span').eq(1).text();
	if($("#emailOrPhone").val().indexOf("@") > -1){	//邮箱
		
		if( $emailMsg == 'ok' && $accountMsg == 'ok' && $passwordMsg=='ok' && $passwordAgainMsg=='ok'&&$realNameMsg=='ok' && $verifyCode=='ok'){
			// 注册按钮状态变化
			registBtnSubmitStyle($("#regSubmitBtn"));
			$("#realName").attr("value",unicode($("#invented_realName").val()));
			$("#registerForm").submit();
			//doPost();	// 提交注册请求
			return true;
			
		}else{
			//检查信息是否填写完整
			checkRegistInfo();
			return false;
		}
		
	}else{//手机
		
		if( $accountMsg == 'ok' && $passwordMsg=='ok' && $passwordAgainMsg=='ok'&&$realNameMsg=='ok'){
			// 注册按钮状态变化
			registBtnSubmitStyle($("#regSubmitBtn"));
			$("#realName").attr("value",unicode($("#invented_realName").val()));
			$("#registerForm").submit();
			//doPost();	// 提交注册请求
			return true;
			
		}else{
			//检查信息是否填写完整
			checkRegistInfo();
			return false;
		}
	}	//end else
}

//检查注册信息是否已填完整
function checkRegistInfo(){
	var account=$("#emailOrPhone").val().length;
	var name=$("#invented_realName").val().length;
	var password=$("#userPassword").val().length;
	var rePassword=$("#passwordAgain").val().length;
	var verifyCode=$("#regCaptcha").val().length;
	//判断是否含有中文的正则表达式
	var warningInfo=$(".errorWarning").text();
	var reg=/^[ok]{0,}$/;
	var isHasError=reg.test(warningInfo);
	//不含有错误提示信息，且含有未填选项
	if (isHasError && (!account || !name || !password || !rePassword || !verifyCode)) {
		$("#js_register_error").text("您还未完成注册信息的填写！");
	}
}

function registBtnSubmitStyle(btnObj){
	$(btnObj).val("注册中...");
	$(btnObj).prop("disabled", true);
	$(btnObj).css("opacity", 0.6);
}

/* 密码校验器 */
function passwordValidator(passwordSelector, tipSelector) {
	var minMaxLength = /^[\S]{6,50}$/, upper = /^[A-Z]+$/, lower = /^[a-z]+$/, lowerUpper = /^[a-zA-Z]+$/, number = /^[0-9]+$/, special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

	var value =  $(passwordSelector).val();
	var $classObj = tipSelector;

	if (value == "") {
		$classObj.text("至少6位由字母、数字、字符任两种的组合");
		styleControl(tipSelector);
		return false;
	} else if (!minMaxLength.test(value)) {
		if (value.length > 50) {
			$classObj.text("密码长度不能超过50位");
		} else {
			$classObj.text("至少6位不含空格的任意字符的组合");
		}
		styleControl(tipSelector);
		return false;
	} else if (upper.test(value)) {
		$classObj.text("密码不能全为大写字母");
		styleControl(tipSelector);
		return false;
	} else if (lower.test(value)) {
		$classObj.text("密码不能全为小写字母");
		styleControl(tipSelector);
		return false;
	} else if (lowerUpper.test(value)) {
		$classObj.text("密码不能全为字母");
		styleControl(tipSelector);
		return false;
	} else if (number.test(value)) {
		$classObj.text("密码不能全为数字");
		styleControl(tipSelector);
		return false;
	} else if(special.test(value)){
		$classObj.text("密码不能全为字符");
		styleControl(tipSelector);
		return false;
	} else {
		$classObj.html('<i class="ok"></i>ok');
		$classObj.css("visibility", "hidden");
		return true;
	}
}

/* 确认密码校验器 */
function passwordAgainValidator(passwordAgainSelector, prepassword, tipSelector) {
	var passwordAgainVal = $(passwordAgainSelector).val();
	if (passwordAgainVal == prepassword.val()) {
		tipSelector.html('<i class="ok"></i>ok');
		tipSelector.css("visibility", "hidden");
		return true;
	} else {
		tipSelector.html('<i class="err"></i>密码确认错误，请重新确认');
		styleControl(tipSelector);
		return false;
	}
}

/* 异步校验的格式控制 */
function styleControl(tipSelector) {
	tipSelector.css("visibility", "visible");
}

/* 加密密码 */
function encryptPassword(realPasswordInput) {
	$("#passwordAgain-hidden").val(SparkMD5.hash($(realPasswordInput).val()));
}

/* 提交 */
function doPost() {
	// 加密“密码”
	encryptPassword($("#passwordAgain"));
	
	// 提交请求
	$("#registerForm").submit();
	$("#passwordAgain-hidden").val("");
}

/* 邮箱校验器 */
function emailValidator(emailSelector, tipSelector) {
	/* 邮箱验证 */
	var re = /^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	var $email = $(emailSelector).val();
	if ($email == "") {
		tipSelector.html('请输入您的邮箱'); 
		styleControl(tipSelector);
		return false;
	} else {
		if (re.test($email)) {
			tipSelector.html('ok');
			tipSelector.css("visibility", "hidden");
			return true;
		} else {
			tipSelector.html("请输入正确的邮箱地址");
			styleControl(tipSelector);
			return false;
		}
	}
}

/*手机号码验证*/
function telValidator(obj, tipSelector){
	var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
	var value = $(obj).val();
	if(reg.test(value)){
		tipSelector.html('ok');
		tipSelector.css("visibility", "hidden");
		return true;
	}else{
		tipSelector.html('请输入合法的11位手机号码');
		styleControl(tipSelector);
		return false;
	}
}

/* 验证邮箱地址是否已经调用 */
function ajaxRegEmailCheckServer(obj, num) {
	var desc = ['该邮箱已经注册', '该手机号码已经注册'];
	var userEmailInput = $(obj);
	var userEmailAddressValidator = $(obj).val();
	var aMes = $(obj).siblings("span").eq(0);
	$.ajax({
		url : URL_base+'/user/' + userEmailAddressValidator + '/check',
		type : 'POST',
		dataType : 'json',
		data : {
			userEmailAddressValidator : userEmailAddressValidator
		},
		success : function(data) {
			if (data.isExisted || data.isExisted == 'true') {
				aMes.html(desc[num]);
				aMes.css("visibility", "visible");
				$("#passwordAgain").val("");
			} else {
				aMes.html("ok");
				aMes.css("visibility", "hidden");
			}
		},
		error : function() {
			aMes.html(desc[num]);
			aMes.css("visibility", "visible");
			$("#passwordAgain").val("");
		}
	});
}

//昵称校验
function realNameValidator(obj,tipSelector){
	var reg = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,10}$/;
	var value = $(obj).val();
	if(reg.test(value)){
		tipSelector.html('ok');
		tipSelector.css("visibility", "hidden");
	}else{
		tipSelector.html("请输入合法的姓名");
		styleControl(tipSelector);
		return false;
	}
}

//验证码输入校验
function captchaValidator(obj,tipSelector){
	var reg = /^[a-zA-Z0-9_\u4e00-\u9fa5]{4,10}$/;
	var value = $(obj).val();
	if(reg.test(value)){
		tipSelector.html('ok');
		tipSelector.css("visibility", "hidden");
	}else{
		tipSelector.html("请输入合法验证码");
		styleControl(tipSelector);
		return false;
	}
}

//Unicode编码转换
function unicode(text){
    return escape(text).replace(/%u/gi, '\\u');
}
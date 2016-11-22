$(document).ready(function(){
	var oCount = $("#loginCount");
	var oPwd = $("#loginPwd");
	var oCaptcha = $('#loginCaptcha');
	$("#loginCount").siblings('span').eq(0).text('remember').css("visibility", "hidden"); //如果浏览器保存了密码
	$("#js-login").click(function(event){
		var $obj = $(this);
		var $oCount=$("#loginCount").siblings('span').eq(0).text();
		$("#js_login_error").text("");		//清除登录提示
		//浏览器记住密码或输入信息非空且正确时提交登录信息
		if(($oCount=="remember"&&oCount.val() != "" && oPwd.val() != "" )||
			(oCount.val() != "" && oPwd.val() != "" && $oCount=="ok")){
			if(typeof oCaptcha != 'undefined' && oCaptcha.val() == ""){
				$("#js-show-verimg").trigger("click");
				return false;
			}
			$(".errorWarning").text("");
			$obj.val("正在登录...");
			$obj.prop("disabled", true);
			$obj.css("opacity", 0.6);
			//提交登录表单
			$("#login-form").submit();
		}//未输入全部信息
		else if(($(".errorWarning").text()=="remember"||$(".errorWarning").text()=="ok")
			&&(oCount.val() == "" || oPwd.val() == "")){
			$("#js_login_error").text("您还未完成登录信息的填写！");
			//confirmInfo("您还未完成登录信息的正确填写！");
			return false;
		}//其他未知错误情况
		else{
			return false;
		}
	});
	
	//检查账户信息
	$("#loginCount").focusout(function(){
		$("#js_login_error").text("");		//清除登录提示
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
			tipSelector.html('请输入正确的登录账号');
			styleControl(tipSelector);
		}
	});

	//检查密码是否为空
	$("#loginPwd").focusout(function(){
		$("#js_login_error").text("");		//清除登录提示
		if ($(this).val().length==0) {
			$("#js_pwd_error").text("请输入密码");
		}else{
			$("#js_pwd_error").text("");
		}
	});

	// 回车提交
	$("body").keydown(function(event) {
		if (event.keyCode == "13") {
			$("#js-login").click();
		}
	});
	
	//换验证码
	$(".js-change-verimg").click(function(){
		$("#js-show-verimg").attr( "src", URL_base + '/wesign/common/captcha/image?' + Math.random() ); 
	});
	
	//清除错误提示
	$(".containerBox").click(function(event){
		$(".tipWarning").text("");
	});
	
	//检查是否存在“记住我”
	if($.cookie('signit-remember-me') != null){
		$("#remember-me").attr("value","checked");
		
	}else{
		$("#remember-me").attr("value","");
	}
});

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

/* 异步校验的格式控制 */
function styleControl(tipSelector) {
	tipSelector.css("visibility", "visible");
}
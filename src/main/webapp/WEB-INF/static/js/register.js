$(function() {
	$("#signup").click(function() {
		signup();
	})
});

/*
 * 向服务器请求检测用户昵称是否存在
 * 服务器端返回格式 {"status": 0}
 * status 状态说明:
 * 0 : 当前用户名尚未注册
 */
function isUsernameRegisted(username, tipInfo) {
	$.ajax({
		url: "/signup/checkuser?username=" + username,
		type: "GET",
		success: function(data) {
			var _type = data.status;
			if (_type != 0) {
				tipInfo.html("该用户已经存在");
				tipInfo.css("visibility", "visible");
			} else {
				tipInfo.html("ok");
				tipInfo.css("visibility", "hidden");
			}
		},
		error: function() {
			alert("用户名检测异常");
		}
	});
}

function isEmailRegisted(email, tipInfo) {
	$.ajax({
		url: "/signup/checkemail?email=" + email,
		type: "GET",
		success: function(data) {
			var _type = data.status;
			if (_type != 0) {
				tipInfo.html("该邮箱已经注册");
				tipInfo.css("visibility", "visible");
			} else {
				tipInfo.html("ok");
				tipInfo.css("visibility", "hidden");
			}
		},
		error: function() {
			alert("用户名检测异常");
		}
	});
}

function isUsernameValid(username, tipInfo) {
	if (username == null || username == "") {
		tipInfo.html("用户昵称不能为空");
		tipInfo.css("visibility", "visible");
		return false;
	}
	
	isUsernameRegisted(username, tipInfo);
}

function isPasswordValid(password, tipInfo) {
	var passwordValid = false;
	
	if (password == null || password == "") {
		tipInfo.html("密码不能为空");
		tipInfo.css("visibility", "visible");
	} else if (password.length < 6) {
		tipInfo.html("密码长度不能小于6位");
		tipInfo.css("visibility", "visible");
	} else {
		tipInfo.html("ok");
		tipInfo.css("visibility", "hidden");
		passwordValid = true;
	}
	
	return passwordValid;
}

function isConfirmPasswordValid(password, confirm, tipInfo) {
	if (password == confirm) {
		tipInfo.html('ok');
		tipInfo.css("visibility", "hidden");
		return true;
	}
	
	tipInfo.html("两次密码输入不一致");
	tipInfo.css("visibility", "visible");
	return false;
}

function isEmailValid(email, tipInfo) {
	if (email == null || email == "") {
		tipInfo.html("邮箱不能为空");
		tipInfo.css("visibility", "visible");
		return false;
	}
	
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	if (!reg.test(email)) {
		tipInfo.html("无效的邮箱地址");
		tipInfo.css("visibility", "visible");
		return false;
	}
	
	isEmailRegisted(email, tipInfo);
}

$("#username").focusout(function() {
	var username = $("#username").val();
	var tipInfo = $(this).siblings("span").eq(0);
	isUsernameValid(username, tipInfo);
});

$("#password").focusout(function() {
	var password = $("#password").val();
	var tipInfo = $(this).siblings('span').eq(0);
	isPasswordValid(password, tipInfo);
});

$("#confirm").focusout(function() {
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	var tipInfo = $(this).siblings('span').eq(0);
	isConfirmPasswordValid(passwrod, confirm, tipInfo);
});

$("#email").focusout(function() {
	var email = $("#email").val();
	var tipInfo = $(this).siblings('span').eq(0);
	isEmailValid(email, tipInfo);
});

function signup() {
	var uTipInfo = $("#username").siblings('span').eq(0);
	var pTipInfo = $("#password").siblings('span').eq(0);
	var cTipInfo = $("#confirm").siblings('span').eq(0);
	var eTipInfo = $("#email").siblings('span').eq(0);
	
	var username = $("#username").val();
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	var email = $("#email").val();

	isUsernameValid(username, uTipInfo);
	isPasswordValid(password, pTipInfo);
	isConfirmPasswordValid(password, confirm, cTipInfo);
	isEmailValid(email, eTipInfo);
	
	if (uTipInfo.text() == "ok" && pTipInfo.text() == "ok" && cTipInfo.text() == "ok" && eTipInfo.text() == "ok") {
		$.ajax({
			url : "/signup",
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				"username" : username,
				"password" : password,
				"email" : email
			}),
			success : function(data) {
				console.log(data);
				var _type = data.status;
				if (_type == 0) {
					alert("注册成功");
					window.location.href = "/cloud/login.html";
				} else {
					alert("注册失败");
				}
			},
			error : function() {
				alert("注册异常");
			}
		});
	}
}
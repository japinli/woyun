$(function() {
	$("#signup").click(function() {
		signup();
	})
});

/*
 * 检测当前用户是否已经注册
 */
$("#username").focusout(function() {
	var username = $("#username").val();
	var errinfo = $(this).siblings("span").eq(0);
	if (username == null || username == "") {
		errinfo.html("用户昵称不能为空");
		errinfo.css("visibility", "visible");
		return;
	}
	/*
	 * 向服务器请求检测用户昵称是否存在
	 * 服务器端返回格式 {"status": 0}
	 * status 状态说明:
	 * 0 : 当前用户名尚未注册
	 */
	$.ajax({
		url: "/signup/checkuser?username=" + username,
		type: "GET",
		success: function(data) {
			var _type = data.status;
			if (_type != 0) {
				errinfo.html("该用户已经存在");
				errinfo.css("visibility", "visible");
			} else {
				errinfo.html("ok");
				errinfo.css("visibility", "hidden");
			}
		},
		error: function() {
			alert("用户名检测异常");
		}
	});
});

/*
 * 检测密码是否为有效密码（长度，字符组成等）
 */
$("#password").focusout(function() {
	var password = $("#password").val();
	var errid = $(this).siblings('span').eq(0);
	if (password == null || password == "") {
		errid.html("密码不能为空");
		errid.css("visibility", "visible");
	} else if (password.length < 6) {
		errid.html("密码长度不能小于6位");
		errid.css("visibility", "visible");
	} /* else if () {  // 密码校验，如不能是单一字符或数字
		
	} */ else {
		errid.html("ok");
		errid.css("visibility", "hidden");
	}
});

/*
 * 检测用户两次输入的密码是否匹相同
 */
$("#confirm").focusout(function() {
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	var errid = $(this).siblings('span').eq(0);
	if (password != confirm) {
		errid.html("两次密码输入不一致");
		errid.css("visibility", "visible");
	} else {
		errid.html('ok');
		errid.css("visibility", "hidden");
	}
});

/*
 * 检测邮箱的合法性及是否已经注册
 */
$("#email").focusout(function() {
	var email = $("#email").val();
	var errinfo = $(this).siblings('span').eq(0);
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	if (!reg.test(email)) {
		errinfo.html("无效的邮箱地址");
		errinfo.css("visibility", "visible");
	} else {
		errinfo.html("ok");
		errinfo.css("visibility", "hidden");
		
		/*
		 * 向服务器发送请求，验证该邮箱是否已经注册
		 */
		$.ajax({
			url: "/signup/checkemail?email=" + email,
			type: "GET",
			success: function(data) {
				var _type = data.status;
				console.log(data);
				if (_type != 0) {
					errinfo.html("该邮箱已经注册");
					errinfo.css("visibility", "visible");
				} else {
					errinfo.html("ok");
					errinfo.css("visibility", "hidden");
				}
			},
			error: function() {
				alert("用户名检测异常");
			}
		});
	}
});

function signup() {
	// 检测用户名是否已经注册
	var username = $("#username").val();
	
	// 检测两次密码是否相同
	var password = $("#password").val();
	var confirm = $("confirm").val();
	
	// 检测邮箱地址是否已经注册
	var email = $("#email").val();
	
	$.ajax({
		url: "/signup",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify({
			"username": username,
			"password": password,
			"email": email
		}),
		success: function(data) {
			console.log(data);
			var _type = data.status;
			if (_type == 0) {
				alert("注册成功");
				window.location.href = "/cloud/login.html";
			} else {
				alert("登录失败");
			}
		},
		error: function() {
			alert("注册异常");
		}
	});
}
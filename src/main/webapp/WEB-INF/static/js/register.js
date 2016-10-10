$(function() {
	$("#signup").click(function() {
		signup();
	})
});

function checkUser(username) {
	$.ajax({
		url: "/signup/checkuser?name=" + username,
		type: "GET",
		success: function() {
			var _type = data.status;
			if (_type == 0) {
				$("#error-msg").html("用户名已经存在");
			}
		},
		error: function() {
			alert("用户名检测异常");
		}
	});
}

function signup() {
	// 检测用户名是否已经注册
	var username = $("#username").val();
	if (username != null && username != "") {
		checkUser(username);
	}
	
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
				window.location.href = "/cloud/home.html";
			} else {
				alert("登录失败");
			}
		},
		error: function() {
			alert("注册异常");
		}
	});
}
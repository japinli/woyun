$(function() {
	$("#login").click(function() {
		signin();
	})
	$("#username, #password").keydown(function(e) {
		if (e.keyCode == 13) {
			signin();
		}
	})
})

function signin() {
	var username = $("#username").val();
	var password = $("#password").val();
	
	if (username.length <= 0 || password.lenght <= 0) {
		alert("请填写用户名或密码");
	} else {
		$.ajax({
			url: "/login",
			type: "POST",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify({"name": username, "password": password}),
			success: function(data) {
				if (data.status == 0) {
					window.location.href = "home.html";
				} else {
					$("#fm .errmsg").show();
				}
			},
			error: function() {
				alert("登录异常");
			}
		});
	}
}
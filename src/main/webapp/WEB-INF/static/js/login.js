$(function() {
	$("#login").click(function() {
		login();
	})
	$("#name, #pwd").keydown(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	})
});
function login() {
	var username = $("#name").val();
	var password = $("#pwd").val();
	
	if (username.length <= 0 || password.length <= 0) {
		alert("请填写用户名或密码");
	} else {
		$.ajax({
			url: "/login",
			type: "POST",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify({"username":username, "password": password}),
			success: function(data) {
				console.log(data);
				var _type = data.status;
				if (_type==0) {
					alert("登录成功");
					window.location.href="/static/html/about.html";
				} else {
					$("#fm .errormsg").show();
				}
			},
			error: function() {
				alert("数据异常");
			}
		});
	}
}
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

function checkUsername(username, tipInfo) {
	$.ajax({
		url: "/checkuser?name=" + username,
		type: "GET",
		success: function(data) {
			if (data.status == 0) {
				$("#fm .errmsg").html("* 用户名不存在！").show();
			} else {
				$("#fm .errmsg").html("ok").hide();
			}
		}
	})
}
$("#username").focusout(function() {
	var name = $("#username").val();
	if (name != null && name != "") {
		var tipInfo = $(this).siblings("span").eq(0);
		checkUsername(name, tipInfo);
	}
})

function signin() {
	var username = $("#username").val();
	var password = $("#password").val();
	
	var msg = $("#fm .errmsg").val();
	if (msg != "ok") {
		return;
	}
	
	if (username == "" || password == "") {
		$("#fm .errmsg").html("* 用户名或密码不能为空!").show();
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
					$("#fm .errmsg").html("* 用户名或密码错误!").show();
				}
			},
			error: function() {
				alert("登录异常");
			}
		});
	}
}
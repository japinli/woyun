$(function() {
	$("#register").click(function() {
		signup();
	})
})

function verifyUsername(name, tipInfo) {
	if (name == "") {
		tipInfo.html("用户名不能为空！").show();
		return;
	}
	
	$.ajax({
		url: "/checkuser?name=" + name,
		type: "GET",
		success: function(data) {
			if (data.status == 1) {
				tipInfo.html("该用户名已存在！").show();
			} else {
				tipInfo.html("ok").hide();
			}
		},
		error: function() {
			alert("验证用户名异常！");
		}
	});
}

function verifyEamil(email, tipInfo) {
	if (email == "") {
		tipInfo.html("邮箱不能为空！").show();
		return;
	}
	
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	if (!reg.test(email)) {
		tipInfo.html("无效的邮箱地址！").show();
		return;
	}
	
	$.ajax({
		url: "/checkemail?email=" + email,
		type: "GET",
		success: function(data) {
			if (data.status == 0) {
				tipInfo.html("ok").hide();
			} else {
				tipInfo.html("该邮箱已经注册！").show();
			}
		},
		error: function() {
			alert("验证邮箱异常！")
		}
	});
}

function verifyPassword(password, tipInfo) {
	var text = "";
	var valid = false;
	
	if (password == "") {
		text = "密码不能为空！";
	} else if (password.length < 6) {
		text = "密码长度不能小于6个字符";
	} else {
		valid = true;
		text = "ok";
	}
	
	tipInfo.html(text);
	tipInfo.css("visibility", valid ? "hidden" : "visible");
}

function verifyPasswordAgain(password, confirm, tipInfo) {
	if (password == confirm) {
		tipInfo.html("ok").hide();
//		tipInfo.css("visibility", "hidden");
	} else {
		tipInfo.html("两次密码不一致！").show();
//		tipInfo.css("visibility", "visible");
	}
}

function verifyPhone(phone, tipInfo) {
	var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
	if (phone == "") {
		tipInfo.html("手机号码不能为空！").show();
//		tipInfo.css("visibility", "visible");
	} else if (!reg.test(phone)) {
		tipInfo.html("无效的手机号码！").show();
//		tipInfo.css("visibility", "visible");
	} else {
		$.ajax({
			url: "/checkphone?phone=" + phone,
			type: "GET",
			success: function(data) {
				if (data.status == 1) {
					tipInfo.html("该手机号已注册！").show();
//					tipInfo.css("visibility", "visible");
				} else {
					tipInfo.html("ok").hide();
//					tipInfo.css("visibility", "hidden");
				}
			},
			error: function() {
				alert("验证手机号异常！");
			}
		});
	}
}

$("#username").focusout(function() {
	var tipInfo = $(this).siblings("span").eq(0);
	var name = $("#username").val();
	verifyUsername(name, tipInfo);
})

$("#password").focusout(function() {
	var tipInfo = $(this).siblings("span").eq(0);
	var password = $("#password").val();
	verifyPassword(password, tipInfo);
})

$("#confirm").focusout(function() {
	var tipInfo = $(this).siblings("span").eq(0);
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	verifyPasswordAgain(password, confirm, tipInfo);
})

$("#email").focusout(function() {
	var tipInfo = $(this).siblings("span").eq(0);
	var email = $("#email").val();
	verifyEamil(email, tipInfo);
})

$("#phone").focusout(function() {
	var tipInfo = $(this).siblings("span").eq(0);
	var phone = $("#phone").val();
	verifyPhone(phone, tipInfo);
})

function signup() {
	var userInfo = $("#username").siblings("span").eq(0);
	var passInfo = $("#password").siblings("span").eq(0);
	var confInfo = $("#confirm").siblings("span").eq(0);
	var emaiInfo = $("#email").siblings("span").eq(0);
	var phonInfo = $("#phone").siblings("span").eq(0);
	
	var username = $("#username").val();
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	var email = $("#email").val();
	var phone = $("#phone").val();
	
	if (userInfo.html() == "ok" && passInfo.html() == "ok" && 
		confInfo.html() == "ok" && emaiInfo.html() == "ok" &&
		phonInfo.html() == "ok")
	{
		$.ajax({
			url: "/signup",
			type: "POST",
			data: "json",
			contentType: "application/json",
			data: JSON.stringify({"name": username, "password": SparkMD5.hash(password), "email": email, "phone": phone}),
			success: function(data) {
				if (data.status == 0) {
					alert("用户注册成功！");
					window.location.href = "index.html";
				} else {
					alert("用户注册失败！");
				}
			},
			error: function() {
				alert("用户注册异常！");
			}
		});
	}
}
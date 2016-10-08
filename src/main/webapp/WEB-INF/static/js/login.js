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
		alert("添加后台登录");
	}
}
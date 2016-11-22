$(document).ready(function(){
	var loginNowButton = $("#loginNowButton");
	var reActivateButton = $("#reActivateButton");
	var showDiv = $("#showDiv");
	if(loginNowButton.length && loginNowButton.length > 0){
		loginNowButton.click(function(e){
			//按钮状态变化
    		$(this).attr("value","转向登录页面...");
    		$(this).prop("disabled",true);
    		$(this).css("opacity",0.6);
    		window.location.href = URL_login;
		});
	}else if(showDiv.length && showDiv.length > 0){
		load(3);
	}else if(reActivateButton.length && reActivateButton.length > 0){
		reActivateButton.click(function(e){
			//按钮状态变化
    		$(this).attr("value","正在激活...");
    		$(this).prop("disabled",true);
    		$(this).css("opacity",0.6);
    		window.location.href = URL_base + "user/"+$("#markId").val()+"/reActivate";
		});
	}
});

$(document).ready(function(){
	var phoneAcDiv = $("#phone-active");
	if(phoneAcDiv.length > 0){//手机激活
		var account = $("#account");
		var codeBtn = $("#get-code-btn");
		var codeInput = $("#code");
		var warnTip = $("#reset-code-text");
		var submitBtn = $("#submit-phone-btn");
		//点击获取验证码按钮
		codeBtn.click(function(e){
			 var jsonData = {
                     "sendDest": account.val(), 
                     "type": 1000
             }
			//发送
			sendValue("phone",codeBtn,jsonData);
		});
		//点击提交
		submitBtn.click(function(e){
			var codeVal = codeInput.val().trim();
			if(codeVal == ""){
				warnTip.text("验证码输入不可为空");
				return;
			}
			if(codeVal.length < 6){
				warnTip.text("验证码输入不合法");
				return;
			}
			$("#active-phone-form").submit();
		});
		//页面加载后若无错，则计时
		var error = getQueryString("error");
		if(error == null || error == ""){
			time(codeBtn);
		}
		//清除错误提示
		$(".containerBox").click(function(event){
			$(".tipWarning").text("");
		});
	}
	
	
	/*var loginNowButton = $("#loginNowButton");
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
	}*/
});

var wait=60;//时间
function time(o,p) {//o为按钮的对象，p为可选，这里是60秒过后，提示文字的改变
	if (wait == 0) {
		o.removeAttr("disabled");
		o.val("获取激活验证码");//改变按钮中value的值
		//p.html("如果您在1分钟内没有收到验证码，请检查您填写的手机号码是否正确或重新发送");
		wait = 60;
	} else {
		o.attr("disabled", true);//倒计时过程中禁止点击按钮
		o.val(wait + "秒后重新获取");//改变按钮中value的值
		wait--;
		setTimeout(function() {
			time(o,p);//循环调用
		},
		1000)
	}
}

function sendValue(tool, obj, jsonData){
    var $obj = obj;
    var sendTool = tool;   
    $.ajax({ 
        url: URL_base + '/wesign/common/' +sendTool+ '/send',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(jsonData),
        contentType: 'application/json',  //不指定此项，google下会报错
        success : function(data){           
            var code = data.resultCode;
            var desc = data.resultDesc;

            if(code == 0){
            	time($obj);           
            }else{
                $obj.html(desc).show();
            }
        },
        error : function(){           
            $obj.html("系统繁忙").css("color","#ff6700").show();
        }
     });
}

function getQueryString(name, url){
	if (!url) url = window.location.href;
    var name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
          results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
$(document).ready(function(){
    $("#link_to_tel").click(function(){
        $(this).addClass("active");
        $("#link_to_email").removeClass("active");
        $(".js_email_container").addClass("displayNo");
        $(".js_tel_container").removeClass("displayNo");
    });
           
    $("#link_to_email").click(function(){
        $(this).addClass("active");
        $("#link_to_tel").removeClass("active");
        $(".js_email_container").removeClass("displayNo");
        $(".js_tel_container").addClass("displayNo");
    });

    //邮箱
    $("#email-submit-btn").click(function(){
        //校验输入是否合法
        var emailValue =$('#email-address').val();
        var result = validEmail(emailValue);
        if(!result){
            $('#checkEmail').html("请输入正确的邮箱").show();            
        }else{
            $('#checkEmail').hide();
            $('#findPwdTip2').html("正在处理，请稍后...").show();
            var jsonData = {
                    "sendDest": emailValue, 
                    "type":2003
            };
            //提交到服务器
            $("#find-pwd-form2").submit();
        }
       
    });
    bindEnter("#email-submit-btn");
    
    //手机
    findByTel();
});

function bindEnter(obj){
    $("body").keydown(function(event){
        if(event.keyCode =="13"){
            obj.click();
        }
    });
    $("#link_to_email").click();
}

//找回
function sendValue(tool, obj, jsonData){
    var $obj = obj;
    var sendTool = tool;   
    $.ajax({ 
        url: URL_base + '/wesign/common/' +sendTool+ '/send',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        beforeSend:function(){
			if(sendTool == 'phone'){
				$(".getCode").prop("disabled", true).addClass("disabled");
				$(".getCode").attr("value", "正在获取验证码");
			}
		},
        success : function(data){           
            var code = data.resultCode;
            var desc = data.resultDesc;

            if(code == 0){
                if(sendTool == "email"){
                    $obj.html("请求链接已发送到指定邮箱,请前往邮箱确认链接").show(); 
                }else{
                    /*$(".getCode").prop("disabled", true).addClass("disabled");*/
                    $obj.html("验证码已发送").css("color","#3c9").show();
                    $('.js-curtext-state').text("").data("limit", false);
                    loadTime(60);
                    return true;
                }                               
            }else{
                $obj.html(desc).show();
                if(sendTool == 'phone'){
                    $('.getCode').attr("disabled", true).addClass("disabled");
                    $(".getCode").attr("value", "获取验证码");
                    $("#verifyCode").attr("disabled", true).addClass("disabled");
    				$(".js_telcode_text").text('验证码获取次数超过限制，请更换手机再试或明天再试');
                }
            }
        },
        error : function(){           
            $obj.html("系统繁忙").css("color","#ff6700").show();
        }
     });
}

//手机找回
function findByTel(){
    var telValidator = new FindValidator();
    var telFlag = {
            number: false,
            code: false
    };
    
    //验证手机号
    $('#phone-num').focusout(function(){     
        telFlag.number = telValidator.validPhone( $(this) );   
        if( $(".js-curtext-state").data("limit") ){
            $(".getCode").prop("disabled", true).addClass("disabled");
            $(".js-curtext-state").removeData("limit");
            $("#verifyCode").prop("disabled",false).removeClass("disabled");
        }
    });
    
    //验证输入码
    $('#verifyCode').focusout(function(){
        telFlag.code = telValidator.code( $(this), $('#reset-code-text'));
    });
        
    
    //获取验证码
    $('.getCode').unbind('click').click(function(){
        var phone = $('input[name="account"]').val();
        if($(this).hasClass("disabled")){
            return false;            
        }else{                        
            if( telFlag.number ){ 
            	//清除验证码输入框中的信息与错误提示信息
      		    $("#verifyCode").val("");
      		    $("#reset-code-text").text("");
      		    telFlag.code = false;
                //先验证是否为注册用户
                judgeReg(phone, function(code){
                    if(code == 0){
                        var jsonData = {
                                "sendDest": phone, 
                                "type": 1000
                        }
                        sendValue('phone', $('#findPwdTip1'), jsonData);            
                    }else{
                        $('#exist').html("请输入正确的手机号").css("color", "#ff6700").show();
                        setTimeout(function(){
                            $('#exist').hide();
                        }, 3000);
                        return false;
                        }
                });
            };                
        }
    });
   
    //提交
    $('#tel-submit-btn').unbind('click').click(function(){
        var activateCode = $('#verifyCode').val();
        var phoneNum = $('input[name="account"]').val();
        var $tip = $('#completeInfo');
        var jsonData = {
                "sendDest": phoneNum,
                "data": activateCode,
                "type": "CODE"
        }
        if(telFlag.code){
            verifyPhoneCode(activateCode, phoneNum, $tip, jsonData);
        }else{
            $tip.html("请将信息填写完整").show();
            setTimeout(function(){
                $tip.hide();
            }, 5000);
            return false;
        }     
      //提交到服务器
        $("#find-pwd-form1").submit();
    });
}

//检查是否为注册用户
function judgeReg(userId,callback){
    $.ajax({
        url: URL_base + '/wesign/anonymous/users/' +userId+ '/check?type=exist',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(data) {
            var code = data.resultCode;
            var desc = data.resultDesc;
            var resultData = data.resultData;
            if(code == 0){
                callback(code);
            }else{
                $('#exist').html(desc).show();
                setTimeout(function(){
                    $('#exist').hide();
                }, 5000);
            }
        },
        error : function(){    
            $('#exist').html("验证失败").show();
            setTimeout(function(){
                $('#exist').hide();
            }, 5000);
        }
    });
}

//校验验证码
function verifyPhoneCode(activateCode, phoneNum, $tip, jsonData){
    $.ajax({
        url: URL_base + '/wesign/common/phone/verify',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        success: function(data) {
            var code = data.resultCode;
            var desc = data.resultDesc;
            
            if(code == 0){
                $tip.html( desc ).css("color","#3c9").show;                
            }else{
                $tip.html( desc ).show();
            }
        },
        error : function(){
            $tip.html("对不起，验证失败，网络不给力或操作太频繁！").show();
            setTimeout(function(){
                $tip.hide();
            }, 3000);
        }
    });
}

function loadTime(secs){
    for(var i=secs;i>=0;i--) { 
      window.setTimeout('doUpdateTime(' + i + ')', (secs-i) * 1000); 
    } 
}

function doUpdateTime(num) { 
    $(".getCode").attr("value", "重新获取（"+num+"）");
    
    if(num == 0) { 
        $(".getCode").attr("value", "重新获取");
        $(".getCode").prop("disabled", false).removeClass("disabled");
        $(".js-curtext-state").data("limit", true);
    }
}

//邮箱校验
function validEmail(value){
    var re=/^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return value==' '? false : re.test(value);
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
//手机校验
function FindValidator(){
    var _this = this;
    
    appendHtml = function(elem, flag, text){
        if(elem.next().hasClass("gERROR")){
            if(flag){
                elem.next().html('');
            }else{
                elem.next().html( '<span class="errorWarning">'+ text +'</span>');
            }           
        }else{
            elem.after('<span class="gERROR"></span>');
            if(flag){
                elem.next().html('');
            }else{
                elem.next().html( '<span class="errorWarning">'+ text +'</span>');
            }
        }        
    }
    
    _this.validPhone = function($obj){      //手机号码
        var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
        var value = $obj.val();
        var pass = false;
        var tip = "";
        
        if( value == "" ){
            tip = " 请输入手机号码";
            pass = false;
            
        }else if( reg.test(value) ){
            pass = true;                                    
        }else{
            tip = " 请输入合法的11位手机号码";
            pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
    }
    
    _this.code = function( $obj , $tip){    //校验码       
        if( $obj.val() == '' ){
            $tip.html('请输入验证码') ;
            return false;
        }else{
            $tip.html('');
            return true;
        }
    }        
}

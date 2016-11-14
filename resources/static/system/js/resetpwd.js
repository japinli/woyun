$(document).ready(function(){
    var $resetBtn = $("#reset-submit-btn");
    if(typeof $resetBtn != 'undefined'){      
    //    $resetBtn.prop("disabled",true).css("cursor","not-allowed");
        //验证
        var $newp = $('input[name="password"]');
        var $rep = $('input[name="rePassword"]');
        var pwdValid = new validator();
        var pwdFlag  = {number: false};
        //新密码
        $newp.focusout(function(){        
            pwdFlag.number = pwdValid.validPassword($(this));
        });        
        //确认密码
        $rep.focusout(function(){
            pwdFlag.number = pwdValid.confirmPassword($newp ,$(this));
        });        
        $resetBtn.click(function(){
            //提交到服务器           
            if(pwdValid.validPassword($newp) && pwdValid.confirmPassword($newp ,$rep)){
                $("#reset-pwd-form").submit();
            }            
        });
    }            
});

//校验
function validator(){
    var _this = this;
    
    appendHtml = function(elem, flag, text){
        if(elem.next().hasClass("gERROR")){
            if(flag){
                elem.next().html('');
            }else{
                elem.next().html( '<span class="tipWarning">'+ text +'</span>');
            }
            
        }else{
            elem.after('<span class="gERROR"></span>');
            if(flag){
                elem.next().html('');
            }else{
                elem.next().html( '<span class="tipWarning">'+ text +'</span>');
            }
        }        
    }
    
    _this.validPassword = function($obj){   //密码
        var minMaxLength = /^[\S]{6,50}$/,
                upper = /^[A-Z]+$/,
                lower = /^[a-z]+$/,
                lowerUpper = /^[a-zA-Z]+$/,
                number = /^[0-9]+$/,
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;
        
        var value = $obj.val();
        var pass = false;
        var tip = "";
        
        if( value == ""){
            tip = "请输入密码";
            pass = false;
            
        }else if(!minMaxLength.test(value)){
            if(value.length > 50){
                tip = "密码长度不能超过50位" ;
            }else{
                tip = "密码长度不能少于6位" ;
            }
            pass = false;
            
        }else if(upper.test(value)){
            tip = "密码不能全为大写字母" ;
            pass = false;
            
        }else if(lower.test(value)){
            tip = "密码不能全为小写字母" ;
            pass = false;
            
        }else if(lowerUpper.test(value)){
            tip = "密码不能全为字母" ;
            pass = false;
            
        }else if(number.test(value)){
            tip = "密码不能全为数字" ;
            pass = false;
            
        }else{
            pass = true;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
    }
    
    _this.confirmPassword = function($prev, $obj){  //确认密码
         var pass = false;
         var tip = "";
        
         if($prev.val() == $obj.val()){
             pass = true;
         }else{
             tip = "密码确认错误" ;
             pass =  false;
         }
         
         appendHtml($obj, pass, tip);
         return pass;
    }    
}
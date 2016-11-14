$(document).ready(function(){   
    comboRest(function(withCertRemain,noCertRemain){
        if(withCertRemain > 0 || noCertRemain > 0){
            judgeEmailInfo();
        }
    });
    toAuth();
    chooseCombo();
});

var purchaseUrl = gfALLDATA("payHref")+"/combo.html";
//获取套餐剩余情况
function comboRest(callback){
    requestServer({
    requestURL:gfALLDATA("payHref")+'/combos/remain',
    requestType: 'GET',
    successCallback: function(data){
        var code = data.resultCode;
        var withCertRemain = data.withCertRemain;
        var noCertRemain = data.noCertRemain;
        callback(withCertRemain,noCertRemain);
        if(code == 0){              
            $('.withCert').html(withCertRemain);
            $('.noCert').html(noCertRemain);            
            //剩余签名为0
            if(withCertRemain<=0 && noCertRemain<=0){ 
                $('.signLink').removeAttr("href").css("cursor","pointer").unbind('click').click(function(){
                    confirmInfo("您的套餐余额不足，请于购买后进行签名流操作！","随便看看","购买套餐",purchaseUrl);
                });                
            }else{
                return;
            }          
            //剩余签名小于3
            if(withCertRemain<=3 && noCertRemain<=3){
                var warnText;
                (withCertRemain<=0 && noCertRemain<=0) ? warnText = "套餐购买提醒：您的套餐余额不足，无法进行签名流操作，请及时购买。" : warnText = "套餐购买提醒：您的套餐余额已不多，请及时购买。";               
                $('.view').prepend(
                        '<div class="purchaseWarn" style="position:fixed;width:100%;height:30px;line-height:30px;color:#5a5a5a;background-color:#FFDC9C;text-align:center;z-index:1111;">'
                            +'<span>'+ warnText +'</span>'
                            +'<a style="color:#3e8cd8;" href='+ purchaseUrl +'>购买套餐</a>'                           
                        +'</div>'
                );
            }
        }else{            
            return;
        } 
    }
    });
}

//邮箱绑定情况
function judgeEmailInfo(){
    requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'get',        
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;    
            var resultData = data.resultData;
            if(code == 0){                
                if(typeof(resultData.email) == "undefined"){
                    $('.signLink').removeAttr("href").css("cursor","pointer").unbind('click').click(function(){
                        confirmInfo("您还未绑定邮箱，请先绑定邮箱。<br>我们将通过邮箱将签名过程和结果告知您。","随便看看","绑定邮箱",gfALLDATA("alipayHref")+"/my-info/reset-email.html");
                    });
                }else{                   
                    return;
                }                                                     
            }else{
                console.log(desc);                
            } 
        }
    });
}

//添加实名认证入口
function toAuth(){
    $(document).delegate('.unauthorized','click',function(){
        $('#js_btn_group').addClass('borderTop');
        //底部备注信息
        var msg="<label class='fs12'>注：实名认证通过，<span class='free'>免费</span>颁发专有证书</label>"            
        //选择按钮链接
        var perIdentifyUrl=gfALLDATA("alipayHref")+"/person-auth.html";            
        chooseMune("实名认证",perIdentifyUrl,msg);
    });
}

//套餐选择
function chooseCombo(){
    if(($(".authIdentify").text()=="未认证"||$(".authIdentify").text()=="未通过"||$(".authIdentify").text()=="审核中")){
        $(".chooseCombo").css("display","none");
    }else if($(".authIdentify").text()=="已认证"){
        $(".chooseCombo").unbind('click').click(function(){
            $(this).attr("href",purchaseUrl);
        });
    }
}

//提示信息
function confirmInfo(html, btnTextL, btnTextR, URL){
    $(".tipMsg").dialog({ autoOpen: true,width: 400,modal: true});
    $('.tipMsgContent').html(html);
    $("#js_btn_group").addClass("borderTop").html(        
       '<span class="btn-low-s confirmNo mr25">'+ btnTextL +'</span>'
       +'<span class="btn-solid-primary-s toNewPage">'+ btnTextR +'</span>'             
    );   
    $('.toNewPage').unbind('click').click(function(){
        window.location.href = URL;
    })
    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });
}
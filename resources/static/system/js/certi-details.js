$(document).ready(function(){
    getCertList(function(aListArray){
       getCert(aListArray, function(resultData){
           showCert(resultData);
       }); 
    });
});

//获取证书列表
function getCertList(callback){
    requestServer({
        requestURL: gfALLDATA("certHref"),
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            hideLoading();
            var code = data.resultCode;
            var aListArray = data.certificates;
            var desc = data.resultDesc;            
            if(code == 0){
                callback(aListArray);                                               
            }else{
                console.log(desc);
                if($('.userIdentify').val()=="审核中"){
                    var stateTip = "请等待审核通过，通过后颁发可信证书";
                    var state = "审核中";                    
                }else if($('.userIdentify').val()=="未通过"){
                    var stateTip = "您的认证未能审核通过，请再次进行实名认证操作";
                    var state = "未通过";
                }else{
                    var stateTip = "请先完成实名认证，认证通过后颁发可信证书";
                    var state = "实名认证";                    
                }
                $('.view').append(
                        '<div class="occup-container textCenter">'
                            +'<div class="cert-occup occup-bg"></div>'
                            +'<div class="occup-info"><p>您还没有证书</p><p>'+ stateTip +'</p></div>'
                            +'<div><a class="btn-default-m">'+ state +'</a></div>'
                        +'</div>'
                );
                if(state=="实名认证" || state=="未通过"){
                    $('.btn-default-m').unbind('click').click(function(){
                        window.location.href = gfALLDATA("alipayHref")+"/person-auth.html";
                    })
                }else{
                    $('.btn-default-m').css({"cursor":"default","background-color":"transparent","color":"#64a0ff","border":"1px solid #64a0ff"});
                }
            } 
        }
    });
}

//获取单个证书
function getCert(aListArray, callback){    
    for(var i in aListArray){
        if(aListArray[i] != null){
            var certId = aListArray[i].id;
            requestServer({
                requestURL: gfALLDATA("certHref") +'/'+ certId,
                requestType: 'get',
                beforeCallback: function(){
                    showLoading();
                },
                successCallback: function(data){
                    hideLoading();
                    var code = data.resultCode;
                    var resultData = data.resultData;
                    var desc = data.resultDesc;       
                    var resultData = data.resultData;
                    if(code == 0){                                                                                           
                        callback(resultData);
                    }else{
                        showErrorInfo(desc);
                    } 
                }
            });
        }
    }    
}

//展示证书
function showCert(resultData){
    var certFrom   = transCertFrom(resultData.certFrom); 
    var startTime  = transformTime(resultData.startTime, false).ymd;
    var endTime    = transformTime(resultData.endTime, false).ymd;
    var useState   = transUseState(resultData.usingState);
    var applyUser  = transRealName(resultData.subject);
    var serialcode = resultData.serialCode; 
    var issuer     = transIssuerName(resultData.issuer);
    
    $('.view').append( '<div class="certiBox certiBoxBg" value="' +serialcode+ '">'
                +'<div class="certiBox-content">'
                    +'<p class="certiPara"><span class="certiTitle" style="position: absolute;">颁发给：</span><span class="certiContent">'+ applyUser +'</span></p>'
                    +'<p class="certiPara"><span class="certiTitle" style="position: absolute;">颁发者：</span><span class="certiContent">'+ issuer +'</span></p>'
                    +'<p class="certiPara"><span class="certiTitle" style="position: absolute;">序列号：</span><span class="certiContent">'+ serialcode +'</span></p>'
                    +'<p class="certiPara"><span class="certiTitle" style="position: absolute;">有效期：</span><span class="certiContent">'+ startTime +' ~ '+ endTime +'</span></p>'
                +'</div>'
            +'</div>');
    //占位作用，若正式证书不会出现某些数据为空的情况，可删去
    $('.certiContent').each(function(){
        var _this = this;
        if($(_this).html()==" "){
            $(_this).append('<span style="visibility:hidden;">123</span>');
        }
    });
}

//获取颁发者
function transIssuerName(value){        
    if(value != "null" && value != null){
        return  ((value.split('O='))[1]).split(',')[0];
    }else{
        return " ";
    }
}


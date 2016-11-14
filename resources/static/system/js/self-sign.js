$(document).ready(function(){
	//添加
    addRecipient();			
    //删除联系人
	$(document).delegate('.deleteRecipient','click',function(){
	    $(this).parent().remove(); 
	});
    //邮箱校验
    $(document).delegate('.valid-email','focusout',function(){
        var emailValue = $(this).val();
           if(!validEmail(emailValue)){               
               showErrorMsg($(this).next().next(),"请输入正确的邮箱");
           }                        
    });
	//点击下一步发送数据    
    $('.submitSignInfo').unbind('click').click(function(){  
        var _this = this;
        if($('.addresserName').prop("readonly") == false){
            addRealName();
        }
        relatedInfo(function(relatedData){
            sendRelatedInfo(relatedData);
        });       
        $(_this).prop("disabled", true);   
        setTimeout(function(){
            $(_this).prop("disabled",false);
        }, 500);
    });   	
});

//添加接收人信息
function addRecipient(){
    $('.addRecipient').unbind('click').click(function(){
        if($('.recipientInfo').size()<10){
            $('.self-recipientInfoArea').append(
                    '<div class="recipientInfo">'                       
                    +'<input type="text" class="self-recipientName" placeholder="姓名（必填）">'+' '
                    +'<input type="text" class="self-recipientEmail valid-email" placeholder="邮箱（必填）">'+' '
                    +'<i class="icon-highlight-remove deleteRecipient"></i>'
                    +'<span class="emailWarn"></span>'
                    +'</div>'
            );
        }else{
            $('.limitWarn').css("margin","10px 0");
            showErrorMsg($('.limitWarn'));
        }               
    });	
}

//本人签相关信息
function relatedInfo(callback){
    var aUserName    = [],
        aUserEmail   = [],
        aRelatedList = [],
        aValidEmail  = [];
    var docId = $('.file-node-name').data('docid');   
    $('.self-recipientName').each(function(){
        var _this = this;
        if(_this.value != ""){
            aUserName.push(_this.value);
        }               
    });
    $('.self-recipientEmail').each(function(){
        var _this = this;
        if(_this.value != ""){
            aUserEmail.push(_this.value);
            aValidEmail.push(validEmail(_this.value));
        }        
    });
    var emailManage = 0;
    emailDittoControl(aUserEmail, function(result){       
        result == 0 ? emailManage = 0 : emailManage = 1;
    });
    var addresserEmail = $('.addresserEmail').val();
    var msgTitle = $('.docTitle').val();
    var msgContent = $('.msgContentTxt').val();
    var revAuthed;
    $('.userIdentify').prop("readonly") == true ? revAuthed = 1 : revAuthed = 0;
    var addresserData ={ //发起人信息
            "username": gfALLDATA("uName"),
            "title": msgTitle,
            "subject": msgContent,
            "docId": docId,
            "reqUserId": gfALLDATA("uId"),
            "docOwnLevel": 1,
            "revAuthed": revAuthed
    }
    aRelatedList.push(addresserData);
    if(aUserEmail.indexOf(addresserEmail) == '-1'){
        var signData ={ //发起人只作为签名人
                "username": gfALLDATA("uName"),
                "revContact": addresserEmail,
                "revAsRecipient": 0,
                "title": msgTitle,
                "subject": msgContent,
                "docId": docId,
                "reqUserId": gfALLDATA("uId"),
                "docOwnLevel": 2,
                "sequence": 0,
                "revAuthed": revAuthed
        }
        aRelatedList.push(signData);
    }
    for(var i = 0; i<$('.self-recipientName').length; i++){ //接收人信息        
        var ownLevel = 3;
        if(aUserEmail[i] == addresserEmail){
            ownLevel = 2;
        } 
        var jsonData = {
                "username": aUserName[i],
                "revContact": aUserEmail[i],
                "revAsRecipient": 1,
                "title": msgTitle,
                "subject": msgContent,
                "docId": docId,
                "reqUserId": gfALLDATA("uId"),
                "docOwnLevel": ownLevel,
                "sequence": 0,
                "revAuthed": revAuthed
            }                               
        aRelatedList.push(jsonData);        
    }    
    var relatedData = {"workflowData": aRelatedList};
    if($('.self-addresserName').val()!="" && aUserName.length==$('.self-recipientName').size() && emailManage==1 && aUserEmail.length==$('.self-recipientEmail').size() && msgTitle.length>0 && msgTitle.length<=100 && msgContent.length<=240 && aValidEmail.indexOf(false)=='-1'){
        callback(relatedData);
    }else if(emailManage == 0){
        showErrorMsg($('.warning'),"*请检查所填写邮箱是否存在重复值");
    }else if(msgTitle.length > 100){
        showErrorMsg($('.warning'),"*信息标题不得超过100个字符");
    }else if(msgContent.length > 240){
        showErrorMsg($('.warning'),"*消息内容不得超过240个字符");
    }else{       
        showErrorMsg($('.warning'),"*请检查是否正确填写了所有必填项");
    }
    console.log(msgTitle.length);
}

//本人签相关信息发送
function sendRelatedInfo(relatedData){
    var docId = $('.file-node-name').data('docid');
    requestServer({
        requestURL: gfALLDATA("docHref") +'/'+ docId +'/workflows',
        requestType: 'post',
        requestAsync: 'false',
        requestData: JSON.stringify(relatedData),
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            var desc = data.resultDesc;
            var resultData = data.resultData;
            if(code == 0){
                window.location.href = gfALLDATA("docHref") +'/sign-self-signature.html?doc-id=' + docId;
            }else{  
                if(desc == "文档id为空"){
                    showErrorMsg($('.warning'),"*请检查是否已上传文件");
                }else if(desc == "文档流已存在"){
                    showErrorMsg($('.warning'),"*该文档流已存在");
                }               
            }
        }
    });
}


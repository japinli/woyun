$(document).ready(function(){   
	//添加
    addMultiRecipient();		
	//删除接收人
	removeRecipient();
	//顺序发送
	sendInOrder();
	//排序
	sequence();
	//邮箱校验
	$(document).delegate('.recipientEmail','focusout',function(){
	    var emailValue = $(this).val();
	       if(!validEmail(emailValue)){
	           $('.emailWarn').css("margin-left","200px");
	           showErrorMsg($('.emailWarn'));
	       }                        
    });
	//点击下一步发送数据    
    $('.submitSignInfo').unbind('click').click(function(){     
        var _this = this;
        if($('.addresserName').prop("readonly") == false){
            addRealName();
        }
        getRelatedInfo(function(relatedData){
            sendRelatedInfo(relatedData);
        });
        $(_this).prop("disabled", true);   
        setTimeout(function(){
            $(_this).prop("disabled",false);
        }, 500);
    });  
})

//多人签相关信息
function getRelatedInfo(callback){
    var docId = $('.file-node-name').data('docid');
    var aUserName   = [],
        aUserEmail  = [],
        aValidEmail = [],
        aInfoList   = [],
        aOwnLevel   = [];
    $('.recipientName').each(function(){
        var _this = this;
        if(_this.value != ""){
            aUserName.push(_this.value);
        }               
    });
    $('.recipientEmail').each(function(){
        var _this = this;
        if(_this.value != ""){
            aUserEmail.push(_this.value);
            aValidEmail.push(validEmail(_this.value));
        }                 
    });
    //判断邮箱是否有重复
    var emailManage = 0;
    emailDittoControl(aUserEmail, function(result){       
        result == 0 ? emailManage = 0 : emailManage = 1;
    });
    var msgTitle = $('.docTitle').val();
    var msgContent = $('.msgContentTxt').val();  
    var revAuthed;
    $('.userIdentify').prop("readonly") == true ? revAuthed = 1 : revAuthed = 0;
    for(var i = 0; i<$('.recipientName').length; i++){ //接收人信息        
        var revType = parseInt($('.recipientType').eq(i).val());
        var revAsRecipient = 0;
        (revType == 0) ? revAsRecipient = 0 : revAsRecipient = 1;
        var docOwnLevel = 1;
        (revType == 0 || revType == 1) ? docOwnLevel = 2 : docOwnLevel = 3;  
        aOwnLevel.push(docOwnLevel);
        var sequence = $('.orderDisplay').eq(i).val();    
        if(typeof(sequence) == "undefined"){
            sequence = 0;
        }                               
        var signSequence = parseInt(sequence);        
        var jsonData = {
            "username": aUserName[i],
            "revContact": aUserEmail[i],
            "revAsRecipient": revAsRecipient,
            "title": msgTitle,
            "subject": msgContent,
            "docId": docId,
            "reqUserId": gfALLDATA("uId"),
            "docOwnLevel": docOwnLevel,
            "sequence": signSequence,
            "revAuthed": revAuthed
        }
        aInfoList.push(jsonData);           
    }   
    var addresserData ={ //发起人信息
            "username": gfALLDATA("uName"),
            "title": msgTitle,
            "subject": msgContent,
            "docId": docId,
            "reqUserId": gfALLDATA("uId"),
            "docOwnLevel": 1,
            "revAuthed": revAuthed
    }
    aInfoList.push(addresserData);
    var relatedData = {"workflowData": aInfoList};
    //获取人数
    var map = {};
    for(var j = 0; j < aOwnLevel.length; j++){
        var num = aOwnLevel[j];
        !map[num] ? map[num] = 1 : map[num]++ ;
    }
    typeof(map[2])=="undefined" ? map[2] = 0 : map[2];
    typeof(map[3])=="undefined" ? map[3] = 0 : map[3];
    //本人必须为签名人
    var myType = parseInt($('.addresserEmail').next().val());
    var myLevel = 0;
    (myType == 0 || myType == 1) ? myLevel = 1 : myLevel = 0;
    if(myLevel==1 && aUserName.length==$('.recipientName').size() && emailManage==1 && aUserEmail.length==$('.recipientEmail').size() && aValidEmail.indexOf(false)=='-1' && map[2]>=2 && map[2]<=10 && map[3]<=10 && msgTitle.length>0 && msgTitle.length<=100 && msgContent.length<=240){
        callback(relatedData);
    }else if(map[2]<2){        
        showErrorMsg($('.warning'),"*请添加包括您在内的至少两位签名人");
    }else if(myLevel == 0){
        showErrorMsg($('.warning'),"*本人必须为签名人，若只需添加自己为接收人，请使用他人签");
    }else if(emailManage == 0){
        showErrorMsg($('.warning'),"*请检查所填写邮箱是否存在重复值");
    }else if(map[2]>10 || map[3]>10){
        showErrorMsg($('.warning'),"*签名人数、接收人数上限均为10");
    }else if(msgTitle.length > 100){
        showErrorMsg($('.warning'),"*信息标题不得超过100个字符");
    }else if(msgContent.length > 240){
        showErrorMsg($('.warning'),"*消息内容不得超过240个字符");
    }else{        
        showErrorMsg($('.warning'),"*请检查是否正确填写了所有必填项");
    }
}

//多人签相关信息发送
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
                window.location.href = gfALLDATA("docHref") +'/sign-fields-assign.html?doc-id=' + docId;
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
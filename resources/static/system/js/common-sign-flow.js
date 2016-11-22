$(document).ready(function(){  
    //填充发起人信息
    // getAddresserInfo();
    //选择签名类型
   /* comboRest(function(){
        signWithCert();
    });*/
    
    //添加联系人UI
    //changeAddUI();
    //点击上传文档
    uploadFile();     
    //删除文档
    $(document).delegate('.file-node-remove','click',function(){
        var _this = this;
        if($(_this).parent().next().hasClass("file-result-success")){
            deleteFile();
        }else{
            $('.upload-files-container').html("");
        }                         
    });      
});

//添加联系人UI
/*function changeAddUI(){
    $('.addRecipient').mouseover(function(){
        $('.addRecipientIcon').css("color","#a9a9a9");
    }).mouseout(function(){
        $('.addRecipientIcon').css("color","#095db1");
    });
}*/

//添加接收人信息(多人签)
/*function addMultiRecipient(){
    $('.addRecipient').unbind('click').click(function(){
        if($('.orderTrig').attr('name') == "nope"){  //未选择顺序发送
            $('.recipientInfo:last').after(
            '<div class="recipientInfo">'
                +'<span class="sortHandle iconImg"></span>'+' '
                +'<input type="text" class="recipientName" placeholder="姓名（必填）">'+' '
                +'<input type="text" class="recipientEmail" placeholder="邮箱（必填）">'+' '
                +'<select class="recipientType">'
                    +'<option value="0">签名</option>'
                    +'<option value="2">接收文档</option>'
                    +'<option value="1">签名并接收文档</option>'
                +'</select>'+' '
                +'<i class="icon-highlight-remove deleteRecipient"></i>'
            +'</div>'
        );
        }else{   //选择了顺序发送
            $('.recipientInfo:last').after(
            '<div class="recipientInfo">'
                +'<span class="sortHandle iconImg"></span>'
                +'<input class="orderDisplay" type="text" readonly="readonly">'+' '
                +'<input type="text" class="recipientName" placeholder="姓名（必填）" style="width:267px;">'+' '
                +'<input type="text" class="recipientEmail" placeholder="邮箱（必填）" style="width:267px;">'+' '
                +'<select class="recipientType" style="width:217px;">'
                    +'<option value="0">签名</option>'
                    +'<option value="2">接收文档</option>'
                    +'<option value="1">签名并接收文档</option>'
                +'</select>'+' '
                +'<i class="icon-highlight-remove deleteRecipient"></i>'
            +'</div>'
        );
        $('.sortHandle').css("display","inline-block"); 
        getSendOrder(); 
        }   
        sequence();
    }); 
}*/

//删除接收人信息
/*function removeRecipient(){
    $(document).delegate('.deleteRecipient','click',function(){
        if($('.deleteRecipient').length > 1){
            $(this).parent().remove();
        }   
        getSendOrder();
    });
}
*/
//排序
/*function sequence(){
    $('.recipientInfoArea').sortable({      
        handle: $('.sortHandle'),
        axis: "y",
        cursor: "move",
        stop: function(){
                  getSendOrder();
              }
    });
}*/

//获取发送顺序
/*function getSendOrder(){    
    $('.orderDisplay').each(function(i){
        var _this = this;
        var sendOrder = i + 1;
        $(_this).attr('value',sendOrder);
    });
}*/

//顺序发送处理
/*function sendInOrder(){
    $('.orderTrig').on('click',function(){
        var _this = this;
        if($(_this).attr('name') == "nope"){
            $('.sortHandle').after(
                '<input class="orderDisplay" type="text" readonly="readonly">'
            );
            $('.recipientName, .recipientEmail').css("width","267px");
            $('.recipientType').css("width","217px");   
            $(_this).attr('name','yep');
            $('.sortHandle').css("display","inline-block");
            getSendOrder();
        }
        else if($(_this).attr('name') == "yep"){
            $('.orderDisplay').remove();
            $('.sortHandle').css("display","none");
            $('.recipientName, .recipientEmail').css("width","285px");
            $('.recipientType').css("width","236px");   
            $(_this).attr('name','nope');           
        }
    }); 
}*/

//套餐剩余情况
/*function comboRest(callback){
    requestServer({
    requestURL:gfALLDATA("payHref")+'/combos/remain',
    requestType: 'GET',
    successCallback: function(data){
        var code = data.resultCode;
        var withCertRemain = data.withCertRemain;
        var noCertRemain = data.noCertRemain;        
        if(code == 0){              
            $('.userIdentify').attr({"data-withcert":withCertRemain, "data-without":noCertRemain});          
            callback();
        }else{            
            return;
        } 
    }
    });
}*/

//有无证书签名选择
/*function signWithCert(){
    if($('.userIdentify').data('withcert')=="0"){ //有证书套餐0
        $('.signWith').prop("disabled",true).css("cursor","not-allowed");
        $('.signWithout').addClass('choosen');
        $('.userIdentify').prop("readonly",false);
    }else if($('.userIdentify').data('withcert')!="0" && $('.userIdentify').data('without')=="0"){//无证书0，有证书>0
        $('.signWith').addClass('choosen');
        $('.signWithout').prop("disabled",true).css("cursor","not-allowed");
        $('.userIdentify').prop("readonly",true);
    }else if($('.userIdentify').data('withcert')!="0" && $('.userIdentify').data('without')!="0"){//都>0
        $('.signWith').addClass('choosen');
        $('.userIdentify').prop("readonly",true);
        $('.signWithout').unbind('click').click(function(){
            $('.signWith').removeClass('choosen');
            $('.signWithout').addClass('choosen');
            $('.userIdentify').prop("readonly",false);
        });
        $('.signWith').unbind('click').click(function(){
            $('.signWithout').removeClass('choosen');
            $('.signWith').addClass('choosen');
            $('.userIdentify').prop("readonly",true);
        });
    }
}*/

//获取发起人信息
/*function getAddresserInfo(){
    requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'get',        
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;    
            var resultData = data.resultData;
            if(code == 0){
                if(typeof(resultData.realName) != "undefined"){
                    $('.addresserName').attr("value", resultData.realName).prop("readonly", true);
                }
                if(typeof(resultData.email) != "undefined"){
                    $('.addresserEmail').attr("value", resultData.email).prop("readonly", true);
                }                                                     
            }else{
                console.log(desc);                
            } 
        }
    });
}*/

//上传文档
function uploadFile(){
    
    //var password = $('#file-password').val();
   
    var $fileContent = $('.upload-files-container');
    var statusCode = {
        "100130000": "上传成功",
        "100130100": "上传失败，文件为空",
        "100130101": "上传失败，文件名长度不符合要求",
        "100130102": "上传失败，文件尺寸不符合要求",
        "100130103": "上传失败，文件类型不符合要求",
        "100130104": "上传失败，达到已有文件数量限制，不允许再上传",
        "100130105": "上传失败，未预测到的错误或异常，请重新操作或联系我们",
        "100130106": "上传失败，存在失败的上传"           
    };
   
    $('#upload-files').fileupload({
        dataType: 'json',
        type: 'post',
       /* autoUpload: true,*/
        /*singleFileUploads: true,*/
        sequentialUploads: true,
        //formData:{properties:JSON.stringify(properties)},
        acceptFileTypes: /(\.|\/)(pdf|doc|docx|xls|xlsx|ppt|pptx|wps|et|dps|jpeg|jpg|jpe|gif|png)$/i ,
        maxFileSize: 2097152,   //1024*1024*2(M)
        formAcceptCharset: 'UTF-8'
    }).on('fileuploadadd', function(e, data){
    	var isEncrypted = $('#addresserInfo input[name="toggle"]:checked').val();
    	 var password = $('#file-password').val();
    	    var signPassword = $('.docTitle').val();
    	    var msgContent = $('.msgContentTxt').val();
    	    var properties ={ //文件附加信息
    	            "password":password,
    	            "isEncrypted":isEncrypted,
    	            "signPassword":signPassword,
    	            "msgContent":msgContent,
    	    }
        $fileContent.html("");
        data.context = $fileContent;
        data.formData={properties:JSON.stringify(properties)};
        $.each(data.files, function(index, file){
            var aFileSplit = file.name.split(".");                      
            aFileSplit.splice(-1);
            var fileName = aFileSplit.join(".");
            // $('.docTitle').attr('value', fileName); //上传文档时给信息标题自动赋值
           
            var node = $('<div class="file-node-container" />') 
                      .append($('<div class="file-node-info" />').html('<i class="icon-description file-thumbnail"></i><span title="'+ file.name +'" class="file-node-name">'+ file.name +'</span><span class="file-node-size">('+ uploadFileSizeConvertTip( file.size ) +')</span><i class="icon-highlight-remove file-node-remove"></i>'))
                      .append($('<div class="fileup-Progress progress-container" />').html('<div class="fileupload-progress"><div style="width:0%;" class="progress-bar"></div></div>'));
            $('.progress-container').show();
            $(node).appendTo(data.context);    
            data.url = gfALLDATA("docHref");
        });
    }).on('fileuploadprocessalways', function(e, data){
        /*var index = data.index;*/
        var file = data.files[0];
        var node = $(data.context.children()[0]);
        if(file.error){
            node.find('.progress-container').remove().end().append($('<p class ="file-result-error"></p>').html(file.error));
            return false;
        }
    }).on('fileuploadprogressall', function(e, data){
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('.fileup-Progress').css('display','block');
        $('.progress-bar').css('width',progress + '%');
    }).on('fileuploaddone', function(e, data){
        var desc = data.result.resultDesc;
        var statusDesc = data.result.resultStatusCode;                
        if(data.result.resultCode == 0){
            $.each(data.files, function(index, file){
                $(data.context.children()[index])
                    .append( $('<p class="file-result-success"></p>').html( '上传成功') )                        
                    .find('.progress-container').remove();
            });
        }else{
            $.each(data.files, function(index, file){
                $(data.context.children()[index]).append( $('<p class="file-result-error"></p>').html( '上传失败：'+ statusCode[statusDesc]));
            });
        }
        var docId = data.result.documents[0].docId;  //唯一标识,与后面流程绑定
        $('.file-node-name').attr("data-docid", docId);
    });
}

//删除文档
function deleteFile(){    
    var docId = $('.file-node-name').data('docid');
    requestServer({
        requestURL: gfALLDATA("docHref")+'/'+ docId,
        requestType: 'delete',       
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            var desc = data.resultDesc;
            var resultData = data.resultData;
            if(code == 0){
                $('.upload-files-container').html("");
                $('.docTitle').attr('value','');
            }else{
                showErrorInfo(desc);
            }
        }
    });
}

//邮箱校验
/*function validEmail(value){
    var re=/^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return value==' '? false : re.test(value);
}*/

//判断填写重复值
/*function emailDittoControl(Array,callback){
    var newArray = Array.join(",") + ",";
    for(var i = 0;i < Array.length; i++){
        if(newArray.replace(Array[i]+ ",", "").indexOf(Array[i]+ ",")>-1){
            var result = 0;
            break;            
        }else{
            result = 1;
        }
    }
    callback(result);
}*/

//添加本人real_name
/*function addRealName(){
    var newRealName = $('.addresserName').val();       
    unicode(newRealName) == false ? newRealName : (newRealName = unicode(newRealName));    
    var jsonData = {
            "newData": newRealName,
            "type": 5
    }
    requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'put',    
        requestAsync: 'false',
        requestData: JSON.stringify(jsonData),
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                console.log(desc);
            }else{                 
                showErrorMsg($('.warning'),"*本人姓名存储失败");
            }
        }
    });
}*/

//Unicode编码转换
function unicode(text){
    var preStr='\\u';
    var cnReg=/[\u0391-\uFFE5]/gm;
    if(cnReg.test(text)){
        var value=text.replace(cnReg,function(str){
            return preStr+str.charCodeAt(0).toString(16)
        });
        return value;
    }else{
        return false;
    }
}

function showErrorMsg($tipSelector,html){    
    $tipSelector.html(html).css("display","inline-block");
    setTimeout(function(){
        $tipSelector.fadeOut('normal');
    }, 4000);
}


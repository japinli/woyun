$(document).ready(function(){
	//申请授权码
	$("#js_apply_code").unbind('click').click(function(){
		requestCode(function(data){
			var result = data.resultCode;
			var resultDesc = data.resultDesc;
			if(!result){	//成功
				$("#js_code_list").html(
					'<tr>'
               			+'<td>1</td>'
               			+'<td>'+ data.apiKey +'</td>'
               			+'<td>'+ data.secretKey +'</td>'
               			+'<td>'+ data.testSecretKey +'</td>'
               			+'<td>'+ data.startDate +'到'+ data.endDate +'</td>'
               			+'<td>'+ data.state +'</td>'
               			+'<td>'+ data.userId +'</td>'
               			+'<td>'+ data.useMode +'</td>'
               			+'<td>'+ data.useSituation +'</td>'
               			+'<td>'+ data.usePermission +'</td>'
               			+'<td>'+ data.useVersion+'</td>'
               		+'</tr>'
				);
			}else{
				alert(resultDesc);
			}
		});
	});
	
	//注册USBKey
	$("#js_regist_usb").unbind('click').click(function(){
		//检测USBKey是否存在
		reqExistUSBKey();
	});
	
});

//申请授权码
function requestCode(callback){
	requestServer({
        requestURL: 'user/sys/open/auth-code/1/1',
        requestType: 'POST',
        async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
          
        	if(callback){
        		callback(data);
        	}
            hideLoading();
        }
    });
}

//ID获取
function requestDeviceId(){
	requestServer({
        requestURL: 'user/sys/certificate/usbkey/device-id',
        requestType: 'GET',
        async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
        	if(!code){	//获取成功
        		
        	}else{
        		
        	}
            hideLoading();
        }
    });
}

//注册USBKey
function reqregistUSBKey( deviceId ){
	var deviceId = deviceId;
	requestServer({
        requestURL: 'user/sys/certificate/usbkey/'+deviceId+'/regist',
        requestType: 'POST',
       // async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
           // var code = data.resultCode;
           // var desc = data.resultDesc;
        	if(callback){
        		callback(data);
        	}
            hideLoading();
        }
    });
}

//检验是否存在USBKey
function reqExistUSBKey(){
	requestServer({
        requestURL: 'user/sys/certificate/usbkey/exist',
        requestType: 'GET',
        async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
        	var result = data.resultCode;
			var resultDesc = data.resultDesc;
			if(!result){	//USBKey存在
				//USBKEY有效性
				reqValidUSBKey();
				
			}else{
				usbComDialog(resultDesc + "。请插入USBKey，然后再注册");
				
			}
            hideLoading();
        }
    });
}

//检测USBKEY有效性
function reqValidUSBKey(){
	requestServer({
        requestURL: 'user/sys/certificate/usbkey/valid',
        requestType: 'GET',
        async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
        	if(!code){
        		//有效
        		getUSBIdUI();
        	}else{
        		usbComDialog(desc + "。您的USBKey已失效，无法完成USBKey注册。");
        	}
            hideLoading();
        }
    });
}

//提示框
function usbComDialog(text){
	$(".confirmDialog").dialog("open").html(
			'<div class="confirmContent textCenter">'+text+'</div>'+
		    '<div class="confirmBtn textRight borderTop">'+
		        '<button class="confirmNo btn-default mr25">我知道了</button>'+
		    '</div>'
	 );
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
}

//检测有效过度到获取ID
function getUSBIdUI(){
	$(".confirmDialog").dialog("open").html(
			'<div class="confirmContent textCenter">您的USBKey已准备就绪，点击”继续“获取注册ID，点击”取消“将结束本次操作。</div>'+
		    '<div class="confirmBtn textRight borderTop">'+
		        '<button class="confirmNo btn-default mr25">取消</button>'+
		        '<button class="confirmYes btn-default mr25">继续</button>'+
		    '</div>'
	 );
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
	
	$(".confirmYes").unbind('click').click(function(){
		requestDeviceId();
	});
}

//获取注册ID到注册
function getIdtoRegist(flag, deviceId){
	if(flag){
		$(".confirmDialog").dialog("open").html(
				'<div class="confirmContent textCenter">'
					+'<div>您的注册ID为: '+deviceId+'</div>'
					+'<div><label>请输入注册ID</label><input id="js_input_usbid" type="text"/><span class="errorClass"></span></div>'
				+'</div>'+
			    '<div class="confirmBtn textRight borderTop">'+
			        '<button class="confirmYes btn-default mr25">注册</button>'+
			    '</div>'
		 );
		
		$(".confirmYes").unbind('click').click(function(){
			var idvalue = $("#js_input_usbid").val();
			if(idvalue === deviceId){
				reqregistUSBKey( idvalue );
				
			}else{
				$(".errorClass").text("输入ID有误");
			}
			
		});
		
	}else{
		$(".confirmDialog").dialog("open").html(
				'<div class="confirmContent textCenter">注册ID获取失败！</div>'+
			    '<div class="confirmBtn textRight borderTop">'+
			        '<button class="confirmNo btn-default mr25">取消</button>'+
			        '<button class="confirmYes btn-default mr25">重新获取</button>'+
			    '</div>'
		 );
		
		$(".confirmNo").unbind('click').click(function(){
			$(".ui-dialog-titlebar-close").click();
		});
		
		$(".confirmYes").unbind('click').click(function(){
			requestDeviceId();
		});
		
	}
	
}
var gsignDOCID = getQueryString("doc-id");
var gRules = {
		"rnAuth": ($("#hidden-type").val()).indexOf("rnAuth")
};

$(document).ready(function(){
	var buttonContainer = parseFloat($("#buttonContainer").css("width")) + 206 +'px';
	var nPageTotal = $(".preview-page").length;
	//设置继续签署
	$(".sign-mask").css('display','block');
	$("#containerViewer").append(
	  '<div class="signTip">'
	  +' <a>是否签署这份文件</a>'
	  +'<input id="continue-sign" type="button" value="继续"/>'
	  +'</div>');
	$("#continue-sign").click(function(){
		$(".sign-mask").css('display','none');
		$(".signTip").css('display','none');
		$("#topContainer").show();
	});
    
   $("#buttonContainer").css("width",buttonContainer);
   
   getFormList(nPageTotal);
}); 

//获取pdf表单
function getFormList(nPageTotal){
	var wfId = $("#hidden-wfId").val();
    requestServer({
    	requestURL:gfALLDATA( "docHref" )+'/'+gsignDOCID+'/workflows/'+ wfId +'/forms?pages=1-'+ nPageTotal ,
        requestType: 'get',  
        requestAsync: false,
        successCallback: function(data){  
           var statusCode = data.resultStatusCode;
           var code = data.resultCode;
           var resultData = data.resultData;
           if(code == 0){
        	   getFormData(resultData, function(signsTop, signsId){
        		   signIndex(signsTop, signsId);
        	   });  
           }else{
               console.log(data.resultDesc);
           }
        }
    });
} 

//获取并展示表单项
function getFormData(resultData, callback){
	var signerName = $("#hidden-name").val();
	var signsTop = new Array();
	var signsId = new Array();
    for(var i in resultData){
        if(resultData[i] != null){            
            var formName = resultData[i].name;                     
            var content = (resultData[i].data == undefined) ? "" : resultData[i].data;             
            var lValue = (resultData[i].positions.ulX) + 'px';
            var tValue = (resultData[i].positions.ulY) + 'px';
            var formWidth = ((resultData[i].positions.lrX) - (resultData[i].positions.ulX)) + 'px';
            var formHeight = ((resultData[i].positions.lrY) - (resultData[i].positions.ulY)) + 'px';
            var objIndex = resultData[i].page;
            var formFieldId = objIndex +'-formItem-'+ i ;
            var type = resultData[i].type; 
            if(type == 7){
            	$('#esignForm' + objIndex).append(
            			'<div id="'+ formFieldId +'" class="formAdornment" style="left:'+ lValue +'; top:'+ tValue +'; width: '+ formWidth +'; height: '+ formHeight+'">'
	            			+'<div id="custom7_'+ formFieldId +'" class="addItemSign displaySignature" name="'+ formName +'"   style="width:'+ formWidth +'; height:'+ formHeight +';">'
	                		+'</div>'     
            			+'</div>');
            	
            	if(content == ""){
                	$('#'+ 'custom7_'+ formFieldId).html(
                            '<div id="custom7_div_'+ formFieldId +'" class="clickSign" name="'+objIndex+'" style="line-height: '+ formHeight +'; width:'+ formWidth +'; height:'+ formHeight +';">'
                            +'<p class="signTxt">点击签名</p>'
                            +'</div>'
                	);
                	$('#'+ 'custom7_div_'+ formFieldId).mouseover(function(){
                		$(this).css("background-color","rgb(222, 177, 124)");
                	}).mouseout(function(){
                		$(this).css("background-color","#EFBF87");
                	});
                	$('#'+ 'custom7_div_'+ formFieldId).unbind("click").click(function(){	
                		showSignaturePanel(this,signerName);
                	});
                }
            }
        }
    }   
    $(".clickSign").each(function (index,element) {
		var p = $(element).attr("name") - 1;
		var top = parseFloat($(element).parent().parent().css("top")) + 17 *p + parseFloat($(element).parent().parent().parent().parent().css("height")) * p ;
		signsId.push( $(element).parent().parent().attr('id') );
		signsTop.push( top );	
	});
    
    if(callback){
    	callback(signsTop, signsId);
    }
    
}

//签名索引
function signIndex(signsTop, signsId){
	//位置排序
    var flag;
	var temp = 0;
	var B = signsTop.length;
    var tempDiv = 0;
	for(var i = 0; i < B - 1 ; i++){
	    flag = 0;   //本趟排序开始前，交换标志应为假
	   for(var j = B - 1 ; j > i;j--) //内循环控制一趟排序的进行  
	   {
	       if(signsTop[j] < signsTop[j-1] ) //相邻元素进行比较，若逆序就交换
	    	 {
	        temp = signsTop[j];
	        signsTop[j] = signsTop[j-1];
	        signsTop[j-1] = temp;       
	        tempDiv = signsId[j];
	        signsId[j] = signsId[j-1];
	        signsId[j-1] = tempDiv; 
	         flag = 1;                 //发生了交换，故将交换标志置为真         
	       }	      
	   }
	   if (flag == 0)  //本趟排序未发生交换，提前终止算法
	       break; 
	}
	 var t = 0;
	 $("#signButton").unbind("click").bind("click",function(){
	    	var btnTop = new Array();
	    	var st = signsTop.length;
	    	var signBtn = signsTop[t] +'px';
	    	var div=$('#'+ signsId[t] );
	    	$('.preview-container-scroll').animate({scrollTop:signBtn}, 500);  
	    	div.animate({ opacity:'0.5',},600);
	    	div.animate({ opacity:'1',},600);
	        if(t < st-1 ){
	        	t++;
	        }else{
	        	t = 0 ;
	        }
	    });
}

//调用签名模块
function showSignaturePanel($obj,defaultText){
   var $signboard= $('.inputSignature');
   $("#signaturePanel" ).dialog( "open" ).find("#signaturePut-link").click();
   //way1
   fPutinSignature( {"obj": $signboard, "userName": defaultText}, function( resultObj ){ 
		showSignatureImage({"type":1, "data": resultObj.imgData}, $obj );
        toggleDialog(false);
    });
   //扫描签名
   if($('.qrcode_container').css("display") == "none"){
		$('.qrcode_container').css("display","block");
		$('.szCanvasDiv').css("display","none");
	}
	if (window.WebSocket){
		fScanSignatrue(function( resultObj ){ 
			showSignatureImage({"type":1, "data": resultObj.imgData}, $obj );
			toggleDialog(false);
			if(gScanObj.stompClient != null)
			{
				gScanObj.stompClient.disconnect();
			}
	    });
	}else{
	    $(".szsignatureScan").addClass("hidden");
		$(".szSuggest").removeClass("hidden");
	 }
   //way2
   //上传
   uploadSignature( {"upload": $("#js_sealUpload")}, function( result ){
 	  $.get( result.seals[0].link.href, function( data ){
 		  if( data.resultCode == 0 ){
 			  var sHref = data.resultData.location;
 			  var sealid = data.resultData.sealId;
 			  showSignatureImage( {"type":2, "data": sHref, "id": sealid}, $obj );
 			  toggleDialog(false);
 		  }
 	  });
   });
   
	if($($obj).hasClass("signPost")){
		$($obj).empty();	
	}
}

//签章时显示图片在签名域中
function showSignatureImage(objData, signConstraint){
		var html = '';
		var imgData = objData.data;
		var imgId = ( (objData.type == 2) ? objData.id : objData.data ) ;
        html += '<div class="seal-box" style="border:none;height:100%;width:100%;margin:0;" name="'+ imgId +'">'+
			             '<div style="height:100%;width:100%;margin:0;padding:0;" class="seal-img imgBox"><img width="100%" height="100%"  src="'+ imgData +'" alt="签章" min-width="65" min-height="65" /></div>'+
			          '</div>' ;
	   $(signConstraint).addClass("signPost");
       $(signConstraint).empty().append(html);
       $(signConstraint).unbind('click');

       addSignature(signConstraint, objData.type, function(addjsonData){
        	addToSignature(addjsonData,function(){
    	         if( $(".signTxt").length == 0 ){  //所有签名已经完成了
    	        	$("#bottomSave").show();
 					$("#saveButton").unbind("click").click(function(){
 						$(".sign-mask").show();
 						transferDevice();
 					});
    	         }
        	}); 		
      	 });
}

//保存签章
function addSignature(obj, type, callback){
	 var _childThis = $(obj);
	 var currentPage = parseInt($(_childThis).parent().parent().parent().parent().attr('id'));
	 var ulX = $(_childThis).parent().parent().position().left;
	 var ulY = $(_childThis).parent().parent().position().top;   
	 var lrx = ulX + $(_childThis).width();
	 var lry = ulY + $(_childThis).height();
	 var sealData = $(_childThis).children().attr("name");
	 var signType = ( (gRules["rnAuth"] >= 0)? 1 : 2 );	//(1-证,2-无证)
	 var formName = $(_childThis).parent().attr("name");
	 var scale = 1.2;
     if($(_childThis).hasClass("signPost")){
    	 var jsonData = {};
    	 if( type == 1 ){ //输入
    			jsonData = {
                        "locked" :false,
                        "lrX" : lrx,
                        "lrY": lry,
                        "page": currentPage,
                        "scale": scale,
                        "sealData":sealData,
                        "type": signType,
                        "ulX": ulX,
                        "ulY": ulY,
                        "signFieldName": formName
                    };
    	 }else if( type == 2 ){	//上传
    			jsonData = {
                        "locked" :false,
                        "lrX" : lrx,
                        "lrY": lry,
                        "page": currentPage,
                        "scale": scale,
                        "sealId":sealData,
                        "type": signType,
                        "ulX": ulX,
                        "ulY": ulY,
                        "signFieldName": formName
                    };
    			}

    	 callback(jsonData);   
        }
}

function addToSignature(jsonData,callback){
	requestServer({
	    	requestURL: gfALLDATA( "docHref" )+'/'+gsignDOCID+'/pdf/sign',
	        requestType: 'post',
	        requestAsync: false,
	        requestData: JSON.stringify(jsonData),      
	        successCallback: function(data){
	            var statusCode = data.resultStatusCode;
	            var code = data.resultCode;
	            if(code == 0){
	                showInfo("保存中...");
	                callback();
	            }else{
	                showErrorInfo("添加失败");
	            }
	        },
	       errorCallback: function(jqXHR, textStatus, errorThrown){
	    	   //需要完善的容错处理
	    	   //容错处理：1.网络中断情况——提醒检查网络；2.会话连接超时——刷新重新连接；3.程序错误——不能抛给用户，程序员调试程序时使用，采用console.log输出
	            alert("出现异常："+textStatus);
	            console.log("code:"+jsonData.code+"jqXHR:"+jqXHR + "textStatus:"+textStatus + "errorThrown:"+errorThrown);
	        }
	  });
}

//author: duyanemi
//中转器-区分顺序和无序签
function transferDevice(){
	var wfId = $("#hidden-wfId").val();
	var sendCode = {
			"notifyself": {"type": "sign", "code": "2200", "docId": gsignDOCID, "wfId":wfId},
			"notifysponsor": {"type": "sign", "code": "2201", "docId": gsignDOCID, "wfId":wfId},
			"notifyCC": {"type": "sign", "code": "2202", "docId": gsignDOCID}
	}; 
	updateSignState( sendCode["notifyself"], function(){	 //通知自己
		updateSignState( sendCode["notifysponsor"], function( state, sequence, nextwfId ){	 //通知发起者
			if( state == 0 ){	 //整个签名已完成
				updateSignState( sendCode["notifyCC"], function(){
					dealFinishUI();
				});
				
			}else{  //签名还未完成
				if( sequence > 0 ){	 //顺序签
					var notifynext = {"type": "sign", "code": "2100", "docId": gsignDOCID, "wfId":nextwfId};
					updateSignState( notifynext, function(){	//通知下一个签名人
						dealFinishUI();
					});
					
				}else{	//无序签
					dealFinishUI();
				}
			}
		});
	});
}

//author: duyanemi
//完成签名更新
function updateSignState(jsonData, callback){
	requestServer({
        requestURL: gfALLDATA( "docHref" )+'/'+gsignDOCID+'/workflows/email/send',
        requestType: 'post',
        requestAsync: false,
        requestData: JSON.stringify(jsonData),
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                showInfo(desc);
                callback( data.state, data.sequence, data.nextWfId );
            }else{ 
            	showErrorInfo(desc);
            } 
            hideLoading();
        },
        errorCallback: function(jqXHR, textStatus, errorThrown){
          alert("出现异常："+textStatus);
          console.log("code:"+jsonData.code+"jqXHR:"+jqXHR + "textStatus:"+textStatus + "errorThrown:"+errorThrown);
        }
    });
}

//author: duyanemi
function dealFinishUI(){
	var uRole = gfALLDATA("uRole");
	$('.signFinishWarn').css('display','block');
    setTimeout(function(){
            $('.signFinishWarn').fadeOut('normal');
            if(uRole == "anonymous"){
            	window.location.href = gfALLDATA("baseHref") + "/wesign/login.html";	
            }else{
            	window.location.href = gfALLDATA("docHref") +'/index.html';	
            }
     }, 3000);
}



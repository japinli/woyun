var gFieldDOCID = getQueryString("doc-id");
var gBeseImg = gfALLDATA("baseHref") + '/static/system/images/pdficons/';
var gFieldSelfNum = 0;
var gStoreArray = [];
var gDealObj;
var gAuthed = false;
$(document).ready(function(){
	 getUserSend();
	 getStoreSeal(function(result, sealList){
		 if(result == "true"){
			 gStoreArray = sealList;
			 showStoreSeal( sealList );
		 }else{
			 getSealOccup();
		 }
	 });
});

//判断是本人签还是多人签
function getUserSend(){
	var sendList = [];
	var updateFlowId = [];
	var otherSignName = [];
	 requestServer({
	    	requestURL:gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/workflows?fields=doc_own_level,sequence,rev_as_recipient,rev_contact,username,rev_authed&filters=doc_serial_code="'+gFieldDOCID+'"',
		    requestType: 'get',
	        successCallback: function(data){
	           var statusCode = data.resultStatusCode;
	           var code = data.resultCode;
	           var resultData = data.workflowLinks;
	           if(code == 0 && resultData!=undefined){
	        	   var iDocOwnLevelTw = 0;
	        	   var selfFlag = 0 ;	//初始化为多人签
	        	   var revContactInitiator ;
	        	   var revContactSigner;
	               for(var i in resultData){
		                  var docOwnLevel = resultData[i].docOwnLevel;
	            		  var id = resultData[i].id;
	                      var revContact = resultData[i].revContact;
	                      var username = resultData[i].username;
	                      var revAsRecipient = resultData[i].revAsRecipient;
	                      var sequence = resultData[i].sequence;		// -1-未指定，0-无序，1~n当前工作顺序
	                      var revAuthed = resultData[i].revAuthed;

	                      if(docOwnLevel == 3 || (docOwnLevel ==2 && revAsRecipient == 1) ){ //统计CC个数
	                    	  sendList.push(id); //记录文档流id
	                      }
	                      if(docOwnLevel == 1){ //文档发起者
	                    	  revContactInitiator = revContact;

	                      }else if(docOwnLevel == 2){ //统计需要签名的用户数-只有自己
	                    	  iDocOwnLevelTw++;
	                    	  updateFlowId.push(id); //记录文档流id
	                    	  otherSignName.push(username);
	                    	  revContactSigner = revContact;
	                    	  gAuthed = revAuthed;
	                      }
	                } //end for

		            //本人签
	                if(iDocOwnLevelTw == 1 && (revContactInitiator == revContactSigner)){
		                selfFlag = 1;
		                sendSelfSign( updateFlowId[0] );

	                }
                    createNewItem(selfFlag,updateFlowId,otherSignName);

	           }else{	 //end two
	        	   showErrorInfo(data.resultDesc);
	        	   return false;
	           }
	        }
	    });
}

//本人签名-发送
function sendSelfSign( updateFwid ){
	$("#jstool_done_doc").unbind("click").click(function(){	//发送邮件
	  	if( gFieldSelfNum > 0 ){ //已签名了
			if( $(".signatureTxt").length == 0 ){ //没有空签名框
				var jsonData = {"type": "sign", "code": "2200", "docId": gFieldDOCID, "wfId": updateFwid};
	        	sendPersonFile(jsonData,function(){	//通知自己
	        		sendPersonFile({"type": "sign", "code": "2202", "docId": gFieldDOCID}, function(){	//通知CC
	        			window.location.href = gfALLDATA("docHref") +'/index.html';
	        		});
	        	});
			}else{
				tipDialog($(".signatureTxt").length + '个签名没有完成，请完成所有签名后再发送。');
	    		return false;
			}

		}else{
			tipDialog("你还未进行任何签名操作，请签名之后再发送文件!");
			return false;
		}
	});
}

//拖拽生成表单项
function createNewItem(selfFlag,updateFlowId,otherSignName){
	var _name = otherSignName[0];

    $(".draggableField").draggable({
        revert: "invalid",
        scroll: false,
        helper: "clone"
    });
    $('.preview-page').droppable({
        accept: ".draggableField",
        drop: function(event, ui){
            var _this = this;
            var thisTop = event.pageY - $(_this).offset().top - 40;
            var thisLeft = event.pageX - $(_this).offset().left - 40;
            var _topTArea = crossBorderH(80, thisTop, $('.preview-page').height()) +'px';
            var _left = crossBorderW(80, thisLeft, $('.preview-page').width()) +'px';
            var dragType = (ui.draggable).attr("id");
            var countNum = new Date().getTime();

            if(dragType == "dragSignature"){
                $(_this).append(
                       '<div id="draggedSignature'+ countNum +'" name="draggedSignature'+ countNum +'" class="propSetControl draggedSignature dragItemAdornment" name="flag_seal" style="width:80px;height:80px;line-height:80px;min-height:20px;top:'+ _topTArea +'; left:'+_left+';">'
		           			+'<p class="signatureTxt">双击签名</p>'
		           			+'<div class="propertySet">'
	                           +'<img class="propEdit propIcon" alt="单击以编辑属性" title="单击以编辑属性" src="'+gBeseImg+'cog.png">'
	                           +'<img class="propDelete propIcon" alt="删除此项" title="删除此项" src="'+gBeseImg+'delete.png">'
	                       +'</div>'
	                  +'</div>'
                );
            }

            $(".dragItemAdornment").draggable({
                containment: "parent",
                cursor: "move",
            }).resizable({
                containment: $(_this),
                autoHide: true,
                aspectRatio: 1,  //等比例缩放
                minWidth: 50,
                resize: function( event, ui ) {
	               	//动态设置文字的line-height,使其始终垂直居中
	               	$(event.target).children(".signatureTxt").css({"line-height": ui.size.height+"px"});
               }
            });

            //***点击签名弹出签名框
            $(".draggedSignature").off("dblclick").on("dblclick", function(e){
           		$( "#signaturePanel" ).dialog( "open" );
           		requestSignPerson( $(this), _name );
           	});

            //***点击签名框
            $(".propEdit").off("click").on("click", function(e){
           		$( "#signaturePanel" ).dialog( "open" );
           		requestSignPerson( $(this).parent().parent(), _name );
           	});

            //***删除签名域
            $(".propDelete").off("click").on("click", function(){
            	var sDeleteFlow = $(this).parent().parent().attr("name");
            	$(this).parent().parent().remove();
            });
        }
    });
}



//author: duyanmei
function requestSignPerson( signConstraint, otherSign ){

	gDealObj = signConstraint;
	//way1: 输入签名
	//签名域填写框
    var $obj = $('.inputSignature');
    var jsonData = {"obj": $obj, "userName": otherSign};
    fPutinSignature( jsonData, function( resultObj ){
			var signImage = resultObj.imgData;
			packSignData("input", signConstraint, signImage, function(signConstraint, addjsonData){
				reqPersonSign(signConstraint, addjsonData);
			});
            toggleDialog(false);
     });

    //way4 扫描
    if (window.WebSocket){
		fScanSignatrue(function( resultObj ){
			var signImage = resultObj.imgData;
			packSignData("input", gDealObj, signImage, function(gDealObj, addjsonData){
				reqPersonSign(gDealObj, addjsonData);
			});
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

    //way2: 选择已有签名
    if( gStoreArray.length == 0 ){
    	 getStoreSeal(function(result, sealList){
    		 if(result == "true"){
    			 gStoreArray = sealList;
    			 showStoreSeal( sealList , function(){
    				 $(".js-store-title").show();
    				 $("#insert-store-seal").show();
    				 selectStore(gDealObj);
    			 });
    		 }else{
    			 getSealOccup();
    		 }
    	 });

    }else{
    	showStoreSeal( gStoreArray, function(){
    		$(".js-store-title").show();
			 selectStore(gDealObj);
		 });
    }

  //way3: 上传签章
  //上传
  uploadSignature( {"upload": $("#js_sealUpload")}, function( result ){
	  $.get( result.seals[0].link.href, function( data ){
		  if( data.resultCode == 0 ){
			  var sHref = data.resultData.location;
			  var sealid = data.resultData.sealId;
			  packSignData("store", signConstraint, {"data": sHref, "id": sealid}, function(signConstraint, addjsonData){
	   				reqPersonSign(signConstraint, addjsonData);
	   		  });
			  toggleDialog(false);
		  }
	  });
  });

}

//author: duyanmei
function packSignData(type, $obj, sealData, callback){
	 var serialCode = getQueryString("doc-id");
	 var _childThis = $obj;
	 var currentPage = parseInt($(_childThis).parent().attr('id'));
	 var ulX = $(_childThis).position().left;
	 var ulY = $(_childThis).position().top;
	 var lrX = ulX + $(_childThis).width();
	 var lrY= ulY + $(_childThis).height();
	 var signType = ( (gAuthed == true)? 1 : 2 );	//签名类型(1-证书签名,2-无证书签名)
	 var scale = 1.2;
	 if( type == "input" ){
		 var jsonData = {
	             "locked" :false,
	             "lrX" : lrX,
	             "lrY": lrY,
	             "page": currentPage,
	             "scale": scale,
	             "sealData":sealData,
	             "type": signType,
	             "ulX": ulX,
	             "ulY": ulY
	          };
	 }else if( type == "store" ){
		 var jsonData = {
	             "locked" :false,
	             "lrX" : lrX,
	             "lrY": lrY,
	             "page": currentPage,
	             "scale": scale,
	             "sealId":sealData["id"],
	             "type": signType,
	             "ulX": ulX,
	             "ulY": ulY
	          };
	 }

    callback(_childThis, jsonData);
}

//author: duyanmei
//本人签-请求签名
function reqPersonSign($obj, jsonData){
	var href = $("#pageitem" + jsonData["page"]).attr("src");
	requestServer({
    	requestURL: gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/pdf/sign',
        requestType: 'post',
        requestAsync: false,
        requestData: JSON.stringify(jsonData),
        beforeCallback: function(){
        	showInfo("请稍等, 正在请求签名...");
        },
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            if(code == 0){
            	$($obj).remove();
            	showInfo("签名成功, 正在请求更新...");
            	//重新获取对应签名页的数据
            	fgGetDocImg({"startPage": jsonData["page"], "endPage": jsonData["page"], "loading":false}, function( imgArray ){
            		var lateImg = imgArray[0].href;
            		//重新加载签名页地址
            		$("#pageitem" + jsonData["page"]).attr("src", lateImg+ '&mark=' + new Date().getTime());
            		//计数本人签的签名个数
            		gFieldSelfNum += 1;
                 });

            }else{
                showErrorInfo("签名失败:" + data.resultDesc);
            }
        },
        errorCallback: function(jqXHR, textStatus, errorThrown){
            alert(textStatus);
            console.log("jqXHR:"+jqXHR + "textStatus:"+textStatus + "errorThrown:"+errorThrown);
        }
    });
}

//邮件发送
function sendPersonFile(jsonData,callback){
	requestServer({
        requestURL: gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/workflows/email/send',
        requestType: 'post',
        requestAsync: false,
        requestData: JSON.stringify(jsonData),
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            var state = data.state;
            if(code == 0){
                showInfo("请稍等，正在请求发送文件");
                callback();
            }else{
            	showErrorInfo(desc);
            }
            hideLoading();
        },
        errorCallback: function(jqXHR, textStatus, errorThrown){
        	alert(textStatus);
            console.log("'sendCode':"+jsonData.code+"'jqXHR':"+jqXHR + "'textStatus'"+textStatus + "'errorThrown':"+errorThrown);
        }
    });
}

//签名方式二：存储签名
function selectStore(signConstraint){
	var sHref;
    var sealid;
    $(".store-list").unbind("click").click(function(){
       	 if( !$(this).hasClass("tdActive") ){
   			 $(this).addClass("tdActive");
   			 $(this).siblings().removeClass("tdActive");
            }else{
                return false;
            }
   		sHref = $(this).find("img").attr("src");
   		sealid = $(this).find("img").attr("value");
   	});
   	$("#insert-store-seal").unbind("click").click(function(){
   		if(sHref && sealid){
   			packSignData("store", signConstraint, {"data": sHref, "id": sealid}, function(signConstraint, addjsonData){
   				reqPersonSign(signConstraint, addjsonData);
   			});
            toggleDialog(false);
   			console.log(sHref + sealid);
   		}else{
   			console.log("no sealid or imghref");
   		}
   	});
}
//author: duyanmei
function getSealOccup(){
	 $("#js-store-lists").html(
			 '<div class="textCenter mt30">'+
				 '<p class="ptb10">没有您的专有存储签名或印章记录!</p>'+
				 '<p>前往设置我的印章和签名</p>'+
				 '<div class="mt30"><a class="js-click-go btn-low-s mr25" style="color:#fff;" href="'+gfALLDATA("sealHref")+'/index.html'+'" target="_blank">离开，马上去设置</a>'+
				 '<span class="js-click-way btn-default-s">不了，用其它方式</span>'+
				 '</div>'+
			 '</div>'
	 );
	 $(".js-store-title").hide();
	 $("#insert-store-seal").hide();
	 $(".js-click-way").unbind("click").click(function(){
		 $("#signaturePut-link").click();
	 });
	 $(".js-click-go").unbind("click").click(function(){
		 toggleDialog(false);
	 });
}

//提示
function tipDialog(text){
	$(".confirmDialog").html('<div class="textCenter ptb20"><i class="warning-ft icon-tip2 fs32"></i></div>'
													    +'<div class="confirmContent textCenter"><p>'+text+'</p></div>'
													    +'<div class="confirmBtn textCenter borderTop">'
													        +'<span class="confirmYes btn-primary-m">我知道了</span>'
													    +'</div>'
											).dialog({autoOpen: true});

    $('.confirmYes').unbind('click').click(function(){ //确认
        $(".ui-dialog-titlebar-close").click();
    });
}


//author: duyanmei
//拖拽表单项越界处理
function crossBorderW(objW, leftF, containerW){ //左右
   if(leftF < 0){
       return 0;
   }else{
       return (containerW - objW) > leftF ? leftF : (containerW - objW);
   }
}
function crossBorderH(objH, topF, containerH){  //上下
    if(topF<0){
        return 0;
    }else{
        return (containerH - objH) > topF ? topF : (containerH - objH);
    }
}

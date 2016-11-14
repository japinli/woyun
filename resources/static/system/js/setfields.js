var gFieldDOCID = getQueryString("doc-id");
var gBeseImg = gfALLDATA("baseHref") + '/static/system/images/pdficons/';
$(document).ready(function(){
	 getUserSend();
});

//判断是本人签还是多人签
function getUserSend(){
	var sendList = [];
	var updateFlowId = [];
	var otherSignName = [];
	 requestServer({
	    	requestURL:gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/workflows?fields=doc_own_level,sequence,rev_as_recipient,rev_contact,username&filters=doc_serial_code="'+gFieldDOCID+'"',
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
	        	   var orderfwid;	 //顺序签——第一个签名者文档流id
	        	   var flagOrder = false;
	        	   var orderNum;
	               for(var i in resultData){
		                  var docOwnLevel = resultData[i].docOwnLevel;
	            		  var id = resultData[i].id;
	                      var revContact = resultData[i].revContact;
	                      var username = resultData[i].username;
	                      var revAsRecipient = resultData[i].revAsRecipient;
	                      var sequence = resultData[i].sequence;		// -1-未指定，0-无序，1~n当前工作顺序

	                      if(docOwnLevel == 3 || (docOwnLevel ==2 && revAsRecipient == 1) ){ //统计CC个数
	                    	  sendList.push(id); //记录文档流id
	                      }
	                      if(docOwnLevel == 1){ //文档发起者
	                    	  revContactInitiator = revContact;

	                      }else if(docOwnLevel == 2){ //统计需要签名的用户数
	                    	  iDocOwnLevelTw++;
	                    	  updateFlowId.push(id); //记录文档流id
	                    	  otherSignName.push(username);
	                    	  revContactSigner = revContact;
	                    	  if( sequence >= 1 && flagOrder == false ){	//判断是顺序签名且要得到第一个签名人的序号
	                    		  flagOrder = true;
	                    		  orderNum = sequence;
	                    		  orderfwid = id ;	//顺序第一个签名人的文档流id
	                    	  }else if( sequence >= 1 && flagOrder == true ){	//排序，筛选出第一个签名人的序号
	                    		  if( orderNum > sequence ){
	                    			  orderNum = sequence;
	                    			  orderfwid = id ;
	                    		  }else{
	                    			  continue;
	                    		  }
	                    	  }
	                      }
	                } //end for

		            //本人签
	                if(iDocOwnLevelTw == 1 && (revContactInitiator == revContactSigner)){
		                selfFlag = 1;
		                //sendSelfSign( updateFlowId[0] );

	                }else{  //多人签
	                    selfFlag = 0;
	                    sendManySign( {"updateFlowId": updateFlowId, "otherSignName": otherSignName, "flagOrder": flagOrder, "orderfwid": orderfwid, "sendList": sendList} );
	                }

                    createNewItem(selfFlag,updateFlowId,otherSignName);

	           }else{	 //end two
	        	   showErrorInfo(data.resultDesc);
	        	   return false;
	           }
	        }
	    });
}

//多人签名-发送
function sendManySign( objData ){
	var updateFlowId = objData.updateFlowId;
	var name = objData.otherSignName;
	var flagOrder = objData.flagOrder;
	var orderfwid = objData.orderfwid;
	var sendList = objData.sendList;
	$("#jstool_done_doc").unbind("click").click(function(){	//发送邮件
		addForm( {"id": updateFlowId, "name": name}, function( addData ){
			addToServer(addData, function(){	 //保存签名域，回调为发送邮件
				var sendCode = {
	        			"orderAll": {"type": "sign", "code": "2101", "docId": gFieldDOCID},
	        			"orderFirst": {"type": "sign", "code": "2100", "docId": gFieldDOCID, "wfId":orderfwid},
	        			"orderCC": {"type": "sign", "code": "2102", "docId": gFieldDOCID},
	        			"inorderAll": {"type": "sign", "code": "2100", "docId": gFieldDOCID}
	        	};
				if(flagOrder){	//顺序签
					sendPersonFile(sendCode["orderAll"], function(){	//开始通知所有签名人
							sendPersonFile(sendCode["orderFirst"], function(){	//通知第一个签名人签名
	    						if( sendList.length > 0 ){	//如果有收件人，通知收件人
	    							sendPersonFile(sendCode["orderCC"], function(){
	    								window.location.href = gfALLDATA("docHref") +'/index.html';
	    							});

	    						}else{
	    							window.location.href = gfALLDATA("docHref") +'/index.html';
	    						}
	    					});
	    				});

				}else{	//无序签
					sendPersonFile(sendCode["inorderAll"], function(){	//开始通知所有签名人
						if( sendList.length > 0 ){	//如果有收件人，通知收件人
							sendPersonFile(sendCode["orderCC"], function(){
								window.location.href = gfALLDATA("docHref") +'/index.html';
							});

						}else{
							window.location.href = gfALLDATA("docHref") +'/index.html';
						}
					});
				}
			});

	    });
	});
}

//更新文档流-用于设置签名域中的增、删、改
//签名域设置数据更新约定：
//（1）增加B的一条签名域，需要将B对应文档流下的所有签名域发到后台更新
//（2）删除B的一条签名域，需要将B删除后的对应文档流下的所有签名域发到后台更新
//（3）修改B的一条已有的签名域为A的，需要将A下的所有签名域发到后台
function updateFlow(updateFlowId,putdata){
	 requestServer({
		    requestURL:gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/workflows/'+updateFlowId,
	        requestType: 'put',
	        dataType:"json",
	        requestData: JSON.stringify(putdata),
	        successCallback: function(data){
	            var statusCode = data.resultStatusCode;
	            var code = data.resultCode;
	            if(code == 0){
	                showInfo("保存中...");
	            }else{
	                showErrorInfo("保存失败");
	            }
	        }
	    });
 }

//last edit: duyanmei
//新增表单项
//特别说明：needObj = {"id":[], "name":[]}; 需要签名的人的 "id" 和 "姓名"
//注： needObj的数据结构构造便于后面判断取值，id[i]对应name[i]
function addForm(needObj, callback){
        var FORMSLIST = [];
        var setFlowId = []; //实际设置的签名人
        var needL = needObj["id"].length; //需要签名的人数
        $('.dragItemAdornment').each(function(){
            var _childThis = this;
            var currentPage = parseInt($(_childThis).parent().attr('id'));
            var addItemId = $(_childThis).attr("id");
            var flowid = $(_childThis).attr("name");
            var ulX = $(_childThis).position().left;
            var ulY = $(_childThis).position().top;
            var lrX = ulX + $(_childThis).width();
            var lrY = ulY + $(_childThis).height();
            var formPosition = {
                "ulX": ulX,
                "ulY": ulY,
                "lrX": lrX,
                "lrY": lrY
            }
            if(addItemId.indexOf("draggedSignature") != '-1'){  //判断当前添加的是签名域
            	var signContent = $(_childThis).val();
                var jsonData = {
                    "type": 7,
                    "name": addItemId,
                    "page": currentPage,
                    "scale": 1.2,
                    "required": false,
                    "readOnly": false,
                    "positions": formPosition
                }

                FORMSLIST.push(jsonData);
                setFlowId.push(flowid);

            }else{
	            var textContent = $(_childThis).val();
	            var multiLine = false;
	            addItemName.indexOf("draggedText") != '-1' ? multiLine = false : multiLine = true;
	            var jsonData = {
	                "type": 4,
	                "desc": "",
	                "name": addItemName,
	                "page": currentPage,
	                "scale": 1.2,
	                "readOnly": false,
	                "password": "",
	                "required": false,
	                "multiLine": multiLine,
	                "data": textContent,
	                "positions": formPosition
	            }
	            FORMSLIST.push(jsonData);
             }
        });

    if(FORMSLIST.length != 0){
    	//剔除数组中的重复项
    	var setNum = setFlowId.distinct();
    	if(setNum.length != needL){
    		var text = '';
    		var noset = [];
    		for(var k=0; k<needL; k++){ //获取未设置的签名人名字
    			var flag = false; //记号是否设置的id
    			for(var n=0; n<setNum.length; n++){
    				 if( needObj["id"][k] == setNum[n] ){
    					 flag = true;
    					 break; //找到已设置的，立即结束
    				 }else{
    					 continue;
    				 }
    			}
    			if(flag==false){ //找到没有设置签名域的签名人的名字
    				noset.push( needObj["name"][k] );
    			}else{
    				continue;
    			}
    		}
    		for(var j=0; j<noset.length; j++){
    			text += ( ( j < noset.length-1 ) ? ( noset[j] + '、' ) : noset[j] );
    		}
    		tipDialog( '还有 " '+text+' " '+(needL - setNum.length)+'个签名人未设置签名域，请设置完再发送文件!' );
    		return false;

    	}else{
    		callback({"serialCode": gFieldDOCID, "formsList": FORMSLIST});
    	}

    }else{
    	tipDialog("文档中还没有设置任何签名项，请设置后再发送文件!");
    	return false;
    }
}

//发送新增表单项数据
function addToServer(addData,callback){
    requestServer({
    	requestURL: gfALLDATA( "docHref" )+'/'+gFieldDOCID+'/pdf/forms',
        requestType: 'post',
        requestAsync: false,
        requestData: JSON.stringify(addData),
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            if(code == 0){
                showInfo("数据保存成功");
                callback();
            }else{
                showErrorInfo("数据保存失败，请重新发送");
            }
        }
    });
}

//last edit by duyanmei 2016.7.10
//表单项交互
function createNewItem(selfFlag,updateFlowId,otherSignName){
	//***记忆用户设置
	var keepui = {
							"signname":{"width": 100, "height": 100},
							"name": otherSignName[0],
							"id": updateFlowId[0]
			  			 };
	var selectLists = '';
	for(var i=0;i<otherSignName.length;i++){
		selectLists += '<option name="'+otherSignName[i]+'" value="'+updateFlowId[i]+'">'+otherSignName[i]+'</option>';
    }

    $(".draggableField").draggable({
        revert: "invalid",
        scroll: false,
        helper: "clone"
    });

    $('.preview-page').droppable({
        accept: ".draggableField",
        drop: function(event, ui){
            var _this = this;
            var pageW = $('.preview-page').width();
            var pageH = $('.preview-page').height();
            var dragType = (ui.draggable).attr("id");
            var countNum = new Date().getTime();
            var putDataList = [];

            if(dragType == "dragSignature"){
            	 var createW = keepui.signname.width;
            	 var createH = keepui.signname.height;
            	 var thisTop = event.pageY - $(_this).offset().top - (createW/2); //(keepui.signname.height/2)使鼠标落下正好在签名域中间位置
                 var thisLeft = event.pageX - $(_this).offset().left - (createH/2); //(keepui.signname.width/2)使鼠标落下正好在签名域中间位置
                 var realTop = crossBorderH(createH, thisTop, pageH);
                 var realLeft = crossBorderW(createW, thisLeft, pageW);
                 var boxLeft = (createW - 160)/2 + 'px';
                 var boxStyle = (realTop < 120) ? ('bottom : -130px; top : auto; left: '+boxLeft) : ('top : -130px; bottom : auto; left: '+boxLeft);
                $(_this).append(
                		'<div id="draggedSignature'+ countNum +'" name="'+ keepui["id"] +'" class="propSetControl draggedSignature dragItemAdornment" name="flag_seal" style="width:'+createW+'px;height:'+createH+'px;line-height:'+createH+'px;top:'+realTop+'px; left:'+realLeft+'px;">'
                			+'<p class="signatureTxt">' +keepui["name"]+ '</p>'
                			+'<div class="propertySet">'
	                            +'<img class="propEdit propIcon" alt="单击以编辑属性" title="单击以编辑属性" src="'+gBeseImg+'cog.png">'
	                            +'<img class="propDelete propIcon" alt="删除此项" title="删除此项" src="'+gBeseImg+'delete.png">'
	                        +'</div>'
	                        + '<div class="propertyBox" style="'+boxStyle+'">'
	                        	+'<img class="hideSet" src="'+gBeseImg+'reduce.png" />'
		       			        +'<p>指定签名人:</p>'
		       		            +'<select class="msgSelect">'+selectLists+'</select>'
		       		         +'</div>'
                       +'</div>'
                );
            }

            //***拖拽和缩放事件绑定
            $(".dragItemAdornment").draggable({  //签名域拖拽事件绑定
                containment: "parent",
                cursor: "move",
                stop: function(event, ui){
                	  var _left = ( $(event.target)[0].offsetWidth - $(event.target).children(".propertyBox")[0].offsetWidth )/2 + "px";
	             	  if(ui.position.top < 120){
	             		  $(event.target).find('.propertyBox').css({"bottom":'-130px',"top":"auto","left": _left});
	             	   }else{
	             		   $(event.target).find('.propertyBox').css({"top":'-130px',"bottom":"auto","left": _left});
	             	   }
                }
            }).resizable({ //签名域缩放事件绑定
                containment: $(_this),
                autoHide: true,
                aspectRatio: 1, //等比例缩放
                minWidth: 50,
                resize: function( event, ui ) {
                	 var _left = ( $(event.target)[0].offsetWidth - $(event.target).children(".propertyBox")[0].offsetWidth )/2 + "px";
                	//动态设置文字的line-height,使其始终垂直居中
                	$(event.target).children(".signatureTxt").css({"line-height": ui.size.height+"px"});
                	$(event.target).find('.propertyBox').css({"left": _left});
                	//同步签名域大小最新设置
                	keepui.signname.width = ui.size.width;
                	keepui.signname.height = ui.size.height;
                }
            });

            //***每个签名域初始化时自己设置框显示同时别的签名设置框隐藏
            $('#draggedSignature'+countNum).siblings().removeClass("propSetControl propSetHover").children(".propertyBox").css({"visibility":"hidden"});

            //***实现点击签名域之外的地方框隐藏
            $("#viewerContainer").on("mousedown",function(){
            	$(".dragItemAdornment").removeClass("propSetControl propSetHover").children(".propertyBox").css({"visibility":"hidden"});
        	 });

            //***动态控制设置及弹框显示
            $(".dragItemAdornment").on("mousedown", function(event){
            	var _first = this;
            	var $box = $(_first).children(".propertyBox");
            	if( !$box.hasClass("hasHide") ){
            		var _left = ( $(_first)[0].offsetWidth - $box[0].offsetWidth )/2 + "px";
            		$box.css({"left": _left, "visibility":"visible"}).end().siblings().children(".propertyBox").css({"visibility":"hidden"});
            	}
            	if ( !$(_first).hasClass("propSetControl") ){
            		$(_first).addClass("propSetControl").siblings().removeClass("propSetControl");
            	}
            	event.stopPropagation(); //阻止冒泡事件

             }).hover(function(){
            	var _in = this;
            	if ( !$(_in).hasClass("propSetHover") ){
            		$(_in).addClass("propSetHover");
            	}else{ return true; }

            }, function(){
            	var _out = this;
            	$(_out).removeClass("propSetHover");
            });

            //***隐藏弹出框
            $(".hideSet").off("click").on("click", function(event){
            	$(this).parent().css({"visibility":"hidden"});
            	$(".propertyBox").addClass("hasHide");
            	event.stopPropagation();
            });

            //***显示弹出框
            $(".propEdit").off("click").on("click", function(event){
            	var _edit = this;
            	var $box = $(_edit).parent().siblings(".propertyBox");
            	var _left = ( $(_edit).parent().parent()[0].offsetWidth - $box[0].offsetWidth )/2 + "px";

            	$(".propertyBox").removeClass("hasHide");
            	//特别说明：采用visibility而不使用display的原因：采用display造成bug——第一次点击 $box[0].offsetWidth == 0，UI框不居中显示
        		$box.css({"left": _left, "visibility":"visible"}).end().parent().parent().siblings().children(".propertyBox").css({"visibility":"hidden"});

             	event.stopPropagation(); //阻止冒泡事件
             });

            //***删除事件绑定
            $(".propDelete").off("click").on("click", function(event){
            	var deleteid = this;
            	var putDataList = [];
            	var sDeleteFlow = $(deleteid).parent().parent().attr("name");
            	$(deleteid).parent().parent().remove();
                $('div[name = ' + sDeleteFlow +']').each(function(index, element){
             		putDataList.push($(element).attr("id"));
             	});
               var putData = {
                              "type": 2,
                              "newData": putDataList.join(",")
                          };
                updateFlow(sDeleteFlow,putData); //通知后台更新

                event.stopPropagation();
            });

            //***签名域分配事件绑定
          	$(".msgSelect").off("change").on("change",function(){
          		 var option = this;
                 if($(option).val()=="meNow"){
                     $( "#signaturePanel" ).dialog( "open" );

                 }else{
                	var flowId = $(option).val();
                	var sName = $(option).find("option:checked").attr("name");
                	var $box = $(option).parent().parent();
                	var putDataList = [];
                	$box.children(".signatureTxt").text(sName);
                	$box.attr("name",flowId);
                	//同步名字设置
                	keepui["name"] = sName;
                	keepui["id"] = flowId;
                	$('div[name = ' + flowId +']').each(function(index, element){
                 		putDataList.push( $(element).attr("id") );
                 	});
                  	if(putDataList.length > 0){
                  		var putData = {
                                  "type": 2,
                                  "newData": putDataList.join(",")
                              };
                  		updateFlow(flowId,putData);
                  	}
                 }
               });

        	if(selfFlag == 0){	//多人签的情况
            	$('#draggedSignature'+countNum).attr("name",keepui["id"]).find("option[value="+keepui["id"]+"]").attr("selected",true);
            	$('div[name = ' + keepui["id"] +']').each(function(){
            		putDataList.push($(this).attr("id"));

             	});
            	//初始化添加固定签名域
                updateFlow( keepui["id"], {"type": 2, "newData": putDataList.join(",")} );
            }
        }
    });
}

//following
//author: duyanmei
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

//删除数组重复项
//先sort()实现排序，在剔除重复元素
Array.prototype.distinct = function(){
    var self = this;
    var oldArr = self.concat().sort();
    var newArr = new Array(oldArr[0]);
    var temp = oldArr[0];
    for(var i=1; i<oldArr.length; i++){
    	if( temp < oldArr[i] ){
    		newArr.push( oldArr[i] );
    		temp = oldArr[i];
    	}else{
    		continue;
    	}
    }
    return newArr;
};

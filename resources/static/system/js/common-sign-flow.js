var upfileNum = 0;

$(document).ready(function(){
    //选择签名类型
    comboRest(function(){
        signWithCert();
    });
    //添加联系人UI
    changeAddUI();
    //初始化接收人信息
    initContactData();
    envelopeOperation();

    //删除文档
    $(document).delegate('.file-node-remove','click',function(){
        var _this = this;
        if($(_this).parent().next().hasClass("file-result-success")){
            deleteFile();
        }else{
            $('.upload-files-container').html("");
        }
    });

		//验证信封标题
    $('.docTitle').bind('blur',function(){
    	if($(this).val() == ""){
    		$(this).css('border','1px solid #fa8130');
    		showErrorMsg($('.msg-warning'),"*必填项");
    	}else{
    		 $(this).css('border','1px solid #e4e8ee');
    	}
    }).bind('keyup',function(){
			var title=$(this).val();
			if(title.length>50){
				$(this).val(title.substring(0,50));
    		showErrorMsg($('.msg-warning'),"*最多50个字符");
			}
		});
		$('.msgContentTxt').bind('keyup',function(){
			var title=$(this).val();
			if(title.length>100){
				$(this).val(title.substring(0,100));
    		showErrorMsg($('.content-warning'),"*最多100个字符");
			}
		});

    $(".signBar-toolTip").jBox("Tooltip",{
       trigger: 'click',
         closeOnClick:"body",
         animation:'zoomIn',
           content: $("#bar-toolTip-box"),
     });

});

//添加联系人UI
function changeAddUI(){
    $('.addRecipient').mouseover(function(){
        $('.addRecipientIcon').css("color","#a9a9a9");
    }).mouseout(function(){
        $('.addRecipientIcon').css("color","#095db1");
    });
}

//添加接收人信息(多人签)
function addMultiRecipient(){
    $('.addRecipient').unbind('click').click(function(){
        if($('.orderTrig').attr('name') == "nope"){  //未选择顺序发送
            $('.recipientInfo:last').after(
            '<div class="recipientInfo">'+
                '<span class="sortHandle iconImg"></span>'+' '+
                '<input type="text" class="recipientName" placeholder="姓名（必填）">'+' '+
                '<input type="text" class="recipientEmail" placeholder="邮箱（必填）">'+' '+
                '<select class="recipientType">'+
                    '<option value="0">签名</option>'+
                    '<option value="1">接收文档</option>'+
                    '<option value="2">签名并接收文档</option>'+
                '</select>'+' '+
                '<i class="icon-highlight-remove deleteRecipient"></i>'+
                '<span class="pos-emailWarn"></span>'+
            '</div>'
        );
        }else{   //选择了顺序发送
            $('.recipientInfo:last').after(
            '<div class="recipientInfo">'+
                '<span class="sortHandle iconImg" style="display:inline-block;"></span>'+
                '<input class="orderDisplay" type="text" readonly="readonly">'+' '+
                '<input type="text" class="recipientName" placeholder="姓名（必填）" style="width:267px;">'+' '+
                '<input type="text" class="recipientEmail" placeholder="邮箱（必填）" style="width:267px;">'+' '+
                '<select class="recipientType" style="width:217px;">'+
                    '<option value="0">签名</option>'+
                    '<option value="1">接收文档</option>'+
                    '<option value="2">签名并接收文档</option>'+
                '</select>'+' '+
                '<i class="icon-highlight-remove deleteRecipient"></i>'+
                '<span class="pos-emailWarn"></span>'+
            '</div>'
        );
        //$('.sortHandle').css("display","inline-block");
        getSendOrder();
        }
        sequence();
    });
}

//删除接收人信息
function removeRecipient(){
    $(document).delegate('.deleteRecipient','click',function(){
        if($('.deleteRecipient').length > 1){
            $(this).parent().remove();
        }
        getSendOrder();
    });
}

//排序
function sequence(){
    $('.recipientInfoArea').sortable({
        handle: $('.sortHandle'),
        axis: "y",
        cursor: "move",
        stop: function(){
                  getSendOrder();
              }
    });
}

//获取发送顺序
function getSendOrder(){
    $('.orderDisplay').each(function(i){
        var _this = this;
        var sendOrder = i + 1;
        $(_this).attr('value',sendOrder);
    });
}

//顺序发送处理
function sendInOrder(){
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
}

//套餐剩余情况
function comboRest(callback){
    requestServer({
	    requestURL:gfALLDATA("payHref")+'/combos/remain',
	    requestType: 'GET',
	    successCallback: function(data){
	        var code = data.resultCode;
	        var withCertRemain = data.withCertRemain;
	        var noCertRemain = data.noCertRemain;
	        if(code === 0){
	            $('.userIdentify').attr({"data-withcert":withCertRemain, "data-without":noCertRemain});
	            callback();
	        }else{
	            return;
	        }
	    }
    });
}

//有无证书签名选择
function signWithCert(){
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
    getEnvelopeData(null,function(){
      console.error("获取信息失败");
    });
}

//获取发起人信息
function getAddresserInfo(){
    requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'get',
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            var resultData = data.resultData;
            if(code === 0){
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
}

/*
陈曦源
2016-9-20
内容： 获取文档信息增加
			上传文档修改
*/
//获取文档信息


 var docsView={
 	views:[],
	docsContainer:null,
	fullDocs:false,
 	init:function(){
 		var that=this;
 		var docsDate=docsModle.getdocsData();
 		this.docsContainer=document.getElementById('js-upload-container')||
		document.getElementById('upload-head');
		var docsContainer=this.docsContainer;
 		docsContainer.innerHTML="";
 		docsDate.forEach(function(docData,index){
			var id=new Date().getTime();
 			var newdiv=document.createElement('div');
 			var smallImg = gfALLDATA("baseHref")+docData.imageLocation;
 			newdiv.className="choose-out file-box upload-file-"+id;
 			newdiv.innerHTML=	'	<div class="fade fileNone">'+
 								        '   <div class="fade-up" >'+
 												'			<div class="file-eye" title="预览"><i class="icon-eye eye"></i></div>'+
 												'		</div>'+
 												'		<div class="fade-down" data-docid="'+docData.docId+'">'+
 												'			<div class="file-del" title="删除"><i class="icon-trash del"></i></div>'+
 												'		</div>'+
 								        ' </div>'+
 								        ' <div class="choose-in">'+
 												'		<img src="'+smallImg+'" alt="'+docData.name+'" style="width:100%;height:100%">'+
 												'	</div>'+
 								    		'	<div class="choose-font">'+
 								    		'		<p class="file-title-font" >'+docData.name+'</p>'+
 								    		'	</div>';
      $(newdiv).children(".choose-in").find("img").bind("error",function(){
        $(this).unbind("error");
        this.style="margin:10px 20px";
        this.src=gfALLDATA("baseHref")+"/static/system/images/doc-upload.png";
      });
 			docsContainer.appendChild(newdiv);
 			that.views.push(newdiv);
			if(that.views.length==1){
				$('.docTitle').attr('value',docData.name.split("\.")[0]);
			}
 		});
 		//绑定事件
 		$('.choose-out').mouseover(function(){
 			$(this).children("div:first-child").removeClass("fileNone");
 			$(this).children("div:first-child").addClass("fileBlock");
 		}).mouseout(function(){
 			$(this).children("div:first-child").removeClass("fileBlock");
 			$(this).children("div:first-child").addClass("fileNone");
 		});
 		//预览
 		$('.fade-up').click(function(){
				previewDoc($(this).next().attr("data-docid"),$(this).parent().parent().find('.file-title-font').text());
 		});
 		//删除
 		$('.fade-down').click(function(){
 			docsController.deDoc($(this).attr('data-docid'));
 		});

 		this.addDocUperView();
 	},
 	addDocUperView:function(){
 		var that=this;
 		if(this.views.length>4) {
			this.fullDocs=true;
			return;
		}
 		var docsContainer=this.docsContainer;
 		var newdiv=document.createElement('div');
		var id=new Date().getTime();
 		newdiv.className="choose-out file-box upload-file-"+id;
 		newdiv.innerHTML=	'<div class="upload-file-in upload-bac fileBlock">'+
 							        '  <span class="localfile">'+
 											'	<i class="icon-display icon1"></i>'+
 											'	<span>本地文件</span><input id="upload-files-'+id+'" class="uploadFilesBtn" tabindex="-1" type="file" name="file"></span>'+
 											'	<!-- <button class="cloudfile"><i class="icon-cloud-queue icon2"></i><span>云端文件</span></button> -->'+
 											'</div>'+
 											'<div class="fade">'+
 											'	<div class="fade-up">'+
 											'		<div class="file-eye" title="预览"><i class="icon-eye eye"></i></div>'+
 											'	</div>'+
 											'	<div class="fade-down">'+
 											'			<div class="file-del" title="删除"><i class="icon-trash del"></i></div>'+
 											'	</div>'+
 											'</div>'+
 											'<div class="choose-in hidden">'+
 											'	<div class="upload-files-container"></div>'+
 											'</div>'+
 											'<div class="choose-font">'+
 											'	<p class="file-title-font"></p>'+
 											'</div>';


 		docsContainer.appendChild(newdiv);
 		//监听提交事件
 		$('#upload-files-'+id).fileupload({
 				url:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1]+"/documents/form",
 				dataType: 'json',
         type: 'post',
         autoUpload: true,
         singleFileUploads: false,
         acceptFileTypes: /(\.|\/)(pdf|doc|docx|xls|xlsx|ppt|pptx|wps|et|dps|jpeg|jpg|jpe|gif|png)$/i ,
         maxFileSize: 2097152,   //1024*1024*2(M)
         formAcceptCharset: 'UTF-8',
     }).on('fileuploadadd', function(e, data){
 			console.log("添加文件",data);
         $.each(data.files, function(index, file){
             var aFileSplit = file.name.split(".");
             aFileSplit.splice(-1);
             var fileName = aFileSplit.join(".");
             console.log("添加文档" + newdiv.className);

             $(newdiv).children("div:first-child").addClass("fileNone").next().next().removeClass("hidden")
 						.next().children("p:first-child").html(file.name);
 						if(that.views.length==1){
							$('.docTitle').attr('value',fileName.split("\.")[0]);
						}
 						var $fileContent = $(newdiv).children('.choose-in').children('.upload-files-container');
             $fileContent.html("");
             data.context = $fileContent;

 						var node = $('<div class="file-node-container" />')
                       .append($('<div style="width:0%;" class="progress-bar"></div>'));

             $(newdiv).children('.choose-in').children("div:first-child").show();
             $(node).appendTo(data.context);
         });
 			}).on('fileuploadprocessalways', function(e, data){
 	        /*var index = data.index;*/
 	        var file = data.files[0];
 	        var node = $(data.context.children()[0]);
 	        if(file.error){
 	            node.find('.choose-in').remove().end().append($('<p class ="file-result-error" style="font-size:1.2rem;"></p>').html('<p style="font-size:1.6rem;">上传失败<p>'+file.error));
 	            node.append('<div class="file-redo"><i class="icon-spinner11 spinner"></i></div>');
 	            node.parent().parent().css('border','none');
 	            node.children("div:first-child").next().next().bind('click',function(){
 	            	node.parent().parent().css('border','1px solid #e9e9e9');
 	            	node.parent().parent().next().children("p:first-child").html("");
 	            	node.parent().parent().prev().prev().removeClass("fileNone");
 	            	node.parent().parent().addClass("hidden");
 	            	node.parent().parent().next().children("div:first-child").html("");
 	            	node.remove();
 	            });
 	            return false;
 	        }
 	    }).on('fileuploadprogressall', function(e, data){
 	         var progress = parseInt(data.loaded / data.total * 100, 10);
 	         $('.progress-bar').css('width',progress + '%');
 	    }).on('fileuploaddone', function(e, data){
 				var statusCode = {
						"100100100": "禁止访问",
 						"100130000": "上传成功",
 						"100130100": "上传失败，文件为空",
 						"100130101": "上传失败，文件名长度不符合要求",
 						"100130102": "上传失败，文件尺寸不符合要求",
 						"100130103": "上传失败，文件类型不符合要求",
 						"100130104": "上传失败，达到已有文件数量限制，不允许再上传",
 						"100130105": "上传失败，未预测到的错误或异常，请重新操作或联系我们",
 						"100130106": "上传失败，存在失败的上传"
 				};
 	    	console.log("上传完成");
 	    	$(newdiv).children('.choose-in').html("");

 			  if(data.result.resultCode === 0){
 	        	console.log("上传成功");

 						var docdata=data.result.resultData.documents[0];
 						$(newdiv).children("div:first-child").remove();
 						docsModle.addDate(docdata);

 						$(newdiv).children(".choose-in").html('<img src="'+docdata.imageLocation+'" style="width:100%;height:100%">');
            $(newdiv).children(".choose-in").find("img").bind("error",function(){
              $(this).unbind("error");
              this.style="margin:10px 20px";
              this.src=gfALLDATA("baseHref")+"/static/system/images/doc-upload.png";
            });
 						//绑定事件
 						$(newdiv).mouseover(function(){
 							$(this).children("div:first-child").removeClass("fileNone");
 							$(this).children("div:first-child").addClass("fileBlock");
 						}).mouseout(function(){
 							$(this).children("div:first-child").removeClass("fileBlock");
 							$(this).children("div:first-child").addClass("fileNone");
 						});
 						//预览
 						$(newdiv).children(".fade").addClass("fileNone").children('.fade-up').click(function(){
							previewDoc($(this).next().attr("data-docid"),$(this).parent().parent().find('.file-title-font').text());
 						});
 						//删除
 						$(newdiv).children(".fade").children('.fade-down').attr("data-docid",docdata.docId).click(function(){
 							docsController.deDoc($(this).attr('data-docid'));
 						});

 						that.addDocUperView();
 	        }else{
 	        	console.log("上传失败");
 							$(newdiv).children('.choose-in').append( $('<p class="file-result-error" style="font-size:1.2rem;"></p>').
							html( '<p style="font-size:1.6rem;">上传失败<p>'+ statusCode[data.result.resultStatusCode]))
 							.append($('<div class="file-redo"><i class="icon-spinner11 spinner"></i></div>').bind('click',function(){
								$(newdiv).children("div:first-child").removeClass("fileNone");
								$(newdiv).children('.choose-in').html('<div class="upload-files-container"></div>').addClass('hidden');
								$(newdiv).children('.choose-font').html("");
 	          	}));
 	        }
 	    });
 		that.views.push(newdiv);
 	},
 	update:function(){

 	},
 	delview:function(index){
 		this.docsContainer.removeChild(this.views[index]);
 		this.views.splice(index,1);
		if(this.fullDocs){
			this.fullDocs=false;
			this.addDocUperView();
		}
 	}
 };

 var docsController={
 	init:function(){
 		requestServer({
 			requestURL:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1]+"/documents?sortings=+time",
 			requestType:"GET",
 			//requestData:JSON.stringify({"docIds": [{"docId":this.parentElement.parentElement.parentElement.parentElement.lastElementChild.lastElementChild.attributes["data-docid"].nodeValue }]}),
 			beforeCallback:function(){
 				console.log("send!");
 			},
 			successCallback:function(data){
				console.log("docs",data);
 				if(data.resultCode === 0){
 						docsModle.setInitDate(data.resultData);
 				}else{
 					console.error(data.resultDesc);
 				}
 			}
 		});
 	},
 	deDoc:function(docid){
 		requestServer({
 			requestURL:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1]+"/documents",
 			requestType:"DELETE",
 			requestData:JSON.stringify({"docIds": [{"docId":docid}]}),
 			successCallback:function(data){
 				console.log(data);
 				if(data.resultCode === 0){
 					docsModle.deDoc(docid);
 				}else{
 					console.error(data.resultDesc);
 				}
 			}
 		});
 	},
 };


 var docsModle={
 	data:{},
 	//:documents:Array[85],envelopeId:"a00ed5b2-f245-4fb7-bd78-dde58c9ce95d"
 	setInitDate:function(initData){
 		this.data=initData;
		if(!this.data.documents) this.data.documents=[];
 		docsView.init();
 	},
 	addDate:function(newData){
 		this.data.documents.push(newData);
 		this.dataChange();
 	},
 	getdocsData:function(){
 		return this.data.documents;
 	},
 	dataChange:function(){
 		docsView.update();
 	},
 	deDoc:function(docid){
 		var docs=this.data.documents;
 		docs.forEach(function(docData,index){
 			if(docData.docId==docid){
 				docs.splice(index,1);
 				docsView.delview(index);
 				return 0;
 			}
 		});
 	}
 };
$(document).ready(function(){
	docsController.init();
});

//邮箱校验
function validEmail(value){
    var re=/^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return value==' '? false : re.test(value);
}

//判断填写重复值
function emailDittoControl(Array,callback){
    var newArray = Array.join(",") + ",";
		var result;
    for(var i = 0;i < Array.length; i++){
        if(newArray.replace(Array[i]+ ",", "").indexOf(Array[i]+ ",")>-1){
            result = 0;
            break;
        }else{
            result = 1;
        }
    }
    callback(result);
}

//添加本人real_name
function addRealName(){
    var newRealName = $('.addresserName').val();
    unicode(newRealName) == false ? newRealName : (newRealName = unicode(newRealName));
    var jsonData = {
            "newData": newRealName,
            "type": 5
    };
    requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'put',
        requestAsync: 'false',
        requestData: JSON.stringify(jsonData),
        successCallback: function(data){
            var statusCode = data.resultStatusCode;
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code === 0){
                console.log(desc);
            }else{
                showErrorMsg($('.warning'),"*本人姓名存储失败");
            }
        }
    });
}

//Unicode编码转换
function unicode(text){
    var preStr='\\u';
    var cnReg=/[\u0391-\uFFE5]/gm;
    if(cnReg.test(text)){
        var value=text.replace(cnReg,function(str){
            return preStr+str.charCodeAt(0).toString(16);
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


/*
陈曦源
2016-9-22
*/


//联系人信息
function ContactDataSender(initData) {
	var that=this;

	//获取URL信息
	var envelope_id=location.href.split("envelope-id=")[1];
	//设置脉冲发送请求
	var timerRuner = new TimerRuner();
	timerRuner.addTimeFun(function(){
		that.getContactData(function(newdata){
			newcontacts=newdata;
		});
		checkChange();
	});
	//设置5s发送一次
	timerRuner.setDelay(5*1000);

	this.getContactData=null;
	this.setGetContactData=function(fun){
		this.getContactData=fun;
		timerRuner.start();
	};

	this.saveDate=function(callback,error){
		timerRuner.stop();
		timerRuner.start();
		var i=0;
		var handler=setTimeout(function(){
			i=4;
			error();
		},3000);

		function success(){
			i++;
			if(i===3){
				callback();
				clearTimeout(handler);
			}
		}
		that.getContactData(function(newdata){
			newcontacts=newdata;
		});
		checkChange({
			add:success,
			update:success,
			del:success,
		});
	};
	//数据操作
	var contacts=initData;
	var newcontacts=[];
	/*
	 *"recipientId":2,//接受者ID 第一次创建后返回的
	  "name": "zhang san",//姓名
        "contact": "1234345@qq.com",//邮箱
        "note": "private message",//接受者私人信息
        "recipientType": 1,//操作类型(0-仅签名，1-仅接收，2-签名并接收文档)
        "sequnce": //签名顺序 可选择
	 * */
	function checkChange(callbackobj){
		//console.log("check");
		callbackobj=callbackobj||{};
		var contact;
		var l=contacts.length;
		var nl=newcontacts.length;
		var adddata=[];
		var updatedata=[];
		var deldata=[];
		for(var i=0;i<l;i++){

			//检测被删除
			if(i>=nl){
				deldata.push(i);
				continue;
			}
			newcontacts[i].recipientId=contacts[i].recipientId;
			//检测被修改
			for(var key in newcontacts[i]){
					if(newcontacts[i][key]&&newcontacts[i][key]!=contacts[i][key]){
						updatedata.push(i);
						break;
					}
			}
			if(!newcontacts[i].sequence)newcontacts[i].sequence=contacts[i].sequence;
		}
		//检测新建
		for(;i<nl;i++){
			adddata.push(i);
		}

		if(adddata.length>0) addContact(adddata,l,callbackobj.add);
		else if(callbackobj.add)callbackobj.add();
		if(updatedata.length>0) updateContact(updatedata,callbackobj.update);
		else if(callbackobj.update)callbackobj.update();
		if(deldata.length>0) delContact(deldata,callbackobj.del);
		else if(callbackobj.del)callbackobj.del();
		contacts=newcontacts;
	}

	function addContact(adddata,startnum,callback) {
		var datas={
			 "recipients":[]
		};
		for(var i=0;i<adddata.length;i++){
			var j=adddata[i];
			var data={};
			data.name=newcontacts[j].name;
			data.contact=newcontacts[j].contact;
			data.recipientType=newcontacts[j].recipientType;
			//data.note=newcontacts[j].note;
			if(newcontacts[j].sequence)data.sequence=newcontacts[j].sequence;
			datas.recipients.push(data);
		}
		requestServer({
			requestURL:gfALLDATA("envelopeHref")+"/"+envelope_id+"/recipients",
			requestType:"POST",
			requestData:JSON.stringify(datas),
			requestHeaders: {
				"Content-Type": "application/json",
				"Access-Token": "ACCESS_TOKEN"
			},
			successCallback:function(data){
				console.log("add",data);
				var resultCode=data.resultCode;
				if(resultCode===1) {
					showLightInfo("添加失败");
					return;
				}
				var recipients=data.resultData.recipients;
				for(var i=0;recipients[i];i++){
					contacts[i+startnum].recipientId=recipients[i].recipientId;
					contacts[i+startnum].sequence=recipients[i].sequence;
				}
				if(callback)callback();
				showLightInfo("添加成功");
			},
			errorCallback:function(){
				showLightInfo("请求失败");
			}
		});
	}

	function updateContact(updatas,callback) {
		var datas={
			 "recipients":[]
		};
		for(var i=0;updatas[i]||updatas[i]===0;i++){
			var j=updatas[i];
			var data={};
			data.recipientId=newcontacts[j].recipientId;
			if(newcontacts[j].name!=contacts[j].name)data.name=newcontacts[j].name;
			if(newcontacts[j].contact!=contacts[j].contact)data.contact=newcontacts[j].contact;
			if(newcontacts[j].note!=contacts[j].note)data.note=newcontacts[j].note;
			if(newcontacts[j].recipientType!=contacts[j].recipientType)data.recipientType=newcontacts[j].recipientType;
			if(newcontacts[j].sequence!=contacts[j].sequence)data.sequence=newcontacts[j].sequence;
			datas.recipients.push(data);
		}
		requestServer({
			requestURL:gfALLDATA("envelopeHref")+"/"+envelope_id+"/recipients",
			requestType:"PUT",
			requestData:JSON.stringify(datas),
			requestHeaders: {
				"Content-Type": "application/json",
				"Access-Token": "ACCESS_TOKEN"
			},
			successCallback:function(data){
				console.log("update",data);
				var resultCode=data.resultCode;
				if(resultCode===0) {
					if(callback)callback();
					return;
				}
				showErrorInfo("保存失败");
				console.error(data.resultDesc);
			},
			errorCallback:function(){
				showErrorInfo("请求失败");
			}
		});
	}

	function delContact(deldatas,callback) {
		var datas={
			 "recipients":[]
		};
		var i,j;
		for(i=0;j=deldatas[i]||deldatas[i]==0;i++){
			var data={};
			data.recipientId=contacts[j].recipientId;
			datas.recipients.push(data);
		}
		requestServer({
			requestURL:gfALLDATA("envelopeHref")+"/"+envelope_id+"/recipients",
			requestType:"DELETE",
			requestData:JSON.stringify(datas),
			requestHeaders: {
				"Content-Type": "application/json",
				"Access-Token": "ACCESS_TOKEN"
			},
			successCallback:function(data){
				console.log("del",data);
				var resultCode=data.resultCode;
				if(resultCode===0) {
					if(callback)callback();
					return;
				}
				showErrorInfo("保存失败");
				log.error(data.resultDesc);
			},
			errorCallback:function(){
				showErrorInfo("请求失败");
			}
		});
	}
}

//定时发送插件
function TimerRuner(delay){

  delay=delay||1000;
  var timeFuns=[];
  var handler=null;

  //添加脉冲事件
  this.addTimeFun=function(timefun){
    timeFuns.push(timefun);
  };
  //添加激发事件
  this.addActiveFun=function(name,fun){
    if(this[name]){
      console.error("there has a function named"+name+"！！");
      return;
    }
    this[name]=function(){
      fun();
      if(handler) return;
      run();
    };
  };

  //设置触发间隔时间
  this.setDelay=function(_delay){
    delay=_delay;
  };

  //手动控制
  this.stop=function(){
  	if(handler){
			clearTimeout(handler);
			handler=null;
		}
  };
  this.start=function(){
  	if(!handler) run();
  };
  this.trigger = function(){
		if(handler)clearTimeout(handler);
		run();
	};

  var run=function(){
    handler=setTimeout(function(){
			for(var l=0;timeFuns[l];l++){
	      timeFuns[l]();
	    }
      run();
    },delay);
  };
}


//接收人信息初始化
//////////////////////监听联系人数据变化
var contactDataSender = null;
function initContactData(){
    requestServer({
  		requestURL:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1]+'/recipients',
  		requestType: 'GET',
  		successCallback: function(data){
  				var code = data.resultCode;
  				var withCertRemain = data.withCertRemain;
  				var noCertRemain = data.noCertRemain;
  				if(code === 0){
  						console.log(data);

  						//接口未实现   伪造数据
  						//空数据
              var contactdata=data.resultData.recipients||[];
  						contactdata.sort(function(a,b){
  				      return a.sequence-b.sequence;
  				    });
  				    //console.log(contactdata);

              //后台接口未实现  直接判断为无
              contactDataSender=new ContactDataSender(contactdata);
              initContactView(contactdata);
  				}else{
  						console.error(data.resultDesc);
  				}
  		}
  	});
}

/*
陈曦源
2016-9-24
主动保存数据
*/

function saveContactData(success,error){
	contactDataSender.saveDate(function(){
		showInfo("保存成功");
    saveEnvelopeData(success,error);
	},function(){
		showErrorInfo("保存失败");
		if(error)error();
	});
}

/*
陈曦源
2016-10-13
初始化信封数据
*/
function getEnvelopeData(success,error){
  requestServer({
		requestURL:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1],
		requestType:"GET",
		requestHeaders: {
			"Content-Type": "application/json",
			"Access-Token": "ACCESS_TOKEN"
		},
		successCallback:function(data){
      var basicdata=data.resultData.envelope.basicInfo;
			var resultCode=data.resultCode;
			if(resultCode===0) {
        if(basicdata.title) $(".docTitle").val(basicdata.title);
        if(basicdata.suject) $(".msgContentTxt").val(basicdata.suject);
        if(basicdata.secureLevel===1){
          $(".signWith").removeClass("choosen");
          $(".signWithout").addClass("choosen");
        }else if(basicdata.secureLevel===3){
          $(".signWithout").removeClass("choosen");
          $(".signWith").addClass("choosen");
        }
				if(success)success();
				return;
			}
			console.error(data.resultDesc);
      if(error)error();
		},
		errorCallback:function(){
      if(error)error();
		}
	});
}
/*
陈曦源
2016-9-24
主动保存信封数据
*/
function saveEnvelopeData(success,error){
  var reqData={
  };
  var title=$(".docTitle").val();
  var subject=$(".msgContentTxt").val();
  var secureLevel=1;


  if($(".signWith").hasClass("choosen")){
    secureLevel=3;
  }else{
    secureLevel=1;
  }

  if(title)reqData.title=title;
  if(subject)reqData.subject=subject;
  reqData.secureLevel=secureLevel;

	requestServer({
		requestURL:gfALLDATA("envelopeHref")+"/"+location.href.split("envelope-id=")[1]+"/basic-info",
		requestType:"PUT",
		requestData:JSON.stringify(reqData),
		requestHeaders: {
			"Content-Type": "application/json",
			"Access-Token": "ACCESS_TOKEN"
		},
		successCallback:function(data){
      //console.log(data);
			var resultCode=data.resultCode;
			if(resultCode===0) {
				if(success)success();
				return;
			}
			console.error(data.resultDesc);
      if(error)error();
		},
		errorCallback:function(){
      if(error)error();
		}
	});
}


/*
陈曦源
2016-9-24
预览文档
*/
function previewDoc(docid,doctitle){
	$("#scanFileimg").dialog({ //弹出框设置
        autoOpen: false,
        height:document.documentElement.clientHeight-50,
        width: 900,
        modal: true,
        resizable: false,
		draggable: false,
        title:doctitle
    });
	$("#scanFileimg").dialog("open");
	document.getElementById("docViewer").innerHTML="正在加载中";
	console.log(docid);
	requestServer({
		requestURL:gfALLDATA( "docHref" )+ "/" + docid + '?fields=ops_page_count,ops_page_height,ops_page_width,name',
		requestType: 'GET',
		successCallback: function(data){
				console.log(data);
				var openDoc=new OpenDoc(docid,{
					elementName:{
						previewScrollCName:"preview-scroll"
					},
					maxWidth:850
				});
				openDoc.init();
		}
	});
}





//信封操作
function envelopeOperation(){
	$("#eo_save").on('click',eo_save);
	$("#eo_delete").on('click',eo_delete);

	function eo_save(){
		saveContactData(function(){
      showInfo("保存信息成功，跳转中");
			window.location=gfALLDATA("docPage")+"/index.html";
		},function(){
			showErrorInfo("保存信息失败，请重试");
		});
	}

	function eo_delete(){
		var deldata={
			"envelopeIds":[{
				 "envelopeId":location.href.split("envelope-id=")[1]
				}]
		};
		requestServer({
			requestURL:gfALLDATA("envelopeHref"),
			requestType: 'DELETE',
			requestData:JSON.stringify(deldata),
			successCallback: function(data){
				if(data.resultCode===0){
					showInfo("删除成功，跳转中");
					window.location=gfALLDATA("docPage")+"/index.html";
				}else{
					console.error("删除失败:",data);
					showErrorInfo("删除失败，"+data.resultDesc);
				}
			},
			errorCallback:function(){
				showErrorInfo("删除请求失败，请重试");
			}
		});
	}
}

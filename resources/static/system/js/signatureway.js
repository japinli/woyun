//sealFrome == 0-默认签名 / 1-上传 / 2-手写 / 3-输入文字/2-扫描
var gScanObj = {
		'stompClient':null,
		'stompClientTest':null,
		'time':0,
		'timeTest':0
}
var socket = null;
var socketTest = null;
$(document).ready(function(){
	
	var $obj = $("#signatureTab-list li");
	var className = "active";
	var liBlock = $("#signatureTab");
	var initBlock = $("#signatureTab-list li.active").attr("id").split('-')[0];
	
	$("#signaturePanel").dialog({ //弹出框设置
        autoOpen: false,
        minHeight: 400,
        width: 600,
        modal: true,
        resizable: false,
        position: { my: "center", at: "center center", of: window },
        open: function(){
        	var current = $("#signatureTab-list li.active").attr("id").split('-')[0];
        	$(liBlock).find('.signature-block[id = '+current+'-block]').removeClass("hidden").siblings(".signature-block").addClass("hidden");
        }
        
    });

	$obj.click(function(){  //tab键切换
		var liThis = this;
		var objId = $(liThis).attr("id").split('-')[0];
		var objName = $(liThis).attr("name");
		$(liThis).addClass(className).siblings().removeClass(className);
		$(liBlock).find('.signature-block[id = '+objId+'-block]').removeClass("hidden").siblings(".signature-block").addClass("hidden");
		if( objName == "upload" ){
			$("#imageFiles").empty();
		}else if( objName == "scan" ){}
		return false;
	});
});

function toggleDialog( command ){
	if(command){
		$("#signaturePanel").dialog("open");
	}else{
		$("#signaturePanel").dialog("close");
	}
}

//请求输入签名-向后台发送印章数据，后台生成返回前台，并且后台将章存储
//适用于制章中心，不适合在线签名(只签名，不存储签名)
function inputWrit( resultata, callback){	
	var defaultName = "sealName";
	var jsonData = {
            "files": [
                  {
                    "data": resultata.imgData,
                    "name": defaultName + '.png'
                  }
                ],
            'from': resultata.from,
            'remainState': 0
	};
    requestServer({
        requestURL: gfALLDATA("sealHref") + '/json',
        requestType: 'post',
        requestAsync: 'false',
        requestData: JSON.stringify(jsonData),
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback(jsonData.remainState, data);
            }else{
            	if(data.resultStatusCode == "100130104"){
            		alert("单个用户设置签名和印章的总数不超过5个");
            	}else{
            		alert(desc);
            	}
            	toggleDialog(false);
                //showInfo(desc);
            } 
            hideLoading();
        }
    });
}
//请求上传签名-向后台发送印章数据，后台生成返回前台，并且后台将章存储
//适用于制章中心，不适合在线签名(只签名，不存储签名)liyulan
function uploadScan( resultata, callback){	
	var defaultName = "sealName";
	var jsonData = {
          "files": [
                {
                  "data": resultata.imgData,
                  "name": defaultName + '.png'
                }
              ],
          'from': resultata.from,
          'remainState': 0
	};
  requestServer({
      requestURL: gfALLDATA("sealHref") + '/json',
      requestType: 'post',
      requestAsync: 'false',
      requestData: JSON.stringify(jsonData),
      beforeCallback: function(){
          showLoading();
      },
      successCallback: function(data){
          var code = data.resultCode;
          var desc = data.resultDesc;
          if(code == 0){
          	callback(jsonData.remainState, data);
          }else{
          	if(data.resultStatusCode == "100130104"){
          		alert("单个用户设置签名和印章的总数不超过5个");
          	}else{
          		alert(desc);
          	}
          	toggleDialog(false);
              //showInfo(desc);
          } 
          hideLoading();
      }
  });
}
//请求扫描签名-向后台发送印章数据，后台生成返回前台，并且后台将章存储
//适用于制章中心，不适合在线签名(只签名，不存储签名)liyulan
function inputScan( resultata, callback){	
	var defaultName = "sealName";
	var jsonData = {
        "files": [
              {
                "data": resultata.imgData,
                "name": defaultName + '.png'
              }
            ],
        'from': resultata.from,
        'remainState': 0
	};
requestServer({
    requestURL: gfALLDATA("sealHref") + '/json',
    requestType: 'post',
    requestAsync: 'false',
    requestData: JSON.stringify(jsonData),
    beforeCallback: function(){
        showLoading();
    },
    successCallback: function(data){
        var code = data.resultCode;
        var desc = data.resultDesc;
        if(code == 0){
        	callback(jsonData.remainState, data);
        }else{
        	if(data.resultStatusCode == "100130104"){
        		alert("单个用户设置签名和印章的总数不超过5个");
        	}else{
        		alert(desc);
        	}
        	toggleDialog(false);
            //showInfo(desc);
        } 
        hideLoading();
    }
});
}
function CanvasText( options ){
	var _this = this;
	var canvasName =options.myCanvas;
	var mycanvas = document.getElementById(canvasName);
	var context = mycanvas.getContext("2d");
	var textCenter = "center";
	var textBaseline = "middle";

	_this.upDateText = function(resetOptions){
		//根据填充字的总长度和高度动态设置canvas的宽和高
		var reCanvasW = Math.ceil(resetOptions.fillWidth) || 120; //初始化canvas的宽，不设置默认宽为120
		var reCanvasH = Math.ceil(resetOptions.fillHeight) || 40; //初始化canvas的高，不设置默认高为40
		mycanvas.width = reCanvasW;
		mycanvas.height = reCanvasH;
		//添加文字
		context.save();
		context.textAlign = textCenter;
		context.textBaseline = textBaseline;
		context.font = resetOptions.font;
		context.fillStyle = resetOptions.fillStyle || fillStyle;
		context.fillText(resetOptions.fillText , reCanvasW/2, reCanvasH/2);	
		context.restore();	
		return true;
	}
	
	_this.getDataURL = function(){
		return mycanvas.toDataURL();
	}
}

//输入签名
function fPutinSignature( obj, callback ){	
	var $obj = obj.obj;   // .inputSignature
	var initText = obj.userName || "同意";  
	var $shows = $(".fontShow");
	var className = "tdActive";
	var $defaultText = $(".fontShow").eq(0);
	var realSize = ($defaultText.children('span').css("font-size").split("px"))[0]; //获取实际字体大小
	$obj.val(initText); //采用val, 不然会出现输入和显示不一样的bug
	$shows.children('span').text(initText);
	
	$obj.on('input', function(){
		var inputVal = $.trim( $(this).val() );
		$shows.children('span').text( inputVal );
	});
	$shows.unbind('click').bind('click', function(){ //字体类型选择
		$defaultText = this;
		var _this = this;
		 if( !$(_this).hasClass(className) ){
			 $shows.removeClass(className);
			 $(_this).addClass(className);
			 
         }else{
             return false;
         }
	});
	
	$("#signaturePut-submit").unbind('click').click(function(){
		var _thisText = $defaultText;
	    var font = "56px " + $(_thisText).children('span').css("font-family");
		var fillText = $(_thisText).children('span').text();
		var fillWidth = ( $(_thisText).children('span').width() ) * (56/realSize); //计算出canvas的宽
		var fillHeight = ( $(_thisText).children('span').height() ) * (56/realSize); //计算出canvas的高
		var canvasText = new CanvasText({"myCanvas":"hiddenCanvas"});
		var upDateFlag = canvasText.upDateText({'font':font, 'fillStyle': '#000000', 'fillText': fillText, 'fillWidth':fillWidth, 'fillHeight':fillHeight});
		var imgData = canvasText.getDataURL();
		var resultObj = {
                "imgData": imgData,
	            "from": 3
		};
		
		if(!fillText){
			return false;
		}else{
			if(callback){
				callback( resultObj );
			}else{
				console.log("no callback");
			}
		}
	});
	
	if(document.all){	//for ie
	    $('input[type="text"]').each(function() {
	        var that=this;
	        if(this.attachEvent) {
	            this.attachEvent('onpropertychange',function(e) {
	                if(e.propertyName!='value') return;
	                $(that).trigger('input');
	            });
	        }
	    });
	}
}

function uploadSignature(obj, callback){	//上传
	var $obj = obj.upload;
	var remainState = Number( ( (obj.state != undefined || obj.state == 0) ? obj.state : 1 ) );  //（0-一直保存；1-签名后删除）
	var uploadURL = gfALLDATA("sealHref") + '/form';
	$obj.fileupload({
        dataType: 'json',
        type: 'post',
        autoUpload: false,
        singleFileUploads: true,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png|jpe|bmp|tiff)$/i, 
        maxFileSize: 512000, // 500 KB = 500*1024
        maxNumberOfFiles: 1,
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
        previewMaxWidth: 60,
        previewMaxHeight: 60,
        previewCrop: true
        // off()移除绑定的事件，避免造成多次执行事件
    }).off("fileuploadadd fileuploadsubmit fileuploadprocessalways fileuploadprogressall fileuploaddone").on("fileuploadadd", function(e, data){
       $("#imageFiles").text("");
       data.context = $("#imageFiles");
       
       $.each(data.files, function(index, file){
        	var _this = this;
        	var node = $('<div class="upload-file-node" />')
					  .append( $('<div class="files-node" />') 
							  .append( $('<div class="file-info" />').html(
						    		  '<span class="file-info-name" title="'+file.name+'">'+ file.name +'</span>'+
						    		  '<span class="file-info-size normalinfo">' + uploadFileSizeConvertTip( file.size ) + '</span>'
						    		/*  '<span class="file-info-cash"><i class="icon-bin"></i></span>'*/
						    		  ) 
						    	)
						      .append( $('<div class="file-node-operation" />').append($('<button class="file-node-btnUpload"/>').text("上传").click(function(){
			                    	   data.submit();
			                     })).append( $('<button class="file-node-btnCancel"/>').text("取消").click(function(){
			                    	   $(this).parent().parent().parent().remove();
			                    	   data.abort();
			                     }) ) ) )
			           .append('<div id="uploadState-'+index+'" class="uploadState"></div>');	
            $(node).appendTo(data.context);		
            data.url = uploadURL; 
        });
	   	
    }).on("fileuploadsubmit", function(e, data){
    	//向后台传额外的参数
    	var formData = new FormData();
    	formData.append('remainState',remainState);
    	data.formData = formData;
    	
    	//此方式safari5-不支持
    	//var formData = new FormData();
    	//formData.append('properties',new Blob([JSON.stringify( {"remainState": remainState} )],{type:"application/json"}));
    	//data.formData = formData;
    }).on("fileuploadprocessalways", function(e, data){
        $.each(data.files, function (index, file) {
        	var  node = $(data.context.children()[index]);
        	  if (file.preview) {
                  node.prepend(file.preview);
              }
              if(file.error){
              	 node
              	 		.find(".file-info-size").text(file.error).removeClass("normalinfo").addClass("warning-ft")
              	 		.end().find(".file-node-btnUpload").attr("disabled", "disabled").addClass("disabled");
                   return false;
              }
        });
       
    }).on("fileuploadprogressall", function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $(".uploadState").removeClass();
        $.each(data.files, function (index, file) {
            $('.uploadState-'+index).removeClass().addClass("state-uploading");
        });
        
    }).on("fileuploaddone", function (e, data) {
        if(data.result.resultCode == 0){
            $.each(data.files, function (index, file) {
                var desc = data.result.resultDesc;
                $('.uploadState-'+index).removeClass().addClass("state-success");
                $(data.context.children()[index]).slideUp(1500);
                if(callback){
                	callback( data.result );
                }
            });
                
        }else{
        	if(data.result.resultStatusCode == "100130104"){
        		alert("单个用户设置签名和印章的总数不超过5个");
        	}else{
        		alert(data.result.resultData);
        	}
        	$("#sealUploadDialog").dialog("close");
        	
        }
    });	
}

//获取印章数据-获取审核通过的章
function getStoreSeal(callback, limitNum){
    var limit = limitNum || 5;
    requestServer({
        requestURL: gfALLDATA("sealHref") + '?fields=location&filters=(check_state=0, remain_state=0)&offset=0&limit='+limit+'&sortings=-time',
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
           
            if(code == 0){
            	var size = (data.seals).length;
            	if(size>0){
            		callback("true", data.seals);
            		
            	}else{
            		//do something
            		callback("false");
            		console.log(data.resultDesc);
            	}
                
            }else{
            	//无数据
            	callback("false");
            } 
            hideLoading();
        }
    });
}

//展示存储签名
function showStoreSeal( sealList, callback ){
	//state==0(审核通过)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
	//state==1(未审核)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
	//state==2(审核未通过)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
	//cssArray[state][sealFrom]

	var html = '';
	var sHref = '';
	var count = 0;
	$("#js-store-lists").empty();
	for(var i=0; i<sealList.length; i++){
	    var filename = sealList[i].name; 
	    var date = transformTime(sealList[i].time, false).ymd;
	    var time = transformTime(sealList[i].time, false).hmm;
	    var name = filename.slice(0,filename.lastIndexOf("."));
	    var type = filename.slice(filename.lastIndexOf(".")+1); 
	    var id = sealList[i].sealId;
	    var state = sealList[i].checkState;
	    var sealFrom = sealList[i].sealFrom;
	    var docImg = sealList[i].location;
	 	
	    if( state == 0 ){ //审核通过的章
	     	 html += '<li class="store-list"><img src="'+ docImg +'" alt="'+ name +'" value="'+ id +'" width="80" height="80" /></li>'
	     	count += 1;
	     }
	}
	$("#js-store-lists").html(html);
	if(callback){
		callback( count );
	}
}
//扫描签名
function fScanSignatrue(callback) {
	reqQraddress(function(result) {
		var qrurl = result.href;
		//console.log(qrurl);
		$(".qrcode_container").show();
		$(".szCanvasDiv").hide();
		$(".js_qrcode_show").empty().qrcode({
			width : 150,
			height : 150,
			text : qrurl
		});
		// 订阅
		connect(result.randomCode);
		
	});
	
	
	$(".generateQrcode").unbind("click").bind('click',function() {
		reqQraddress(function(result) {
			var qrurl = result.href;
			console.log(qrurl);
			$(".qrcode_container").show();
			$(".szCanvasDiv").hide();
			$(".js_qrcode_show").empty().qrcode({
				width : 150,
				height : 150,
				text : qrurl
			});
			// 订阅
			connect(result.randomCode);
		});
	});
	
	$("#sig-clearBtn").bind('click', function() {
		$("#signPad").attr("src", "");
		$("#signPad").addClass("hidden");
	});
	
	$("#sig-submitBtn").off("click").on('click',function() {
				var imgData = $("#signPad").attr("src");
				if($("#signPad").attr("src") == ""){
					return;
				}
				var resultObj = {
					"imgData" : imgData,
					"from" : 2
				};
				if($("#signPad").attr("src") == ""){
					return;
				}
				if (callback) {
					callback(resultObj);
					$(".szCanvasDiv").hide();
					$(".qrcode_container").show();
				} else {
					console.log("no callback");
				}
		 });
	$(".ui-dialog-titlebar-close").click(function(){
		
		$(".szCanvasDiv").hide();
		$(".qrcode_container").show();
		$("#signPad").attr("src", "");
		$("#signPad").addClass("hidden");
	});
	//请求二维码地址
	function reqQraddress(callback) {
		$.ajax({
			url: gfALLDATA('publicHref')+'/open/sign-pads/web/qrcode' ,
			async : false, // 同步
			type : 'post',
			dataType : 'json',
			contentType : 'application/json',
			success : function(data) {
				if (data.resultCode == 0) {
					if(".szsignatureScan"){
						$(".szsignatureScan").removeClass("hidden");
					}
					if($(".szSuggest1")){
						$(".szSuggest1").addClass("hidden");
					}
					if($(".szSuggest2")){
						$(".szSuggest2").addClass("hidden");
					}
					if($(".js_qrcode_show").css("display") == "none"){
						$(".js_qrcode_show").removeClass("hidden");
					}
					if($(".szSuggest3")){
						$(".szSuggest3").addClass("hidden");
					}
					if($(".szSuggest4")){
						$(".szSuggest4").addClass("hidden");
					}
					callback(data);
					$("#signPad").attr("src", "");
				} else {
					$(".js_qrcode_show").addClass("hidden");
					$(".szSuggest2").removeClass("hidden");
					$(".szCanvasDiv").addClass("hidden");
					/*alert("生成二维码失败，请重新获取二维码");*/
					return;
				}
			},
			error : function() {
				$(".js_qrcode_show").addClass("hidden");
				$(".szSuggest3").removeClass("hidden");
				if($(".szCanvasDiv").css("display") == "block"){
					$(".szSuggest4").removeClass("hidden");
				}
				$(".szCanvasDiv").css("display","none");
				/*alert("网络异常，二维码获取失败");*/
				return;
			}
		});
	}
	//web socket
	// 连接
	function connect(signPadRoom) {
		if(socket != null)
		{
			socket = null;
		}
		if(socket == null)
		{
			socket = new SockJS(gfALLDATA('baseHref')+'/sign-pad');
		}
		if(gScanObj.stompClient != null)
		{
			gScanObj.stompClient.disconnect();
		}
		gScanObj.stompClient = Stomp.over(socket);
		gScanObj.stompClient.connect({}, function(frame){
			gScanObj.time=0;
			gScanObj.stompClient.subscribe('/topic/'+signPadRoom, function(response){
				// 处理
				var message = response.body;
				var calResult = JSON.parse(message);
				if (calResult.type == 1) {
					// 签名面板
					$(".qrcode_container").hide();
					if($("#signPad")){
						$("#signPad").addClass("hidden");
					}
					$(".szCanvasDiv").show();
					return;
				} else if (calResult.type == 4) {
					// 生成签名
					$(".qrcode_container").hide();
					$(".szCanvasDiv").show();
					$("#signPad").removeClass("hidden");
					var canvasState = $(".qrcode_container").css("display");
					if (calResult.data != null && canvasState == "none") {
						$("#signPad").attr("src", calResult.data);
					} else {
						return false;
					}
				}
			});
		},function(error){
			function disp_confirm()
			{
				if(gScanObj.time >= 3){
					/*alert("网络连接失败，请稍后再试或选用其他签名方式！");*/
					$(".szsignatureScan").addClass("hidden");
					$(".szSuggest1").removeClass("hidden");
					return;
				}
				if($(".szCanvasDiv").css("dispaly") == "block"){
					$('.szCanvasDiv').css("display","none");
				}
				gScanObj.time++;
				setTimeout("$('.generateQrcode').trigger('click')",1000);  	
			}
			
			disp_confirm();
		});	

	}

	
}
	

//上传签名
function fScanUpload(callback) {
	reqQraddressupload(function(result) {
		var qrurl = result.href+"#1";
		//console.log(qrurl);
		$(".qrcode_container_upload").show();
		$(".szCanvasupload").hide();
		$(".js_qrcode_show_upload").empty().qrcode({
			width : 150,
			height : 150,
			text : qrurl
		});
		
		connectupload(result.randomCode);
		
	});
	//请求二维码地址
	function reqQraddressupload(callback) {
		$.ajax({
			url: gfALLDATA('publicHref')+'/open/sign-pads/web/qrcode' ,
			async : false, // 同步
			type : 'post',
			dataType : 'json',
			contentType : 'application/json',
			success : function(data) {
				if (data.resultCode == 0) {
					if(".szsignatureScanupload"){
						$(".szsignatureScanupload").removeClass("hidden");
					}
					if($(".szupload1")){
						$(".szupload1").addClass("hidden");
					}
					if($(".szupload2")){
						$(".szupload2").addClass("hidden");
					}
					if($(".js_qrcode_show_upload").css("display") == "none"){
						$(".js_qrcode_show_upload").removeClass("hidden");
					}
					if($(".szupload3")){
						$(".szupload3").addClass("hidden");
					}
					if($(".szupload4")){
						$(".szupload4").addClass("hidden");
					}
					callback(data);
					$("#signPad").attr("src", "");
				} else {
					$(".js_qrcode_show_upload").addClass("hidden");
					$(".szupload2").removeClass("hidden");
					$(".szCanvasupload").addClass("hidden");
					/*alert("生成二维码失败，请重新获取二维码");*/
					return;
				}
			},
			error : function() {
				$(".js_qrcode_show_upload").addClass("hidden");
				$(".szupload3").removeClass("hidden");
				if($(".szCanvasupload").css("display") == "block"){
					$(".szupload4").removeClass("hidden");
				}
				$(".szCanvasupload").css("display","none");
				/*alert("网络异常，二维码获取失败");*/
				return;
			}
		});
	}
	
	$(".generateQrcodeupload").unbind("click").bind('click',function() {
		reqQraddressupload(function(result) {
			var qrurl = result.href+"#1";
			console.log(qrurl);
			$(".qrcode_container_upload").show();
			$(".szCanvasupload").hide();
			$(".js_qrcode_show_upload").empty().qrcode({
				width : 150,
				height : 150,
				text : qrurl
			});
			// 订阅
			connectupload(result.randomCode);
		});
	});
	function connectupload(signPadRoom) {
		/*if(gScanObj.stompClient != null)
		{
			gScanObj.stompClient.disconnect();
		}
		gScanObj.stompClient = Stomp.over(socket);*/
		if(socketTest != null)
		{
			socketTest = null;
		}
		if(socketTest == null)
		{
			socketTest = new SockJS(gfALLDATA('baseHref')+'/sign-pad');
		}
		if(gScanObj.stompClientTest != null)
		{
			gScanObj.stompClientTest.disconnect();
		}
		gScanObj.stompClientTest = Stomp.over(socketTest);
		gScanObj.stompClientTest.connect({}, function(frame){
			gScanObj.timeTest=0;
			gScanObj.stompClientTest.subscribe('/topic/'+signPadRoom, function(response){
				// 处理
				var message = response.body;
				var calResult = JSON.parse(message);
				if (calResult.type == 1) {
					// 签名面板
					$(".qrcode_container_upload").hide();
					if($("#signPadupload")){
						$("#signPadupload").addClass("hidden");
					}
					$(".szCanvasupload").show();
					return;
				} else if (calResult.type == 4) {
					// 生成签名
					$(".qrcode_container_upload").hide();
					$(".szCanvasupload").show();
					$("#signPadupload").removeClass("hidden");
					//alert("hello");
					$("#signPadupload").attr("src", calResult.data);
					var canvasState = $(".qrcode_container_upload").css("display");
					if (calResult.data != null && canvasState == "none") {
						$("#signPadupload").attr("src", calResult.data);
					} else {
						return false;
					}
				}
			});
		},function(error){
			function disp_confirm()
			{
				if(gScanObj.timeTest >= 3){
					/*alert("网络连接失败，请稍后再试或选用其他签名方式！");*/
					$(".szsignatureScanupload").addClass("hidden");
					$(".szupload1").removeClass("hidden");
					return;
				}
				if($(".szCanvasupload").css("dispaly") == "block"){
					$('.szCanvasupload').css("display","none");
				}
				gScanObj.timeTest++;
				setTimeout("$('.generateQrcodeupload').trigger('click')",1000);  	
			}
			
			disp_confirm();
		});	

	}
	$("#sig-clearBtn-upload").bind('click', function() {
		$("#signPadupload").attr("src", "");
		$("#signPadupload").addClass("hidden");
	});
	
	$("#sig-submitBtn-upload").off("click").on('click',function() {
				var imgData = $("#signPadupload").attr("src");
				if($("#signPadupload").attr("src") == ""){
					return;
				}
				var resultObj = {
					"imgData" : imgData,
					"from" : 2
				};
				if($("#signPadupload").attr("src") == ""){
					return;
				}
				if (callback) {
					callback(resultObj);
					$(".szCanvasupload").hide();
					$(".qrcode_container_upload").show();
				} else {
					console.log("no callback");
				}
		 });
	$(".ui-dialog-titlebar-close").click(function(){
		
		$(".szCanvasupload").hide();
		$(".qrcode_container_upload").show();
		$("#signPadupload").attr("src", "");
		$("#signPadupload").addClass("hidden");
	});	
}
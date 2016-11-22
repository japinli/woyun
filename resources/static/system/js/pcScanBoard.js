/*var canvas = document.querySelector('#signPadCanvas');
var ctx = canvas.getContext('2d');
var signaturePadX = new SignaturePadX(canvas);*/
$(document).ready( function(){
	reqQraddress( function( result ){
		var qrurl = result.href;
		$(".qrcode_container").show();
		$(".szCanvasDiv").hide();
		$(".js_qrcode_show").empty().qrcode({
			width: 150,
			height: 150,
			text: qrurl
		});	
		
		//订阅
		connect( result.randomCode );
	 });
	
	$(".generateQrcode").unbind('click').click(function(){
		    clearSign();
			reqQraddress( function( result ){
				var qrurl = result.href;
				$(".qrcode_container").show();
				$(".szCanvasDiv").hide();
				$(".js_qrcode_show").empty().qrcode({
					width: 150,
					height: 150,
					text: qrurl
				});	
				
				//订阅
				connect( result.randomCode );
			});
			
			return false;
		});
		
	$("#signaturePanel .ui-dialog-titlebar-close").unbind("click").click(function(){
		$(".generateQrcode").click(); //消除签名
	});
	
});
	
//请求二维码地址
function reqQraddress( callback ){
	$.ajax({
        url: gfALLDATA('publicHref')+'/open/sign-pads/web/qrcode' ,
        async: false,	//同步
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function( data ){
        	if( data.resultCode == 0){
        		callback( data );
        	}else{
        		alert("生成二维码失败");
        	}
        },
        error: function(){
        	alert("连接失败");
        }
    });
}

//web socket
//连接
function connect( signPadRoom ) {
    var socket = new SockJS(gfALLDATA('baseHref')+'/sign-pad');
	stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: frame' + frame);
        stompClient.subscribe('/topic/'+signPadRoom, function(response){
        	//处理
        	var message = response.body;
        	var calResult = JSON.parse(message);
        	
        	if( calResult.type == 1 ){
        		//签名面板
        		$(".qrcode_container").hide();
    			$(".szCanvasDiv").show();
        		return;
        	}else if( calResult.type == 2 ){
        		//绘图
        		//drawSign( canvas, ctx, calResult.x, calResult.y, calResult.desc,calResult.timestamp);
        	}else if(calResult.type == 3){
        		//清除
        		//clearSign();
        	}else if( calResult.type == 4 ){
        		$(".qrcode_container").hide();
				$(".szCanvasDiv").show();
				var canvasState = $(".qrcode_container").css("display");
				if (calResult.data != null && canvasState == "none") {
					var attr = $("#signPad").attr("src");
					if (attr != "") {
						return;
					} else {
						$("#signPad").attr("src", calResult.data);
					}
				} else {
					return false;
				}
        	}
        	
        });
    });
}

//订阅显示签名面板

//实时绘图
function drawSign( canvas, ctx, x, y, type,ts) {
	var mouse = {x: x, y: y};
	
	if ( type == "touchstart" || type == "mousedown" ){
		signaturePadX.sq_action(x,y,type,true,null,ts);
	}else if ( type == "touchmove" || type == "mousemove" ){
		 signaturePadX.sq_action(x,y,type,true,null,ts);
	} else if( type == "touchend" || type == "mouseup"){
		 signaturePadX.sq_action(x,y,type,true,null,ts);
	}else{
		return;
	}
}

//清除绘图
function clearSign() {
	//signaturePadX.clear();
}

//生成签名
function createCodeSign(data){
	//if( signaturePadX.isEmpty() ){
	signaturePadX.fromDataURL(data);
	alert(data);
	if( data==null){
		console.log("empty");
		return false;
	}else{
		//var imgData = signaturePadX.toDataURL();
		var imgData = data;
		var sealHash = SparkMD5.hash(imgData);
		var submitId = GUID();
		var jsonData = {
				'randomId':submitId,
				'sealData': imgData,
	        	'sealName': null,
	            'sealHash': sealHash,
	            'saveAfterSign':false,
	            'sealHashAlgorithm': 'MD5'
		};
		 requestHandWrit( jsonData, function(isSave, data){
			 var certId = $("#esign-lists").data("data").certId;
         	 getEsign(certId, function(size, sealList){
                 showEsign(size, sealList);
              });
             toggleDialog(false);
             $(".generateQrcode").click(); //避免重复生成签名
         	 if(isSave){	//签名并保存
         		addPageSign( data.serialCode, data.location, 0 );
         	 }else{	//签名不保存
         		addPageSign( data.serialCode, data.location, 1 );
         	 }
		 });
	}
}
var connectTimer;
var tempUrlPath;
var tryTimeCount=1;
function connect(urlPath){
	//动态加载相关js库
	$.getScript("js/sockjs/sockjs-0.3.4.min.js",function(){
		$.getScript("js/stomp/stomp.mini.js",function(){
			var websocket = new SockJS(urlPath);
			var stompClient = Stomp.over(websocket);
			tempUrlPath=urlPath;
			 // stomp客户端成功连接到服务器后的回调
		    var connectCallback = function(frame) {
		    	window.clearTimeout(connectTimer);
		    	console.log("成功连接到服务器...");
		    	stompClient.heartbeat.outgoing = 20000; 
		    	stompClient.subscribe('/queue/'+$("#_user-serial-code").val().trim(),onMessage,headers);
		    	sendMessage(stompClient,"/app/notify/get", JSON.stringify({ 'msg': ''}));
		    }; 

		    // stomp客户端不能连接到服务器后的回调
		    var errorCallback = function(error) {
		    	console.log("无法连接到服务器..."+error);
		    	/*if(tryTimeCount == 30){
		        	window.clearTimeout(connectTimer);
		    	}else{
		    		window.setTimeout("connect(tempUrlPath)",tryTimeCount*500);
		    		tryTimeCount++;
		    	}*/
		    };
		    
		    // 通过websocket连接服务器
		    stompClient.connect(headers,connectCallback, errorCallback);
		    
		    return stompClient;
		});
	});
}

//指定连接文件头
var headers = {
	      login: 'guest',
	      passcode: 'guest',
	      // additional header
	      //'clientId': $("#user-header-name").text().trim()
	      //使用"client"确认模式，还有"client-individual"确认模式
	      ack: 'client'
};

//接收数据
function onMessage(response){
	var message = response.body;
	console.log("接收到服务器发送的数据: "+message);
	try {
		var json =  JSON.parse(message);
		var count = json.uncheckedCount;
		if(count > 0){
			$('#msg-send').text(count);
			$('#msg-send').css("visibility","visible");
		}else{
			$('#msg-send').text("0");
			$('#msg-send').css("visibility","hidden");
		}
		$.totalStorage("uncheckedCount",count);
	} catch (e) {
		console.log('解析JSON数据 '+message+' 错误: '+ e);
	}finally{
		//客户端确认消息
		response.ack();
		return;
	}
}

//发送数据
function sendMessage(stompClient,dest,jsonMsg){
	stompClient.send(dest,{},jsonMsg);
}

//断开连接
function disconnect(stompClient){
	stompClient.disconnect(function() {
		console.log("断开连接...");
	  });
}
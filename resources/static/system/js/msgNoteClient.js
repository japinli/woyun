var socket = null;

function connect() { // 请求mananger地址并建立连接
	requestServer({
		requestURL : '/user/sys/seal/getManager',
		requestType : 'get',
		successCallback : function(data) {

			var hostUrl = "ws://" + data.host + ":" + data.port;
			socket = new WebSocket(hostUrl + '/websocket');

			socket.onopen = function() {
				alert('Info: connection opened.');
			};
			socket.onmessage = function(event) {
				// 暂时没有接受需求
			};
			socket.onclose = function(event) {
				socket.close(1000, "");
			};
		}
	})
}

// 增加待审核数
function sendSealCount() {
	socket.send("seal");
}
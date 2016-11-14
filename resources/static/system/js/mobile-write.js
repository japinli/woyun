var signPadRoom = ( window.location.hash ).split("#")[1] ;
var opMethod = {
				drawing:"drawing",
				clearing:"clearing"
};

$(document).ready( function(){
	 writeTest();
	 connect(); 
});
	
//web socket
//连接
function connect() {
    var socket = new SockJS('/sign-pad');
	stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/'+signPadRoom, function(calResult){
        	//处理
        	
        });
    });
}

function sendData(method,x, y,desc,ts, type){
	stompClient.send( '/app/'+method+'/'+signPadRoom, {}, JSON.stringify({ 'x': x, 'y': y,"desc":desc,"timestamp":ts,"type":type }));
}

//订阅显示签名面板
function writeTest() {
	var canvasHeight;
	var canvasWidth;
	var isVertical;
	
	var handleMove,handleUp,handleDown;

	  var doLayout = function(){

	    var $can = document.getElementById('signPadCanvas');
	    var $btnClear = document.getElementById('sig-clearBtn');
	    var $btnSubmit = document.getElementById('sig-submitBtn');
	    var signaturePadX = new SignaturePadX($can,{minWidth:0.7*devicePixelRatio,maxWidth:2.5*devicePixelRatio});
	    var $wrapper = document.getElementById('signatureWrite-block');
	    
	    window.xOffset = 0;
	    window.yOffset = 0;
	    
	   if(document.documentElement.clientHeight > document.documentElement.clientWidth){
            $wrapper.classList.add('vertical');
            isVertical = true;
         }else{
            $wrapper.classList.remove('vertical');
            isVertical = false;
         }
	    
	    //by virtual pixel
	    var clientHeight = document.documentElement.clientHeight;
	    var clientWidth = document.documentElement.clientWidth;
	    //处理 iOS 8 bug，如果宽度小于300则强制指定
	    if(clientHeight < 300){
	      clientWidth = (clientWidth/clientHeight) * 320;
	      clientHeight = 320;
	    }

	    if(isVertical){
	    //by virtual pixel
	      canvasWidth = clientWidth-50;//total width 217+50
	      canvasHeight = clientHeight;

	      if( (canvasWidth/canvasHeight) > (217/500) ){
	        window.xOffset = ( (500/217)-(canvasHeight/canvasWidth) ) * canvasWidth / 2;
	      }else{
	        window.yOffset = ( (217/500)-(canvasWidth/canvasHeight) ) * canvasHeight / 2;
	      }

	      $can.height = canvasHeight*devicePixelRatio;
	      $can.width = canvasWidth*devicePixelRatio;
	      $can.style.width = canvasWidth+'px';
	      $can.style.height = canvasHeight+'px';
	      
	      var $header= document.getElementById('header-container');
		  var $footer= document.getElementById('footer-container');
          $header.style.left = clientWidth + 'px';
          $header.style.width = canvasHeight + 'px';
          
          $footer.style.left = 50 + 'px';
          $footer.style.width = canvasHeight + 'px';

	    }else{
	      //by virtual pixel
	      canvasHeight = clientHeight-50;
	      canvasWidth = clientWidth;

	      if( (canvasHeight/canvasWidth) > (217/500) ){
	        window.xOffset = ( (500/217)-(canvasWidth/canvasHeight) ) * canvasHeight / 2;
	      }else{
	        window.yOffset = ( (217/500)-(canvasHeight/canvasWidth) ) * canvasWidth / 2;
	      }

	      $can.height = canvasHeight*devicePixelRatio;
	      $can.width = canvasWidth*devicePixelRatio;
	      $can.style.width = canvasWidth+'px';
	      $can.style.height = canvasHeight+'px';
	      
	      var $header= document.getElementById('header-container');
		  var $footer= document.getElementById('footer-container');
	      $header.style.left = 'auto';
          $header.style.width = 'auto';
          
          $footer.style.left = '0';
          $footer.style.width = 'auto';
	    }
	    
	    var penHandler = function(evtType) {
	      return function(e) {
	        var emitX,emitY;
	       // console.log(e.type);
	        var x = e.clientX || (e.touches[0] ? e.touches[0].pageX : 0);
	        var y = e.clientY || (e.touches[0] ? e.touches[0].pageY : 0);
	        if (isVertical) {
	          physicalX = x * devicePixelRatio;
	          physicalY = y * devicePixelRatio;
	        }else{
	          physicalX = x * devicePixelRatio;
	          physicalY = (y-50) * devicePixelRatio;
	        }
	       // console.log(physicalX,physicalY,evtType,e.timeStamp);

	        signaturePadX.sq_action(physicalX,physicalY,evtType,true,null,e.timeStamp);

	        if (isVertical) {
	         if(window.xOffset){//切除宽边，以窄边为准
	            var shouldScaleRatio = 217/canvasWidth;
	            emitX = (y + window.xOffset) * shouldScaleRatio;
	            emitY = 217 - x * shouldScaleRatio ;
	          }else{
	            var shouldScaleRatio = 500/canvasHeight;
	            emitX = y * shouldScaleRatio;
	            emitY = (x + window.yOffset) * shouldScaleRatio;
	          }
	        	 
	        }else{
	          if(window.xOffset){//切除宽边，以窄边为准
	            var shouldScaleRatio = 217/canvasHeight;
	            emitX = (x + window.xOffset) * shouldScaleRatio;
	            emitY = (y-50) * shouldScaleRatio;
	          }else{
	            var shouldScaleRatio = 500/canvasWidth;
	            emitX = x * shouldScaleRatio;
	            emitY = (y + window.yOffset - 50) * shouldScaleRatio;
	          }
	        
	        }
	       // console.log(shouldScaleRatio,canvasWidth,canvasHeight);
	        
	        //提交操作相关坐标
	        var method = opMethod.drawing;
	        if(evtType == 'clear'){//清除画布
	        	method = opMethod.clearing;
	        	sendData(method,emitX,emitY,evtType,e.timeStamp,3);
	        }else if(evtType == 'confirmed'){//发送数据
	        	//method = opMethod.clearing;
	        	sendData(method,emitX,emitY,evtType,e.timeStamp,4);
	        	
	        }else{
	        	//method = opMethod.drawing;
		        sendData(method,emitX,emitY,evtType,e.timeStamp,2);
	        }
	        
	        try {
	        	
	        } catch (e) {
	        	
	        }
	        e.preventDefault();
	      }
	    }
	    
	    //TODO document.removeEventListeners
	    if(handleMove){
	      $can.removeEventListener('mousedown',handleDown);
	      $can.removeEventListener('mouseup',handleUp);
	      $can.removeEventListener('mousemove',handleMove);
	    }else{
	      handleDown = penHandler('mousedown');
	      handleMove = penHandler('mousemove');
	      handleUp = penHandler('mouseup');
	    }


	    $can.addEventListener('mousedown', handleDown, false);
	    $can.addEventListener('mousemove', handleMove, false);
	    $can.addEventListener('mouseup', handleUp, false);

	    $can.addEventListener('touchstart', handleDown, false);
	    $can.addEventListener('touchmove', handleMove, false);
	    $can.addEventListener('touchend', handleUp, false);
	    $can.addEventListener('touchcancel', penHandler('mouseup'), false);
	    
	    //$btnClear.addEventListener('click', penHandler('clear'), false);
	    $btnClear.addEventListener('touchend', penHandler('clear'), false);
	    //$btnSubmit.addEventListener('click', penHandler('confirmed'), false);
	    $btnSubmit.addEventListener('touchend', penHandler('confirmed'), false);
	  }
	  
	  doLayout();
	  window.addEventListener('resize',doLayout);
	  
}
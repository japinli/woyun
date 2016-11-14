
var signPadRoom = ( window.location.hash ).split("#")[1] ;
var PadRoom = ( window.location.href ).split("#")[2];
if(PadRoom == '1'){
	  if($(".signature-scan")){
		  $(".signature-scan").remove();
	  } 
	  var signPadRoom = ( window.location.hash ).split("#")[1] ;
	  var cropper = null;
	  connect();
	  var src = $("#image").attr("src");
		 function selectFileImage(fileObj) {  
		      var file = fileObj.files['0'];  
		      //图片方向角 added by lzk  
		      var Orientation = null;  
		        
		      if (file) {  
		          console.log("正在上传,请稍后...");
		          $('.uploadTip').removeClass('hidden');
		          var rFilter = /^(image\/jpeg|image\/png)$/i; // 检查图片格式  
		          if (!rFilter.test(file.type)) {  
		              //showMyTips("请选择jpeg、png格式的图片", false);  
		              return;  
		          }  
		            
		          var oReader = new FileReader();  
		          oReader.onload = function(e) {  
		              var image = new Image();  
		              image.src = e.target.result;  
		              image.onload = function() {  
		                  var expectWidth = this.naturalWidth;  
		                  var expectHeight = this.naturalHeight;  
		                    
		                  if (this.naturalWidth > this.naturalHeight && this.naturalWidth > 800) {  
		                      expectWidth = 800;  
		                      expectHeight = expectWidth * this.naturalHeight / this.naturalWidth;  
		                  } else if (this.naturalHeight > this.naturalWidth && this.naturalHeight > 1200) {  
		                      expectHeight = 1200;  
		                      expectWidth = expectHeight * this.naturalWidth / this.naturalHeight;  
		                  }  
		                  //alert(expectWidth+','+expectHeight);  
		                  var canvas = document.createElement("canvas");  
		                  var ctx = canvas.getContext("2d");  
		                  canvas.width = expectWidth;  
		                  canvas.height = expectHeight;  
		                  ctx.drawImage(this, 0, 0, expectWidth, expectHeight);  
		                  //alert(canvas.width+','+canvas.height);  
		                    
		                  var base64 = null;  
		                  var mpImg = new MegaPixImage(image);  
		                      mpImg.render(canvas, {  
		                          maxWidth: 800,  
		                          maxHeight: 1200, 
		                          quality: 0.5,  
		                          orientation: Orientation  
		                      });  	                        
		                  base64 = canvas.toDataURL("image/jpeg", 0.5); 
		                  
		                  var URL = window.URL || window.webkitURL;
	 					  var blobURL;
	 					  blobURL = URL.createObjectURL(file);
		                  $('.uploadTip').addClass('hidden');
		                  cropper.reset().replace(blobURL); 
		              };  
		          };  
		          oReader.readAsDataURL(file);  
		      }  	      
		  }
	/* $(function(){ */
		function getRoundedCanvas(sourceCanvas) {
		    var canvas = document.createElement('canvas');
		    var context = canvas.getContext('2d');
		    var width = sourceCanvas.width;
		    var height = sourceCanvas.height;
		    canvas.width = width;
		    canvas.height = height;
		    context.beginPath();
		    context.arc(width / 2, height / 2, Math.min(width, height), 0, 2 * Math.PI);
		    context.strokeStyle = 'rgba(0,0,0,0)';
		    context.stroke();
		    context.clip();
		    context.drawImage(sourceCanvas, 0, 0, width, height);		
		    return canvas;
			}
		      /* window.addEventListener('DOMContentLoaded', function () { */
		        var image = document.getElementById('image');
		        var button = document.getElementById('button');
		        var result = document.getElementById('result');
		        var croppable = false;
		       cropper = new Cropper(image, {
		          aspectRatio: 1,
		          viewMode: 1,
		          dragMode: 'move',
		          autoCropArea: 1,
		          restore: false,
		          guides: false,
		          center: false,
		          highlight: false,
		          cropBoxMovable: false,
		          cropBoxResizable: false,
		          
		          ready: function () {
		            croppable = true;
		          }
		        });

		        button.onclick = function () {
		          var croppedCanvas;
		          var roundedCanvas;
		          var roundedImage;

		          if (!croppable) {
		            return;
		          }

		          // Crop
		          croppedCanvas = cropper.getCroppedCanvas();

		          // Round
		          roundedCanvas = getRoundedCanvas(croppedCanvas);

		          // Show
		          roundedImage = document.createElement('img');
		          roundedImage.src = roundedCanvas.toDataURL();
		          var method = 'create';
		          var img = new Image(); 
                  img.src = roundedImage.src;
                 //alert(roundedImage.fileSize);
                  var Date = compress(img);
		          stompClient.send('/app/sign-pads/'+signPadRoom+'/'+method, {},JSON.stringify({"data":Date, "desc":'confirmed',"timestamp":24621.820000000003,"type":4 }));
		          //alert(roundedImage.src);
		          $("#contain-upload").remove();
				    $(".szComupload").removeClass("hidden");
				    $(".szComupload").addClass("Appear");
		          if(stompClient){
		        	  stompClient.disconnect();
		          }
		          setInterval("ftimeOut()",1000*10);
		          /* result.innerHTML = '';
		          result.appendChild(roundedImage);
		          alert(roundedImage.src);*/
		        };
		        
		        function connect() {
		          var  socket = new SockJS(gfALLDATA('baseHref')+'/sign-pad');
		        	stompClient = Stomp.over(socket);	        	
		            stompClient.connect({}, function(frame){
		                console.log('Connected: ' + frame);
		                stompClient.subscribe('/topic/'+signPadRoom, function(calResult){
		                	//处理
		                });
		            },function(frame){	            	
		            	function disp_confirm()
		        		{
		            		alert("连接好像出问题了");
		            		window.location.reload();
		        		}
		        		disp_confirm();
		        	});
		        }
}
if(PadRoom == null || PadRoom == undefined){
	  if($(".upload-scan")){
		  $(".upload-scan").remove();
	  }
	  var opMethod = {
				draw:"draw",
				clear:"clear"
};
var $can = document.getElementById('signPadCanvas');
function resizeCanvas() {
	 var ratio = Math.max(window.devicePixelRatio || 1, 1);
		$can.width = $can.offsetWidth * ratio;
		$can.height = $can.offsetHeight * ratio;
		$can.getContext("2d").scale(ratio, ratio);
}
window.onresize = resizeCanvas;
resizeCanvas();
//判断手机横竖屏状态：
window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function() {
      if (window.orientation === 180 || window.orientation === 0) { 
          //window.location.reload();
      		if($(".szComDiv").hasClass("hidden")){
      			/* $(".m-signature-pad").css("height",""); */
      			resizeCanvas();
      		}
      }else if (window.orientation === 90 || window.orientation === -90 ){
      	if($(".szComDiv").hasClass("hidden")){
      		$(".m-signature-pad").css("height","auto");
      		resizeCanvas();
  		}
      }  
  }, false); 
//移动端的浏览器一般都支持window.orientation这个参数，通过这个参数可以判断出手机是处在横屏还是竖屏状态。
	writeCommit();
	 connect();	

//web socket
//连接
function connect() {
  var socket = new SockJS(gfALLDATA('baseHref')+'/sign-pad');
	stompClient = Stomp.over(socket);
	
  stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/'+signPadRoom, function(calResult){
      	//处理
      });
  },function(frame){
  	
  	function disp_confirm()
		{
	  		alert("连接好像出问题了");
	  		window.location.reload();
		}
		disp_confirm();
	});
}
function sendPicData(method,desc,data,ts, type){
	stompClient.send('/app/sign-pads/'+signPadRoom+'/'+method, {}, JSON.stringify({"data":data, "desc":desc,"timestamp":ts,"type":type }));
}

function sendData(method,x, y,desc,ts, type){
	stompClient.send('/app/sign-pads/'+signPadRoom+'/'+method, {}, JSON.stringify({ 'x': x, 'y': y,"desc":desc,"timestamp":ts,"type":type }));
}

//订阅显示签名面板
function writeCommit() {
	var canvasHeight;
	var canvasWidth;
	var isVertical;

	var handleMove, handleUp, handleDown;

	var doLayout = function() {

		
		var $btnClear = document.getElementById('sig-clearBtn');
		var $btnSubmit = document.getElementById('sig-submitBtn');
		var signaturePadX = new SignaturePad($can,{minWidth: 2.5,minHeight: 2.5});
		var $wrapper = document.getElementById('signatureWrite-block');

		
		var penHandler = function(evtType) {
			return function(e) {
				// 提交操作相关坐标
				var method = opMethod.drawing;
				if (evtType == 'clear') {// 清除画布
					method = opMethod.clearing;
					signaturePadX.clear();
					console.log(evtType);
				} 
				if(evtType == 'confirmed'){// 发送数据
					method = 'create';
					    if (signaturePadX.isEmpty()) {
					        alert("请先提供您的签名");
					    } else {
					    	sendPicData(method, evtType,signaturePadX.toDataURL(),e.timeStamp, 4);
				    		$("#szpad").remove();
						    $(".szComDiv").removeClass("hidden");
						    $(".szComDiv").addClass("Appear");
						    if(stompClient){
		    		    		stompClient.disconnect();
		    		    	}
						    setInterval("ftimeOut()",1000*10);
					    }					
				}
			}
		}
		$btnClear.addEventListener('click', penHandler('clear'), false);
		$btnSubmit.addEventListener('click', penHandler('confirmed'), false);
	}
	doLayout();
	
}


}

function ftimeOut(){
	$(".szComDiv").addClass("hidden");
	window.location.href = gfALLDATA('baseHref')+'/index.html';
}
function compress(img) {
    var initSize = img.src.length;
    var width = img.width;
    var height = img.height;

    //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
    var ratio;
    if ((ratio = width * height / 4000000)>1) {
        ratio = Math.sqrt(ratio);
        width /= ratio;
        height /= ratio;
    }else {
        ratio = 1;
    }
    var Canvas = document.createElement('canvas');
    var ctx = Canvas.getContext('2d');
    var tCanvas = document.createElement('canvas');
    var tctx = tCanvas.getContext('2d');
    Canvas.width = width;
    Canvas.height = height;

//    铺底色
    ctx.fillStyle = "#fff";
    ctx.fillRect(0, 0, Canvas.width, Canvas.height);

    //如果图片像素大于100万则使用瓦片绘制
    var count;
    if ((count = width * height / 1000000) > 1) {
        count = (Math.sqrt(count)+1); //计算要分成多少块瓦片

//        计算每块瓦片的宽和高
        var nw = (width / count);
        var nh = (height / count);

        tCanvas.width = nw;
        tCanvas.height = nh;

        for (var i = 0; i < count; i++) {
            for (var j = 0; j < count; j++) {
                tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);

                ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
            }
        }
    } else {
        ctx.drawImage(img, 0, 0, width, height);
    }

    //进行最小压缩
    var ndata = Canvas.toDataURL("image/jpeg", 0.2);
    return ndata;
   }
/*$('#upload-file').unbind().bind('click',function(){
	var poseId = $('#id-globle').children('a:first').attr('repoid');
	fuploadFile(poseId);
});*/
function fuploadFile(repoId){
	var poseId = $('#id-globle').children('a:first').attr('repoid');
	console.log(poseId);
	$('#upload-file').dmUploader({
		        url:' /wesign/repos/'+poseId+'/file',
		        dataType: 'json',
		        method:'post',
		        allowedTypes: '*',
		        fileName:'file',
		        /*data: JSON.stringify( imgData ),*/
		        /*extFilter: 'jpg;png;gif',*/
		          //初始化事件
		        onInit: function(){
		        	console.log('Plugin successfully initialized');
		        },
		        //选择新文件后的事件
		        onNewFile: function(id, file){
		        	console.log(id+" "+file);
		        },
		        //上传之前事件
		        onBeforeUpload: function(id){
		        	console.log('Starting to upload #' + id);
		        },
		        //所有待上传文件发送完成后的事件
		        onComplete: function(){
		        	console.log('We reach the end of the upload Queue!');
		        },
		        //上传处理中的事件
		        onUploadProgress: function(id, percent){
		         
		        },
		        //上传成功后的事件
		        onUploadSuccess: function(id, data){
		        
		        },
		        //上传失败后的事件
		        onUploadError: function(id, message){
		          
		        },
		        //选择上传文件的类型错误事件
		        onFileTypeError: function(file){
		          
		        },
		        //选择上传文件的尺寸错误事件
		        onFileSizeError: function(file){
		         
		        },
		        /*onFileExtError: function(file){
		          $.danidemo.addLog('#demo-debug', 'error', 'File \'' + file.name + '\' has a Not Allowed Extension');
		        },*/
		        //浏览器不支持该插件上传文件的事件
		        onFallbackMode: function(message){
		          alert('Browser not supported(do something else here!): ' + message);
		        }
		      });
}


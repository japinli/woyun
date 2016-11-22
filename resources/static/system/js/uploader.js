//var fireUploadZoneId = "#drag-and-drop-zone";
//var onId = "#fileList";

function ajaxDocumentUpload(){
	
}

function ajaxImageUpload(fireUploadZoneId,onId,url){
    return ajaxUpload(fireUploadZoneId,onId,url,'image/*');
}

//进行文件上传
function ajaxUpload(fireUploadZoneId,onId,url,types){
	//动态加载css
	var $onId = $("#" +onId);
	$("<link>")
    .attr({ rel: "stylesheet",
        type: "text/css",
        href: "/css/uploader.css"
    }).appendTo("head");
	
	//动态加载js
	$.getScript("/js/jquery-dmuploader/dmuploader.min.js",function(){
		fireUploadZoneId.dmUploader({
	        url: url,
	        dataType: 'json',
	        method:'post',
	        allowedTypes: types,
	        /*extFilter: 'jpg;png;gif',*/
	          //初始化事件
	        onInit: function(){
	        },
	        //选择新文件后的事件
	        onNewFile: function(id, file){
	        	addFile(onId, id, file);
	        },
	        //上传之前事件
	        onBeforeUpload: function(id){
	         updateFileStatus(onId, id,"uploading","上传中...");
	        },
	        //所有待上传文件发送完成后的事件
	        onComplete: function(){
	        },
	        //上传处理中的事件
	        onUploadProgress: function(id, percent){
	          var percentStr = percent + '%';
	          updateFileProgress(onId, id, percentStr);
	        },
	        //上传成功后的事件
	        onUploadSuccess: function(id, data){
	         updateFileStatus(onId, id, 'success', '上传成功');
	          
	         updateFileProgress(onId, id, '100%');
	          
	          if(data.resultCode == 0){
	        	  $.getJSON(data.uri,function(data,status){
	        		  $onId.append('<img class="set-upload-viewImg" src='+data.uri + '?id=' + new Date().getTime()+'/>');
	        	    });
	          }
	        },
	        //上传失败后的事件
	        onUploadError: function(id, message){
	          
	        	updateFileStatus(onId, id, 'error', message);
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
	});
}

//更新文件状态
function updateFileStatus(onId, addFileOrder, status, message){
	  $('#'+ onId + addFileOrder).find('.set-upload-fileStatus').html(message).addClass(status);
	  if( status == "error" ){
		  $("#" + onId).html('').end().siblings('.set-upload-title').text('重新上传');
		  
	  }
	  if( status == "success"){
		  $("#" + onId).data("flag", true);
	  }else{
		  $("#" + onId).data("flag", false);
	  }
}

//更新上传进度
function updateFileProgress(onId, addFileOrder, percent){
	$('#'+ onId + addFileOrder).find('.set-upload-progress').width(percent);
}

//添加上传文件
function addFile(onId,addFileOrder, file){
  var template = '' +
    '<div class="set-upload-fileContent" id="' + onId + addFileOrder + '">' +
      '<div class="set-upload-fileInfo">' +
        '<p title="Size: ' + file.size + 'bytes - Mimetype: ' + file.type + '"><span class="set-upload-fileStatus">等待上传</span></p>' +
      '</div>' +
      '<div class="set-upload-bar">' +
        '<div class="set-upload-progress" style="width:0%"></div>' +
      '</div>' +
    '</div>';
    
    $("#" + onId).html(template);
}

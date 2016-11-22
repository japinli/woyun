/*****
 * author: duyanmei
 * last edit time: 2016-6
 * *****/
var SEALHREF = gfALLDATA("sealHref");
$(document).ready(function(){
    getsealLists(function(sealList){
        showsealList(sealList);
    });
    
    $("#js_makesign_btn").unbind("click").click(function(){
    	toggleDialog(true); 
		//扫描   点击扫描签名建立连接
	    if($('.qrcode_container').css("display") == "none"){
			$('.qrcode_container').css("display","block");
			$('.szCanvasDiv').css("display","none");
		}
    		if (window.WebSocket){   
    			fScanSignatrue(function( resultData ){    				
    				inputScan( resultData, function(){
    					getsealLists(function(sealList){
    					        showsealList(sealList);
    					 });   		            
    				});
    				toggleDialog(false);
    				if(gScanObj.stompClient != null)
    				{
    					gScanObj.stompClient.disconnect();
    				}
    	    	});
    			
    	    }else{
    	    	$(".szsignatureScan").addClass("hidden");
    			$(".szSuggest").removeClass("hidden"); 
    	    }
    		if(window.WebSocket){
    			fScanUpload(function( resultData ){    				
    				uploadScan( resultData, function(){
    					getsealLists(function(sealList){
    					        showsealList(sealList);
    					 });   		            
    				});
    				toggleDialog(false);
    				if(gScanObj.stompClientTest != null)
    				{
    					gScanObj.stompClientTest.disconnect();
    				}
    	    	});
    		}
		 return false;
	 });
	//输入
    fPutinSignature( { "obj": $('.inputSignature') }, function( resultData ){
		inputWrit( resultData, function(){
			getsealLists(function(sealList){
			        showsealList(sealList);
			 });
            toggleDialog(false);
		});
	});
  
	//弹出框设置
	$("#sealUploadDialog").dialog({ 
        autoOpen: false,
        minHeight: 500,
        width: 700,
        modal: true,
        resizable: false
    });
	
	$("#js_sealUpload_btn").click(function(){
		$("#sealUploadDialog").dialog("open");
	});
    
	//上传: "state"=0 ——》上传签章并保存
	uploadSignature( {"upload": $("#js_sealUpload"), "state": 0 }, function(){
		 getsealLists(function(sealList){
		        showsealList(sealList);
		        $("#sealUploadDialog").dialog("close");
		  });
	});
});

//印章列表
function showsealList(sealList){
	//state==0(审核通过)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
	//state==1(未审核)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
	//state==2(审核未通过)，sealFrome == 0(默认签名) / 1(上传) / 2(手写) / 3(输入文字)
    //cssArray[state][sealFrom]
    var cssArray = [
                    [{color:"#80bcfb", text:"默认签名"},  {color:"#95da44", text:"通过"}, {color:"#95da44", text:"通过"}, {color:"#95da44", text:"通过"}],	
                    [{color:"#80bcfb", text:"默认签名"}, {color:"#ff9000", text:"审核中"}, {color:"#95da44", text:"通过"}, {color:"#95da44", text:"通过"}],	
                    [{color:"#80bcfb", text:"默认签名"},  {color:"#e31010", text:"未通过"}, {color:"#95da44", text:"通过"}, {color:"#95da44", text:"通过"}]	
             ];
    var html = '';
    $('.view').empty();
    for(var i in sealList){
        var filename = sealList[i].name; 
        var name = filename.slice(0,filename.lastIndexOf("."));
        var type = filename.slice(filename.lastIndexOf(".")+1); 
        var date = transformTime(sealList[i].time, false).ymd;
        var time = transformTime(sealList[i].time, false).hmm;
        var id = sealList[i].sealId;
        var state = sealList[i].checkState;
        var sealFrom = sealList[i].sealFrom;
        var docImg = sealList[i].location;
     	
        if( state == 1 ){
         	 html += '<div class="seal-box" value="'+id+'" name="'+name+'">'+
  					  		   '<ul class="seal-operateBar hidden">'+
  					                 '<li title="下载"><a href="'+ SEALHREF + "/" + id+ "/download"+'"><i class="icon-download3"></i></a></li>'+
  					                 '<li title="删除" class="delete-doc"><i class="icon-bin"></i></li>'+
  					             '</ul>'+
  					             '<div class="seal-img"><img src="'+ docImg +'" alt="'+ name +'" width="100" height="100" /></div>'+
  					             '<div class="seal-desc-bar">'+
  					             	 '<div class="docSate"><span class="sealSateCss" style="background-color:'+cssArray[state][sealFrom].color+'"></span>'+ cssArray[state][sealFrom].text +'</div>'+
  					                 '<div class="seal-name" title="签名生成时间">'+date+' '+time+'</div>'+
  					             '</div>'+
  					          '</div>'  
         }else{
         	 html += '<div class="seal-box" value="'+id+'" name="'+name+'">'+
  					  		   '<ul class="seal-operateBar hidden">'+
  					  		   		 '<li title="下载"><a href="'+ SEALHREF + "/" + id+ "/download"+'"><i class="icon-download3"></i></a></li>'+
  					                 '<li title="删除" class="delete-doc"><i class="icon-bin"></i></li>'+
  					             '</ul>'+
  					             '<div class="seal-img"><img src="'+ docImg +'" alt="'+ name +'" width="100" height="100" /></div>'+
  					             '<div class="seal-desc-bar">'+
  					             	 '<div class="docSate"><span class="sealSateCss" style="background-color:'+cssArray[state][sealFrom].color+'"></span>'+ cssArray[state][sealFrom].text +'</div>'+
  					                 '<div class="seal-name" title="签名生成时间">'+ date+' '+time +'</div>'+			                 
  					             '</div>'+
  					          '</div>'  
      }
   	  
    }
    $('.view').html(html);
 	$('.seal-box .delete-doc').unbind('click').click(function(){ //删除
 	        var eleId = $(this).parent().parent().attr('value');
 	        var fileName = $(this).parent().parent().attr('name');
 	        confirmDelete(fileName, eleId);
 	    });
    
    $('.seal-box').each(function(){ //鼠标滑入滑出
		    $(this).mouseEnterLeave( $(this).find(".seal-operateBar"), "hidden");
	});
}

//获取印章数据
function getsealLists(callback, limitNum){
    var limit = limitNum || 5;
    requestServer({
        requestURL: SEALHREF + '?fields=location&filters=(remain_state=0)&offset=0&limit='+limit+'&sortings=-time',
        requestType: 'get',
        requestAsync: false,
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
           
            if(code == 0){
            	 var size = (data.seals).length;
            	if(size>0){
            		callback(data.seals);
            		
            	}else{
            		showInfo(data.resultDesc);
            		occupSign();
            	}
                
            }else{
            	//无数据
                occupSign();
            } 
            hideLoading();
        }
    });
}

//空白处理
function occupSign(){
	var html = '';
	$('.view').empty();
	html = '<div class="occup-container textCenter">'
					+'<div class="sign-occup occup-bg"></div>'
					+'<div class="occup-info"><p>您还没有任何签名或印章</P><p>赶快"添加签名"或"上传印章"，开启无纸化签署之旅吧！</p></div>'
			   +'</div>';
	
	$('.view').html(html);
}

//确认是否删除
function confirmDelete(fileName, eleId){
	$(".confirmDialog").html('<div class="textCenter ptb20"><i class="warning-ft icon-tip2 fs32"></i></div>'
													    +'<div class="confirmContent textCenter"><p>印章删除后将无法找回，您确认要删除印章？</p><p></p></div>'
													    +'<div class="confirmBtn textCenter borderTop">'
													        +'<span class="confirmNo btn-border-default-m mr25">取消</span>'
													        +'<span class="confirmYes btn-primary-m">确认</span>'
													    +'</div>'
											).dialog({autoOpen: true, title: "删除"});
	
    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });

    $('.confirmYes').unbind('click').click(function(){ //确认
    	deleteSeal(eleId);
        $(".ui-dialog-titlebar-close").click();
    });
}

//删除印证
function deleteSeal( sealId ){
    requestServer({
        requestURL: SEALHREF + "/" + sealId,
        requestType: 'delete',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                showInfo("删除成功");
                getsealLists(function(docList){
                    showsealList(docList);
                });
                
            }else{
            	showErrorInfo(desc);
            } 
            hideLoading();
        }
    });
}




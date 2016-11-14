/*****
 * author: duyanmei
 * last edit time: 2016-5
 * *****/

var DOC_PAGESIZE = 20;
var TASK_HREF = gfALLDATA("docHref");

$(document).ready(function(){

    $("#senfileDialog").dialog({
        autoOpen: false,
        width: 500,
        mxHeight: 600,
        modal: true
    });
    
   // getDocLists( 1, 1, DOC_PAGESIZE, function( docList, totalPages ){
	  // showBarTitle("全部文件");
	  // $("#doc-id-bar").attr("name", 1);
   //    showDocList(docList, 1);
   //    docPagination(totalPages, DOC_PAGESIZE, 1);
   // });
   getDocLists( 4, 1, DOC_PAGESIZE, function( docList, totalPages ){
	  showBarTitle("保全列表");
	  $("#doc-id-bar").attr("name", 4);
      showDocList(docList, 4);
      docPagination(totalPages, DOC_PAGESIZE, 4);
   });
    
   $("#allDocBarId").unbind("click").bind('click',function(){		//所有文档-1
    	var _this = this;
    	showBarTitle("全部文件");
    	$("#doc-id-bar").attr("name", 1);
    	if( !$(_this).hasClass("active") ){
    		$(_this).addClass("active").siblings().removeClass("active");
    	}else{
    		//return false;
    	}
    	getDocLists( 1, 1, DOC_PAGESIZE, function(docList, totalPages){
    	      showDocList(docList, 1);
    	      docPagination(totalPages, DOC_PAGESIZE, 1);
    	  });
    });
    
    $("#waitMeBarId").unbind("click").bind('click',function(){		//待我签署-2
    	var _this = this;
    	// showBarTitle("待我签署");
    	showBarTitle("出证列表");
    	$("#doc-id-bar").attr("name", 2);
        if( !$(_this).hasClass("active") ){
    	$(_this).addClass("active").siblings().removeClass("active");
    	 }
    	getDocLists( 2, 1, DOC_PAGESIZE, function(docList, totalPages){
	  	    showDocList(docList, 2);
	  	    docPagination(totalPages, DOC_PAGESIZE, 2);
	  	  });
    });
    
    $("#waitOtherBarId").unbind("click").bind('click',function(){		//等待他人签署-3
    	var _this = this;
    	showBarTitle("等待他人签署");
    	$("#doc-id-bar").attr("name", 3);
    	if( !$(_this).hasClass("active") ){
    		$(_this).addClass("active").siblings().removeClass("active");
    	}else{
    		//return false;
    	}
    	getDocLists( 3, 1, DOC_PAGESIZE, function(docList, totalPages){
	  	      showDocList(docList, 3);
	  	     docPagination(totalPages, DOC_PAGESIZE, 3);
	  	  });
    });
    
    $("#signedBarId").unbind("click").bind('click',function(){		//已完成-4
    	var _this = this;
    	showBarTitle("保全列表");
    	// showBarTitle("已完成");
    	$("#doc-id-bar").attr("name", 4);
    	if( !$(_this).hasClass("active") ){
    		$(_this).addClass("active").siblings().removeClass("active");
    	}else{
    		//return false;
    	}
    	getDocLists( 4, 1, DOC_PAGESIZE, function(docList, totalPages){
	  	     showDocList(docList, 4);
	  	     docPagination(totalPages, DOC_PAGESIZE, 4);
	  	  });
    });
    
    //搜索
   $("#submit-search-icon").click(function(){
	   var inputV = $("#search-input-id").val();
	   if(inputV!=''){
		   if($("#doc-id-bar").attr("name") != 1){
			   showBarTitle("全部文件", "noclear");
			   $("#doc-id-bar").attr("name", 1);
			   $("#allDocBarId").addClass("active").siblings().removeClass("active");
		   }
		   searchDocs(inputV, 1, DOC_PAGESIZE, function(docList, totalPages){
			   showDocList(docList, 1);
			   docPagination(totalPages, DOC_PAGESIZE, "search", inputV);
   	       });
		   
	   }else{
		   return false;
	   }
   });
   
   $("body").keydown(function(event) {	//回车搜索
		if (event.keyCode == "13") {
			$("#submit-search-icon").click();
		}
	});
   
});

//控制当前title展示
function showBarTitle(text, clearSearch){
	$(".js-doctitle").text(text);
	if( clearSearch == undefined ){	 //清空搜索框的值
		$("#search-input-id").val('');
	}else{
		return true;
	}
}

//文档列表
function showDocList(docList, viewState){
    var htmlList = '';
    $("#js_taskinfo_box").hide();
    for(var i in docList){
    	var sequence = docList[i].sequence;
        var state = docList[i].state;
        var docOwnLevel = docList[i].docOwnLevel;
        
        if( viewState == 1 ){	//全部文件
        	if( state == 0 ){	//已完成
        		htmlList += dealUI( docList[i], 0 );
        		
        	}else{  //未完成
        		if( docOwnLevel == 2 && state == 1  ){	//待我签
        			htmlList += dealUI( docList[i], 1 );
        		}else if( (docOwnLevel == 2 && state == 2) || (docOwnLevel == 3 && state == 1) ||  (docOwnLevel == 1 && state == 1) ){	//等待他人签
        			htmlList += dealUI( docList[i], 2 );
        		}
        	}
        	
        }else if( viewState == 2 ){ //待我签名的
        	htmlList += dealUI( docList[i], 1 );
        	
        }else if( viewState == 3 ){ //待他人签名的
        	htmlList += dealUI( docList[i], 2 );
        	
        }else if( viewState == 4 ){ //已完成
        	htmlList += dealUI( docList[i], 0 );
        	
        }else{ continue; }
       
    }
    
    $('#docListContainer').empty().html(htmlList);
    
    function dealUI( listdata, result ){
    	//文档流中当前次序下文档状态：-1-未指定，0-已完成，1-待处理，2-已正常处理，3-拒绝处理
    	//拒绝处理本阶段未做
        var cssArray = [{color:"#fe7575", text:"未指定"}, {color:"#33cc99", text:"已完成"}, {color:"#80bcfb", text:"待我签署"}, {color:"#80bcfb", text:"等待他人签署"}, {color:"#80bcfb", text:"拒绝处理"} ];
        var title = listdata.title;
        var date = transformTime(listdata.time, false).ymd;
        var time = transformTime(listdata.time, false).hmm;
        var docid = listdata.docId;
        var wfid = listdata.id;
        var viewlink= TASK_HREF + '/preview.html?doc-id=' + docid + '&wf-id=' + wfid; 
        var signlink = TASK_HREF + '/signature.html?doc-id=' + docid + '&wf-id=' + wfid;
        var item = '';
        if( result == 0 ){		//已完成
        	item = '<div class="table-item" value="'+docid+'" name="'+wfid+'">'+
									'<div class="td td-f1 firstEle">'+
										'<span class="circle-state success-bg"></span>'+
										'<span title="点击打开文档" class="doc-name-td">'+ cssArray[1].text +'</span>'+
									'</div>'+
									'<div class="td td-f2" title="点击查看'+docid+'签名流详情" onclick="fgetMoreInfo( this )">'+ docid +'</div>'+
									// '<div class="td td-f2" title="点击查看'+title+'签名流详情" onclick="fgetMoreInfo( this )">'+ title +'</div>'+
									'<div class="td td-f3">'+ date +' ' + time+'</div>'+
									'<div class="opbar td td-f4">'+
										'<ul class="operateBarTd">'+
											'<li title="预览文档"><a href="'+viewlink+'"><i class="icon-eye"></i></a></li>'+
											//'<li title="删除" class="delete-doc-td" onclick="confirmDelete(this)" name="'+title+'" data-fw="'+wfid+'" data-doc="'+docid+'"><i class="icon-bin"></i></li>'+
											'<li title="下载" class="download-doc"><i class="icon-download3"></i>'+
												'<div class="downlink">'+
												   '<div><a title="文档下载" href="'+TASK_HREF + '/' + docid + '/download'+'">文档下载</a></div>'+
												   '<div><a title="凭证下载" href="'+TASK_HREF + '/' + docid + '/workflows/audit?type=31'+'" target="_blank">凭证下载</a></div>'+
												'</div>'+
											'</li>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp'+
											'<li title="申请出证">申请出证</li>'+
										'</ul>'+
									'</div>'+
								'</div>' ;
        	return item;
        	
        }else if( result == 1 ){		//待我签署
        	item = '<div class="table-item" value="'+docid+'" name="'+wfid+'">'+
									'<div class="td td-f1 firstEle">'+
									    '<span class="circle-state default-bg"></span>'+ 
										'<span title="点击打开文档" class="doc-name-td">'+ cssArray[2].text +'</span>'+
										'<a href="'+signlink+'" class="taskbtn">签名</a>'+
									'</div>'+
									'<div class="td td-f2" title="点击查看'+docid+'签名流详情" onclick="fgetMoreInfo( this )">'+ docid +'</div>'+
									// '<div class="td td-f2" title="点击查看'+title+'签名流详情" onclick="fgetMoreInfo( this )">'+ title +'</div>'+
									'<div class="td td-f3">'+ date +' ' + time+'</div>'+
									'<div class="opbar td td-f4">'+
										'<ul class="operateBarTd">'+
											'<li title="预览文档"><a href="'+viewlink+'"><i class="icon-eye"></i></a></li>'+
											//'<li title="删除" class="delete-doc-td" onclick="confirmDelete(this)" name="'+title+'" data-fw="'+wfid+'" data-doc="'+docid+'"><i class="icon-bin"></i></li>'+
										'</ul>'+
									'</div>'+
								'</div>' ;
        	return item;
        	
        }else if( result == 2){		//等待他人签署
        	item = '<div class="table-item" value="'+docid+'" name="'+wfid+'">'+
									'<div class="td td-f1 firstEle">'+
										'<span class="circle-state transit-bg"></span>'+ 
										'<span title="点击打开文档" class="doc-name-td">'+ cssArray[3].text +'</span>'+
									'</div>'+
									'<div class="td td-f2" title="点击查看'+title+'签名流详情" onclick="fgetMoreInfo( this )">'+ title +'</div>'+
									'<div class="td td-f3">'+ date +' ' + time+'</div>'+
									'<div class="opbar td td-f4">'+
										'<ul class="operateBarTd">'+
											'<li title="预览文档"><a href="'+viewlink+'"><i class="icon-eye"></i></a></li>'+
											//'<li title="删除" class="delete-doc-td" onclick="confirmDelete(this)" name="'+title+'" data-fw="'+wfid+'" data-doc="'+docid+'"><i class="icon-bin"></i></li>'+
										'</ul>'+
									'</div>'+
								'</div>' ;
        	return item;
        	
        }else{ return '';}
        
    }
    //实现点击“下载”之外的地方弹出框隐藏
    $(document).click(function(){
	   	$(".downlink").hide();
	 });
    $(".download-doc").click(function(event){
    	$(".download-doc .downlink").hide();
     	$(this).children(".downlink").toggle();
     	//阻止冒泡事件，避免出现点击document，全部元素都触发点击
     	event.stopPropagation(); 
     });
}

//获取指定文档流更多信息
function fgetMoreInfo( _this ){
	if ( $(_this).parent().hasClass("active") ){
		$(_this).parent().removeClass("active");
		$("#js_taskinfo_box").hide();
	}else{
		$(_this).parent().addClass("active").siblings().removeClass("active");
		var docid = $(_this).parent().attr("value");
		$("#js_taskinfo_box").empty();
		
	    requestServer({
	        requestURL: TASK_HREF+'/1/workflows?fields=username,rev_contact,rev_as_recipient,req_as_recipient,subject,state,doc_own_level,sequence&filters=doc_serial_code="'+docid+'"',
	        requestType: 'get',
	        beforeCallback: function(){
	           // showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            if(code == 0 && data.workflowLinks){
	            	fshowMoreInfo( data.workflowLinks, _this );
	            }else{
	            	//occupDoc();
	            	//容错处理
	            } 
	            //hideLoading();
	        }
	    });
	}
}

//展示更多信息
function fshowMoreInfo( objData, _this ){
	//flag作用：控制样式加载——签署人、接收人、附加信息有可能为空，为空，不显示界面
	var flag = { "title":false, "reqer": false, "signer": 0, "reciver": false, "docid": false, "subject": false};
	//文档流中当前次序下文档状态：-1-未指定，0-已完成，1-待处理，2-已正常处理，3-拒绝处理
	//映射：-1-草稿，0-已完成，1-待我签署，2-等待别人签署，3-拒绝处理
	var stateCss = [{"desc": "", "css": ""}, {"desc": "已签", "css": "success-bg"}, {"desc":"未签", "css":"warning-bg"},{"desc": "已签", "css": "success-bg"}, {"desc": "拒绝", "css": "cancel-bg"}];
	
	$("#js_taskinfo_box").empty().append( $('<div></div>').append( $(_this).siblings(".opbar").find(".operateBarTd").clone(true) ) ).append(
			 '<div class="mt20"><h4 class="taskinfo-title">主题</h4><p class="fw-title"></p></div>'+
			 '<div class="mt20"><h4 class="taskinfo-title">发起人</h4><p class="fw-reqer"></p></div>'+
			 '<div class="hidden mt20"><h4 class="taskinfo-title">签署人</h4><div class="fw-signer"></div></div>'+
			 '<div class="hidden mt20"><h4 class="taskinfo-title">接收人</h4><div class="fw-reciver"></div></div>'+
			 '<div class="mt20"><h4 class="taskinfo-title">文档ID</h4><p class="fw-id"></p></div>'+
			 '<div class="hidden mt20"><h4 class="taskinfo-title">附加信息</h4><p class="fw-subject"></p></div>'
			);
	for(var i in objData){
		var userName = objData[i].username;
		var contact =( ( objData[i].revContact != undefined )?( '-'+objData[i].revContact ): '' );
		var title = objData[i].title;
		var docId = objData[i].docId;
		var docOwnLevel = objData[i].docOwnLevel;		// 1-发起者,2-接收者1（文档流中），3-接收者2（非文档流中）
		var revAsRecipient = objData[i].revAsRecipient;	//文档流中(0:签名且不接收文档,1:签名且接收文档)
		var sequence = objData[i].sequence;		// -1-未指定，0-无序，1~n当前工作顺序
		var subject = objData[i].subject;
		var state = objData[i].state;
		
		if( flag["title"] == false ){	//主题
			$(".fw-title").text( title );
			flag["title"] = true;
		}
		if( flag["reqer"] == false && docOwnLevel == 1 ){	//发起人
			$(".fw-reqer").text( userName+contact );
			flag["reqer"] = true;
		}
		if( flag["signer"] == 0 && docOwnLevel == 2 ){	//签名人
			flag["signer"] = flag["signer"] + 1;
			if( sequence < 1 ){	//签名为无序时，-1：未指定；0-无序
				$(".fw-signer").append( '<p class="mt10">'+userName+contact+'</p><p><span class="plr10 '+stateCss[state+1].css+'">'+stateCss[state+1].desc+'</span></p>' ).parent().removeClass("hidden") ;
			
			}else{ //签名有序，1-n
				$(".fw-signer").append( '<p class="mt10"><span>'+flag["signer"]+'. </span>'+userName+contact+'</p><p><span class="plr10 '+stateCss[state+1].css+'">'+stateCss[state+1].desc+'</span></p>' ).parent().removeClass("hidden") ;
			}
			
		}else if( flag["signer"] > 0 && docOwnLevel == 2 ){
			flag["signer"] = flag["signer"] + 1;
			if( sequence < 1 ){	 //签名为无序时，-1：未指定；0-无序
				$(".fw-signer").append( '<p class="mt10">'+userName+contact+'</p><p><span class="plr10 '+stateCss[state+1].css+'">'+stateCss[state+1].desc+'</span></p>' ) ;
			
			}else{ //签名有序，1-n
				$(".fw-signer").append( '<p class="mt10"><span>'+flag["signer"]+'. </span>'+userName+contact+'</p><p><span class="plr10 '+stateCss[state+1].css+'">'+stateCss[state+1].desc+'</span></p>' ) ;
				
			}
		}
		if( flag["reciver"] == false && docOwnLevel == 3 ){	//接收人
			$(".fw-reciver").append('<p>'+userName+contact+'</p>').parent().removeClass("hidden");
			flag["reciver"] = true;
			
		}else if( flag["reciver"] == true && docOwnLevel == 3 ){
			$(".fw-reciver").append('<p>'+userName+contact+'</p>');
		}
		if( flag["docid"] == false ){	//文档id 
			$(".fw-id").text( docId );
			flag["docid"] = true;
		}
		if( flag["subject"] == false && subject!=undefined ){	  //附加信息
			$(".fw-subject").text( subject ).parent().removeClass("hidden");
			flag["subject"] = true;
		}
	}
	//展示信息
	$("#js_taskinfo_box").show();
}

//分页
function docPagination(totalPages, pageSize, flag,  search){
	var totalPages = totalPages;	//总页数
	var pageSize = pageSize;  	//每页条数
	
	$('.js_pagination').pagination({
         items: totalPages,
         itemsOnPage: pageSize,
         displayedPages:3,
         prevText:"< 上一页",
         nextText:"下一页 >",
         onPageClick: function(pageNumber,event){
        	 pageClick(pageNumber, pageSize, flag, search);
         }
     });
}

function pageClick(pageNumber, pageSize, flag, search){
	if(flag == 1 || flag == 2 || flag == 3 || flag == 4){	
		getDocLists( flag, pageNumber, pageSize, function( docList, totalPages ){
		      showDocList(docList, flag);
		     // docPagination(totalPages, DOC_PAGESIZE, 1);
		 });
		
	}else if(flag == "search"){
		if(search){	//搜索值不为空
			searchDocs(search, pageNumber, pageSize, function(docList){
				   showDocList(docList, 1);
	   	       });
			
		}else{
			return;
		}
	}else{
		console.log("no flag value");
	}
}

//获取合同文件数据
function getDocLists(select, selectPage, limit, callback){
	var offset = ( selectPage - 1 ) * 20;
	var getItems = 'offset='+offset+'&'+'limit='+limit ;
	var type = select;

    requestServer({
        requestURL: gfALLDATA("userInfoHref")+'/workflows?'+ getItems + '&sortings=-time&type='+type ,			
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data, textStatus, resObj){
            var code = data.resultCode;
            if(code == 0 && data.workflowLinks){
            	if(data.workflowLinks.length > 0){
            		callback(data.workflowLinks, resObj.getResponseHeader("X-Total-Count") );
            	}else{
            		occupDoc();
            		showErrorInfo(data.resultDesc);
            	}
                
            }else{
            	occupDoc();
               // showErrorInfo(data.resultDesc);
            } 
            hideLoading();
        }
    });
}

//搜索用户文档
function searchDocs( searchValue, selectPage, limit, callback ){
	var offset = ( selectPage - 1 ) * 20;
	var getItems = '&offset='+offset+'&'+'limit='+limit;	
	var filters = ( searchValue !='' )?( "filters=title like '%"+searchValue+"%'" ) : '' ;
	//encodeURI( filters )-转码，不然是中文的时候有错
    requestServer({
        requestURL: gfALLDATA("userInfoHref")+'/workflows?' + encodeURI(encodeURI( filters )) + getItems + "&sortings=-time&type=1",
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data, textStatus, resObj){
            var code = data.resultCode;
            if(code == 0 && data.workflowLinks){
            	var size = data.workflowLinks.length;
            	if(size > 0){
            		callback(data.workflowLinks, resObj.getResponseHeader("X-Total-Count"));
            	}else{
            		occupDoc();
            		showErrorInfo(data.resultDesc);
            	}
                
            }else{
            	occupDoc();
            } 
            hideLoading();
        }
    });
}

//删除文档流
function fDeleteFlow(docid, flowid){
	  requestServer({
	        requestURL: TASK_HREF+'/'+docid+'/workflows/'+flowid ,
	        requestType: 'delete',
	        beforeCallback: function(){
	            showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            var desc = data.resultDesc;
	            if(code == 0){
	                showInfo("删除成功");
	                var flagTag = $("#doc-id-bar").attr("name");
	                getDocLists( flagTag, 1, DOC_PAGESIZE, function( docList, totalPages ){
	                    showDocList(docList, flagTag);
	                    docPagination(totalPages, DOC_PAGESIZE, flagTag);
	                 });
	            }else{
	            	showErrorInfo(desc);
	            } 
	            hideLoading();
	        }
	    });
}

//确认是否删除
function confirmDelete( _this ){
    $(".confirmDialog").html('<div class="textCenter ptb20"><i class="warning-ft icon-tip2 fs32"></i></div>'
										    +'<div class="confirmContent textCenter"><p>文件删除后将无法找回，您确认要删除文件 </p><p>'+$(_this).attr("name")+'</p></div>'
										    +'<div class="confirmBtn textCenter borderTop">'
										    +'<span class="confirmNo btn-border-default-m mr25">取消</span>'
									        +'<span class="confirmYes btn-primary-m">确认</span>'
										    +'</div>'
								).dialog({autoOpen: true, title: "删除"});

    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });

    $('.confirmYes').unbind('click').click(function(){ //确认
    	var docid = $(_this).attr("data-doc");
    	var flowid = $(_this).attr("data-fw");
    	fDeleteFlow( docid, flowid );
        $(".ui-dialog-titlebar-close").click();
        $("#js_taskinfo_box").hide();
    });
}

//文档空白处理
function occupDoc(){
	 var html = '';
	 $('#docListContainer').empty();
	 $("#js_taskinfo_box").hide();
	 $(".js_pagination").empty();
	 
	 html +=  '<div class="occup-container textCenter">'
						+'<div class="doc-occup occup-bg"></div>'
							+'<div class="occup-info"><p>没有任何文件记录</P><p class="mt20"><a href="'+gfALLDATA("alipayHref")+ '/index.html" class="btn-success-m">马上开启签名</a></p></div>'
					   +'</div>';
	 
	 $('#docListContainer').html(html);
}

//文档空处理
function emptyShow(){
    $('#docListContainer').empty();
    $(".js_pagination").empty();
}



//******以下暂时未用*******
//邮件发送
function shareFile( shareFileData ){
	 var jsonData = shareFileData ;	 
	  requestServer({
	        requestURL: '/user/sys/users/contact/mail/fileshare',
	        requestType: 'post',
	        requestData: JSON.stringify(jsonData),
	        beforeCallback: function(){
	            showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            var desc = data.resultDesc;
	            if(code == 0){
	                showInfo(desc);
	                $("#senfileDialog").dialog("close");
	            }else{
	            	showErrorInfo(desc);
	            } 
	            hideLoading();
	        }
	    });
}

function sendfileDialog(){
	$("#senfileDialog").dialog("open");

	$("#js_send_emailbtn").unbind('click').click(function(){
		 var str = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
		 var toSomeone = $("#js_send_emailto").val();
		 var fileSerialCode = $('.docOpen-name').attr('value');
		 var message = '你好，'+toSomeone+'，您的伙伴给你发送了一份签名文件，注意查收';
		 var jsonData =	 {
				 "to": toSomeone,
				 "fileSerialCode": fileSerialCode,
				 "message": message
		 };
		 if( !toSomeone ){
			 return false;
		 }else if( !str.test(toSomeone) ){
			 alert("错误邮箱");
			 return false;
		 }else{
			 shareFile( jsonData );
		 }
		 
	});
}

//文档操作权限设置
function setAuthority(docId){
	var $container = $("#js_myAuthor_container");
	var docId = docId;
	
	$container.empty().append(
			'<div class="toolBarHeader"><span>我的特权</span></div>'
	      		+'<div>'
	      			+'<div  id="jstool_done_doc" class="operContainer" onclick="sendfileDialog()">'
	      				+'<div class="operTextContainer">'
	      					+'<p class="js-hover">发送</p>'
	      					+'<p class="operTextDesc">通过邮件发送文档给伙伴</p>'
	      				+'</div>'
	      				+'<i class="icon-email operTextIcon"></i>'
	      			+'</div>'
		            +'<div class="operContainer">'
			            +'<a class="posArl" href="/user/sys/document/'+ docId +'/download"></a>'
		            	+'<div class="operTextContainer">'
	      					+'<p id="jstool_download_doc" class="js-hover">下载</p>'
	      					+'<p class="operTextDesc">点击下载文档</p>'
	      				+'</div>'
		            	+'<i class="icon-download3 operTextIcon"></i>'
		            +'</div>'
		            +'<div id="jstool_getVerifyInfo" class="operContainer">'
		            	+'<div class="operTextContainer">'
	      					+'<p id="jstool_verify_doc" class="js-hover">验证</p>'
	      					+'<p class="operTextDesc">验证文档及签名信息</p>'
	      				+'</div>'
		            	+'<i class="icon-description operTextIcon"></i>'
		            +'</div>'
	      		+'</div>'
		);
	
	$("#jstool_getVerifyInfo").unbind('click').click(function(){
		//getVerifyInfo(docId);
	});
}




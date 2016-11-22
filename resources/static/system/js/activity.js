$(document).ready(function(){
	var PAGESIZE = 20;	//每页条数
	var $obj = $(".lbar-link li");
	var className = "active";
	var liBlock = $(".rmain-main");
	
	$obj.click(function(){
			var liThis = this;
			var objId = ( $(liThis).attr("id") ).split('-')[0];
			var objName = $(liThis).attr("name");
			$(liThis).addClass(className).siblings().removeClass(className);
			$(liBlock).find('.rmain-main-block[id = '+objId +'-block]').removeClass("hidden").siblings().addClass("hidden");
			
			if( objName == "activity-msg" ){
				$.totalStorage("activityTabType",1);
				getAllMsg(1, PAGESIZE, function(dataArray, totalPages){
					showMsg( dataArray );
					if( dataArray.length > 0 ){
						msgPagination(totalPages, PAGESIZE);
					}else{
						showMsg( dataArray );
					}
				});
			return false;
				
			}else if( objName == "activity-logs" ){
				$.totalStorage("activityTabType",2);
				getAllLog( 1, PAGESIZE, function(dataArray, totalPages){
					showLog( dataArray );
					if( dataArray.length > 0 ){
						logPagination(totalPages, PAGESIZE, true);
					}else{
						showLog( dataArray );
					}
				});
			}
			return false;
		});
		
	//检测tab状态，切换到该tab
	var nowActivityTab = $.totalStorage("activityTabType");
	if(nowActivityTab != null && nowActivityTab == 1){//信息
		$("#msgId-link").trigger("click");
	}else if(nowActivityTab != null && nowActivityTab == 2){//日志
		$("#logId-link").trigger("click");
	}
});

/****日志*****/
//分页
/*function logPagination(totalPages, pageSize){
	var totalPages = totalPages;	//总页数
	var pageSize = pageSize;  	//每页条数
	
	$('#logId-Pagination').pagination({
         items: totalPages,
         itemsOnPage: 1,
         cssStyle: 'dark-theme',
         displayedPages:3,
         prevText:"< 上一页",
         nextText:"下一页 >",
         //onInit: changePage,
         onPageClick: function(pageNumber,event){
        	 //console.log(pageNumber);
        	 getAllLog(pageNumber, pageSize , function(dataArray){
 	    		showLog( dataArray );
 	    	});
         }
     });
}*/

function logPagination(totalPages, pageSize, flag){
	var totalPages = totalPages;	//总页数
	var pageSize = pageSize;  	//每页条数
	
	$('#logId-Pagination').pagination({
         items: totalPages,
         itemsOnPage: 1,
         //cssStyle: 'dark-theme',
         displayedPages:3,
         prevText:"< 上一页",
         nextText:"下一页 >",
         //onInit: changePage,
         onPageClick: function(pageNumber,event){
        	 pageAll(pageNumber, pageSize, flag);
         }
     });
}

function pageAll(pageNumber,pageSize, flag){
	if(flag){
		getAllLog(pageNumber, pageSize , function(dataArray){
	 		showLog( dataArray );
	 	});
	}else{
		return;
	}
}

//获取日志
function getAllLog(currentPage, perPageRows, callback){
	var currentPage = currentPage;
	var perPageRows = perPageRows;
    requestServer({
        requestURL: '/user/sys/log/'+ currentPage +'/'+ perPageRows +'/get',
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback( data.logList, data.totalPages );
            	hideLoading();
            	
            }else{
            	hideLoading();
            } 
        }
    });
}

//展示日志
function showLog( dataArray ){
	var listArray = dataArray;
	var $obj = $("#logId-view table tbody");
	var html = '';
	if( listArray.length > 0 ){
		if (!Date.prototype.toISOString) {
		    Date.prototype.toISOString = function() {
		        function pad(n) { return n < 10 ? '0' + n : n }
		        return this.getUTCFullYear() + '-'
		            + pad(this.getUTCMonth() + 1) + '-'
		            + pad(this.getUTCDate()) + 'T'
		            + pad(this.getUTCHours()) + ':'
		            + pad(this.getUTCMinutes()) + ':'
		            + pad(this.getUTCSeconds()) + '.'
		            + pad(this.getUTCMilliseconds()) + 'Z';
		    }
		}
		for(var i in listArray){
			html += '<tr>'
								+'<td class="firstTd"><abbr title='+new Date(listArray[i].time).toISOString()+' class="timeago" /><span class="real-date">('+transformTime( listArray[i].time, true )+')</span></td>'
								+'<td>'+ listArray[i].username +'</td>'
								+'<td>'+ listArray[i].info +'</div></td>'
								+'<td>'+ getDecorateResult((listArray[i].result).split('_')[0],(listArray[i].result).split('_')[0]) +'</td>'
								+'<td>'+ listArray[i].ip +'</td>'
						  +'</tr>' ;
		}
		$obj.html( html );
	}else{
		html += '<tr class="empty-info"><td>暂无消息记录</td></tr>';
		$obj.html( html );
	}
	$(".timeago").timeago(); 
}

//转换IP地址
/*function transeIp(ip){
	var url = "http://ip.taobao.com/service/getIpInfo.php";
	var result ;
	$.getJSON(url,{format:"json",ip:ip}, function(data){
		result = data.data.city || ip;
		console.log( "ip"+ ip+";"+data.data.city);
	});
	
	$.ajax({
	    url: "http://apis.baidu.com/apistore/iplookupservice/iplookup?ip="+ip,
	    beforeSend: function (request)
        {
            request.setRequestHeader("apikey","7b4509c161ff2a08030c163b9732f045");
        },
	    type: "GET",
	    dataType: "json",
	    success: function (data) {
	        console.log(data);
	    },
	    error: function (xhr, textStatus, errorThrow) {
	    	console.log(xhr.readyState);
	    }
	});
	
	return result;
}*/

/****消息*****/
//分页
function msgPagination(totalPages, pageSize){
	var totalPages = totalPages;	//总页数
	var pageSize = pageSize;  	//每页条数
	$('#msgId-Pagination').pagination({
        items: totalPages,
        itemsOnPage: 1,
        /*cssStyle: 'dark-theme',*/
        displayedPages:3,
        prevText:"< 上一页",
        nextText:"下一页 >",
       /* onInit: changePage,*/
        onPageClick: function(pageNumber,event){
       	 console.log(pageNumber);
     	getAllMsg(pagination.pageNumber, pageSize , function(dataArray){
    		showMsg( dataArray );
     	   });
        }
    });
}

//获取消息
function getAllMsg(currentPage, perPageRows, callback){
	var currentPage = currentPage;
	var perPageRows = perPageRows;
    requestServer({
        requestURL: '/user/sys/push/all/'+ currentPage +'/'+ perPageRows +'/notify/get',
        requestType: 'get',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback(data.notifications, data.totalPages);
            	hideLoading();
            }else{
            	hideLoading();
            } 
            
        }
    });
}

//展示消息
function showMsg( dataArray ){
	var listArray = dataArray;
	var $obj = $("#msgId-view table tbody");
	var html = '';
	if( listArray.length > 0 ){
		if (!Date.prototype.toISOString) {
		    Date.prototype.toISOString = function() {
		        function pad(n) { return n < 10 ? '0' + n : n }
		        return this.getUTCFullYear() + '-'
		            + pad(this.getUTCMonth() + 1) + '-'
		            + pad(this.getUTCDate()) + 'T'
		            + pad(this.getUTCHours()) + ':'
		            + pad(this.getUTCMinutes()) + ':'
		            + pad(this.getUTCSeconds()) + '.'
		            + pad(this.getUTCMilliseconds()) + 'Z';
		    }
		}
		for(var i in listArray){
			html += '<tr>'
							+'<td class="firstTd">'+ listArray[i].validName +'</td>'
							+'<td>'+ listArray[i].message+'</td>'
							+'<td><abbr title='+new Date(listArray[i].time).toISOString()+' class="timeago" /><span class="real-date">('+transformTime( listArray[i].time, true )+')</span></td>'
						  +'</tr>' ;
		}
		$obj.html( html );
		
	}else{
		html += '<tr class="empty-info"><td>暂无消息记录</td></tr>';
		$obj.html( html );
	}
	$(".timeago").timeago(); 
}

function getDecorateResult(msg,flag){
	if(flag == '失败'){
		return '<span style="color: #D43C3C">'+msg+'</span>'
	}else if(flag == '成功'){
		return  '<span>'+msg+'</span>'
	}else{
		return msg
	}
}

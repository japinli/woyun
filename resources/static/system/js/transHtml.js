$(document).ready(function(){
	$("#htmlDialog").dialog({
        autoOpen: false,
        width: 500,
        mxHeight: 600,
        modal: true,
        open: function(){   //js_transe_btn
        	$("#htmlDialog_content").html(
        			'<div><label>输入地址</label><input id="getUrl" /></div>'
        	);
        }
    });
	
	$("#js_transe_btn").unbind("click").click(function(){
		$("#htmlDialog").dialog("open");
	});
	
	$("#js_submit_link").unbind("click").click(function(){
		var url = $("#getUrl").val();
		requestHtml( url );
	});
	
});

function requestHtml( url ){
	var jsonData = {
			url: url,
			fileName: null
	}
	 requestServer({
	        requestURL: '/user/sys/document/web/online',
	        requestType: 'post',
	        requestAsync: 'false',
	        requestData: JSON.stringify(jsonData),
	        beforeCallback: function(){
	            showLoading();
	        },
	        successCallback: function(data){
	            var code = data.resultCode;
	            var size = data.size;
	            if(code == 0){
	            	getDocLists(1, 20, function(docList, totalPages){
	          		  
	          	      showDocList(docList);
	          	      docPagination(totalPages, 20, 1);
	          	      
	          	  });
	            }else{
	            	return ;
	            } 
	            hideLoading();
	        }
	    });
}
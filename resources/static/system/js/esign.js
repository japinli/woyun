$(document).ready(function(){
    getECertList(function(size, listArray){
        showECertList(size, listArray);
    });
});

function getECertList(callback){ //获取证书列表 0-查询所有证书；1-服务器端证书；2-USBKEY证书；3-一次一密证
    var id = 0;
    requestServer({
        requestURL: '/user/sys/certificate/1/1/'+ id +'/path/get',
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var listArray = data.certList;
            var desc = data.resultDesc;
            var size = data.size;
            if(code == 0){
            	callback(size, listArray);
               /* if(size > 0){
                    
                }else{
                    confirm('您没有证书');
                }*/
                
                hideLoading();
            }else{
               showInfo(desc);
            } 
        }
    });
}

//获取颁发者
function transIssuerName(value){		
	if(value != "null" && value != null){
		return  ((value.split('O='))[1]).split(',')[0];
	}else{
		return " ";
	}
}

function showECertList(size, listArray){
    var html = '';
   // $('.view').html('');
    if(size > 0){
    	 for(var i in listArray){
    	        var certFrom = transCertFrom(listArray[i].certFrom);
    	        var startTime = transformTime(listArray[i].startTime, false).ymd;
    	        var endTime = transformTime(listArray[i].endTime, false).ymd;
    	        var useState = transUseState(listArray[i].usingState);
    	        var applyUser =  transRealName(listArray[i].subject);
    	        var serialcode = listArray[i].serialCode;  //证书序列号
    	        var issuer =  transIssuerName( listArray[i].issuer );  //证书发行者

    	        html += '<div class="certiBox certiBoxBg" value="'+ serialcode +'">'
    			                    /*+'<div class="certiBox-header"><span>本证书无效，只用于测试</span><i class="certiBox-img"></i></div>'*/
    			                    +'<div class="certiBox-content">'
    			                        +'<p><span>颁发给：</span>'+ applyUser +'</p>'
    			                        +'<p><span>颁发者：</span>'+ issuer +'</p>'
    			                        +'<p><span>序列号：</span>'+ serialcode +'</p>'
    			                        +'<p><span>有效期：</span>'+ startTime +' ~ '+ endTime +'</p>'
    			                      //  +'<p><span>开始时间：</span>'+ startTime +'</p>'
    			                      //  +'<p><span>结束时间：</span>'+ endTime +'</p>'
    			                     
    			                    +'</div>'
    		                    +'</div>' ;
    	    }
    	 
    }else{
    	html +=  '<div class="occup-container textCenter">'
							+'<div class="cert-occup occup-bg"></div>'
							+'<div class="occup-info"><p>您还没有证书哦</P><p>请先完成实名认证，认证通过后颁发可信证书</p></div>'
							+'<div><a class="occup-btn" href="user/sys/users/setting">实名认证</a></div>'
					   +'</div>';
    }
    
    $('.view').html(html);

}





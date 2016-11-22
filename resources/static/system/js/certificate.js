$(document).ready(function(){
    getCertList(function(listArray){
        showCertList(listArray);
    });

    $("#apply-certificate").click(function(){ //申请证书
        var cerdialog = $("#applyDialog").dialog({
            title:"申请证书",
            width:480,
            height:580,
            modal:true
        });
        cerdialog.dialog("open");
    });

    $('#submitForm').unbind('click').click(function(){	//提交
        applyCert(function(){
            $(".ui-dialog-titlebar-close").click();
             getCertList(function(listArray){
                showCertList(listArray);
            });
        });
    });
    
    $("#resetForm").unbind("click").click(function(){		//重置
    	$(".formCss").find("input[type='text']").val("");
    });
});

function getServCert(){  //获取服务器端证书

}

function getCertList(callback){ //获取证书列表 0-查询所有证书；1-服务器端证书；2-USBKEY证书；3-一次一密证
    var id = 0;
    requestServer({
        requestURL: '/user/sys/certificate/1/20/'+ id +'/path/get',
        requestType: 'get',
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var listArray = data.certList;
            var desc = data.resultDesc;
            if(code == 0){
                callback(listArray);
                hideLoading();
            }else{
               showInfo(desc);
            } 
        }
    });
}

function showCertList(listArray){   //展示证书列表
    var tbody = $('.tableCss tbody');
    var html = '';
    tbody.empty();
    var userEmail = $("#user-header-name").text();
    for(var i in listArray){
        var certFrom = transCertFrom( listArray[i].certFrom );
        var startTime = transformTime(listArray[i].startTime, true);
        var endTime = transformTime(listArray[i].endTime, true);
        var useState = transUseState( listArray[i].usingState );
        var applyUser = transRealName(listArray[i].subject);
        var serialcode = listArray[i].serialCode;  //证书序列号
       
        var userName = (applyUser !=""? applyUser : userEmail);
        
        html += '<tr>'
                    +'<td>'+ userName +'</td>'
                    +'<td>'+ serialcode +'</td>'
                    +'<td>'+ certFrom +'</td>'
                    +'<td><span>开始：'+ startTime +'</span><br><span>结束：'+ endTime +'</span></td>'
                    +'<td>'+ useState +'</td>'
                +'</tr>' ;
    }
    tbody.html(html);
}

function applyCert(callback){   //请求申请证书  0:服务器端证书类型；1:USBKEY证书类型；2:一次一密证书类型
    var reqCertType = 0;
    var priKeyPwd = "123456";
    var realUserName = $("#realUsername").val();
    var orgUnit = '';
    var orgName = '';
    var locName = '';
    var stateName = '';
    var country = 'CN';

    var jsonData = {
        "reqCertType": reqCertType,
        "priKeyPwd": priKeyPwd,
        "realUserName": realUserName,
        "orgUnit": orgUnit,
        "orgName": orgName,
        "locName": locName,
        "stateName": stateName,
        "country": country
    };
   
    requestServer({
        requestURL: '/user/sys/certificate/'+ reqCertType +'/certRequest',
        requestType: 'post',
        requestData: JSON.stringify(jsonData),
        beforeCallback: function(){
            showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                callback();
                showInfo(desc);
                hideLoading();
            }else{
               showInfo(desc);
            } 
        }
    });
}

